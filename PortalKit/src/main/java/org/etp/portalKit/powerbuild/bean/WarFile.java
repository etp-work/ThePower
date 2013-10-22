package org.etp.portalKit.powerbuild.bean;


/**
 * The purpose of this class is to provide WarFile data.
 */
public class WarFile {
    private String packageName;
    private String relativePath;
    private String[] dependencies;

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
     * @return Returns the dependencies.
     */
    public String[] getDependencies() {
        return dependencies;
    }

    /**
     * @param dependencies The dependencies to set.
     */
    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }
}
