package org.etp.portalKit.powerbuild.logic;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.shell.CommandResult;
import org.etp.portalKit.powerbuild.bean.DirTree;
import org.etp.portalKit.powerbuild.bean.Selection;
import org.etp.portalKit.powerbuild.service.BuildExecutor;
import org.etp.portalKit.powerbuild.service.DirProvider;
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

    /**
     * Build one package.
     * 
     * @param selection
     * @return CommandResult
     */
    public CommandResult build(String selection) {
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

        if (StringUtils.isBlank(absPath))
            throw new RuntimeException("Incorrect path to build.");
        return executor.compile(absPath);
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
