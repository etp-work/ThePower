package org.etp.portalKit.powerbuild.service;

import java.util.List;

import org.etp.portalKit.powerbuild.bean.DirTree;

/**
 * The purpose of this class is a interface indicates directory
 * information in DirTree.
 */
public interface DirProvider {
    /**
     * @return list of DirTree, retrieve specified directory
     *         information due to configured path.
     */
    public List<DirTree> retrieveDirInfo();
}
