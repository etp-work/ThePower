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
    var deployUrl = "/deploy/deploy.ajax";
    var downloadedPath = undefined;
    var checkedList = [];
    var portals = {};
    
//=========================================functions========================================
    function checkChoose(){
        checkedList = [];
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
            var isChosen = false;
            for(var i in choose){
                var fileName = $(this).text();
                if(fileName.indexOf(choose[i]) > -1){
                    checkedList.push(fileName);
                    isChosen = true;
                    break;
                }
            }
            if(isChosen){
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
            if(!PackageCheckedResult.allGzFiles){
                ViewManager.simpleWarning("Download path is invalid.");
                return;
            }
            var scope = angular.element($('#deploy-content .deployList')).scope();
            scope.$apply(function(){
                scope.allGzFiles = PackageCheckedResult.allGzFiles;
            });
            portals["referencePortal"] = PackageCheckedResult.referencePortal;
            portals["multiscreenPortal"] = PackageCheckedResult.multiscreenPortal;
            checkChoose();
        },function(error){
            var scope = angular.element($('#deploy-content .deployList')).scope();
            scope.$apply(function(){
                scope.allGzFiles = [];
            });
            ViewManager.simpleError(error.message);
         });
    }
    
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
        if(!$('#deploy-content #downloadedPath').val()){
            ViewManager.simpleWarning("Please set download path first.");
            return false;
        }
        if(checkedList.length === 0){
            ViewManager.simpleWarning("Please check the download path first.");
            return false;
        }
        var choosedElement = undefined;
        $('#deploy-content .war-list-wrapper input').each(function(){
            if($(this).is(':checked')){
                choosedElement = $(this);
            }
        });
        if(!choosedElement){
            ViewManager.simpleWarning("Please choose one type for deploy.");
            return false;
        }
        
        DynamicLoad.postJSON(deployUrl, {
            downloadPath: downloadedPath,
            typeToDeploy: choosedElement.val(),
            deployPackages: checkedList
        }, function(data){
            ViewManager.simpleSuccess("Please choose one type for deploy.");
        });
    });
    
}(window));
