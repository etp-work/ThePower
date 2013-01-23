package org.etp.portalKit.test.framework;

import java.util.List;

public class TestSuite {

    private String id;
    private String name;
    private String version;

    private List<TestCase> testCases;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

}
