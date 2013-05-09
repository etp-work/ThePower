package org.etp.portalKit.powerbuild.service;

/**
 */
public enum ExecuteType {
    /**
     * execute <code>mvn clean install -Dmaven.test.skip=true</code>
     * command to the source code.
     */
    COMPILE,

    /**
     * execute <code>mvn clean test</code> command to the source code.
     */
    TEST,

    /**
     * execute <code>mvn clean install</code> to the source code.
     */
    COMPILE_TEST;
}
