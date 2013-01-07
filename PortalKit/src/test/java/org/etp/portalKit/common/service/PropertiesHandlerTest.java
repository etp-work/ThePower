package org.etp.portalKit.common.service;

import org.etp.portalKit.powerbuild.service.DirProvider;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The purpose of this class is test PropertiesHandler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-servlet.xml")
public class PropertiesHandlerTest {
    @javax.annotation.Resource(name = "propertiesHandler")
    private PropertiesHandler handler;

    /**
     * Test get functionality from PropertiesHandler.
     */
    @Test
    public void testGet() {
        Assert.assertEquals("E:\\Study\\portal-team", handler.get(DirProvider.PORTAL_TEAM_PATH));
    }

    /**
     * Test set functionality from PropertiesHandler.
     */
    @Test
    public void testSet() {
        String test = "testing";
        handler.set(DirProvider.PORTAL_TEAM_PATH, test);
        handler.write();
        handler.read();
        Assert.assertEquals(test, handler.get(DirProvider.PORTAL_TEAM_PATH));
    }

    /**
     * Reset mock configuration file after each case runs.
     */
    @Before
    public void before() {
        resetConfiguration();
    }

    /**
     * Reset mock configuration file after each case runs.
     */
    @After
    public void after() {
        resetConfiguration();
    }

    private void resetConfiguration() {
        handler.set(DirProvider.PORTAL_TEAM_PATH, "E:\\Study\\portal-team");
        handler.write();
    }
}
