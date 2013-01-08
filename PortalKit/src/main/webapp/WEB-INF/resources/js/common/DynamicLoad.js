var DynamicLoad;
if (!DynamicLoad) {
    DynamicLoad = {};
}
/**
 * This module called DynamicLoad, it provide some APIs which have to do with network connectivity.
 * Such as postJSON, loadJSON... 
 *
 */
(function () {

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
   
   /**
    *  Post JSON data to backend.
    */
   DynamicLoad.postJSON = function(url, data, success, failure) {
	   var tempData = data || {};
	   
	   jQuery.ajax({
	       "type" : "POST",
	       "url" : "/PortalKit" + url,
	       "contentType" : "application/json; charset=UTF-8",
	       "data" : JSON.stringify(tempData),
	       "dataType" : "json",
	       "success" : function(data, textStatus, jqXHR) {
		                     eventQueue.push(function() {
		                                       if(success){
			                                      success(data);
		                                       }
		                     });
		                     executeEventQueue();
	       },
	       "error" : function(jqXHR, textStatus, errorThrown) {
		                     var obj = JSON.parse(jqXHR.responseText);
		                     eventQueue.push(function() {
		                                       if(failure){
		                                          failure(obj);
		                                       }
		                     });
		                     executeEventQueue();
	        }
	   });
    };
    
    /**
     * Load JSON data from backend.
     */
    DynamicLoad.loadJSON = function(url, data, success, failure) {
	    var tempData = data || {};
	
	    jQuery.ajax({
	       "type" : "GET",
	       "url" : "/PortalKit" + url,
	       "contentType" : "application/json; charset=UTF-8",
	       "data" : tempData,
	       "dataType" : "json",
	       "success" : function(data, textStatus, jqXHR) {
		                     eventQueue.push(function() {
		                                       if(success){
		                                          success(data);
		                                       }
		                     });
		                     executeEventQueue();
	       },
	       "error" : function(jqXHR, textStatus, errorThrown) {
		                     var obj = JSON.parse(jqXHR.responseText);
		                     eventQueue.push(function() {
		                                       if(failure){
		                                          failure(obj);
		                                       }
		                     });
		                     executeEventQueue();
	       }
	   });
    };

}());