package org.etp.portalKit.powerbuild.bean;

import java.util.List;
import java.util.Map;

/**
 * BuildInformation will be shown on build page.
 */
public class BuildInformation {

    private List<DirTree> buildList;
    private Map<String, List<String>> deployInfo;

    /**
     * Creates a new instance of <code>BuildInformation</code>. T
     */
    public BuildInformation() {
        //        
    }

    /**
     * Creates a new instance of <code>BuildInformation</code>.
     * 
     * @param list
     * @param deploy
     */
    public BuildInformation(List<DirTree> list, Map<String, List<String>> deploy) {
        this.buildList = list;
        this.deployInfo = deploy;
    }

    /**
     * @return Returns the buildList.
     */
    public List<DirTree> getBuildList() {
        return buildList;
    }

    /**
     * @param buildList The buildList to set.
     */
    public void setBuildList(List<DirTree> buildList) {
        this.buildList = buildList;
    }

    /**
     * @return Returns the deployInfo.
     */
    public Map<String, List<String>> getDeployInfo() {
        return deployInfo;
    }

    /**
     * @param deployInfo The deployInfo to set.
     */
    public void setDeployInfo(Map<String, List<String>> deployInfo) {
        this.deployInfo = deployInfo;
    }
}
