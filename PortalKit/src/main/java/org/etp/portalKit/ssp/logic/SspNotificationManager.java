package org.etp.portalKit.ssp.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * SspNotification can be called by any other thread or function to notification
 * data is dirty in server.
 */
@Component(value = "sspNotificationManager")
@Scope("singleton")
public class SspNotificationManager {

	private List<DeferredResult<List<String>>> queue = new ArrayList<DeferredResult<List<String>>>();

	private List<String> dirtyAliases = new ArrayList<String>();

	/**
	 * @return DeferredResult<List<String>> list of alias, each alias indicate
	 *         what data is dirty right now.
	 */
	public DeferredResult<List<String>> retrieveNotifications() {
		final DeferredResult<List<String>> deferredResult = new DeferredResult<List<String>>();
		deferredResult.onCompletion(new Runnable() {
			@Override
			public void run() {
				queue.remove(deferredResult);
				dirtyAliases.clear();
			}
		});
		queue.add(deferredResult);
		return deferredResult;

	}

	/**
	 * 
	 * @param dirtyAlias
	 *            alias of dirty data.
	 */
	public void notifyClient(String dirtyAlias) {
		if (!dirtyAliases.contains(dirtyAlias)) {
			dirtyAliases.add(dirtyAlias);
		}
		if (queue.size() <= 0) {
			return;
		}
		queue.get(0).setResult(dirtyAliases);
	}
}
