package org.etp.portalKit.powerbuild.bean;

/**
 * ExecuteMultiCommand only works for env build.
 */
public class ExecuteMultiCommand extends ExecuteCommand {
    private DeployType type;

    /**
     * @return Returns the type.
     */
    public DeployType getType() {
        return type;
    }

    /**
     * @param type The type to set.
     */
    public void setType(DeployType type) {
        this.type = type;
    }
}
