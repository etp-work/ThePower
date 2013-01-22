/**
 * This module is only worked for view build-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    var isInit = false;
    var viewId = "build-content";
    
    function setDisableElements(isDisable){
        $('#setDefault4Build').attr("disabled", isDisable);
        $('#resetDefault4Build').attr("disabled", isDisable);
        $('#buildButton').attr("disabled", isDisable);
        $('#needDeploy').attr("disabled", isDisable);
    }
    
    function initJQueryBinding(){
        $('#build-content .bulid-feature-header a').click(function(event){
            $(this).parent().siblings().find('a').removeClass("active");
            $(this).addClass("active");
            var id = $(this).attr("href").substring(2);
            ViewManager.hide('.bulid-feature-content');
            ViewManager.show("#"+id);
            return false;
        });
        
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
        var url = "/PortalKit/powerbuild/setDefault.ajax";
        $http.post(url, {selection:selection}).success(function(data, status, headers, config) {
            successNotification("Successfully saved default selection.");
        }).error(function(data, status, headers, config){
            errorNotification(data.message);
        });
    }
    
    function BuildInfoController($http, $scope){
        if(!isInit){
            initJQueryBinding();
            isInit = true;
        }
        $('#build-content .bulid-feature-header a').first().addClass("active");
        ViewManager.hide("#environment");
        
        //Data load only when status of portal is loaded or newconfig.
        if(Lifecycle.getState(viewId) !== Lifecycle.LOADED && Lifecycle.getState(viewId) !== Lifecycle.NEWCONFIG){
            return;
        }
        
        var url = "/PortalKit/powerbuild/getAllTrees.ajax";
        $http.get(url).success(function(data, status, headers, config) {
            $scope.dirTrees = data;
            $scope.checkParent = function($event){
                var checkbox = $event.target;
                var value = checkbox.value;
                updateChildren($scope.dirTrees, value, checkbox.checked);
            };
            $scope.checkChild = function($event, parent){
                var checkbox = $event.target;
                var value = checkbox.value;
                updateParent($scope.dirTrees, parent, value, checkbox.checked);
            };
            $scope.needDeploy = function($event){
                canDeploy($event.target, $scope.dirTrees);
            };
            $scope.setDefaultSelection = function($event){
               var selection = [];
               getAllSelection($scope.dirTrees, selection);
               setDefaultSelection($http, selection);
            };
            
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
          }).error(function(data, status, headers, config){
              errorNotification(data.message);
          });
        

    }
    
 // Create a new module
    var switchModule = Lifecycle.getModule("switchModule");
    
    switchModule.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
        when('/', {templateUrl: 'resources/templates/index-views/build-content.html', controller: BuildInfoController}).
        when('/build-content', {templateUrl: 'resources/templates/index-views/build-content.html', controller: BuildInfoController});
  }]);

}(window));