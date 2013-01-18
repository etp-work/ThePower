/**
 * This JS module is written for view setting-content in index.jsp.
 */
(function () {
    
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
               if(data.portalTeamPath){
                   $('#portalTeamPath').val(data.portalTeamPath);
                   ptPath = data.portalTeamPath;
               }
               if(data.tomcatWebappsPath){
                   $('#tomcatWebappsPath').val(data.tomcatWebappsPath);
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
    
    
    
    $('#portalTeamPath').keyup(function(event) {
        if($(this).val() !== ptPath){
            setSaveDisable(false);
        }else if($('#tomcatWebappsPath').val() !== twPath){
            setSaveDisable(false);
        }else{
            setSaveDisable(true);
        }
    });
    
    $('#tomcatWebappsPath').keyup(function(event) {
        if($(this).val() !== twPath){
            setSaveDisable(false);
        }else if($('#portalTeamPath').val() !== ptPath){
            setSaveDisable(false);
        }else{
            setSaveDisable(true);
        }
    });
    
    
    $('#saveSettings').click(
            function(event) {
                ptPath = $('#portalTeamPath').val();
                twPath = $('#tomcatWebappsPath').val();
                var url = "/settings/set.ajax";
                var settings = {
                        portalTeamPath: ptPath,
                        tomcatWebappsPath: twPath
                    };
                DynamicLoad.postJSON(url, settings, function(){
                    ViewManager.addNotification({
                        type: "success",
                        message: "Successfully saved settings",
                        timeout: 5000
                    });
                    
                    Lifecycle.setState("bulid-content", Lifecycle.NEWCONFIG);
                    
                }, function(error){
                    ViewManager.addNotification({
                        type: "error",
                        message: error.message
                    });
                });
                
                setSaveDisable(true);
            }
        );
});
