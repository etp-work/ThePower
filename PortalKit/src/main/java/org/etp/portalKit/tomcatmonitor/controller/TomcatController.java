package org.etp.portalKit.tomcatmonitor.controller;

import javax.annotation.Resource;

import org.etp.portalKit.tomcatmonitor.logic.TomcatLogic;
import org.etp.portalKit.tomcatmonitor.service.TomcatStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <code>TomcatController</code> handle all the request from client that related
 * with tomcat.
 */
@Controller
public class TomcatController {

	@Resource(name = "tomcatLogic")
	private TomcatLogic logic;

	/**
	 * true if it is first time to request this status.
	 * 
	 * @return <code>TomcatStatus.RUNNING</code>,
	 *         <code>TomcatStatus.STOPPED</code>
	 */
	@RequestMapping(value = "/tomcatMonitor/retrieveStatus.ajax", method = RequestMethod.GET)
	public @ResponseBody
	TomcatStatus retrieveStatus() {
		return logic.retrieveStatus();

	}

	/**
	 * @return true if the tomcat you have installed on your laptop is
	 *         controllable. Otherwise, false.
	 */
	@RequestMapping(value = "/tomcatMonitor/controllable.ajax", method = RequestMethod.GET)
	public @ResponseBody
	boolean canbeControlled() {
		return logic.canBeControlled();
	}

	/**
	 * @return true if tomcat is started. Otherwise false.
	 */
	@RequestMapping(value = "/tomcatMonitor/startTomcat.ajax", method = RequestMethod.POST)
	public @ResponseBody
	boolean startTomcat() {
		return logic.startTomcat();
	}

	/**
	 * @return true if tomcat is stopped. Otherwise false.
	 */
	@RequestMapping(value = "/tomcatMonitor/stopTomcat.ajax", method = RequestMethod.POST)
	public @ResponseBody
	boolean stopTomcat() {
		return logic.startTomcat();
	}
}
