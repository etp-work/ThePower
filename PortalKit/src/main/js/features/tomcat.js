/**
 * This module is only worked for view tomcat-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';

//=========================================variable=====================================
    var retrieveStatusUrl = "/tomcatMonitor/retrieveStatus.ajax";
    var controllableUrl = "/tomcatMonitor/controllable.ajax";
    var startUrl = "/tomcatMonitor/startTomcat.ajax";
    var stopUrl = "/tomcatMonitor/stopTomcat.ajax";
    
    var RUNNING = "RUNNING";
    var STOPPED = "STOPPED";
    
    var TOMCAT_STATUS_ALIAS = "TOMCAT_STATUS_ALIAS";
    
    var settled = false;
    
    //=========================================functions=====================================
    function adjustDisable(isDisable){
        //TODO disable startButton, enable stopButton
    }
    
    function setElementsDisable(isDisable){
        //TODO disable all the buttons
    }
    
    function retrieveStatus(){

        DynamicLoad.loadJSON(retrieveStatusUrl, undefined, function(status){
            adjustDisable(status === RUNNING ? true : false);
            ViewManager.simpleSuccess(status);
        });
    
    }
    
    function tomcatViewOnShow(){
        setElementsDisable(true);
        
        if(!settled){
            DynamicLoad.addDataListener(TOMCAT_STATUS_ALIAS, function(){
                retrieveStatus();
            });
        }
        
        retrieveStatus();
    }
    
    
//========================================init listener=====================================

    
    ViewManager.addViewListener("onShow", "#tomcat-content", tomcatViewOnShow); //add listener to monitor what will happen when tomcat-content shown.
    
    
    Lifecycle.addStateListener(function(status){
        switch (status) {
            case Lifecycle.NORMAL:
                break;
            case Lifecycle.IN_PROCESS:
                break;
            default:
                break;
        }
    });
    
//================================================event bind==================================
    
    
}(window));