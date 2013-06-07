/**
\ * This module called DynamicLoad, it provide some APIs which have to do with network connectivity.
 * Such as postJSON, loadJSON... 
 *
 */
(function (window) {
    'use strict';
    
    
    
    //this part provides the capbility that data render can be used within the dynamic loaded html templates.
    angular.module('myModule', []).
    factory('compiler', function($compile) {
      return {
        compile: function(html){
            var scope = angular.element($('.content-wrapper')).scope();
            return $compile(html)(scope);
        }
      };
    });
   
    angular.injector(['myModule', 'ng']);
  
    var _DynamicLoad = {};
    window.DynamicLoad = _DynamicLoad;

    var eventQueue = [];
    var dataListeners = {};
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
   
    function successHandler(success, data){
        eventQueue.push(function() {
                          if(success){
                             success(data);
                          }
        });
        executeEventQueue();
    }
   
    function errorHandler(jqXHR, failure){
        var obj = undefined;
        try
        {
            obj = JSON.parse(jqXHR.responseText);
        }
        catch(err)
        {
           obj = jqXHR.responseText;
        }
        eventQueue.push(function() {
                          if(failure){
                             failure(obj);
                          }
        });
        executeEventQueue();
    }
   
    /**
     *  Post JSON data to backend.
     */
    _DynamicLoad.postJSON = function(url, data, success, failure) {
	    var tempData = data || {};
	    if(typeof(tempData) !== "string"){
	        tempData = JSON.stringify(tempData);
	    }
	   
	    jQuery.ajax({
	        "type" : "POST",
	        "url" : "/PortalKit" + url,
	        "contentType" : "application/json; charset=UTF-8",
	        "data" : tempData,
	        "dataType" : "json",
	        "success" : function(dataFromServer, textStatus, jqXHR) {
	            successHandler(success, dataFromServer);
	         },
	        "error" : function(jqXHR, textStatus, errorThrown) {
	            errorHandler(jqXHR, failure);
	        }
	    });
     };
    
    /**
     * Load JSON data from backend.
     */
    _DynamicLoad.loadJSON = function(url, data, success, failure) {
	    var tempData = data || {};
	    jQuery.ajax({
	       "type" : "GET",
	       "url" : "/PortalKit" + url,
	       "contentType" : "application/json; charset=UTF-8",
	       "data" : tempData,
	       "dataType" : "json",
	       "success" : function(dataFromServer, textStatus, jqXHR) {
               successHandler(success, dataFromServer);
           },
	       "error" : function(jqXHR, textStatus, errorThrown) {
               errorHandler(jqXHR, failure);
           }
	   });
    };
    
    _DynamicLoad.loadStaticJs = function(url, success, failure){
        jQuery.ajax({
            "type" : "GET",
            "url" : url,
            "contentType" : "application/x-www-form-urlencoded; charset=UTF-8",
            "dataType" : "text",
            "success" : function(data, textStatus, jqXHR) {
                                  var json = null;
                                  eval("json=" + data);
                                  if(typeof(json) === "function"){
                                      json();
                                  }
                                  if(success){
                                     success();
                                  }
            },
            "error" : function(jqXHR, textStatus, errorThrown) {
                              var obj = {
                                      type: "Internal Error",
                                      message: textStatus
                              };
                                                if(failure){
                                                   failure(obj);
                                                }
            }
        });
    };
    
    _DynamicLoad.loadStaticHtml = function(url, success, failure){
        jQuery.ajax({
            "type" : "GET",
            "url" : url,
            "contentType" : "application/x-www-form-urlencoded; charset=UTF-8",
            "dataType" : "text",
            "success" : function(data, textStatus, jqXHR) {
                                  if(success){
                                      if(data){
                                          success(data);
                                      }else{
                                          success();
                                      }
                                  }
            },
            "error" : function(jqXHR, textStatus, errorThrown) {
                              var obj = {
                                      type: "Internal Error",
                                      message: textStatus
                              };
                                                if(failure){
                                                   failure(obj);
                                                }
            }
        });
    };
    
    _DynamicLoad.addDataListener = function(dataAlias, listener){
        if(!dataAlias || !listener){
            return;
        }
        
        if(!dataListeners[dataAlias]){
            dataListeners[dataAlias] = [];
        }
        
        dataListeners[dataAlias].push(listener);
    };
    
    _DynamicLoad.removeDataListener = function(dataAlias, listener){
        if(!dataAlias || !listener){
            return;
        }
        
        if(!dataListeners[dataAlias] || dataListeners[dataAlias].length === 0){
            return;
        }
        
        for(var i = 0; i < dataListeners[dataAlias].length; i++){
            if(dataListeners[dataAlias][i] === listener){
                dataListeners[dataAlias].splice(i, 1);
                break;
            }
        }
    };
    
    function fireDataListener(dataAlias){
        if(!dataAlias){
            return;
        }
        
        if(!dataListeners[dataAlias] || dataListeners[dataAlias].length === 0){
            return;
        }
        
        for(var i = 0; i < dataListeners[dataAlias].length; i++){
            if(dataListeners[dataAlias][i]){
                dataListeners[dataAlias][i]();
            }
        }
    }
    
    $(document).ready(function(){
        
        var initUrl = "/init/getViewsInfo.ajax";
        var pollingUrl = "/ssp/poll.ajax";
        var shownViewId = undefined;
        var viewsInfo = undefined;
        
        //longPolling to server.
        function poll(){
            DynamicLoad.loadJSON(pollingUrl, undefined, function(dirtyDatas){
                poll();
                if(dirtyDatas && dirtyDatas.length > 0){
                    for(var i = 0; i < dirtyDatas.length; i++){
                        fireDataListener(dirtyDatas[i]);
                    }
                }
            }, function(){
                poll();
            });
        }
        
        //remove a view from viewsInfo with specified viewId.
        function removeViewInfo(viewId){
            for(var i in viewsInfo){
                if(viewsInfo[i].viewId === viewId){
                    viewsInfo.splice(i, 1);
                    break;
                }
            }
        }

        //find the view from viewsInfo with specified viewId.
        function findViewInfo(viewId){
            if(viewsInfo.length === 0){
                return undefined;
            }
            
            for(var i in viewsInfo){
                if(viewsInfo[i].viewId === viewId){
                    return viewsInfo[i];
                }
            }
            //implicitly falsy
        }
        
        //load a specified view for its html,js.
        //callback will be executed after html and js are loaded successfully.
        function loadViewJS(view, callback){
            if(!view){
                return;
            }
            
            var loadJS = function(){
                DynamicLoad.loadStaticJs(view.js, function(){
                    if(callback){
                        callback();
                    }
                    removeViewInfo(view.viewId);
                }, function(error){ViewManager.simpleError("Load js "+view.js+ " error : " +error.message);});
            };
            
            var loadView = function(html){
                var injector = angular.injector(['myModule', 'ng']);
                var compiler = injector.get('compiler');
                $('.maincontent .content-wrapper').append(compiler.compile(html));
                ViewManager.hide(".maincontent .content-wrapper .content-box");
                loadJS();
            };
            
            DynamicLoad.loadStaticHtml(view.templateUrl, loadView, function(error){ViewManager.simpleError("Load view "+view.viewId+ " error : " +error.message);});
        }
        
        function viewsInfoLoad(ViewsInfo){
            viewsInfo = ViewsInfo;
            
            //init default view
            var defaultView = undefined;
            for(var i =0; i < viewsInfo.length; i++){
                if(viewsInfo[i].defaultView){
                    defaultView = viewsInfo[i];
                    break;
                }
            }
            
            loadViewJS(defaultView, function(){

                ViewManager.show("#" + defaultView.viewId);
                poll();
            });
        }
      
        //load viewSettings info, in order to append views dynamically.
        DynamicLoad.loadJSON(initUrl, undefined, viewsInfoLoad, function(error){ViewManager.simpleError(error.message);});
        

        
        
        $('.tab-header ul li a').click(
                function(event) {
                        var newId = $(this).attr("href").substring(2);
                        if(newId === shownViewId){
                            return false;
                        }
                        $(this).parent().siblings().find("a").removeClass('active'); // Remove active class from all the other tabs
                        $(this).addClass('active');
                        $('.maincontent .content-wrapper').removeClass("contentAnimation");
                        ViewManager.hide(".maincontent .content-wrapper .content-box");
                        shownViewId = newId;
                        var callbackOnshow = function(){
                            $('.maincontent .content-wrapper').addClass("contentAnimation");
                            ViewManager.show("#" + shownViewId);
                        };
                        
                        var view = findViewInfo(shownViewId);
                        if(view){
                            loadViewJS(view, callbackOnshow);
                        }else{
                            callbackOnshow();
                        }
                       
                        return false;
        });
        
    });

}(window));