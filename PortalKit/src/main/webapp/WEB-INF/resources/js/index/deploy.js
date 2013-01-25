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
    var portals = {};
    
//=========================================functions========================================
    function setDisableElements(isDisable){
        $('#deploy-content #deploy4CI').attr("disabled", isDisable);
        $('#deploy-content #downloadedPath').attr("disabled", isDisable);
        $('#deploy-content #checkDeployPathButton').attr("disabled", isDisable);
        $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", isDisable);
    }
    
    function checkChoose(){
        $('#deploy-content .table-wrapper .deployList li').removeClass("grayed");
        if(!downloadedPath){
            return;
        }
        var chooseElement = undefined;
        $('#deploy-content .table-wrapper .war-list-wrapper input').each(function(){
            if($(this).is(':checked')){
                chooseElement = $(this);
            }
        });
        if(!chooseElement){
            return;
        }
        
        var choose = portals[chooseElement.val()];
        if(!choose){
            return;
        }
        $('#deploy-content .table-wrapper .deployList li').each(function(){
            var isFalsy = false;
            for(var i in choose){
                if($(this).text().indexOf(choose[i]) > -1){
                    isFalsy = true;
                    break;
                }
            }
            if(isFalsy){
                $(this).addClass("grayed");
            }
        });
        
    }
    
    function doCheck(){
        if(!downloadedPath){
            return;
        }
        DynamicLoad.postJSON(setDownloadedPathUrl, {downloadPath: downloadedPath}, function(PackageCheckedResult){
            if(!PackageCheckedResult){
                return;
            }
            if(!PackageCheckedResult.allGzFiles || PackageCheckedResult.allGzFiles.length === 0){
                return;
            }
            var scope = angular.element($('#deploy-content .deployList')).scope();
            scope.$apply(function(){
                scope.allGzFiles = PackageCheckedResult.allGzFiles;
            });
            portals["referencePortal"] = PackageCheckedResult.referencePortal;
            portals["multiscreenPortal"] = PackageCheckedResult.multiscreenPortal;
            checkChoose();
        },function(error){ViewManager.simpleError(error.message);});
    }
    
    function deployViewOnShow(){
        if(!downloadedPath){
            DynamicLoad.loadJSON(getDownloadedPathUrl, undefined, function(data){
                if(data && data.downloadedPath){
                    downloadedPath = data.downloadedPath;
                    $('#deploy-content #downloadedPath').val(downloadedPath);
                    doCheck();
                }
            });
        }
    }
    
    
//========================================init listener=====================================
    ViewManager.addViewListener("onShow", "#"+viewId, deployViewOnShow);
    Lifecycle.setCallback("build-content", function(status){
        switch (status) {
            case Lifecycle.NORMAL:
                setDisableElements(false);
                break;
            case Lifecycle.BUILD_EXECUTING:
                setDisableElements(true);
                break;
            default:
                break;
        }
    });
//================================================event bind================================
    $('#deploy-content #checkDeployPathButton').click(function(event){
        var path = $('#deploy-content #downloadedPath').val();
        if(!path){
            ViewManager.simpleWarning("Sources Folder could not be empty.");
            return false;
        }
        downloadedPath = path;
        doCheck();
    });
    
    
    $('#deploy-content .table-wrapper .war-list-wrapper input').click(function(event){
        if($(this).is(':checked')){
            $(this).parent().parent().siblings('li').find('input').attr("checked", false);
        }
        checkChoose();
    });
    
    $('#deploy-content #deploy4CI').click(function(event){
        //TODO
    });
    
}(window));
