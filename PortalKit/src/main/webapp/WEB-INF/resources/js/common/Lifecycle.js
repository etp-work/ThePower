var Lifecycle;
if (!Lifecycle) {
    Lifecycle = {};
}

(function () {
    
    /**
     * This status means portal just loaded. When portal has been loaded completely, the status of portal is LOADED. But it should set to NORMAL after data initialized.
     */
    Lifecycle.LOADED = "LOADED";
    
    /**
     * This status means portal run fine.
     */
    Lifecycle.NORMAL = "NORMAL";
    
    /**
     * This status means there is a new setting saved, all the views that depends on the settings should refresh.
     */
    Lifecycle.NEWCONFIG = "NEWCONFIG";
    
    /**
     * This status means build-content view is doing package compiling.
     */
    Lifecycle.BUILD_EXECUTING = "BUILD_EXECUTING";
    
    /**
     * state = {
     *       build-content: {
     *             state: "LOADED",
     *             callback: [function(){}, function(){}]
     *       },
     *       deploy-content: {
     *             state: "NORMAL",
     *             callback: [function(){}, function(){}]
     *       }
     * };
     * 
     */
    var state = {};
    
    
    
    Lifecycle.setState = function(viewId, status){
        if(!state[viewId]){
            state[viewId] = {};
        }
        state[viewId].state = status;
        
        var calls = state[viewId].callback;
        if(!calls){
            return;
        }
        
        for(var i in calls){
            calls[i](status);
        }
        
    };
    
    
    Lifecycle.getState = function(viewId){
        if(!state[viewId]){
            state[viewId] = {};
            state[viewId].state = Lifecycle.LOADED;
        }
        if(!state[viewId].state){
            state[viewId].state = Lifecycle.LOADED;
        }
        return state[viewId].state;
    };
    
    
    Lifecycle.setCallback = function(viewId, callback){
        if(!state[viewId]){
            state[viewId] = {};
            state[viewId].callback = [];
        }
        if(!state[viewId].callback){
            state[viewId].callback = [];
        }
        state[viewId].callback.push(callback);
    };
    
    
}());
