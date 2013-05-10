package org.etp.portalKit.powerbuild.bean;

/**
 * The purpose of this class is to provide a Model that hold the
 * request from client.
 */
public class ExecuteCommand {
    private boolean needDeploy;
    private boolean needBuild;
    private boolean needTest;

    /**
     * @return Returns the needDeploy.
     */
    public boolean isNeedDeploy() {
        return needDeploy;
    }

    /**
     * @return Returns the needTest.
     */
    public boolean isNeedTest() {
        return needTest;
    }

    /**
     * @param needTest The needTest to set.
     */
    public void setNeedTest(boolean needTest) {
        this.needTest = needTest;
    }

    /**
     * @return Returns the needBuild.
     */
    public boolean isNeedBuild() {
        return needBuild;
    }

    /**
     * @param needBuild The needBuild to set.
     */
    public void setNeedBuild(boolean needBuild) {
        this.needBuild = needBuild;
    }

    /**
     * @param needDeploy The needDeploy to set.
     */
    public void setNeedDeploy(boolean needDeploy) {
        this.needDeploy = needDeploy;
    }
}
