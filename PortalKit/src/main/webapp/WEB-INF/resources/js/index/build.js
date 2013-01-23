/**
 * This module is only worked for view build-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    var viewId = "build-content";
    var getTreesUrl = "/powerbuild/getAllTrees.ajax";
    var setDefaultUrl = "/powerbuild/setDefault.ajax";
    var buildUrl = "/powerbuild/build.ajax";
    
//=========================================functions=====================================
    
    function needDeploy(isNeeded){
        if(isNeeded === undefined){
            return $('#needDeploy').is(':checked');
        }
        $('#needDeploy').attr("checked", isNeeded);
    }
    
    //rebind click event build-list checkbox items
    function rebindSelection(){
        $('.parentB').on("click", function(event){
                    var isChecked = $(this).is(':checked');
                    $(this).attr("checked", isChecked);
                    $(this).parent().parent().siblings().find('.childB').attr("checked", isChecked);
                    if($(this).val() === "Others" && isChecked && needDeploy()){//unselect deploy button if something in Others selected.
                        needDeploy(false);
                        ViewManager.simpleWarning("Items in Others can't be deployed.");
                    }
        });
        $('.childB').on("click", function(event){
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
        ViewManager.show("#common");
        ViewManager.hide("#environment");
        $('#build-content .bulid-feature-header a').first().addClass("active");
        
        if(Lifecycle.getState(viewId) !== Lifecycle.LOADED && Lifecycle.getState(viewId) !== Lifecycle.NEWCONFIG){
            return;
        }
        
        DynamicLoad.loadJSON(getTreesUrl, undefined, function(dirTrees){
            if(!dirTrees || dirTrees.length === 0){
                return;
            }
            
            $('.parentB').off("click");//remove click binding to .parent first.
            $('.childB').off("click");//remove click binding to .child first.
//            var scope = angular.element($('#common')).scope();
//            scope.$apply(function(){
//                scope.dirTrees = dirTrees;
//            });
            $( "#common .bulid-list" ).html(
                    $( "#DirTreeTemplate" ).render( dirTrees )
                );
            rebindSelection();
          
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
        });
    }
    
    //set isdisable to all the editable elements on build-content.
    function setDisableElements(isDisable){
        $('#setDefault4Build').attr("disabled", isDisable);
        $('#resetDefault4Build').attr("disabled", isDisable);
        $('#buildButton').attr("disabled", isDisable);
        $('#needDeploy').attr("disabled", isDisable);
    }
    
    //check which sub view is shown, and return it's id.
    function subViewShownId(){
        var subViewId = undefined;
        $('.bulid-feature-content').each(function(){
            if($(this).is(':visible')){
                subViewId = $(this).attr("id");
            }
        });
        return subViewId;
    }
    //return all the selection
    function getBuildSelection(){
        var defaultSelection = [];
        $('#common .bulid-list input').each(function (){
            if($(this).is(':checked')){
                defaultSelection.push($(this).val());
            }
        });
        return defaultSelection;
    }
    
     //return all the 2 level selection
    function getSubBuildSelection(){
        var defaultSelection = [];
        $('.childB').each(function (){
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
        var element = $(".childB[value=\""+packagename+"\"]").parent().siblings('.status');
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
        setDisableElements(true);
        build(subSelection);
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
        ViewManager.hide('.bulid-feature-content');
        ViewManager.show("#"+id);
        return false;
    });
    
    $('#setDefault4Build').click(function(event){
                var defaultSelection = getBuildSelection();
                
                if(defaultSelection.length === 0){//if no anything checked, give a warning.
                    ViewManager.simpleWarning("You can't set nothing to default.");
                    return false;
                }
                saveDefaultSelection(defaultSelection, "saved");
                return false;
    });
    
    $('#resetDefault4Build').click(function(event){
                $('#common .bulid-list input').attr("checked", false);
                saveDefaultSelection([], "reset");
                return false;
    });
    
    
    
    $('#buildButton').click(function(event){
        var subViewId = subViewShownId();
        switch (subViewId) {
        case "common":
            commonBuild();
            break;
        case "environment":
            
            break;
        default:
            break;
        }
        return false;
    });
    
    
}(window));