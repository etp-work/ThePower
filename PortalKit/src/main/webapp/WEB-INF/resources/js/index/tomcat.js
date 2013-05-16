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
    
    var running;
    
    //=========================================functions=====================================
    function adjustDisable(isDisable){
        //TODO disable startButton, enable stopButton
    }
    
    function setElementsDisable(isDisable){
        //TODO disable all the buttons
    }
    
    
    function poll(firstRequest){
        if(!running){
            return;
        }
        DynamicLoad.loadJSON(retrieveStatusUrl, {
            firstRequest: firstRequest
        }, function(status){
                adjustDisable(running);
                ViewManager.simpleSuccess(status);
                poll(false);
            }, function(error){
                ViewManager.simpleWarning(error.message);
                poll(false);
        });
    }
    
    function startPoll(){
        if(!running){
            running = true;
            setElementsDisable(true);
            poll(true);
        }
    }
    
    function stopPoll(){
        if(running){
            running = false;
        }
    }
    
//========================================init listener=====================================

    
    ViewManager.addViewListener("onShow", "#tomcat-content", startPoll); //add listener to monitor what will happen when tomcat-content shown.
    
    
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