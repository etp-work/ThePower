package org.etp.portalKit.tomcatmonitor.controller;

import javax.annotation.Resource;

import org.etp.portalKit.tomcatmonitor.logic.TomcatMonitorLogic;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <code>TomcatMonitorController</code> handle all the request from
 * client that related with tomcat status.
 */
public class TomcatMonitorController {

    @Resource(name = "tomcatMonitorLogic")
    private TomcatMonitorLogic logic;

    /**
     * @return true if tomcat is running. Otherwise false.
     */
    @RequestMapping(value = "/tomcatMonitor/isRunning.ajax", method = RequestMethod.GET)
    public @ResponseBody
    boolean isStarted() {
        return logic.tomcatStarted();
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
