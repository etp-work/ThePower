package org.etp.portalKit.powerbuild.bean;

/**
 * BuildCommand works for build.
 */
public class BuildCommand {
    private WarFile warFile;

    private ExecuteParam param;

    /**
     * @return Returns the warFile.
     */
    public WarFile getWarFile() {
        return warFile;
    }

    /**
     * @param warFile The warFile to set.
     */
    public void setWarFile(WarFile warFile) {
        this.warFile = warFile;
    }

    /**
     * @return Returns the param.
     */
    public ExecuteParam getParam() {
        return param;
    }

    /**
     * @param param The param to set.
     */
    public void setParam(ExecuteParam param) {
        this.param = param;
    }

}
