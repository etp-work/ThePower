package org.etp.portalKit.powerbuild.bean;

/**
 * ExecuteSingleCommand only works for common build.
 */
public class ExecuteSingleCommand extends ExecuteCommand {
    private String absolutePath;

    /**
     * @return Returns the absolutePath.
     */
    public String getAbsolutePath() {
        return absolutePath;
    }

    /**
     * @param absolutePath The absolutePath to set.
     */
    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }
}
