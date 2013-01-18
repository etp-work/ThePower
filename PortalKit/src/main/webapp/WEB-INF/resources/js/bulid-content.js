/**
 * This JS module is written for view build-content in index.jsp.
 */
(function () {
    
    var viewId = "bulid-content";
    
    //listeners
    function buildOnShowListener(){
        
        //Data load only when status of portal is loaded or newconfig.
        if(Lifecycle.getState(viewId) !== Lifecycle.LOADED && Lifecycle.getState(viewId) !== Lifecycle.NEWCONFIG){
            return;
        }
        
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
    
    //add listener on 'onShow' event to view 'build-content'
    ViewManager.addViewListener("onShow", "#bulid-content", buildOnShowListener); //add listener to monitor what will happen when build-content shown.
    
    
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
    
    
});
