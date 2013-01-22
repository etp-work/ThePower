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
    
    function setDef4Build(defaultSelection, message){
        var url = "/powerbuild/setDefault.ajax";
        
        DynamicLoad.postJSON(url, {
            selection: defaultSelection
        }, function (){
            ViewManager.addNotification({
                type: "success",
                message: message,
                timeout: 5000
            });        
        }, function (error){
            ViewManager.addNotification({
                type: "error",
                message: error.message
            });
        });
    }
    
    function getBuildSelection(){
        var defaultSelection = [];
        $('.bulid-list .group input').each(function (){
            if($(this).is(':checked')){
                defaultSelection.push($(this).val());
            }
        });
        return defaultSelection;
    }
    
    function build(selection, needDeploy){
        var url = "/powerbuild/build.ajax";
        if(selection.length === 0){
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
            return;
        }
        var packagename = selection.shift();
        var element = $(".bulid-list .group .child[value=\""+packagename+"\"]").parent().siblings('.status');
        element.addClass("s-working");
        DynamicLoad.postJSON(url, {
            selection: packagename,
            needDeploy: needDeploy
        }, function(BuildResult){
            if(!BuildResult.success){
                ViewManager.addNotification({
                    type: "error",
                    message: "Build error",
                    callback: function(){
                        //TODO show (BuildResult.message);
                    }
                });
                element.removeClass("s-working");
                element.addClass("s-error");
                Lifecycle.setState(viewId, Lifecycle.NORMAL);
            }else if(!BuildResult.deployed && needDeploy){
                ViewManager.addNotification({
                    type: "error",
                    message: "Deploy error"
                });
                element.removeClass("s-working");
                element.addClass("s-error");
                Lifecycle.setState(viewId, Lifecycle.NORMAL);
            }else{
                var sucMessage = packagename + " build "+(needDeploy ? "+ deploy" : "" )+" successfully.";
                ViewManager.addNotification({
                    type: "success",
                    timeout: 5000,
                    message: sucMessage
                });
                element.removeClass("s-working");
                element.addClass("s-success");
                build(selection, needDeploy);
            }
        }, function(error){
            ViewManager.addNotification({
                type: "error",
                message: "Internal error:"+error.message,
                timeout: 15000
            });
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
        });
    }
    
    function initJQueryBinding(){
        $('#setDefault4Build').click(
                function(event){
                    var defaultSelection = getBuildSelection();
                    
                    if(defaultSelection.length === 0){//if no anything checked, give a warning.
                        ViewManager.addNotification({
                            type: "attention",
                            timeout: 30000,
                            message: "You can't set nothing to default."
                        });
                        return;
                    }
                    setDef4Build(defaultSelection, "Successfully saved default selection.");
                    event.preventDefault();
                }
           );
            
         $('#resetDefault4Build').click(
                function(event){
                    
                    $('.bulid-list .group input').each(function (){
                        if($(this).is(':checked')){
                            $(this).attr("checked", false);
                        }
                    });
                    setDef4Build([], "Successfully reset default selection.");
                
                    event.preventDefault();
               }
            );
            $('#buildButton').click(function(event){
                var defaultSelection = [];
                $('.bulid-list .group .child').each(function (){
                    if($(this).is(':checked')){
                        defaultSelection.push($(this).val());
                    }
                });
                var needDeploy = $('#needDeploy').is(':checked');
                
                $('.bulid-list .group .status').attr('class', 'status');
                
                Lifecycle.setState(viewId, Lifecycle.BUILD_EXECUTING);//set portal state to BUILD_EXECUTING
                setDisableElements(true);
                build(defaultSelection, needDeploy);
                event.preventDefault();
            });
            
            $('#needDeploy').click(function(event){
                if($(this).is(':checked')){
                    var canDeploy = true;
                    $('.bulid-list .group .parent[value="Others"]').parent().parent().siblings().find('.child').each(function(){
                        if($(this).is(':checked')){
                            canDeploy = false;
                        }
                    });
                    
                    if(!canDeploy){
                        ViewManager.addNotification({
                            type: "attention",
                            message: "Items in Others can't be deployed.",
                            timeout: 10000
                        });
                        event.preventDefault();
                    }
                }
            });
    }
    
    function checkItems(level,$event){
        if(level === "parent"){
            
        }
    }
    
    function BuildInfoController($http, $scope){
        if(!isInit){
            initJQueryBinding();
            isInit = true;
        }
        var url = "/PortalKit/powerbuild/getAllTrees.ajax";
        $http.get(url).success(function(data, status, headers, config) {
            $scope.dirTrees = data;
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
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