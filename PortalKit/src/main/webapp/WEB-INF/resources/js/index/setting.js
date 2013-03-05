/**
 * This module is only worked for view setting-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';

//=========================================variable=====================================
    var getSettingsUrl = "/settings/getAll.ajax";
    var ptPath = undefined;
    var twPath = undefined;
//=========================================functions=====================================
    function setSaveDisable(isDisable){
        $('#saveSettings').attr("disabled", isDisable);
    }
    
    function setInputDisable(isDisable){
        $('#setting-content #portalTeamPath').attr("disabled", isDisable);
        $('#setting-content #tomcatWebappsPath').attr("disabled", isDisable);
    }
    
    function valueDirty(){
        if(!ptPath && !twPath && !$('#setting-content #portalTeamPath').val() && !$('#setting-content #tomcatWebappsPath').val()){
            setSaveDisable(true);
        }else if($('#setting-content #portalTeamPath').val() === ptPath && $('#setting-content #tomcatWebappsPath').val() === twPath){
            setSaveDisable(true);
        }else{
            setSaveDisable(false);
        }
    }

//========================================init listener=====================================

    function settingsOnShowListener(){
        if(ptPath && twPath){
            return;
        }
        
        DynamicLoad.loadJSON(getSettingsUrl, undefined, function(data){
            if(data){
                var scope = angular.element($('#setting-content')).scope();
                if(data.portalTeamPath){
                    ptPath = data.portalTeamPath;
                }
                if(data.tomcatWebappsPath){
                    twPath = data.tomcatWebappsPath;
                }
                scope.$apply(function(){
                    scope.portalTeamPath = ptPath ? ptPath : "";
                    scope.tomcatWebappsPath = twPath ? twPath : "";
                });
            }
            
        });
    }
    
    ViewManager.addViewListener("onShow", "#setting-content", settingsOnShowListener); //add listener to monitor what will happen when settings-content shown.
    
    
    Lifecycle.addStateListener(function(status){
        switch (status) {
            case Lifecycle.NORMAL:
                setInputDisable(false);
                valueDirty();
                break;
            case Lifecycle.IN_PROCESS:
                setInputDisable(true);
                setSaveDisable(true);
                break;
            default:
                break;
        }
    });
    
//================================================event bind==================================
    
    $('#setting-content #portalTeamPath').keyup(function(event) {
        valueDirty();
    });
    
    $('#setting-content #tomcatWebappsPath').keyup(function(event) {
        valueDirty();
    });
    
    $('#setting-content #saveSettings').click(function(event) {
                ptPath = $('#setting-content #portalTeamPath').val();
                twPath = $('#setting-content #tomcatWebappsPath').val();
                var url = "/settings/set.ajax";
                var settings = {
                        portalTeamPath: ptPath,
                        tomcatWebappsPath: twPath
                    };
                DynamicLoad.postJSON(url, settings, function(){
                    ViewManager.simpleSuccess("Successfully saved settings");
                    Lifecycle.setState(Lifecycle.NEWCONFIG);
                    
                }, function(error){
                    ViewManager.simpleError(error.message);
                });
                
                setSaveDisable(true);
   });
    
}(window));