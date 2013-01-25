
(function () {
    var _viewManager = {};
    window.ViewManager = _viewManager;
    
    /*
     *var listener = {
     *        onShow: [
     *             {
     *               selector: 'build-content', 
     *               callback: [function () {...}]
     *             },
     *             {
     *               selector: 'deploy-content', 
     *               callback: [function () {...}]
     *             }
     *        ],
     *        onHide:[
     *             {
     *               selector: 'build-content', 
     *               callback: [function () {...}]
     *             },
     *             {
     *               selector: 'deploy-content', 
     *               callback: [function () {...}]
     *             }
     *        ]
     *
     *};
     */
    var listeners = {};
    
    /*
     * var notifications = [
     *            {
     *                id: 
     *            
     *            
     *            },
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * ];
     * 
     */
    var notifications = [];
    
    /**
     * Remove the viewListener with specified type and selector.
     * 
     * @param type a string determining what kind of event 
     *        will trigger the specified view element.
     * @param selector a string indicate which view element 
     *        will be triggered by specified event.
     * @param callback a function which will be removed from listeners
     */
    _viewManager.removeViewListener = function (type, selector, callback){
        if(!listeners[type]){
            return;
        }
        
        var list = listeners[type];
        if(!callback){
            for(var i = 0; i < list.length; i++){
                if(list[i].selector === selector){
                    list.splice(i, 1);
                }
            }
        }else{
            for(var i = 0; i < list.length; i++){
                if(list[i].selector === selector){
                    var callbacks = list[i].callback;
                    if(!callbacks){
                        return;
                    }
                    for(var j = 0; j < callbacks.length; j++){
                        if(callbacks[j] === callback){
                            callbacks.splice(j, 1);
                        }
                    }
                }
            }
        }
        
    };

    /**
     * Add a viewListener with specified type and selector.
     * The listener will be fired only when specified type 
     * event executed.
     * Note: selector should be string.
     * 
     * @param type a string determining what kind of event 
     *        will trigger the specified view element.
     * @param selector a string indicate which view element 
     *        will be triggered by specified event.
     * @param callback A function to call once the specified 
     *        event executed.
     */
    _viewManager.addViewListener = function (type, selector, callback) {
        if(!listeners[type]){
            listeners[type] = [];
        }

        var list = listeners[type];
        var listener = undefined;
        for(var i in list){
            if(list[i].selector === selector){
                listener = list[i];
                break;
            }
        }
        if(!listener){
            listener = {
                    selector : selector,
                    callback : []
            };
            list.push(listener);
        }
        if(!listener.callback){
            listener.callback = [];
        }
        listener.callback.push(callback);
    };
    

    /**
     * Fire the specified viewListener. Only one listener 
     * will be triggered at once.
     * 
     * @param type a string determining what kind of event 
     *        will trigger the specified view element.
     * @param selector a string indicate which view element 
     *        will be triggered by specified event.
     */
    _viewManager.fireViewListener = function (type, selector){
        if(!listeners[type]){
              return;
        }
        
        var list = listeners[type];
        for(var i = 0; i < list.length; i++){
             if(list[i].selector === selector){
                  var callbacks = list[i].callback;
                  for(var j in callbacks){
                      callbacks[j]();
                  }
                  break;
             }
        }
    };
    
    /**
     * Clean all the view listeners.
     */
    _viewManager.cleanAllViewListeners = function (){
        for(var i in listeners){
            listeners[i] = [];
        }
    };
    
    /**
     * Display the matched elements. The added listener 
     * with matched selector and 'onShow' event will 
     * be fired.
     * 
     * @param selector a string value that indicate 
     *        which element will be displayed.
     * @param speed A string or number determining 
     *        how long the animation will run.
     * @param callback A function to call once the 
     *        animation is complete.
     */
    _viewManager.show = function (selector, speed, callback){
	    var transparencySpeed = 0;
	    if(speed){
	        transparencySpeed = speed;
        }
	    $(selector).show(transparencySpeed, function (){
	        if(callback){
		        callback();
	        }
	        ViewManager.fireViewListener("onShow", selector);
	    });
    };
    
    /**
     * Hide the matched elements. The added listener 
     * with matched selector and 'onHide' event will 
     * be fired.
     * 
     * @param selector a string value that indicate 
     *        which element will be displayed.
     * @param speed A string or number determining 
     *        how long the animation will run.
     * @param callback A function to call once the 
     *        animation is complete.
     */
    _viewManager.hide = function (selector, speed, callback){
	    var transparencySpeed = 0;
	    if(speed){
	        transparencySpeed = speed;
        }
	    $(selector).hide(transparencySpeed, function (){
	            if(callback){
		           callback();
	            }
	            ViewManager.fireViewListener("onHide", selector);
	        }
	    );
    };
    
    function indexOfNotification(id){
        for(var i = 0; i < notifications.length; i++){
            if(notifications[i] === id){
               return i;
            }
        }
        return -1;
    }
    
    /**
     * Remove specified notification from whole page.
     * If the notification has been shown at the top 
     * two positions, the next hidden notification 
     * will be displayed to the top two position.
     * 
     * @param id identity of the notification.
     */
    _viewManager.removeNotification = function(id){
        var index = indexOfNotification(id);
        if(index < 0){
            return;
        }
        var needShowNext = (index >= (notifications.length - 2)) ? true : false;
        for(var i = 0; i < notifications.length; i++){
            if(notifications[i] === id){
                notifications.splice(i, 1);
            }
        }
        $("#"+id).fadeTo(400, 0, function(){
                $(this).slideUp(400, function(){
                        $(this).remove();
                        if(needShowNext){
                            var showNotification = notifications[notifications.length - 2];
                            if(showNotification){
                                $("#"+showNotification).fadeTo(400, 1, function(){
                                    $(this).slideDown(400);
                                });
                            }
                        }
                });
        });
        
    };
    
    /**
     * Add a notification with specified args at the bottom of the 
     * whole page.
     * 
     * @param options.type mandatory. indicate what kind of this 
     *        notification. Such as success/information/error/attention.
     *        options.message mandatory. indicate message notification.
     *        options.callback optional. A function to call once the 
     *        message clicked.
     *        options.timeout optional. A millisecond determining how 
     *        long the notification will stay. Default value is 1800000ms.
     * @returns id identity of the added notification. Can be used to 
     *        remove it.
     */
    _viewManager.addNotification = function (options){
        if(!options){
            return undefined;
        }
        var type = options.type;
        var message = options.message;
        var callback = options.callback;
        var timeout = options.timeout;
        
        if(!type){
            return undefined;
        }
        
        var id = new Date().getTime()+"-noti";
        var nodificationHtml = "<div id=\""+id+"\" class=\"notification "+type+"\" style=\"display: none;\">"+
                "<a href=\"#\" class=\"close\"><img src=\"resources/images/icons/cross_grey_small.png\" title=\"Close this notification\" alt=\"close\" /></a>"+
                "<span class=\"message\">"+
                       message+
                "</span>"+
            "</div>";
        if(callback){
            nodificationHtml = "<div id=\""+id+"\" class=\"notification "+type+"\" style=\"display: none;\">"+
            "<a href=\"#\" class=\"close\"><img src=\"resources/images/icons/cross_grey_small.png\" title=\"Close this notification\" alt=\"close\" /></a>"+
            "<span class=\"message\"><a href=\"#\">"+
                   message+
            "</a></span>"+
            "</div>";
        }
        var closeCallback = function(event){
            ViewManager.removeNotification(id);
            event.preventDefault();
        };
        
        var addHtmlCallback = function(){
            $('.foot').prepend(nodificationHtml);
            $("#"+id).fadeTo(400, 1, function(){
                $(this).slideDown(400);
                }
            );
            $("#"+id).find('.close').on("click", closeCallback);
            if(callback){
                $("#"+id).find('.message').on("click", function(event){
                    callback();
                    ViewManager.removeNotification(id);
                    event.preventDefault();
                });
            }
            if(!timeout){
                timeout = 1800000;
            }
            setTimeout(function(){
                ViewManager.removeNotification(id);
            }, timeout);
        };
        
        if(notifications.length >= 2){
            $("#"+notifications[notifications.length-2]).fadeTo(400, 0, function(){
                $(this).slideUp(400, addHtmlCallback);
            });
        }else{
            addHtmlCallback();
        }
        
        notifications.push(id);
        return id;
    };
    
    _viewManager.simpleSuccess = function(msg){
        ViewManager.addNotification({
            type: "success",
            message: msg,
            timeout: 5000
        });
    };
    
    _viewManager.simpleWarning = function(msg){
        ViewManager.addNotification({
            type: "attention",
            message: msg,
            timeout: 10000
        });
    };
    
    _viewManager.simpleError = function(msg, callback){
        ViewManager.addNotification({
            type: "error",
            message: msg,
            callback: callback
        });
    };

}());