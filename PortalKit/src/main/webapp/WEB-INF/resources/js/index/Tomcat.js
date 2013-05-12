/**
 * This module is only worked for view tomcat-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';

//=========================================variable=====================================
    var isRunningUrl = "/tomcatMonitor/isRunning.ajax";
    var startUrl = "/tomcatMonitor/startTomcat.ajax";
    var stopUrl = "/tomcatMonitor/stopTomcat.ajax";
    
    var timeout = 5000;//5s
    var running;
    
    //=========================================functions=====================================
    function adjustDisable(isDisable){
        //TODO disable startButton, enable stopButton
    }
    
    function setElementsDisable(isDisable){
        //TODO disable all the buttons
    }
    
    
    function poll(){
        if(!running){
            return;
        }
        DynamicLoad.loadJSON(isRunningUrl, undefined, function(){
                adjustDisable(running);
                setTimeout(poll,timeout);//re fetch status in 5s
            }, function(){
                setTimeout(poll,timeout);//re fetch status in 5s
        });
    }
    
    function startPoll(){
        if(!running){
            running = true;
            setElementsDisable(true);
            poll();
        }
    }
    
    function stopPoll(){
        if(running){
            running = false;
        }
    }
    
//========================================init listener=====================================

    
    ViewManager.addViewListener("onShow", "#tomcat-content", startPoll); //add listener to monitor what will happen when tomcat-content shown.
    ViewManager.addViewListener("onHide", "#tomcat-content", stopPoll); //add listener to monitor what will happen when tomcat-content shown.
    
    
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