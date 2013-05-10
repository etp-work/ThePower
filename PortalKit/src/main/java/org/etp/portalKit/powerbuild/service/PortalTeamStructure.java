package org.etp.portalKit.powerbuild.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.MavenUtils;
import org.etp.portalKit.powerbuild.bean.DirTree;
import org.etp.portalKit.setting.bean.SettingsCommand;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is to provide the portal-team workspace
 * based on maven.
 */
@Component(value = "portalTeamBuildListProvider")
public class PortalTeamStructure implements BuildListProvider {
    @Resource(name = "propertiesManager")
    private PropertiesManager prop;
    private String basePath;

    private List<DirTree> retrieveTree;

    @PostConstruct
    private void init() {
        prop.addObserver(this);
        resetDirInfo();
    }

    /**
     * Iterate specified workspace, set the absolute path for each
     * maven project.
     */
    private void resetDirInfo() {
        basePath = prop.get(SettingsCommand.PORTAL_TEAM_PATH);
        if (StringUtils.isBlank(basePath)) {
            retrieveTree = new ArrayList<DirTree>();
            return;
        }

        retrieveTree = new ArrayList<DirTree>();

        iteratorPortalTeamWorkspace(basePath, retrieveTree, null);
    }

    private void iteratorPortalTeamWorkspace(String basePath2, List<DirTree> retrieveTree2, DirTree parentTree) {
        if (StringUtils.isBlank(basePath2)) {
            return;
        }
        File path = new File(basePath2);
        File[] listFiles = path.listFiles();
        if (listFiles == null) {
            return;
        }
        for (File listFile : listFiles) {
            File file = listFile;
            if (!MavenUtils.isMavenProject(file)) {
                continue;
            }
            DirTree tree = new DirTree(file.getName(), file.getAbsolutePath());
            if (parentTree == null) {
                retrieveTree2.add(tree);
            } else {
                parentTree.addSub(tree);
            }
            iteratorPortalTeamWorkspace(file.getAbsolutePath(), retrieveTree2, tree);
        }
    }

    @Override
    public List<DirTree> retrieveDirTrees() {
        return retrieveTree;
    }

    @Override
    public void update(Observable o, Object key) {
        if (key.equals(SettingsCommand.PORTAL_TEAM_PATH)) {
            resetDirInfo();
        }
    }

}
