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
                var scope = angular.element($('#setting-content')).scope();
                
               if(data.portalTeamPath){
                   scope.$apply(function(){
                       scope.portalTeamPath = data.portalTeamPath;
                   });
                   ptPath = data.portalTeamPath;
               }
               if(data.tomcatWebappsPath){
                   scope.$apply(function(){
                       scope.tomcatWebappsPath = data.tomcatWebappsPath;
                   });
                   twPath = data.tomcatWebappsPath;
               }
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
    
    
    Lifecycle.setCallback("bulid-content" ,function(status){
        switch (status) {
        case Lifecycle.NORMAL:
            setInputDisable(false);
            break;
        case Lifecycle.BUILD_EXECUTING:
            setInputDisable(true);
            break;
        default:
            break;
        }
    });
    
}(window));