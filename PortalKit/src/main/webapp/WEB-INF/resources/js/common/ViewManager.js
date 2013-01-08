var ViewManager;
if (!ViewManager) {
    ViewManager = {};
}

(function () {
    
    /*
     *var listener = {
     *        onShow: [
     *             {
     *               viewId: 'build-content', 
     *               callback: function () {...}
     *             },
     *             {
     *               viewId: 'deploy-content', 
     *               callback: function () {...}
     *             }
     *        ],
     *        onHide:[
     *             {
     *               viewId: 'build-content', 
     *               callback: function () {...}
     *             },
     *             {
     *               viewId: 'deploy-content', 
     *               callback: function () {...}
     *             }
     *        ]
     *
     *};
     */
    var listeners = {};
    
    /**
     * remove the viewListener with specified type and viewId.
     */
    ViewManager.removeViewListener = function (type, viewId){
        if(!listeners[type]){
            return;
        }
        
        var list = listeners[type];
        for(var i = 0; i < list.length; i++){
            if(list[i].viewId === viewId){
                list.splice(i, 1);
            }
        }
    };

    /**
     * Sdd a viewListener with specified type and viewId.
     * If it already exists, remove the original one first,
     * then add the new one.
     */
    ViewManager.addViewListener = function (type, viewId, callback) {
        if(!listeners[type]){
            listeners[type] = [];
        }

        var list = listeners[type];
        
        ViewManager.removeViewListener(type, viewId);
        
        list.push({
            viewId: viewId,
            callback: callback
        });
    };
    

    /**
     * Fire the specified viewListener. Only one listener 
     * will be triggered at once.
     */
    ViewManager.fireViewListener = function (type, viewId){
        if(!listeners[type]){
              return;
        }
        
        var list = listeners[type];
        for(var i = 0; i < list.length; i++){
             if(list[i].viewId === viewId){
                  list[i].callback();
                  break;
             }
        }
    };
    
    ViewManager.cleanAllViewListeners = function (){
        for(var i in listeners){
            listeners[i] = [];
        }
    };

}());