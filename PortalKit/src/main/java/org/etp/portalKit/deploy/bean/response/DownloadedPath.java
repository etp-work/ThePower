package org.etp.portalKit.deploy.bean.response;

import org.etp.portalKit.fw.annotation.MarkinFile;

/**
 * This class hold data to give response to client for downloadpath.
 */
public class DownloadedPath {

    /**
     * <code>DOWNLOAD_CI_PACKAGE_PATH</code> path of downloaded
     * packages from CI.
     */
    public static final String DOWNLOAD_CI_PACKAGE_PATH = "ci_packages_download_path";

    @MarkinFile(name = DOWNLOAD_CI_PACKAGE_PATH)
    private String downloadedPath;

    /**
     * @return Returns the downloadedPath.
     */
    public String getDownloadedPath() {
        return downloadedPath;
    }

    /**
     * @param downloadedPath The downloadedPath to set.
     */
    public void setDownloadedPath(String downloadedPath) {
        this.downloadedPath = downloadedPath;
    }
}
