package org.etp.portalKit.powerbuild.logic;

import java.io.File;
import java.io.FileFilter;
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

    @Resource(name = "customizedBuildListProvider")
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

    private String checkDeployPath() {
        String deployPath = prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
        if (StringUtils.isBlank(deployPath)) {
            throw new RuntimeException("You haven't deploy path setted.");
        }
        return deployPath;
    }

    private String checkDesignPath() {
        String path = prop.get(SettingsCommand.PORTAL_TEAM_PATH);
        if (StringUtils.isBlank(path)) {
            throw new RuntimeException("You haven't design path setted.");
        }
        return path;
    }

    private boolean checkCanBeDeployed(String absolutePath) {
        File file = new File(absolutePath);
        File[] files = file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File arg0) {
                return arg0.isDirectory() && "target".equals(arg0.getName());
            }
        });
        if (files.length == 0) {
            return false;
        }
        File target = new File(absolutePath, "target");
        File[] wars = target.listFiles(new FileFilter() {
            @Override
            public boolean accept(File arg0) {
                return arg0.isFile() && arg0.getName().endsWith(".war");
            }
        });
        if (wars.length == 0) {
            return false;
        }
        return true;
    }

    /**
     * Build and deploy one package with specified
     * <code>absolutePath</code> to tomcat. Note:
     * <code>absolutePath</code> can not be empty or null. Otherwise,
     * failed to build.
     * 
     * @param absolutePath used to scan the absolute path from common
     *            build list if absPath is null or empty.
     * @return BuildResult
     */
    public BuildResult build(String absolutePath) {
        CommandResult compile = executor.compile(absolutePath);
        BuildResult br = new BuildResult(compile);
        return br;
    }

    /**
     * Build and deploy one package with specified
     * <code>absolutePath</code> to tomcat. Note:
     * <code>absolutePath</code> can not be empty or null. Otherwise,
     * failed to build and deploy.
     * 
     * @param absolutePath absolute path of project.
     * @return BuildResult
     */
    public BuildResult buildDeploy(String absolutePath) {
        String deployPath = checkDeployPath();
        BuildResult result = build(absolutePath);
        if (!result.isSuccess()) {
            return result;
        }
        boolean deployed;
        boolean canDeploy = checkCanBeDeployed(absolutePath);
        if (canDeploy) {
            deployed = deployService.deployFromFolder(absolutePath, deployPath);
        } else {
            deployed = true;
        }
        result.setDeployed(deployed);
        return result;
    }

    /**
     * Deploy one package with specified <code>absolutePath</code> to
     * tomcat. Note: <code>absolutePath</code> can not be null or
     * empty. Otherwise, failed to deploy this package.
     * 
     * @param absolutePath absolute path of project.
     * @return BuildResult
     */
    public BuildResult deploy(String absolutePath) {
        String deployPath = checkDeployPath();
        BuildResult result = new BuildResult();
        result.setSuccess(true);
        if (checkCanBeDeployed(absolutePath)) {
            result.setDeployed(deployService.deployFromFolder(absolutePath, deployPath));
        } else {
            result.setDeployed(true);
        }
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
        BuildResult result = build(new File(basePath, "design").getAbsolutePath());
        if (!result.isSuccess()) {
            return result;
        }
        if (deployInformation == null) {
            throw new RuntimeException("failed to read deployinformation.");
        }
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

}
