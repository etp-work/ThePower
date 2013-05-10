package org.etp.portalKit.powerbuild.bean;

/**
 * Deploy type to indicate whether if it is a reference portal
 * requirement or multiscreen portal.
 */
public enum DeployType {
    /**
     * A list of war files for reference portal to be deployed.
     */
    REFERENCE_PORTAL,

    /**
     * A list of war files for multiscreen portal to be deployed.
     */
    MULTISCREEN_PORTAL;
}
