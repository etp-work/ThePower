package org.etp.portalKit.clean.logic;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;

import org.etp.portalKit.clean.bean.CleanItems;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.FileUtils;
import org.etp.portalKit.setting.bean.SettingsCommand;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * A test case for <code>CleanLogic</code>
 */
public class CleanLogicTest {
	@Tested
	CleanLogic logic;
	@Injectable
	PropertiesManager prop;

	/**
	 * test retrieveCleanItems without tomcat webapps path configured.
	 * 
	 * @param system
	 *            System class to be mocked for static methods
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void retrieveCleanItemsWithoutUserhome_test(
			@SuppressWarnings("unused") @Mocked final System system) {
		new Expectations() {
			{
				System.getProperty("user.home");
				result = null;
				prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
				result = "home";
			}
		};
		logic.retrieveCleanItems();

	}

	/**
	 * test retrieveCleanItems without tomcat webapps path configured.
	 * 
	 */
	@Test
	public void retrieveCleanItemsWithoutTomcatPath_test() {
		new Expectations() {
			{

				prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
				result = null;
			}
		};
		CleanItems retrieveCleanItems = logic.retrieveCleanItems();

		assertNull(retrieveCleanItems.getWarFiles());
		assertNull(retrieveCleanItems.getWidgetCaches());
	}

	/**
	 * test retrieveCleanItems.
	 * @param sys System class to be mocked for static methods
	 * @param cacheBase intend to mock getAbsolutePath method.
	 * @param util intend to mock static methods in FileUtils
	 * 
	 */
	@SuppressWarnings("unused") 
	@Test
	public void retrieveCleanItems_test(@Mocked System sys,
			@Mocked(methods = { "getAbsolutePath" }) final File cacheBase,
			@Mocked final FileUtils util) {
		final String WIDGET_PREFIX = "Widget MSDK Widget";

		final String userHome = "/c/users/haozuo/";
		final String webappsHome = "/c/designenv/tomcat/";
		final String operaAbsPath = "/c/users/haozuo/opera/";
		new Expectations() {
			{
				System.getProperty("user.home");
				result = userHome;
				prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
				result = webappsHome;
				cacheBase.getAbsolutePath();
				result = operaAbsPath;
				FileUtils.FolderFinder(operaAbsPath, WIDGET_PREFIX);
				result = new File[] { new File("cache1") };
				FileUtils.FileFinder(webappsHome, anyString, ".war");
				times = 4;
				result = new File[] { new File("war1") };
			}
		};
		CleanItems retrieveCleanItems = logic.retrieveCleanItems();
		assertSame(1, retrieveCleanItems.getWidgetCaches().size());
		assertSame(4, retrieveCleanItems.getWarFiles().size());
	}

	/**
	 * 
	 */
	@Test
	@Ignore
	public void test() {
		final String webappsHome = "/c/designenv/tomcat/";
		new Expectations() {
			{
				prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
				result = webappsHome;
			}
		};
		new MockUp<File>() {
			@Mock(invocations = 1)
			public String getAbsolutePath() {
				System.out.println("nidie de chenggongla !?");
				return "nidaye";
			}
		};
		logic.retrieveCleanItems();
	}
}
