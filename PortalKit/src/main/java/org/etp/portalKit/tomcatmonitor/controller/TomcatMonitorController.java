package org.etp.portalKit.tomcatmonitor.controller;

import java.util.concurrent.Callable;

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
     * @return <code>TomcatStatus.RUNNING</code>,
     *         <code>TomcatStatus.STOPPED</code>
     */
    @RequestMapping(value = "/tomcatMonitor/retrieveStatus.ajax", method = RequestMethod.GET)
    public Callable<Boolean> retrieveStatus() {
        return new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return null;
            }

        };
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
