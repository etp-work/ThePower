package org.etp.portalKit.powerbuild.bean;

import org.etp.portalKit.common.util.CommandResult;

/**
 * The purpose of this class is a subclass of CommandResult, it hold
 * an extra parameter that indicate the specified war file is deployed
 * or not.
 */
public class BuildResult extends CommandResult {
    private boolean deployed;

    /**
     * Creates a new instance of <code>BuildResult</code>.
     */
    public BuildResult() {
        //        
    }

    /**
     * Creates a new instance of <code>BuildResult</code>.
     * 
     * @param cmd wrap from a CommandResult
     */
    public BuildResult(CommandResult cmd) {
        super.setMessage(cmd.getMessage());
        super.setStateCode(cmd.getStateCode());
        super.setSuccess(cmd.isSuccess());
    }

    /**
     * @return Returns the isDeployed.
     */
    public boolean isDeployed() {
        return deployed;
    }

    /**
     * @param isDeployed The isDeployed to set.
     */
    public void setDeployed(boolean isDeployed) {
        this.deployed = isDeployed;
    }
}
