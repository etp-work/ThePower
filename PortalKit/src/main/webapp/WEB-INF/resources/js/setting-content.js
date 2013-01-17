/**
 * This JS module is written for view setting-content in index.jsp.
 */
(function () {
    
  //listeners
    function settingsOnShowListener(){
        var url = "/settings/getAll.ajax";
        DynamicLoad.loadJSON(url, undefined, function(data){
            if(data){
               if(data.portalTeamPath){
                   $('#portalTeamPath').val(data.portalTeamPath);
               }
               if(data.tomcatWebappsPath){
                   $('#tomcatWebappsPath').val(data.tomcatWebappsPath);
               }
            }
        });
    }
    
    ViewManager.addViewListener("onShow", "#setting-content", settingsOnShowListener); //add listener to monitor what will happen when settings-content shown.
    
    $('#saveSettings').click(
            function(event) {
                var ptPath = $('#portalTeamPath').val();
                var twPath = $('#tomcatWebappsPath').val();
                var url = "/settings/set.ajax";
                var settings = {
                        portalTeamPath: ptPath,
                        tomcatWebappsPath: twPath
                    };
                DynamicLoad.postJSON(url, settings, function(){
                    ViewManager.addNotification({
                        type: "success",
                        message: "Successfully saved settings",
                        timeout: 5000,
                        callback: function(){
                            alert("hello save");
                        }
                    });
                }, function(error){
                    ViewManager.addNotification({
                        type: "error",
                        message: error.message
                    });
                });
            }
        );
});
