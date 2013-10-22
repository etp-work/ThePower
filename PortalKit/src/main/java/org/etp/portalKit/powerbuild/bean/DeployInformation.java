package org.etp.portalKit.powerbuild.bean;

/**
 * The purpose of this class is to provide deploy information.
 */
public class DeployInformation {
    private WarFile[] referencePortal;
    private WarFile[] multiscreenPortal;

    /**
     * @return Returns the referencePortal.
     */
    public WarFile[] getReferencePortal() {
        return referencePortal;
    }

    /**
     * @param referencePortal The referencePortal to set.
     */
    public void setReferencePortal(WarFile[] referencePortal) {
        this.referencePortal = referencePortal;
    }

    /**
     * @return Returns the multiscreenPortal.
     */
    public WarFile[] getMultiscreenPortal() {
        return multiscreenPortal;
    }

    /**
     * @param multiscreenPortal The multiscreenPortal to set.
     */
    public void setMultiscreenPortal(WarFile[] multiscreenPortal) {
        this.multiscreenPortal = multiscreenPortal;
    }

}
