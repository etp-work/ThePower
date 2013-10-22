package org.etp.portalKit.powerbuild.bean;


/**
 * The purpose of this class is to provide a Model that hold the
 * request from client.
 */
public class ExecuteParam {
    private boolean deploy;
    private boolean build;
    private boolean test;

    /**
     * @return Returns the deploy.
     */
    public boolean isDeploy() {
        return deploy;
    }

    /**
     * @param deploy The deploy to set.
     */
    public void setDeploy(boolean deploy) {
        this.deploy = deploy;
    }

    /**
     * @return Returns the build.
     */
    public boolean isBuild() {
        return build;
    }

    /**
     * @param build The build to set.
     */
    public void setBuild(boolean build) {
        this.build = build;
    }

    /**
     * @return Returns the test.
     */
    public boolean isTest() {
        return test;
    }

    /**
     * @param test The test to set.
     */
    public void setTest(boolean test) {
        this.test = test;
    }

}
