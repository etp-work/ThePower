package org.etp.portalKit.test.bean;

import java.util.List;

/**
 * TestSuite
 */
public class TestSuite {

    private String id;
    private String name;
    private String version;

    private List<TestCase> testCases;

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return Returns the testCases.
     */
    public List<TestCase> getTestCases() {
        return testCases;
    }

    /**
     * @param testCases The testCases to set.
     */
    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

}
