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
    var allfiles = [];
    var portals = {};
    
//=========================================functions========================================
    //check deploy type, and set the corresponding selection to grayed.
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
    
    //check the set download path for exist .gz packages.
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
            allfiles = PackageCheckedResult.allGzFiles;
            portals["referencePortal"] = PackageCheckedResult.referencePortal;
            portals["multiscreenPortal"] = PackageCheckedResult.multiscreenPortal;
            if(PackageCheckedResult.allGzFiles.length > 0){
                $('#deploy-content #deploy4CI').attr("disabled", false);
                $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", false);
            }
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
                    $('#deploy-content #checkDeployPathButton').attr("disabled", false);
                }else{
                    $('#deploy-content #checkDeployPathButton').attr("disabled", true);
                }
                $('#deploy-content #deploy4CI').attr("disabled", true);
                $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", true);
            });
        }
    }
    
    function isValueDirty(){
        if(!$('#deploy-content #downloadedPath').val()){
            $('#deploy-content #checkDeployPathButton').attr("disabled", true);
            $('#deploy-content #deploy4CI').attr("disabled", true);
            $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", true);
        }else if($('#deploy-content #downloadedPath').val() === downloadedPath && allfiles.length > 0){
            $('#deploy-content #checkDeployPathButton').attr("disabled", true);
            $('#deploy-content #deploy4CI').attr("disabled", false);
            $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", false);
        }else if($('#deploy-content #downloadedPath').val() === downloadedPath && allfiles.length === 0){
            $('#deploy-content #checkDeployPathButton').attr("disabled", false);
            $('#deploy-content #deploy4CI').attr("disabled", true);
            $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", true);
        }else if($('#deploy-content #downloadedPath').val() !== downloadedPath && allfiles.length > 0){
            var scope = angular.element($('#deploy-content .deployList')).scope();
            scope.$apply(function(){
                scope.allGzFiles = [];
            });
            allfiles = [];
            $('#deploy-content #checkDeployPathButton').attr("disabled", false);
            $('#deploy-content #deploy4CI').attr("disabled", true);
            $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", true);
            $('#deploy-content .table-wrapper .war-list-wrapper input').attr("checked", false);
        }else if($('#deploy-content #downloadedPath').val() !== downloadedPath && allfiles.length === 0){
            $('#deploy-content #checkDeployPathButton').attr("disabled", false);
            $('#deploy-content #deploy4CI').attr("disabled", true);
            $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", true);
        }
    
    }
    
    
//========================================init listener=====================================
    ViewManager.addViewListener("onShow", "#"+viewId, deployViewOnShow);
    
    Lifecycle.addStateListener(function(status){
        switch (status) {
            case Lifecycle.NORMAL:
                isValueDirty();
                $('#deploy-content #downloadedPath').attr("disabled", false);
                break;
            case Lifecycle.IN_PROCESS:
            case Lifecycle.NO_CONFIGURATION:
                $('#deploy-content #checkDeployPathButton').attr("disabled", true);
                $('#deploy-content #deploy4CI').attr("disabled", true);
                $('#deploy-content .table-wrapper .war-list-wrapper input').attr("disabled", true);
                $('#deploy-content .table-wrapper .war-list-wrapper input').attr("checked", false);
                $('#deploy-content #downloadedPath').attr("disabled", true);
                break;
            default:
                break;
        }
    });
    
//================================================event bind================================
    //click event bind on check button
    $('#deploy-content #checkDeployPathButton').click(function(event){
        var path = $('#deploy-content #downloadedPath').val();
        if(!path){
            ViewManager.simpleWarning("Sources Folder could not be empty.");
            return false;
        }
        downloadedPath = path;
        $('#deploy-content #checkDeployPathButton').attr("disabled", true);
        doCheck();
    });
    
    //click event bind on the checkboxs that indicate the deploy type.
    $('#deploy-content .table-wrapper .war-list-wrapper input').click(function(event){
        if($(this).is(':checked')){
            $(this).parent().parent().siblings('li').find('input').attr("checked", false);
        }
        checkChoose();
    });
    
    //click event bind on the deploy button.
    $('#deploy-content #deploy4CI').click(function(event){
        if(!$('#deploy-content #downloadedPath').val()){
            ViewManager.simpleWarning("Please set download path first.");
            return false;
        }
        if(checkedList.length === 0){
            ViewManager.simpleWarning("Please choose one type to deploy.");
            return false;
        }
        var choosedElement = undefined;
        $('#deploy-content .war-list-wrapper input').each(function(){
            if($(this).is(':checked')){
                choosedElement = $(this);
            }
        });
        if(!choosedElement){
            ViewManager.simpleWarning("Invalid type.");
            return false;
        }
        
        DynamicLoad.postJSON(deployUrl, {
            downloadPath: downloadedPath,
            typeToDeploy: choosedElement.val(),
            deployPackages: checkedList
        }, function(data){
            ViewManager.simpleSuccess("Deployed successfully.");
            $('#deploy-content #deploy4CI').attr("disabled", true);
        },function(error){
            ViewManager.simpleError("Deployed error : "+error.message);
        });
    });
    
    //keyup event bind on the downloadpath input field.
    $('#deploy-content #downloadedPath').keyup(function(event){
        isValueDirty();
    });
    
}(window));
