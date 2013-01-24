/**
 * This module is only worked for view setting-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';

//=========================================variable=====================================
    var ptPath = undefined;
    var twPath = undefined;
//=========================================functions=====================================
    function setSaveDisable(isDisable){
        $('#saveSettings').attr("disabled", isDisable);
    }
    
    function setInputDisable(isDisable){
        $('#portalTeamPath').attr("disabled", isDisable);
        $('#tomcatWebappsPath').attr("disabled", isDisable);
    }
    
    function valueDirty(){
        if(!ptPath && !twPath){
            setSaveDisable(true);
        }else if($('#portalTeamPath').val() === ptPath && $('#tomcatWebappsPath').val() === twPath){
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
        
        var url = "/settings/getAll.ajax";
        DynamicLoad.loadJSON(url, undefined, function(data){
            if(data){
                var scope = angular.element($('#setting-content')).scope();
                scope.$apply(function(){
                    scope.portalTeamPath = data.portalTeamPath;
                    scope.tomcatWebappsPath = data.tomcatWebappsPath;
                });
                ptPath = data.portalTeamPath;
                twPath = data.tomcatWebappsPath;
            }
            
        });
    }
    
    ViewManager.addViewListener("onShow", "#setting-content", settingsOnShowListener); //add listener to monitor what will happen when settings-content shown.
    
    
    Lifecycle.setCallback("build-content" ,function(status){
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
    
//================================================event bind==================================
    
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