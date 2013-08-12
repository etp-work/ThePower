package org.etp.portalKit.tomcatmonitor.service;

import javax.annotation.Resource;

import org.etp.portalKit.ssp.logic.SspNotificationManager;
import org.etp.portalKit.tomcatmonitor.logic.TomcatLogic;

/**
 * TomcatStatusMonitor is a monitor to tomcat.
 */
//@Component(value = "tomcatStatusMonitor")
//@EnableScheduling
public class TomcatStatusMonitor {

	@Resource(name = "tomcatLogic")
	private TomcatLogic logic;

	@Resource(name = "sspNotificationManager")
	private SspNotificationManager ssp;

	private String TOMCAT_STATUS_ALIAS = "TOMCAT_STATUS_ALIAS";

	private TomcatStatus last;

	/**
	 * check if tomcat is alive.
	 */
//	@Scheduled(fixedDelay = 10000)
	public void monitor() {
		TomcatStatus status = logic.retrieveStatus();
		if (last != status) {
			ssp.notifyClient(TOMCAT_STATUS_ALIAS);
			last = status;
		}
		System.out.println("Tomcat status = " + System.currentTimeMillis()
				+ " || " + status);
	}
}
