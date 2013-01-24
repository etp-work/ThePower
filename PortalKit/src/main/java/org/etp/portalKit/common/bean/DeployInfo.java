package org.etp.portalKit.common.bean;

import java.util.List;

/**
 * The purpose of this class is to provide deploy information.
 */
public class DeployInfo {
    private List<PackageInfo> referencePortal;
    private List<PackageInfo> multiscreen;
    private List<PackageInfo> framework;

    /**
     * @return Returns the referencePortal.
     */
    public List<PackageInfo> getReferencePortal() {
        return referencePortal;
    }

    /**
     * @param referencePortal The referencePortal to set.
     */
    public void setReferencePortal(List<PackageInfo> referencePortal) {
        this.referencePortal = referencePortal;
    }

    /**
     * @return Returns the multiscreen.
     */
    public List<PackageInfo> getMultiscreen() {
        return multiscreen;
    }

    /**
     * @param multiscreen The multiscreen to set.
     */
    public void setMultiscreen(List<PackageInfo> multiscreen) {
        this.multiscreen = multiscreen;
    }

    /**
     * @return Returns the framework.
     */
    public List<PackageInfo> getFramework() {
        return framework;
    }

    /**
     * @param framework The framework to set.
     */
    public void setFramework(List<PackageInfo> framework) {
        this.framework = framework;
    }
}
