package org.etp.portalKit.deploy.bean;

import java.util.List;

/**
 * This class hold the data from deployInfo4CI.json.
 */
public class DeployInfo4CI {
    private List<PackageInfo4CI> referencePortal;
    private List<PackageInfo4CI> multiscreenPortal;

    /**
     * @return Returns the referencePortal.
     */
    public List<PackageInfo4CI> getReferencePortal() {
        return referencePortal;
    }

    /**
     * @param referencePortal The referencePortal to set.
     */
    public void setReferencePortal(List<PackageInfo4CI> referencePortal) {
        this.referencePortal = referencePortal;
    }

    /**
     * @return Returns the multiscreenPortal.
     */
    public List<PackageInfo4CI> getMultiscreenPortal() {
        return multiscreenPortal;
    }

    /**
     * @param multiscreenPortal The multiscreenPortal to set.
     */
    public void setMultiscreenPortal(List<PackageInfo4CI> multiscreenPortal) {
        this.multiscreenPortal = multiscreenPortal;
    }
}
