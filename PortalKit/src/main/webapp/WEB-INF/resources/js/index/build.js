/**
 * This module is only worked for view build-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    
//=========================================variable=====================================
    var viewId = "build-content";
    var getTreesUrl = "/powerbuild/getAllTrees.ajax";
    var setDefaultUrl = "/powerbuild/setDefault.ajax";
    var buildUrl = "/powerbuild/build.ajax";
    var buildSetUrl = "/powerbuild/buildset.ajax";
    
    
//=========================================functions=====================================
    
    function needDeploy(isNeeded){
        if(isNeeded === undefined){
            return $('#build-content #needDeploy').is(':checked');
        }
        $('#build-content #needDeploy').attr("checked", isNeeded);
    }
    
    //rebind click event build-list checkbox items
    function rebindSelection(){
        $('#build-content #common .parentB').on("click", function(event){
                    var isChecked = $(this).is(':checked');
                    $(this).parent().parent().siblings().find('.childB').attr("checked", isChecked);
                    if($(this).val() === "Others" && isChecked && needDeploy()){//unselect deploy button if something in Others selected.
                        needDeploy(false);
                        ViewManager.simpleWarning("Items in Others can't be deployed.");
                    }
        });
        $('#build-content #common .childB').on("click", function(event){
                    var isChecked = $(this).is(':checked');
                    var parent = $(this).parent().parent().parent().find('.parentB');
                    if(isChecked){
                        parent.attr("checked", true);
                        if(parent.val() === "Others" && needDeploy()){//unselect deploy button if something in Others selected.
                            needDeploy(false);
                            ViewManager.simpleWarning("Items in Others can't be deployed.");
                        }
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
        
        if(Lifecycle.getState(viewId) !== Lifecycle.LOADED && Lifecycle.getState(viewId) !== Lifecycle.NEWCONFIG){
            return;
        }
        
        DynamicLoad.loadJSON(getTreesUrl, undefined, function(dirTrees){
            if(!dirTrees || dirTrees.length === 0){
                return;
            }
            
            $('#build-content #common .parentB').off("click");//remove click binding to .parent first.
            $('#build-content #common .childB').off("click");//remove click binding to .child first.
            var scope = angular.element($('.bulid-list')).scope();
            scope.$apply(function(){
                scope.dirTrees = dirTrees;
            });
//            $( "#common .bulid-list" ).html(
//                    $( "#DirTreeTemplate" ).render( dirTrees )
//                );
            rebindSelection();
          
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
        });
    }
    
    //set isdisable to all the editable elements on build-content.
    function setDisableElements(isDisable){
        $('#build-content #common #setDefault4Build').attr("disabled", isDisable);
        $('#build-content #common #resetDefault4Build').attr("disabled", isDisable);
        $('#build-content #buildButton').attr("disabled", isDisable);
        $('#build-content #needDeploy').attr("disabled", isDisable);
        $('#build-content #common input').attr("disabled", isDisable);
        $('#build-content #environment input').attr("disabled", isDisable);
    }
    
    //check which sub view is shown, and return it's id.
    function subViewShownId(){
        var subViewId = undefined;
        $('#build-content .bulid-feature-content').each(function(){
            if($(this).is(':visible')){
                subViewId = $(this).attr("id");
            }
        });
        return subViewId;
    }
    //return all the selection
    function getBuildSelection(){
        var defaultSelection = [];
        $('#build-content #common input').each(function (){
            if($(this).is(':checked')){
                defaultSelection.push($(this).val());
            }
        });
        return defaultSelection;
    }
    
     //return all the 2 level selection
    function getSubBuildSelection(){
        var defaultSelection = [];
        $('#build-content #common .childB').each(function (){
            if($(this).is(':checked')){
                defaultSelection.push($(this).val());
            }
        });
        return defaultSelection;
    }
    //save selecton to server
    function saveDefaultSelection(defaultSelection, keyWord){
        Lifecycle.setState(viewId, Lifecycle.BUILD_EXECUTING);
        DynamicLoad.postJSON(setDefaultUrl, { selection: defaultSelection }, function (){
                ViewManager.simpleSuccess("Successfully "+keyWord+" default selection.");
                Lifecycle.setState(viewId, Lifecycle.NORMAL);
            }, function (error){ ViewManager.simpleError(error.message); });
    }
    
    function build(selection){
        if(selection.length === 0){
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
            return;
        }
        var packagename = selection.shift();
        var element = $("#build-content #common .childB[value=\""+packagename+"\"]").parent().siblings('.status');
        element.addClass("s-working");
        DynamicLoad.postJSON(buildUrl, {
                                          selection: packagename,
                                          needDeploy: needDeploy()
                                       }, function(BuildResult){
            if(!BuildResult.success){
                ViewManager.simpleError("Build error");
                element.removeClass("s-working");
                element.addClass("s-error");
                Lifecycle.setState(viewId, Lifecycle.NORMAL);
            }else if(!BuildResult.deployed && needDeploy()){
                ViewManager.simpleError("Deploy error");
                element.removeClass("s-working");
                element.addClass("s-error");
                Lifecycle.setState(viewId, Lifecycle.NORMAL);
            }else{
                var sucMessage = packagename + " build "+(needDeploy() ? "+ deploy" : "" )+" successfully.";
                ViewManager.simpleSuccess(sucMessage);
                element.removeClass("s-working");
                element.addClass("s-success");
                build(selection);
            }
        }, function(error){
            ViewManager.simpleError("Internal error:"+error.message);
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
        });
    }
    //build all sub selection on common view. 
    function commonBuild(){
        var subSelection = getSubBuildSelection();
        $('#build-content #common .status').attr("class", "status");
        Lifecycle.setState(viewId, Lifecycle.BUILD_EXECUTING);
        build(subSelection);
    }
    //build all the desing packages, and deploy the specified set of them.
    function environmentBuild(){
        var chooseId = undefined;
        var choosedElement = undefined;
        $('#build-content #environment input').each(function(){
            if($(this).is(':checked')){
                chooseId = $(this).val();
                choosedElement = $(this);
            }
        });
        $('#build-content #environment .status').attr("class", "status");
        Lifecycle.setState(viewId, Lifecycle.BUILD_EXECUTING);
        var element = choosedElement.parent().next('.status');
        element.addClass("s-working");
        DynamicLoad.postJSON(buildSetUrl, {
                                 selection: chooseId,
                                 needDeploy: true
                            }, function(BuildResult){
                                   if(!BuildResult.success){
                                        ViewManager.simpleError("Build error");
                                        element.removeClass("s-working");
                                        element.addClass("s-error");
                                        Lifecycle.setState(viewId, Lifecycle.NORMAL);
                                   }else if(!BuildResult.deployed){
                                        ViewManager.simpleError("There might be some of packages failed to deploy.");
                                        element.removeClass("s-working");
                                        element.addClass("s-error");
                                        Lifecycle.setState(viewId, Lifecycle.NORMAL);
                                   }else{
                                        var sucMessage = "All packages build + deploy successfully.";
                                        ViewManager.simpleSuccess(sucMessage);
                                        element.removeClass("s-working");
                                        element.addClass("s-success");
                                   }
                            }, function(error){
                                   ViewManager.simpleError("Internal error:"+error.message);
                                        Lifecycle.setState(viewId, Lifecycle.NORMAL);
                            }
        );        
    }
    
    
//========================================init listener=====================================
    
    ViewManager.addViewListener("onShow", "#"+viewId, buildViewOnShow);
    
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
    
    $('#build-content #common #setDefault4Build').click(function(event){
                var defaultSelection = getBuildSelection();
                
                if(defaultSelection.length === 0){//if no anything checked, give a warning.
                    ViewManager.simpleWarning("You can't set nothing to default.");
                    return false;
                }
                saveDefaultSelection(defaultSelection, "saved");
                return false;
    });
    
    $('#build-content #common #resetDefault4Build').click(function(event){
                $('#common .bulid-list input').attr("checked", false);
                saveDefaultSelection([], "reset");
                return false;
    });
    
    $('#build-content #buildButton').click(function(event){
        var subViewId = subViewShownId();
        switch (subViewId) {
        case "common":
            commonBuild();
            break;
        case "environment":
            environmentBuild();
            break;
        default:
            break;
        }
        return false;
    });
    
    $('#build-content #needDeploy').click(function(event){
        var isNeeded = needDeploy();
        if(!isNeeded){
            return;
        }
        if($('#build-content #common .parentB[value="Others"]').is(':checked')){
            ViewManager.simpleWarning("Items in Others can't be deployed.");
            needDeploy(false);
        }
    });
    
    
}(window));