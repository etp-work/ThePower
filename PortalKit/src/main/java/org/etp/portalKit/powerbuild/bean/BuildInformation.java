package org.etp.portalKit.powerbuild.bean;

import java.util.List;

/**
 * BuildInformation will be shown on build page.
 */
public class BuildInformation {

    private List<DirTree> buildList;
    private DeployInformation deployInfo;

    /**
     * Creates a new instance of <code>BuildInformation</code>. T
     */
    public BuildInformation() {
        //        
    }

    /**
     * Creates a new instance of <code>BuildInformation</code>.
     * 
     * @param list list of DirTree
     * @param deploy type/names mapping of deployment information.
     */
    public BuildInformation(List<DirTree> list, DeployInformation deploy) {
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
    public DeployInformation getDeployInfo() {
        return deployInfo;
    }

    /**
     * @param deployInfo The deployInfo to set.
     */
    public void setDeployInfo(DeployInformation deployInfo) {
        this.deployInfo = deployInfo;
    }
}
