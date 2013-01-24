package org.etp.portalKit.common.bean;

/**
 * This class hold a convertion to package information.
 */
public class PackageInfo {
    private String packageName;
    private String packagePathInDesignHome;

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
     * @return Returns the packagePathInDesignHome.
     */
    public String getPackagePathInDesignHome() {
        return packagePathInDesignHome;
    }

    /**
     * @param packagePathInDesignHome The packagePathInDesignHome to
     *            set.
     */
    public void setPackagePathInDesignHome(String packagePathInDesignHome) {
        this.packagePathInDesignHome = packagePathInDesignHome;
    }

}
