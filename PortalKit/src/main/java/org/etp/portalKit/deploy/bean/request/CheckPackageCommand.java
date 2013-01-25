package org.etp.portalKit.deploy.bean.request;

/**
 * The class hold the data convertion from http request for checking
 * package.
 */
public class CheckPackageCommand {
    private String downloadPath;
    private String typeToDeploy;

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
