var ViewManager;
if (!ViewManager) {
    ViewManager = {};
}

(function () {
    
    /*
     *var listener = {
     *        onShow: [
     *             {
     *               selector: 'build-content', 
     *               callback: function () {...}
     *             },
     *             {
     *               selector: 'deploy-content', 
     *               callback: function () {...}
     *             }
     *        ],
     *        onHide:[
     *             {
     *               selector: 'build-content', 
     *               callback: function () {...}
     *             },
     *             {
     *               selector: 'deploy-content', 
     *               callback: function () {...}
     *             }
     *        ]
     *
     *};
     */
    var listeners = {};
    var notifications = [];
    
    /**
     * Remove the viewListener with specified type and selector.
     * 
     * @param type a string determining what kind of event 
     *        will trigger the specified view element.
     * @param selector a string indicate which view element 
     *        will be triggered by specified event.
     */
    ViewManager.removeViewListener = function (type, selector){
        if(!listeners[type]){
            return;
        }
        
        var list = listeners[type];
        for(var i = 0; i < list.length; i++){
            if(list[i].selector === selector){
                list.splice(i, 1);
            }
        }
    };

    /**
     * Add a viewListener with specified type and selector.
     * If it already exists, remove the original one first,
     * then add the new one.
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
    ViewManager.addViewListener = function (type, selector, callback) {
        if(!listeners[type]){
            listeners[type] = [];
        }

        var list = listeners[type];
        
        ViewManager.removeViewListener(type, selector);
        
        list.push({
            selector: selector,
            callback: callback
        });
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
    ViewManager.fireViewListener = function (type, selector){
        if(!listeners[type]){
              return;
        }
        
        var list = listeners[type];
        for(var i = 0; i < list.length; i++){
             if(list[i].selector === selector){
                  list[i].callback();
                  break;
             }
        }
    };
    
    /**
     * Clean all the view listeners.
     */
    ViewManager.cleanAllViewListeners = function (){
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
    ViewManager.show = function (selector, speed, callback){
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
    ViewManager.hide = function (selector, speed, callback){
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
    
    ViewManager.removeNotification = function(id){
        var isRemoved = false;
        for(var i = 0; i < notifications.length; i++){
            if(notifications[i].id === id){
                notifications.splice(i, 1);
                isRemoved = true;
            }
        }
        $("#"+id).fadeTo(400, 0, function(){
                $(this).slideUp(400, function(){
                        $(this).remove();
                        var showNotification = notifications[notifications.length - 2];
                        var showId;
                        if(showNotification){
                           showId = showNotification.id;
                        }
                        if(showId){
                            $("#"+showId).fadeTo(400, 1, function(){
                                $(this).slideDown(400);
                            });
                        }
                    }
                );
            }
        );
        
    };
    
    function indexOfNotification(id){
        for(var i = 0; i < notifications.length; i++){
            if(notifications[i].id === id){
               return i;
            }
        }
        return -1;
    }
    
    
    ViewManager.addNotification = function (type, message){
        
        var resources = $('#resources').val();
        var id = new Date().getTime()+"-noti";
        var nodificationHtml = "<div id=\""+id+"\" class=\"notification "+type+"\" style=\"display: none;\">"+
                "<a href=\"#\" class=\"close\"><img src=\""+resources+"/images/icons/cross_grey_small.png\" title=\"Close this notification\" alt=\"close\" /></a>"+
                "<span>"+
                       message+
                "</span>"+
            "</div>";
        var closeCallback = function(event){
            ViewManager.removeNotification(id);
            event.preventDefault();
        };
        
        if(notifications.length >= 2){
            $("#"+notifications[notifications.length-2].id).fadeTo(400, 0, function(){
                $(this).slideUp(400, function(){
                    $('.foot').prepend(nodificationHtml);
                    $("#"+id).fadeTo(400, 1, function(){
                        $(this).slideDown(400);
                        }
                    );
                    $("#"+id).find('.close').on("click", closeCallback);
                });
            });
        }else{
            $('.foot').prepend(nodificationHtml);
            $("#"+id).fadeTo(400, 1, function(){
                $(this).slideDown(400);
                }
            );
            $("#"+id).find('.close').on("click", closeCallback);
        }
        
        notifications.push({
            id: id,
            callback: closeCallback
        });
    };
    

}());