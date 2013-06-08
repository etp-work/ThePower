
(function (window) {
    
    var _lifecycle = {};
    window.Lifecycle = _lifecycle;
    
    /**
     * This status means portal just loaded. When portal has been loaded completely, the status of portal is LOADED. But it should set to NORMAL after data initialized.
     */
    _lifecycle.LOADED = "LOADED";
    
    /**
     * This status means portal run fine.
     */
    _lifecycle.NORMAL = "NORMAL";
    
    /**
     * This status means there is a new setting saved, all the views that depends on the settings should refresh.
     */
    _lifecycle.NEWCONFIG = "NEWCONFIG";
    
    /**
     * This status means there is something are working, any view need care about this status should addStateListener to do things that need to do.
     */
    _lifecycle.IN_PROCESS = "IN_PROCESS";
    /**
     * This status means there is no configuration setted yet. much of functionalites should not be used.
     */
    _lifecycle.NO_CONFIGURATION = "NO_CONFIGURATION";
    
    /**
     * state = {
     *             status: "LOADED", 
     *             callback: [function(){}, function(){}]
     * };
     * 
     */
    var stateObject = {};
    
    
    _lifecycle.setState = function(status){
        stateObject.status = status;
        
        var calls = stateObject.callback;
        if(!calls){
            return;
        }
        
        for(var i in calls){
            calls[i](status);
        }
    };
    
    
    _lifecycle.getState = function(){
        if(!stateObject.status){
            stateObject.status = Lifecycle.LOADED;
        }
        return stateObject.status;
    };
    
    _lifecycle.removeStateListener = function(callback){
        if(!stateObject.callback){
            return;
        }
        for(var i = 0; i < stateObject.callback.length; i++){
            if(stateObject.callback[i] === callback){
                stateObject.callback.slice(i, 1);
                break;
            }
        }
    };
    
    
    _lifecycle.addStateListener = function(callback){
        if(!stateObject.callback){
            stateObject.callback = [];
        }
        if(callback){
            stateObject.callback.push(callback);
        }
    };
    
    
    
    
}(window));
