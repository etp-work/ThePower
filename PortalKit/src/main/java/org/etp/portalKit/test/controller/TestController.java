package org.etp.portalKit.test.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.etp.portalKit.common.service.PropertiesManager;
import org.etp.portalKit.common.util.FileUtils;
import org.etp.portalKit.setting.bean.SettingsCommand;
import org.etp.portalKit.test.bean.DataCommand;
import org.etp.portalKit.test.bean.TestCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Hold all the request that related to settings.
 */
@Controller
public class TestController {

	@Resource(name = "propertiesManager")
	private PropertiesManager prop;

	private static final HttpClient httpclient = new HttpClient();

	private String getDefatltIP() {
		return "127.0.0.1";
	}

	private String getDefaultPort() {
		return "8080";
	}

	private String getDefaultContextPath() {
		String webapps = prop.get(SettingsCommand.TOMCAT_WEBAPPS_PATH);
		if (StringUtils.isBlank(webapps))
			return "portal-testserver-war-14.1-SNAPSHOT";
		File[] testservers = FileUtils.FolderFinder(webapps,
				"portal-testserver-war");
		return testservers[0].getName();
	}

	private String getDataRequestURL() {
		return "public/test/data.ajax";
	}

	private String getStartTestRequestURL() {
		return "public/test/startTest.ajax?async=ture";
	}

	/**
	 * @param cmd
	 *            DataCommand
	 * @return auto testcases
	 * @throws HttpException
	 *             error when Http exception occurs.
	 * @throws IOException
	 *             error when IOException occurs.
	 */
	@RequestMapping(value = "/test/data.ajax", method = RequestMethod.POST)
	public @ResponseBody
	String getData(@RequestBody DataCommand cmd) throws HttpException,
			IOException {
		String requestIP = null != cmd.getTargetIP() ? cmd.getTargetIP()
				: getDefatltIP();
		String requestPort = null != cmd.getTargetPort() ? cmd.getTargetPort()
				: getDefaultPort();
		String requestContextPath = null != cmd.getTargetContextPath() ? cmd
				.getTargetContextPath() : getDefaultContextPath();
		String requestURL = String.format("http://%s:%s/%s/%s", requestIP,
				requestPort, requestContextPath, getDataRequestURL());
		return sendGetRequest(requestURL);
	}

	/**
	 * @param cmd
	 *            TestCommand
	 * @return nothing
	 * @throws HttpException
	 *             error when Http exception occurs.
	 * @throws IOException
	 *             error when IOException occurs.
	 */
	@RequestMapping(value = "/test/start.ajax", method = RequestMethod.POST)
	public @ResponseBody
	String startTest(@RequestBody TestCommand cmd) throws HttpException,
			IOException {
		String requestIP = null != cmd.getTargetIP() ? cmd.getTargetIP()
				: getDefatltIP();
		String requestPort = null != cmd.getTargetPort() ? cmd.getTargetPort()
				: getDefaultPort();
		String requestContextPath = null != cmd.getTargetContextPath() ? cmd
				.getTargetContextPath() : getDefaultContextPath();

		StringBuffer cases = new StringBuffer();
		for (String caseId : cmd.getCases()) {
			cases.append("|" + caseId);
		}
		if (cases.length() > 0) {
			cases.delete(0, 1);
		}

		String requestURL = String.format("http://%s:%s/%s/%s", requestIP,
				requestPort, requestContextPath, getStartTestRequestURL());

		requestURL += "&cases=" + URLEncoder.encode(cases.toString(), "utf-8");

		return sendGetRequest(requestURL);
	}

	/**
	 * @param url
	 * @return execute result
	 * @throws HttpException
	 * @throws IOException
	 */
	protected String sendGetRequest(String url) throws HttpException,
			IOException {
		return execMethod(new GetMethod(url));
	}

	/**
	 * @param url
	 * @return execute result
	 * @throws HttpException
	 * @throws IOException
	 */
	protected String sendPostRequest(String url) throws HttpException,
			IOException {
		return execMethod(new PostMethod(url));
	}

	/**
	 * @param method
	 * @return execute result
	 * @throws HttpException
	 * @throws IOException
	 */
	protected String execMethod(HttpMethodBase method) throws HttpException,
			IOException {
		String result = null;
		try {
			int statusCode = httpclient.executeMethod(method);
			if (statusCode != HttpStatus.SC_OK) {
				throw new HttpException("Failed to get response!");
			}
			result = method.getResponseBodyAsString();
		} catch (HttpException e) {
			throw new IllegalStateException(e.toString());
		} catch (IOException e) {
			throw new IllegalStateException(e.toString());
		} finally {
			method.releaseConnection();
		}
		return result;
	}
}
