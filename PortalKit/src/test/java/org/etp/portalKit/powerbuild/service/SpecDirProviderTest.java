//package org.etp.portalKit.powerbuild.service;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.List;
//
//import javax.annotation.Resource;
//
//import org.etp.portalKit.powerbuild.bean.response.DirTree;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * Test SpecDirPrivider
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "/test-servlet.xml")
//@Ignore
//public class SpecDirProviderTest {
//    @Resource(name = "specProvider")
//    private SpecDirProvider specProvider;
//
//    /**
//     * 
//     */
//    @Test
//    public void testRetrieveFwAbstolutePath() {
//        List<DirTree> retrieveDirInfo = specProvider.retrieveDirInfo();
//        String fwAbsPath = retrieveDirInfo.get(0).getAbsolutePath();
//        assertEquals(fwAbsPath, "");
//    }
//
//    /**
//     * 
//     */
//    @Test
//    public void testRetrieveMvnPluginAbstolutePath() {
//        List<DirTree> retrieveDirInfo = specProvider.retrieveDirInfo();
//        String mvnPluginPath = retrieveDirInfo.get(0).getSubDirs().get(0).getAbsolutePath();
//        assertEquals("E:\\Study\\portal-team\\maven-plugins", mvnPluginPath);
//    }
//}
