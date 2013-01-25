/**
 * This module is only worked for view deploy-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    
//=========================================variable=========================================
    var viewId = "deploy-content";
    var getDownloadedPathUrl = "/deploy/getDownLoadedPath.ajax";
    var setDownloadedPathUrl = "/deploy/setCheckPackages.ajax";
    var downloadedPath = undefined;
    
//=========================================functions========================================
    function deployViewOnShow(){
        if(!downloadedPath){
            DynamicLoad.loadJSON(getDownloadedPathUrl, undefined, function(data){
                if(data && data.downloadedPath){
                    downloadedPath = data.downloadedPath;
                    $('#deploy-content #downloadedPath').val(downloadedPath);
                }
            });
        }
    }
//========================================init listener=====================================
    ViewManager.addViewListener("onShow", "#"+viewId, deployViewOnShow);
//================================================event bind================================
    $('#deploy-content #checkDeployPathButton').click(function(event){
        var path = $('#deploy-content #downloadedPath').val();
        if(!path){
            ViewManager.simpleWarning("Sources Folder could not be empty.");
            return false;
        }
        var typeToDeploy = undefined;
        $('#deploy-content .war-list-wrapper input').each(function(){
            if($(this).is(':checked')){
                typeToDeploy = $(this).val();
            }
        });
        DynamicLoad.postJSON(setDownloadedPathUrl, {downloadPath: path, typeToDeploy: typeToDeploy}, function(packages){
            //            
        },function(error){ViewManager.simpleError(error.message);});
    });
    
    
}(window));
