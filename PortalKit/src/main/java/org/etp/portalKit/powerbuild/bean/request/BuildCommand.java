package org.etp.portalKit.powerbuild.bean.request;

/**
 * The purpose of this class is to provide a Model that hold the
 * request from client.
 */
public class BuildCommand {
    private String selection;
    private boolean needDeploy;

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
