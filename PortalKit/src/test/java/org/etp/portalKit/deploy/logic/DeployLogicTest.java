package org.etp.portalKit.deploy.logic;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;

import org.etp.portalKit.common.service.DeployService;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.deploy.bean.CheckPackageCommand;
import org.etp.portalKit.deploy.bean.DeployInfo4CI;
import org.etp.portalKit.deploy.bean.PackageCheckedResult;
import org.etp.portalKit.deploy.bean.PackageInfo4CI;
import org.etp.portalKit.deploy.service.PackagesCheckService;
import org.junit.Test;

/**
 * 
 * A test case for <code>DeployLogic</code>
 * 
 * @author ehaozuo
 * 
 */
public class DeployLogicTest {
	@Tested
	private DeployLogic logic;

	@Injectable
	private PackagesCheckService packageCheck;

	@Injectable
	private PropertiesManager prop;

	@Injectable
	private DeployService deployService;

	/**
	 * 
	 */
	@Test(expected = RuntimeException.class)
	public void setPathAndRetrieveincludedPkgsWithoutDownloadPath_test() {
		CheckPackageCommand cmd = new CheckPackageCommand();
		logic.setPathAndRetrieveincludedPkgs(cmd);
	}

	/**
	 * 
	 */
	@Test
	public void setPathAndRetrieveincludedPkgsWithNonDirDownloadPath_test() {
		CheckPackageCommand cmd = new CheckPackageCommand();
		String downloadPath = "/c/Users/ehaozuo/downloads/";
		cmd.setDownloadPath(downloadPath);
		PackageCheckedResult res = logic.setPathAndRetrieveincludedPkgs(cmd);
		assertNull(res.getAllGzFiles());
		assertNull(res.getMultiscreenPortal());
		assertNull(res.getReferencePortal());
	}

	/**
	 * @param file
	 *            File to be mocked for isDirectory method.
	 * @param deployInfo
	 *            DeployInfo need be mocked.
	 * 
	 */
	@Test
	public void setPathAndRetrieveincludedPkgs_test(
			@Mocked(methods = { "isDirectory" }) final File file,
			@Mocked(methods = { "getReferencePortal", "getMultiscreenPortal" }) final DeployInfo4CI deployInfo) {
		final String downloadPath = "/c/Users/ehaozuo/downloads/";
		CheckPackageCommand cmd = new CheckPackageCommand();
		cmd.setDownloadPath(downloadPath);

		final List<PackageInfo4CI> refPacks = new ArrayList<PackageInfo4CI>();
		PackageInfo4CI ref = new PackageInfo4CI();
		ref.setPackageName("coreapps");
		refPacks.add(ref);

		final List<PackageInfo4CI> mulPacks = new ArrayList<PackageInfo4CI>();
		PackageInfo4CI mul = new PackageInfo4CI();
		mul.setPackageName("multiscreen");
		mulPacks.add(mul);

		new Expectations() {
			{
				file.isDirectory();
				result = true;
				packageCheck.retrieveDeployedPkgs(downloadPath);
				result = new File[] { new File("gz1") };
				packageCheck.retrieveRelationShip();
				result = deployInfo;
				deployInfo.getReferencePortal();
				result = refPacks;
				deployInfo.getMultiscreenPortal();
				result = mulPacks;
			}
		};
		PackageCheckedResult res = logic.setPathAndRetrieveincludedPkgs(cmd);
		assertSame(1, res.getAllGzFiles().size());
		assertSame(1, res.getMultiscreenPortal().size());
		assertSame(1, res.getReferencePortal().size());
	}
}
