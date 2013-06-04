package org.etp.portalKit.ssp.controller;

import java.util.List;

import javax.annotation.Resource;

import org.etp.portalKit.ssp.logic.SspNotificationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * <code>SspController</code> holds the long polling request from client.
 */
@Controller
public class SspController {

	@Resource(name = "sspNotificationManager")
	private SspNotificationManager sspNotificationManager;

	/**
	 * Long polling from client, respond when data is dirty.
	 * 
	 * @return DeferredResult<List<String>> list of alias, each alias indicate
	 *         what data is dirty right now.
	 */
	@RequestMapping(value = "/ssp/poll.ajax", method = RequestMethod.GET)
	public @ResponseBody
	DeferredResult<List<String>> poll() {
		return sspNotificationManager.retrieveNotifications();
	}
}
