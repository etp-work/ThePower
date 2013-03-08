package org.etp.portalKit.powerbuild.service;

import java.util.List;
import java.util.Observer;

import org.etp.portalKit.powerbuild.bean.DirTree;

/**
 * The purpose of this class is to provide a interface to control the
 * data of BuildList.
 */
public interface BuildListProvider extends Observer {
    /**
     * retrieve the workspace structure from specified folder.
     * 
     * @return List<DirTree>
     */
    public List<DirTree> retrieveDirTrees();
}
