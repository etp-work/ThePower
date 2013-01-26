package org.etp.portalKit.powerbuild.bean;

import java.util.List;
import java.util.Map;

/**
 * The purpose of this class is to provide deploy information.
 */
public class DeployInformation {
    private List<Map<String, String>> referencePortal;
    private List<Map<String, String>> multiscreenPortal;
    private List<Map<String, String>> framework;

    /**
     * @return Returns the referencePortal.
     */
    public List<Map<String, String>> getReferencePortal() {
        return referencePortal;
    }

    /**
     * @param referencePortal The referencePortal to set.
     */
    public void setReferencePortal(List<Map<String, String>> referencePortal) {
        this.referencePortal = referencePortal;
    }

    /**
     * @return Returns the multiscreenPortal.
     */
    public List<Map<String, String>> getMultiscreenPortal() {
        return multiscreenPortal;
    }

    /**
     * @param multiscreenPortal The multiscreenPortal to set.
     */
    public void setMultiscreenPortal(List<Map<String, String>> multiscreenPortal) {
        this.multiscreenPortal = multiscreenPortal;
    }

    /**
     * @return Returns the framework.
     */
    public List<Map<String, String>> getFramework() {
        return framework;
    }

    /**
     * @param framework The framework to set.
     */
    public void setFramework(List<Map<String, String>> framework) {
        this.framework = framework;
    }

}
