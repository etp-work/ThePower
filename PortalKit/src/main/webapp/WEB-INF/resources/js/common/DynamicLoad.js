var eventQueue = [];
var queueTimer;
var eventQueueIsExecuting = false;

function executeEventQueue() {
    if (!eventQueueIsExecuting) {
	while (eventQueue[0]) {
	    eventQueueIsExecuting = true;
	    eventQueue.shift()();
	    eventQueueIsExecuting = false;
	}
    } else {
	if (queueTimer) {
	    clearTimeout(queueTimer);
	}
	queueTimer = setTimeout(function() {
	    executeEventQueue();
	}, 1000);
    }

}
var DynamicLoad = {
    postJSON : function(url, data, success, failure) {
	jQuery.ajax({
	    "type" : "POST",
	    "url" : "/PortalKit" + url,
	    "contentType" : "application/json; charset=UTF-8",
	    "data" : JSON.stringify(data),
	    "dataType" : "json",
	    "success" : function(data, textStatus, jqXHR) {
		eventQueue.push(function() {
		    success(data);
		});
		executeEventQueue();
	    },
	    "error" : function(jqXHR, textStatus, errorThrown) {
		var obj = JSON.parse(jqXHR.responseText);
		eventQueue.push(function() {
		    failure(obj);
		});
		executeEventQueue();
	    }
	});
    },
    getJSON : function(url, data, success, failure) {
	jQuery.ajax({
	    "type" : "GET",
	    "url" : "/PortalKit" + url,
	    "contentType" : "application/json; charset=UTF-8",
	    "data" : data,
	    "dataType" : "json",
	    "success" : function(data, textStatus, jqXHR) {
		eventQueue.push(function() {
		    success(data);
		});
		executeEventQueue();
	    },
	    "error" : function(jqXHR, textStatus, errorThrown) {
		var obj = JSON.parse(jqXHR.responseText);
		eventQueue.push(function() {
		    failure(obj);
		});
		executeEventQueue();
	    }
	});
    }
};
