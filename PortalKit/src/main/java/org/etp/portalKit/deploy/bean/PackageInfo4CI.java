package org.etp.portalKit.deploy.bean;

import java.util.List;

/**
 * This class hold data from deployInfo4Ci.json about package part.
 */
public class PackageInfo4CI {
    private String packageName;
    private String relativePath;
    private List<String> warfiles;

    /**
     * @return Returns the packageName.
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName The packageName to set.
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * @return Returns the relativePath.
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * @param relativePath The relativePath to set.
     */
    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    /**
     * @return Returns the warfiles.
     */
    public List<String> getWarfiles() {
        return warfiles;
    }

    /**
     * @param warfiles The warfiles to set.
     */
    public void setWarfiles(List<String> warfiles) {
        this.warfiles = warfiles;
    }

}
