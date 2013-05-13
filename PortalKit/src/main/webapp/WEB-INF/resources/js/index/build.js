/**
 * This module is only worked for view build-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    
//=========================================variable=====================================
    var viewId = "build-content";
    var getBuildInfoUrl = "/powerbuild/getBuildInformation.ajax";
    var executeUrl = "/powerbuild/execute.ajax";
    var executeWithTypeUrl = "/powerbuild/executeWithType.ajax";
    
    var deployInformation;
    
    
//=========================================functions=====================================
    
    function needDeploy(viewId, isNeeded){
        if(isNeeded === undefined){
            return $("#build-content "+ viewId +" #needDeploy").is(':checked');
        }
        $("#build-content "+ viewId +" #needDeploy").attr("checked", isNeeded);
    }
    
    function needBuild(viewId, isNeeded){
        if(isNeeded === undefined){
            return $("#build-content "+ viewId +" #needBuild").is(':checked');
        }
        $("#build-content "+ viewId +" #needBuild").attr("checked", isNeeded);
    }
    
    function needTest(viewId, isNeeded){
        if(isNeeded === undefined){
            return $("#build-content "+ viewId +" #needTest").is(':checked');
        }
        $("#build-content "+ viewId +" #needTest").attr("checked", isNeeded);
    }
    
  //set isdisable to all the editable elements on build-content.
    function setDisableElements(isDisable){
        $('#build-content #common #buildButton').attr("disabled", isDisable);
        $('#build-content #common #needDeploy').attr("disabled", isDisable);
        $('#build-content #common #needBuild').attr("disabled", isDisable);
        $('#build-content #common #needTest').attr("disabled", isDisable);
        $('#build-content #environment #build4Set').attr("disabled", isDisable);
        $('#build-content #environment #needDeploy').attr("disabled", isDisable);
        $('#build-content #environment #needBuild').attr("disabled", isDisable);
        $('#build-content #environment #needTest').attr("disabled", isDisable);
        $('#build-content #common input').attr("disabled", isDisable);
        $('#build-content #environment input').attr("disabled", isDisable);
    }
    
    //rebind click event build-list checkbox items
    function rebindSelection(){
        $('#build-content #common .parentB').on("click", function(event){
                    var isChecked = $(this).is(':checked');
                    $(this).parent().parent().siblings().find('.childB').attr("checked", isChecked);
        });
        $('#build-content #common .childB').on("click", function(event){
                    var isChecked = $(this).is(':checked');
                    var parent = $(this).parent().parent().parent().find('.parentB');
                    if(isChecked){
                        parent.attr("checked", true);
                    }else{
                        var isAllFalse = true;
                        $(this).parent().parent().siblings().find('.childB').each(function(){
                            if($(this).is(':checked')){
                                isAllFalse = false;
                            }
                        });
                        if(isAllFalse){
                            parent.attr("checked", false);
                        }
                    }
                }
        );
    }
    
    function buildViewOnShow(){
        ViewManager.show("#build-content #common");
        ViewManager.hide("#build-content #environment");
        $('#build-content .bulid-feature-header a').removeClass("active");
        $('#build-content .bulid-feature-header a').first().addClass("active");
        
        //this is because the data on this page won't fetch each time.
        if(Lifecycle.getState() !== Lifecycle.LOADED && Lifecycle.getState() !== Lifecycle.NEWCONFIG){
            return;
        }
        
        DynamicLoad.loadJSON(getBuildInfoUrl, undefined, function(buildInfo){
            if(!buildInfo){
                Lifecycle.setState(Lifecycle.NO_CONFIGURATION);
                return;
            }
            deployInformation = buildInfo.deployInfo;
            $('#build-content #common .parentB').off("click");//remove click binding to .parent first. Then will be re-bind again when data fetched later.
            $('#build-content #common .childB').off("click");//remove click binding to .child first. Then will be re-bind again when data fetched later.
            var scope = angular.element($('.build-list')).scope();
            scope.$apply(function(){
                scope.dirTrees = buildInfo.buildList;
            });
            rebindSelection();//re-bind click event to .parent .child element. 
          
            Lifecycle.setState(Lifecycle.NORMAL);
        }, function(){
            var scope = angular.element($('.build-list')).scope();
            scope.$apply(function(){
                scope.dirTrees = [];
            });
            setDisableElements(true);
            $('#build-content #environment #build4Set').attr("disabled", true);
        });
    }
    
     //return all the 2 level selection
    function getSubBuildSelection(){
        var defaultSelection = [];
        $('#build-content #common .childB').each(function (){
            if($(this).is(':checked')){
                defaultSelection.push($(this));
            }
        });
        return defaultSelection;
    }
    
    function build(selection){
        if(selection.length === 0){
            Lifecycle.setState(Lifecycle.NORMAL);
            return;
        }
        var pack = selection.shift();
        var element = pack.parent().siblings('.status');
        element.addClass("s-working");
        DynamicLoad.postJSON(executeUrl, {absolutePath: pack.val(), needDeploy: needDeploy("#common"), needBuild: needBuild("#common"), needTest: needTest("#common")}, 
                function(BuildResult){
                    element.removeClass("s-working");
                    if(!BuildResult.success){
                        ViewManager.simpleError("Executed failure, click to check log.",function(){alert(BuildResult.message)});
                        element.addClass("s-error");
                        Lifecycle.setState(Lifecycle.NORMAL);
                    }else if(!BuildResult.deployed && needDeploy("#common")){
                        ViewManager.simpleError(pack.attr("title") + " deployed failure");
                        element.addClass("s-error");
                        Lifecycle.setState(Lifecycle.NORMAL);
                     }else{
                        var sucMessage = pack.attr("title") + " executed successfully.";
                        ViewManager.simpleSuccess(sucMessage);
                        element.addClass("s-success");
                        build(selection);
                     }
                 }, function(error){
                       element.removeClass("s-working");
                       ViewManager.simpleError("Internal error:"+error.message);
                       Lifecycle.setState(Lifecycle.NORMAL);
                 }
        );
    }
    //build all sub selection on common view. 
    function commonBuild(subSelection){
        $('#build-content #common .status').attr("class", "status");
        Lifecycle.setState(Lifecycle.IN_PROCESS);
        build(subSelection);
    }
    //build all the desing packages, and deploy the specified set of them.
    function environmentBuild(choosedElement){
        var chooseId = choosedElement.val();
        $('#build-content #environment .status').attr("class", "status");
        Lifecycle.setState(Lifecycle.IN_PROCESS);
        var element = choosedElement.parent().next('.status');
        element.addClass("s-working");
        DynamicLoad.postJSON(executeWithTypeUrl, {
                                 type: chooseId,
                                 needDeploy: needDeploy("#environment"),
                                 needBuild: needBuild("#environment"), 
                                 needTest: needTest("#environment")
                            }, function(BuildResult){
                                   element.removeClass("s-working");
                                   if(!BuildResult.success){
                                        ViewManager.simpleError("Executed failure", function(){alert(BuildResult.message)});
                                        element.addClass("s-error");
                                   }else if(!BuildResult.deployed && needDeploy("#environment")){
                                        ViewManager.simpleError("There might be some of packages failed to deploy.");
                                        element.addClass("s-error");
                                   }else{
                                        var sucMessage = "Executed successfully.";
                                        ViewManager.simpleSuccess(sucMessage);
                                        element.addClass("s-success");
                                   }
                                   Lifecycle.setState(Lifecycle.NORMAL);
                            }, function(error){
                                   element.removeClass("s-working");
                                   ViewManager.simpleError("Internal error:"+error.message);
                                   Lifecycle.setState(Lifecycle.NORMAL);
                            }
        );
    }
    
    //do filter the specified text within a specified range of elements.
    function doFilter(text){
        ViewManager.filter("#build-content #common .build-list ul li", "div input[type=\"checkbox\"]", text);
    }
    
    function checkBuild4SetValid(){
        var options = $('#build-content #environment input[type="checkbox"]');
        for(var i = 0; i < options.length; i++){
            if($(options[i]).is(':checked')){
                $('#build-content #environment #build4Set').attr("disabled", false);
                return true;
            }
        }
        $('#build-content #environment #build4Set').attr("disabled", true);
    }
    
//========================================init listener=====================================
    
    ViewManager.addViewListener("onShow", "#"+viewId, buildViewOnShow);
    
    Lifecycle.addStateListener(function(status){
            switch (status) {
                case Lifecycle.NORMAL:
                    setDisableElements(false);
                    checkBuild4SetValid();
                    break;
                case Lifecycle.IN_PROCESS:
                    setDisableElements(true);
                    $('#build-content #environment #build4Set').attr("disabled", true);
                    break;
                case Lifecycle.NO_CONFIGURATION:
                    var scope = angular.element($('.build-list')).scope();
                    scope.$apply(function(){
                        scope.dirTrees = [];
                    });
                    setDisableElements(true);
                    $('#build-content #environment #build4Set').attr("disabled", true);
                    break;
                default:
                    break;
            }
    });
    
//================================================event bind======================
  //bind tab click event within build-content.
    $('#build-content .bulid-feature-header a').click(function(event){
        $(this).parent().siblings().find('a').removeClass("active");
        $(this).addClass("active");
        var id = $(this).attr("href").substring(2);
        ViewManager.hide('#build-content .bulid-feature-content');
        ViewManager.show("#build-content #"+id);
        return false;
    });
    
    $('#build-content #common #buildButton').click(function(event){
        $('#build-content #common #quickSearch').val("");
        doFilter("");
        
        var subSelection = getSubBuildSelection();
        if(subSelection.length === 0){
            ViewManager.simpleWarning("You should choose at least one project.");
            return false;
        }
        if(!needBuild("#common") && !needDeploy("#common") && !needTest("#common")){
            ViewManager.simpleWarning("You should choose at least one option.");
            return false;
        }
        commonBuild(subSelection);
        return false;
    });
    
    $('#build-content #environment #build4Set').click(function(event){
        var choosedElement = undefined;
        $('#build-content #environment .build-list input').each(function(){
            if($(this).is(':checked')){
                choosedElement = $(this);
            }
        });
        if(!choosedElement){
            ViewManager.simpleWarning("Please choose one type for execution.");
            return false;
        }
        if(!needBuild("#environment") && !needDeploy("#environment") && !needTest("#environment")){
            ViewManager.simpleWarning("You should choose at least one option.");
            return false;
        }
        environmentBuild(choosedElement);
    });
    
    $('#build-content #environment input[type="checkbox"]').click(function(event){
        if($(this).is(':checked')){
            $(this).parent().parent().siblings('li').find('input').attr("checked", false);
            if(deployInformation){
                //TODO list deploy list.
            }
        }
        checkBuild4SetValid();
    });
    
    
    $('#build-content #common #quickSearch').keyup(function(event) {
        var text = $(this).val();
        doFilter(text);
    });
    
    
}(window));