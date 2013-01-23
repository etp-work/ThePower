/**
 * This module is only worked for view build-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    var viewId = "build-content";
    
    function viewOnShow(){
        ViewManager.show("#common");
        ViewManager.hide("#environment");
        $('#build-content .bulid-feature-header a').first().addClass("active");
        
        var url = "/powerbuild/getAllTrees.ajax";
        
        DynamicLoad.loadJSON(url, undefined, function(dirTrees){
            if(!dirTrees || dirTrees.length === 0){
                return;
            }
            
            $('.group label .parent').off("click");//remove click binding to .parent first.
            $('.group .child').off("click");//remove click binding to .child first.
            var scope = angular.element($('.bulid-list')).scope();
            scope.$apply(function(){
                scope.dirTrees = dirTrees;
            });
            //rebinding click to .parent.
            $('.group label .parent').on("click", 
                    function(event){
                        var isChecked = $(this).is(':checked');
                        $(this).parent().parent().siblings().find('.child').attr("checked", isChecked);
                        
                        var needDeploy = $('#needDeploy').is(':checked');
                        
                        if($(this).val() === "Others" && isChecked && needDeploy){//unselect deploy button if something in Others selected.
                            $('#needDeploy').attr("checked", false);
                            ViewManager.addNotification({
                                type: "attention",
                                message: "Items in Others can't be deployed.",
                                timeout: 10000
                            });
                        }
                    }
            );
          //rebinding click to .child.
            $('.group .child').on("click", 
                    function(event){
                        var isChecked = $(this).is(':checked');
                        var parent = $(this).parent().parent().parent().find('.parent');
                        if(isChecked){
                            var needDeploy = $('#needDeploy').is(':checked');
                            parent.attr("checked", isChecked);
                            if(parent.val() === "Others" && needDeploy){//unselect deploy button if something in Others selected.
                                $('#needDeploy').attr("checked", false);
                                ViewManager.addNotification({
                                    type: "attention",
                                    message: "Items in Others can't be deployed.",
                                    timeout: 10000
                                });
                            }
                        }else{
                            var isAllFalse = true;
                            $(this).parent().parent().siblings().find('.child').each(function(){
                                if($(this).is(':checked')){
                                    isAllFalse = false;
                                }
                            });
                            if(isAllFalse){
                                parent.attr("checked", isChecked);
                            }
                        }
                    }
            );
          
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
    });
    }
    
    ViewManager.addViewListener("onShow", "#"+viewId, viewOnShow);
    
    //bind tab click event within build-content.
    $('#build-content .bulid-feature-header a').click(function(event){
        $(this).parent().siblings().find('a').removeClass("active");
        $(this).addClass("active");
        var id = $(this).attr("href").substring(2);
        ViewManager.hide('.bulid-feature-content');
        ViewManager.show("#"+id);
        return false;
    });
    
    
    function setDisableElements(isDisable){
        $('#setDefault4Build').attr("disabled", isDisable);
        $('#resetDefault4Build').attr("disabled", isDisable);
        $('#buildButton').attr("disabled", isDisable);
        $('#needDeploy').attr("disabled", isDisable);
    }
    
    function initJQueryBinding(){
        
        Lifecycle.setCallback(viewId, function(status){
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
        
    }
    
    /**
     * return isChecked value of element needDeploy if isNeeded is undefined.
     * set checked value to isNeeded if isNeeded is not undefined.
     */
    function needDeploy(isNeeded){
        if(isNeeded === undefined){
            return $('#needDeploy').is(':checked');
        }
        $('#needDeploy').attr("checked", isNeeded);
    }
    
    function notification(type, message, timeout){
        ViewManager.addNotification({
            type: type,
            message: message,
            timeout: timeout
        });
    }
    
    function deployWarning(){
        notification("attention", "Items in Others can't be deployed.", 10000);
    }
    
    function successNotification(message){
        notification("success", message, 5000);
    }
    
    function errorNotification(message){
        notification("error", message, undefined);
    }
    
    function getTreeDir(dirTrees, value){
        for ( var i = 0; i < dirTrees.length; i++) {
            var dirTree = dirTrees[i];
            if(dirTree.name === value){
                return dirTree;
            }
        }
        //implicitly falsy
    }
    
    /**
     * update corresponding children items when clicking a parent item
     */
    function updateChildren(dirTrees, value, checked){
        var dirTree = getTreeDir(dirTrees, value);
        if(!dirTree){
            return;
        }
        dirTree.checked = checked;
        if(checked && (value === "Others") && needDeploy()){
            needDeploy(false);
            deployWarning();
        }
        for( var j = 0; j < dirTree.subDirs.length; j++){
            dirTree.subDirs[j].checked = checked;
        }
    }
    
    /**
     * update parent item when clicking a child item
     */
    function updateParent(dirTrees, parent, value, checked){
        var dirTree = getTreeDir(dirTrees, parent);
        if(!dirTree){
            return;
        }
        var sub = getTreeDir(dirTree.subDirs, value);
        if(sub){
            sub.checked = checked;
        }
        if(checked){
            dirTree.checked = checked;
            if(parent === "Others" && needDeploy()){
                needDeploy(false);
                deployWarning();
            }
        }else{
            var isAllFalse = true;
            for( var j = 0; j < dirTree.subDirs.length; j++){
                if(dirTree.subDirs[j].name === value){
                    continue;
                }
                if(dirTree.subDirs[j].checked){
                    isAllFalse = false;
                    break;
                }
            }
            if(isAllFalse){
                dirTree.checked = false;
            }
        }
    }
    
    function canDeploy(checkbox, dirTrees){
        if(!checkbox.checked){
            return;
        }
        
        var others = getTreeDir(dirTrees, "Others");
        if(!others){
            return;
        }
        if(others.checked){
            checkbox.checked = false;
            deployWarning();
        }
    }
    
    function getAllSelection(dirTrees, selection){
        for(var i = 0; i < dirTrees.length; i++){
            if(dirTrees[i].checked){
                selection.push(dirTrees[i].name);
            }
            if(dirTrees[i].subDirs.length > 0){
                getAllSelection(dirTrees[i].subDirs, selection);
            }
        }
    }
    
    function setDefaultSelection($http, selection){
        Lifecycle.setState(viewId, Lifecycle.BUILD_EXECUTING);
        var url = "/PortalKit/powerbuild/setDefault.ajax";
        $http.post(url, {selection:selection}).success(function(data, status, headers, config) {
            successNotification("Successfully "+(selection.length === 0) ? "reset" : "saved" +" default selection.");
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
        }).error(function(data, status, headers, config){
            errorNotification(data.message);
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
        });
    }
    
}(window));