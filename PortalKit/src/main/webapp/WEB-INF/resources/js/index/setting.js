/**
 * This module is only worked for view setting-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';

    //variables
    var ptPath = undefined;
    var twPath = undefined;
    
    //listeners
    function settingsOnShowListener(){
        if(ptPath && twPath){
            return;
        }
        
        var url = "/settings/getAll.ajax";
        DynamicLoad.loadJSON(url, undefined, function(data){
            if(data){
                $('#portalTeamPath').val(data.portalTeamPath);
                ptPath = data.portalTeamPath;
                $('#tomcatWebappsPath').val(data.tomcatWebappsPath);
                twPath = data.tomcatWebappsPath;
            }
            
        });
    }
    
    ViewManager.addViewListener("onShow", "#setting-content", settingsOnShowListener); //add listener to monitor what will happen when settings-content shown.
    
    function setSaveDisable(isDisable){
        $('#saveSettings').attr("disabled", isDisable);
    }
    
    function setInputDisable(isDisable){
        $('#portalTeamPath').attr("disabled", isDisable);
        $('#tomcatWebappsPath').attr("disabled", isDisable);
    }
    
    function successNotification(message){
        ViewManager.addNotification({
            type: "success",
            message: message,
            timeout: 5000
        });
    }
    
    function errorNotification(message){
        ViewManager.addNotification({
            type: "error",
            message: message
        });
    }
    
    function valueDirty(){
        if($('#portalTeamPath').val() === ptPath && $('#tomcatWebappsPath').val() === twPath){
            setSaveDisable(true);
        }else{
            setSaveDisable(false);
        }
    }
    
    Lifecycle.setCallback("bulid-content" ,function(status){
        switch (status) {
        case Lifecycle.NORMAL:
            setInputDisable(false);
            valueDirty();
            break;
        case Lifecycle.BUILD_EXECUTING:
            setInputDisable(true);
            setSaveDisable(true);
            break;
        default:
            break;
        }
    });
    
    $('#portalTeamPath').keyup(function(event) {
        valueDirty();
    });
    
    $('#tomcatWebappsPath').keyup(function(event) {
        valueDirty();
    });
    
    $('#saveSettings').click(function(event) {
                ptPath = $('#portalTeamPath').val();
                twPath = $('#tomcatWebappsPath').val();
                var url = "/settings/set.ajax";
                var settings = {
                        portalTeamPath: ptPath,
                        tomcatWebappsPath: twPath
                    };
                DynamicLoad.postJSON(url, settings, function(){
                    successNotification("Successfully saved settings");
                    Lifecycle.setState("bulid-content", Lifecycle.NEWCONFIG);
                    
                }, function(error){
                    errorNotification(error.message);
                });
                
                setSaveDisable(true);
   });
    
}(window));