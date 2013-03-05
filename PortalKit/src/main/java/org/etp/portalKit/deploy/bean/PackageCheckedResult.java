package org.etp.portalKit.deploy.bean;

import java.util.List;

/**
 * This class hold the checked result to client.
 */
public class PackageCheckedResult {

    private List<String> allGzFiles;
    private List<String> referencePortal;
    private List<String> multiscreenPortal;

    /**
     * @return Returns the allGzFiles.
     */
    public List<String> getAllGzFiles() {
        return allGzFiles;
    }

    /**
     * @param allGzFiles The allGzFiles to set.
     */
    public void setAllGzFiles(List<String> allGzFiles) {
        this.allGzFiles = allGzFiles;
    }

    /**
     * @return Returns the referencePortal.
     */
    public List<String> getReferencePortal() {
        return referencePortal;
    }

    /**
     * @param referencePortal The referencePortal to set.
     */
    public void setReferencePortal(List<String> referencePortal) {
        this.referencePortal = referencePortal;
    }

    /**
     * @return Returns the multiscreenPortal.
     */
    public List<String> getMultiscreenPortal() {
        return multiscreenPortal;
    }

    /**
     * @param multiscreenPortal The multiscreenPortal to set.
     */
    public void setMultiscreenPortal(List<String> multiscreenPortal) {
        this.multiscreenPortal = multiscreenPortal;
    }
}
