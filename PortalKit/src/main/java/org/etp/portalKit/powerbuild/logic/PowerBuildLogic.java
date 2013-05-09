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
import org.etp.portalKit.powerbuild.bean.ExecuteCommand;
import org.etp.portalKit.powerbuild.service.BuildListProvider;
import org.etp.portalKit.powerbuild.service.ExecuteType;
import org.etp.portalKit.powerbuild.service.MavenExecutor;
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

    @Resource(name = "mavenExecutor")
    private MavenExecutor executor;

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
     * Execute specified command to the project which given
     * <code>absolutePath</code> indicates. There are four kinds of
     * command, compile, test, compile_test, deploy.
     * 
     * @param absolutePath absolute path of project.
     * @param cmd ExecuteCommand
     * @return BuildResult
     */
    public BuildResult executeCommand(String absolutePath, ExecuteCommand cmd) {
        BuildResult br = new BuildResult();
        ExecuteType type = null;
        if (cmd.isNeedBuild() && cmd.isNeedTest()) {
            type = ExecuteType.COMPILE_TEST;
        } else if (cmd.isNeedBuild()) {
            type = ExecuteType.COMPILE;
        } else if (cmd.isNeedTest()) {
            type = ExecuteType.TEST;
        }
        if ((type == null) && !cmd.isNeedDeploy()) {
            throw new RuntimeException("You have to choose at least one option.");
        }
        if (type != null) {
            CommandResult cr = executor.exec(absolutePath, type);
            br.setCommandResult(cr);
        }

        if (cmd.isNeedDeploy()) {
            if ((type != null) && !br.isSuccess()) {
                return br;
            }
            String deployPath = checkDeployPath();
            if (checkCanBeDeployed(absolutePath)) {
                br.setDeployed(deployService.deployFromFolder(absolutePath, deployPath));
            } else {
                br.setDeployed(true);
            }
        }
        return br;
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
        BuildResult result = new BuildResult(executor.exec(new File(basePath, "design").getAbsolutePath(),
                ExecuteType.COMPILE));
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
