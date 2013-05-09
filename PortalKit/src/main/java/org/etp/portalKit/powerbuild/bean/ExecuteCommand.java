package org.etp.portalKit.powerbuild.bean;

/**
 * The purpose of this class is to provide a Model that hold the
 * request from client.
 */
public class ExecuteCommand {
    private String selection;
    private boolean needDeploy;
    private boolean needBuild;
    private boolean needTest;

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
     * @return Returns the selection.
     */
    public String getSelection() {
        return selection;
    }

    /**
     * @param selection The selection to set.
     */
    public void setSelection(String selection) {
        this.selection = selection;
    }

    /**
     * @return Returns the needDeploy.
     */
    public boolean isNeedDeploy() {
        return needDeploy;
    }

    /**
     * @param needDeploy The needDeploy to set.
     */
    public void setNeedDeploy(boolean needDeploy) {
        this.needDeploy = needDeploy;
    }
}
