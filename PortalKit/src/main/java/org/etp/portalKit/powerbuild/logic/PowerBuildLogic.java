package org.etp.portalKit.powerbuild.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.etp.portalKit.common.service.DeployService;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.CommandResult;
import org.etp.portalKit.common.util.JSONUtils;
import org.etp.portalKit.powerbuild.bean.BuildResult;
import org.etp.portalKit.powerbuild.bean.DeployInformation;
import org.etp.portalKit.powerbuild.bean.DirTree;
import org.etp.portalKit.powerbuild.bean.SelectionCommand;
import org.etp.portalKit.powerbuild.service.BuildExecutor;
import org.etp.portalKit.powerbuild.service.BuildListProvider;
import org.etp.portalKit.setting.bean.SettingsCommand;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is
 */
@Component(value = "buildLogic")
public class PowerBuildLogic {

    @Resource(name = "pathMatchingResourcePatternResolver")
    private PathMatchingResourcePatternResolver pathResolver;

    @Resource(name = "portalTeamBuildListProvider")
    private BuildListProvider buildListProvider;

    @Resource(name = "propertiesManager")
    private PropertiesManager prop;

    @Resource(name = "buildExecutor")
    private BuildExecutor executor;

    @Resource(name = "deployService")
    private DeployService deployService;

    private String ENVIRONMENT_DEPLOY_JSON = "powerbuild/deployInformation.json";

    private DeployInformation deployInformation;

    /**
     * initialize the basedListtree
     */
    @PostConstruct
    public void initCommbuildList() {
        deployInformation = JSONUtils.fromJSONResource(pathResolver.getResource(ENVIRONMENT_DEPLOY_JSON),
                new TypeReference<DeployInformation>() {
                    //            
                });
    }

    /**
     * Get absolute path of specified project name.
     * 
     * @param selection specified project name.
     * @return path
     */
    private String getAbsPathFromCommonBuildList(String selection) {
        List<DirTree> retrieveDirInfo = getCommonBuildListDirTrees();
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

    private String getFinalPath(String selection, String absPath) {
        String path = null;
        if (StringUtils.isBlank(absPath)) {
            if (StringUtils.isBlank(selection))
                throw new NullPointerException("selection could not be null or empty if absPath is not defined.");
            path = getAbsPathFromCommonBuildList(selection);
        } else
            path = absPath;
        if (StringUtils.isBlank(path))
            throw new RuntimeException("Incorrect path to build.");
        return path;
    }

    private String checkDeployPath() {
        String deployPath = prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
        if (StringUtils.isBlank(deployPath))
            throw new RuntimeException("You haven't deploy path setted.");
        return deployPath;
    }

    private String checkDesignPath() {
        String path = prop.get(SettingsCommand.PORTAL_TEAM_PATH);
        if (StringUtils.isBlank(path))
            throw new RuntimeException("You haven't design path setted.");
        return path;
    }

    /**
     * Build one package with specified absPath. Note: if absPath is
     * empty or null, selection will be used to scan from common build
     * list tree for its own absolute path. Otherwise, absPath will be
     * used as a maven project folder to compile.
     * 
     * @param selection used to scan the absolute path from common
     *            build list if absPath is null or empty.
     * @param absPath used to compile a maven project, if exists.
     * @return BuildResult
     */
    public BuildResult build(String selection, String absPath) {
        String path = getFinalPath(selection, absPath);
        CommandResult compile = executor.compile(path);
        BuildResult br = new BuildResult(compile);
        return br;
    }

    /**
     * Build and deploy one package with specified absPath to setted
     * web container. Note: if absPath is empty or null, selection
     * will be used to scan from common build list tree for its own
     * absolute path. Otherwise, absPath will be used as a maven
     * project folder to compile.
     * 
     * @param selection used to scan the absolute path from common
     *            build list if absPath is null or empty.
     * @param absPath used to compile a maven project, if exists.
     * @return BuildResult
     */
    public BuildResult buildDeploy(String selection, String absPath) {
        String deployPath = checkDeployPath();
        String path = getFinalPath(selection, absPath);
        BuildResult result = build(null, path);
        if (!result.isSuccess())
            return result;
        boolean deployed = deployService.deployFromFolder(path, deployPath);
        result.setDeployed(deployed);
        return result;
    }

    /**
     * Build + deploy a set of packages.
     * 
     * @param deployType referencePortal/multiscreenPortal
     * @return BuildResult
     */
    public BuildResult buildDeploySet(String deployType) {
        String deployPath = checkDeployPath();
        String basePath = checkDesignPath();
        BuildResult result = build(null, new File(basePath, "design").getAbsolutePath());
        if (!result.isSuccess())
            return result;
        if (deployInformation == null)
            throw new RuntimeException("failed to read deployinformation.");
        List<String> deploySet = new ArrayList<String>();
        boolean deployed = true;
        if ("referencePortal".equals(deployType)) {
            for (Map<String, String> fw : deployInformation.getFramework()) {
                deploySet.add(new File(basePath, fw.get("relativePath")).getAbsolutePath());
            }
            for (Map<String, String> fw : deployInformation.getReferencePortal()) {
                deploySet.add(new File(basePath, fw.get("relativePath")).getAbsolutePath());
            }
        } else if ("multiscreenPortal".equals(deployType)) {
            for (Map<String, String> fw : deployInformation.getFramework()) {
                deploySet.add(new File(basePath, fw.get("relativePath")).getAbsolutePath());
            }
            for (Map<String, String> fw : deployInformation.getMultiscreenPortal()) {
                deploySet.add(new File(basePath, fw.get("relativePath")).getAbsolutePath());
            }
        }
        deployed = deployService.deployListFromFolder(deploySet, deployPath);
        result.setDeployed(deployed);
        return result;
    }

    /**
     * Get Specified build trees due to design path setted in settings
     * page.
     * 
     * @return List<DirTree>
     */
    public List<DirTree> getCommonBuildListDirTrees() {
        return buildListProvider.retrieveDirTrees();
    }

    /**
     * Set default selections to settings page.
     * 
     * @param selection
     */
    public void setSelectionsToSettings(SelectionCommand selection) {
        prop.fromBean(selection);
    }
}
