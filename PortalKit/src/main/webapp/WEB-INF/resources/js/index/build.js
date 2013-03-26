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
    
  //set isdisable to all the editable elements on build-content.
    function setDisableElements(isDisable){
        $('#build-content #common #setDefault4Build').attr("disabled", isDisable);
        $('#build-content #common #resetDefault4Build').attr("disabled", isDisable);
        $('#build-content #common #buildButton').attr("disabled", isDisable);
        $('#build-content #common #needDeploy').attr("disabled", isDisable);
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
        
        DynamicLoad.loadJSON(getTreesUrl, undefined, function(dirTrees){
            if(!dirTrees || dirTrees.length === 0){
                Lifecycle.setState(Lifecycle.NO_CONFIGURATION);
                return;
            }
            
            $('#build-content #common .parentB').off("click");//remove click binding to .parent first. Then will be re-bind again when data fetched later.
            $('#build-content #common .childB').off("click");//remove click binding to .child first. Then will be re-bind again when data fetched later.
            var scope = angular.element($('.bulid-list')).scope();
            scope.$apply(function(){
                scope.dirTrees = dirTrees;
            });
            rebindSelection();//re-bind click event to .parent .child element. 
          
            Lifecycle.setState(Lifecycle.NORMAL);
        }, function(){
            var scope = angular.element($('.bulid-list')).scope();
            scope.$apply(function(){
                scope.dirTrees = [];
            });
            setDisableElements(true);
            $('#build-content #environment #build4Set').attr("disabled", true);
        });
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
                defaultSelection.push($(this));
            }
        });
        return defaultSelection;
    }
    //save selecton to server
    function saveDefaultSelection(defaultSelection, keyWord){
        Lifecycle.setState(Lifecycle.IN_PROCESS);
        DynamicLoad.postJSON(setDefaultUrl, { selection: defaultSelection }, function (){
                ViewManager.simpleSuccess("Successfully "+keyWord+" default selection.");
                Lifecycle.setState(Lifecycle.NORMAL);
            }, function (error){ ViewManager.simpleError(error.message); });
    }
    
    function build(selection){
        if(selection.length === 0){
            Lifecycle.setState(Lifecycle.NORMAL);
            return;
        }
        var pack = selection.shift();
        var element = pack.parent().siblings('.status');
        element.addClass("s-working");
        DynamicLoad.postJSON(buildUrl, {selection: pack.val(), needDeploy: needDeploy()}, 
                function(BuildResult){
                    element.removeClass("s-working");
                    if(!BuildResult.success){
                        ViewManager.simpleError("Build error, click to check log.",function(){ViewManager.showLog(BuildResult.message)});
                        element.addClass("s-error");
                        Lifecycle.setState(Lifecycle.NORMAL);
                    }else if(!BuildResult.deployed && needDeploy()){
                        ViewManager.simpleError("Deploy error");
                        element.addClass("s-error");
                        Lifecycle.setState(Lifecycle.NORMAL);
                     }else{
                        var sucMessage = pack.val() + " build "+(needDeploy() ? "+ deploy" : "" )+" successfully.";
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
        DynamicLoad.postJSON(buildSetUrl, {
                                 selection: chooseId,
                                 needDeploy: true
                            }, function(BuildResult){
                                   element.removeClass("s-working");
                                   if(!BuildResult.success){
                                        ViewManager.simpleError("Build error", function(){ViewManager.showLog(BuildResult.message)});
                                        element.addClass("s-error");
                                   }else if(!BuildResult.deployed){
                                        ViewManager.simpleError("There might be some of packages failed to deploy.");
                                        element.addClass("s-error");
                                   }else{
                                        var sucMessage = "All packages build + deploy successfully.";
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
        ViewManager.filter("#build-content #common .bulid-list ul li", "div input[type=\"checkbox\"]", text);
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
                    var scope = angular.element($('.bulid-list')).scope();
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
    
    $('#build-content #common #setDefault4Build').click(function(event){
        $('#build-content #common #quickSearch').val("");
        doFilter("");
        
        var defaultSelection = getBuildSelection();
                
        if(defaultSelection.length === 0){//if no anything checked, give a warning.
            ViewManager.simpleWarning("You can't set nothing to default.");
            return false;
        }
        saveDefaultSelection(defaultSelection, "saved");
        return false;
    });
    
    $('#build-content #common #resetDefault4Build').click(function(event){
        $('#build-content #common #quickSearch').val("");
        doFilter("");
        
        $('#common .bulid-list input').attr("checked", false);
        saveDefaultSelection([], "reset");
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
        commonBuild(subSelection);
        return false;
    });
    
    $('#build-content #environment #build4Set').click(function(event){
        var choosedElement = undefined;
        $('#build-content #environment input').each(function(){
            if($(this).is(':checked')){
                choosedElement = $(this);
            }
        });
        if(!choosedElement){
            ViewManager.simpleWarning("Please choose one type for build + deploy.");
            return false;
        }
        environmentBuild(choosedElement);
    });
    
    $('#build-content #environment input[type="checkbox"]').click(function(event){
        if($(this).is(':checked')){
            $(this).parent().parent().siblings('li').find('input').attr("checked", false);
        }
        checkBuild4SetValid();
    });
    
    
    $('#build-content #common #quickSearch').keyup(function(event) {
        var text = $(this).val();
        doFilter(text);
    });
    
    
}(window));