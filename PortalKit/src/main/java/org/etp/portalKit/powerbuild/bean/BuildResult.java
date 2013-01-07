package org.etp.portalKit.powerbuild.bean;

import org.etp.portalKit.common.shell.CommandResult;

/**
 * The purpose of this class is a subclass of CommandResult, it hold
 * an extra parameter that indicate the specified war file is deployed
 * or not.
 */
public class BuildResult extends CommandResult {
    private boolean isDeployed;

    /**
     * @return Returns the isDeployed.
     */
    public boolean isDeployed() {
        return isDeployed;
    }

    /**
     * @param isDeployed The isDeployed to set.
     */
    public void setDeployed(boolean isDeployed) {
        this.isDeployed = isDeployed;
    }
}
