package org.etp.portalKit.powerbuild.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.bean.DeployInfo;
import org.etp.portalKit.common.bean.PackageInfo;
import org.etp.portalKit.common.service.DeployInfoReader;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.shell.CommandResult;
import org.etp.portalKit.powerbuild.bean.request.Selection;
import org.etp.portalKit.powerbuild.bean.response.BuildResult;
import org.etp.portalKit.powerbuild.bean.response.DirTree;
import org.etp.portalKit.powerbuild.service.BuildExecutor;
import org.etp.portalKit.powerbuild.service.DirProvider;
import org.etp.portalKit.setting.bean.Settings;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is
 */
@Component(value = "buildLogic")
public class PowerBuildLogic {

    @Resource(name = "specProvider")
    private DirProvider specProvider;

    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    @Resource(name = "buildExecutor")
    private BuildExecutor executor;

    @Resource(name = "deployInfoDesignReader")
    private DeployInfoReader deployInfoDesignReader;

    /**
     * Get absolute path of specified project name.
     * 
     * @param selection specified project name.
     * @return path
     */
    private String getAbsPath(String selection) {
        List<DirTree> retrieveDirInfo = specProvider.retrieveDirInfo();
        String absPath = null;
        for (DirTree dirTree : retrieveDirInfo) {
            for (DirTree subTree : dirTree.getSubDirs()) {
                if (subTree.getName().equals(selection)) {
                    absPath = subTree.getAbsolutePath();
                    break;
                }
            }
        }
        return absPath;
    }

    /**
     * Build one package.
     * 
     * @param selection
     * @param absPath
     * @return CommandResult
     */
    public BuildResult build(String selection, String absPath) {
        String path = null;
        if (StringUtils.isBlank(absPath))
            path = getAbsPath(selection);
        else
            path = absPath;
        if (StringUtils.isBlank(path))
            throw new RuntimeException("Incorrect path to build.");
        CommandResult compile = executor.compile(path);
        BuildResult br = new BuildResult(compile);
        return br;
    }

    /**
     * Build + deploy a package.
     * 
     * @param selection
     * @return BuildResult
     */
    public BuildResult buildDeploy(String selection) {
        String deployPath = prop.get(Settings.TOMCAT_WEBAPPS_PATH);
        if (StringUtils.isBlank(deployPath))
            throw new RuntimeException("You haven't deploy path setted.");
        String absPath = getAbsPath(selection);
        BuildResult result = build(selection, absPath);
        if (!result.isSuccess())
            return result;
        boolean deployed = executor.deploy(absPath, deployPath);
        result.setDeployed(deployed);
        return result;
    }

    /**
     * Build + deploy a set of packages.
     * 
     * @param selection
     * @return BuildResult
     */
    public BuildResult buildDeploySet(String selection) {
        String deployPath = prop.get(Settings.TOMCAT_WEBAPPS_PATH);
        if (StringUtils.isBlank(deployPath))
            throw new RuntimeException("You haven't deploy path setted.");
        String path = prop.get(Settings.PORTAL_TEAM_PATH);
        if (StringUtils.isBlank(path))
            throw new RuntimeException("You haven't design path setted.");
        BuildResult result = build("design", new File(path, "design").getAbsolutePath());
        if (!result.isSuccess())
            return result;
        DeployInfo deployInfo = deployInfoDesignReader.retrieve();
        if (deployInfo == null)
            return result;
        List<String> deploySet = new ArrayList<String>();
        boolean deployed = true;
        if ("referencePortal".equals(selection)) {
            for (PackageInfo packageInfo : deployInfo.getFramework()) {
                deploySet.add(new File(path, packageInfo.getPackagePathInDesignHome()).getAbsolutePath());
            }
            for (PackageInfo packageInfo : deployInfo.getReferencePortal()) {
                deploySet.add(new File(path, packageInfo.getPackagePathInDesignHome()).getAbsolutePath());
            }
        } else if ("multiscreenPortal".equals(selection)) {
            for (PackageInfo packageInfo : deployInfo.getFramework()) {
                deploySet.add(new File(path, packageInfo.getPackagePathInDesignHome()).getAbsolutePath());
            }
            for (PackageInfo packageInfo : deployInfo.getMultiscreenPortal()) {
                deploySet.add(new File(path, packageInfo.getPackagePathInDesignHome()).getAbsolutePath());
            }
        }
        deployed = executor.deploy(deploySet, deployPath);
        result.setDeployed(deployed);
        return result;
    }

    /**
     * Get Specified build trees due to design path setted in settings
     * page.
     * 
     * @return List<DirTree>
     */
    public List<DirTree> getSpecBuildTrees() {
        return specProvider.retrieveDirInfo();
    }

    /**
     * Set default selections to settings page.
     * 
     * @param selection
     */
    public void setSelectionsToSettings(Selection selection) {
        prop.fromBean(selection);
    }
}
