package org.etp.portalKit.common.service;

import org.etp.portalKit.setting.bean.Settings;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The purpose of this class is test PropertiesHandler
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-servlet.xml")
@Ignore
public class PropertiesHandlerTest {
    @javax.annotation.Resource(name = "propertiesManager")
    private PropertiesManager handler;

    /**
     * Test get functionality from PropertiesHandler.
     */
    @Test
    public void testGet() {
        Assert.assertEquals("E:\\Study\\portal-team", handler.get(Settings.PORTAL_TEAM_PATH));
    }

    /**
     * Test set functionality from PropertiesHandler.
     */
    @Test
    public void testSet() {
        String test = "testing";
        handler.set(Settings.PORTAL_TEAM_PATH, test);
        handler.write();
        handler.read();
        Assert.assertEquals(test, handler.get(Settings.PORTAL_TEAM_PATH));
    }

    /**
     * test fromBean method
     */
    @Test
    public void testFromBean() {
        Settings set = new Settings();
        set.setPortalTeamPath("E:\\Study\\portal-team");
        set.setTomcatWebappsPath("C:\\Users\\ehaozuo\\designenv\\apache-tomcat-7.0.30\\webapps");
        handler.fromBean(set);
        Assert.assertEquals("E:\\Study\\portal-team", handler.get(Settings.PORTAL_TEAM_PATH));
        Assert.assertEquals("C:\\Users\\ehaozuo\\designenv\\apache-tomcat-7.0.30\\webapps",
                handler.get(Settings.TOMCAT_WEBAPPS_PATH));
    }

    /**
     * test fromBean method
     */
    @Test
    public void testToBean() {
        Settings set1 = new Settings();
        set1.setPortalTeamPath("E:\\Study\\portal-team");
        set1.setTomcatWebappsPath("C:\\Users\\ehaozuo\\designenv\\apache-tomcat-7.0.30\\webapps");
        handler.fromBean(set1);
        handler.write();
        Settings set2 = new Settings();
        handler.toBean(set2);
        Assert.assertEquals("E:\\Study\\portal-team", set2.getPortalTeamPath());
        Assert.assertEquals("C:\\Users\\ehaozuo\\designenv\\apache-tomcat-7.0.30\\webapps", set2.getTomcatWebappsPath());
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
        handler.set(Settings.PORTAL_TEAM_PATH, "E:\\Study\\portal-team");
        handler.write();
    }
}
