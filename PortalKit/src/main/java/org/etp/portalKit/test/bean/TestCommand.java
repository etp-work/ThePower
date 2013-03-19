package org.etp.portalKit.test.bean;

import java.util.List;

public class TestCommand {

    private String targetIP;
    private String targetPort;
    private String targetContextPath;
    private List<String> cases;

    /**
     * @return Returns the targetIP.
     */
    public String getTargetIP() {
        return targetIP;
    }

    /**
     * @param targetIP The targetIP to set.
     */
    public void setTargetIP(String targetIP) {
        this.targetIP = targetIP;
    }

    /**
     * @return Returns the targetPort.
     */
    public String getTargetPort() {
        return targetPort;
    }

    /**
     * @param targetPort The targetPort to set.
     */
    public void setTargetPort(String targetPort) {
        this.targetPort = targetPort;
    }

    /**
     * @return Returns the targetContextPath.
     */
    public String getTargetContextPath() {
        return targetContextPath;
    }

    /**
     * @param targetContextPath The targetContextPath to set.
     */
    public void setTargetContextPath(String targetContextPath) {
        this.targetContextPath = targetContextPath;
    }

    /**
     * @return Returns the cases.
     */
    public List<String> getCases() {
        return cases;
    }

    /**
     * @param cases The cases to set.
     */
    public void setCases(List<String> cases) {
        this.cases = cases;
    }

}
