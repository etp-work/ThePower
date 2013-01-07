package org.etp.portalKit.test.service;

import org.etp.portalKit.test.bean.TestBean;
import org.springframework.stereotype.Component;

/**
 * The purpose of this class is provide a test service.
 */
@Component(value = "testservice")
public class TestService {

    /**
     * test
     */
    public void test(TestBean tb) {
        tb.setTestId(1);
        tb.setTestName("beifeng");
        System.out.println("TestBean = " + tb);
    }

    public void test2(TestBean tb) {
//        throw new RuntimeException("hello exception");
    }

}
