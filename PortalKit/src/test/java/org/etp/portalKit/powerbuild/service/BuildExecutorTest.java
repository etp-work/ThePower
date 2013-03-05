package org.etp.portalKit.powerbuild.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.etp.portalKit.common.util.CommandResult;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The purpose of this class is to test BuildExecutor.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-servlet.xml")
@Ignore
public class BuildExecutorTest {
    @Resource(name = "buildExecutor")
    private BuildExecutor executor;

    /**
     * test compile
     */
    @Test
    public void testExecutor() {
        CommandResult compile = executor.compile("E:\\Study\\GitHub\\workstation\\PortalKit");
        assertTrue(compile.isSuccess());
        assertEquals(0, compile.getStateCode());
    }
}
