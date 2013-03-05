package org.etp.portalKit.deploy.bean;

import java.util.List;

/**
 * The class hold the data convertion from http request for checking
 * package.
 */
public class CheckPackageCommand {
    private String downloadPath;
    private String typeToDeploy;
    private List<String> deployPackages;

    /**
     * @return Returns the deployPackages.
     */
    public List<String> getDeployPackages() {
        return deployPackages;
    }

    /**
     * @param deployPackages The deployPackages to set.
     */
    public void setDeployPackages(List<String> deployPackages) {
        this.deployPackages = deployPackages;
    }

    /**
     * @return Returns the downloadPath.
     */
    public String getDownloadPath() {
        return downloadPath;
    }

    /**
     * @param downloadPath The downloadPath to set.
     */
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    /**
     * @return Returns the typeToDeploy.
     */
    public String getTypeToDeploy() {
        return typeToDeploy;
    }

    /**
     * @param typeToDeploy The typeToDeploy to set.
     */
    public void setTypeToDeploy(String typeToDeploy) {
        this.typeToDeploy = typeToDeploy;
    }
}
