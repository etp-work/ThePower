package org.etp.portalKit.tomcatmonitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.etp.portalKit.common.service.ProcessMonitor;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.FileUtils;
import org.etp.portalKit.common.util.ShellRunner;
import org.etp.portalKit.setting.bean.SettingsCommand;
import org.etp.portalKit.tomcatmonitor.logic.TomcatLogic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * test <code>TomcatLogicTest</code>
 */
@RunWith(MockitoJUnitRunner.class)
public class TomcatLogicTest {

	@InjectMocks
	private TomcatLogic logic;

	@Mock
	private ProcessMonitor monitor;

	@Mock
	private PropertiesManager prop;

	@Mock
	private ShellRunner runner;

	@Mock
	private FileUtils fileUtils;

	/**
	 * test canBeControlled method without tomcat webapps path configured
	 */
	@Test
	public void canBeControlledWithoutTomcatConfigured_test() {
		when(prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH)).thenReturn(null);
		try {
			logic.canBeControlled();
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("You have set tomcat's webapps path first.",
					e.getMessage());
		}
	}

	/**
	 * test canBeControlled method with incorrect tomcat webapps path configured
	 */
	@Test
	public void canBeControlledWithoutCorrectTomcatPath_test() {
		String webapps = "C:\\aaa";
		when(prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH)).thenReturn(webapps);
		when(fileUtils.isDirectory(webapps)).thenReturn(false);
		try {
			logic.canBeControlled();
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("You have set a incorrect tomcat's webapps path.",
					e.getMessage());
		}
	}

	/**
	 * test canBeControlled method with incorrect tomcat bin path
	 */
	@Test
	public void canBeControlledWithoutCorrectTomcatBin_test() {
		String webapps = "C:\\aaa";
		File parent = mock(File.class);
		File bin = mock(File.class);

		when(prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH)).thenReturn(webapps);
		when(fileUtils.isDirectory(webapps)).thenReturn(true);
		when(fileUtils.createFile(webapps)).thenReturn(parent);
		when(parent.getParent()).thenReturn("parent");
		when(fileUtils.createFile("parent", "bin")).thenReturn(bin);
		when(bin.isDirectory()).thenReturn(false);
		try {
			logic.canBeControlled();
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertEquals("You have set a incorrect tomcat path.",
					e.getMessage());
		}
	}

	/**
	 * test canBeControlled method
	 */
	@Test
	public void canBeControlled_test() {
		String webapps = "C:\\aaa";
		File parent = mock(File.class);
		File bin = mock(File.class);
		File startup = mock(File.class);

		when(prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH)).thenReturn(webapps);
		when(fileUtils.isDirectory(webapps)).thenReturn(true);
		when(fileUtils.createFile(webapps)).thenReturn(parent);
		when(parent.getParent()).thenReturn("parent");
		when(fileUtils.createFile("parent", "bin")).thenReturn(bin);
		when(bin.isDirectory()).thenReturn(true);
		when(fileUtils.createFile(bin, "startup.bat")).thenReturn(startup);
		when(startup.exists()).thenReturn(true);
		logic.canBeControlled();
		verify(startup).exists();
	}
}
