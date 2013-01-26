
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
     * This status means build-content view is doing package compiling.
     */
    _lifecycle.BUILD_EXECUTING = "BUILD_EXECUTING";
    /**
     * This status means there is no configuration setted yet. much of functionalites should not be used.
     */
    _lifecycle.NO_CONFIGURATION = "NO_CONFIGURATION";
    
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
    
    /**
     * modules = [
     *           {
     *               moduleId: '',
     *               module: object
     *           }
     * ];
     */
    var modules = [];
    
    _lifecycle.getModule = function (moduleId){
        var module = undefined;
        for ( var i = 0; i < modules.length; i++) {
            if(modules[i].moduleId === moduleId){
                module = modules[i].module;
                break;
            }
        }
        if(!module){
            module = angular.module(moduleId, []);
            var mo = {
                    moduleId: moduleId,
                    module: module
            };
            modules.push(mo);
        }
        return module;
    };
    
    
    
    
    _lifecycle.setState = function(viewId, status){
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
    
    
    _lifecycle.getState = function(viewId){
        if(!state[viewId]){
            state[viewId] = {};
            state[viewId].state = Lifecycle.LOADED;
        }
        if(!state[viewId].state){
            state[viewId].state = Lifecycle.LOADED;
        }
        return state[viewId].state;
    };
    
    
    _lifecycle.setCallback = function(viewId, callback){
        if(!state[viewId]){
            state[viewId] = {};
            state[viewId].callback = [];
        }
        if(!state[viewId].callback){
            state[viewId].callback = [];
        }
        state[viewId].callback.push(callback);
    };
    
    
    
    
}(window));
