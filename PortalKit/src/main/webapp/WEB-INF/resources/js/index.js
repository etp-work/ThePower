$(document).ready(function(){
    var resource = $('#resources').val();
    DynamicLoad.loadStaticJS(resource+"/js/common/testJS.js");

//=======================initialization=================================
    //variables
    var shownViewId;
    var needDeploy;
    
    function setNeedDeploy(param){
        needDeploy = param;
        $('#needDeploy').attr("checked", needDeploy);
    }
    
    //listeners
    function buildOnShowListener(){
        var url = "/powerbuild/getAllTrees.ajax";
        DynamicLoad.loadJSON(url, undefined, function(dirTrees){
                if(!dirTrees){
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
                            
                            if($(this).val() === "Others" && isChecked && needDeploy){//unselect deploy button if something in Others selected.
                                setNeedDeploy(false);
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
                                parent.attr("checked", isChecked);
                                if(parent.val() === "Others" && needDeploy){//unselect deploy button if something in Others selected.
                                    setNeedDeploy(false);
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
        });
    }

    function deployOnShowListener(){
        //TODO
    }

    function settingsOnShowListener(){
        var url = "/settings/getAll.ajax";
        DynamicLoad.loadJSON(url, undefined, function(data){
            if(data){
               if(data.portalTeamPath){
                   $('#portalTeamPath').val(data.portalTeamPath);
               }
               if(data.tomcatWebappsPath){
                   $('#tomcatWebappsPath').val(data.tomcatWebappsPath);
               }
            }
        });
    }
    
    ViewManager.addViewListener("onShow", "#bulid-content", buildOnShowListener); //add listener to monitor what will happen when build-content shown.
    ViewManager.addViewListener("onShow", "#deploy-content", deployOnShowListener); //add listener to monitor what will happen when deploy-content shown.
    ViewManager.addViewListener("onShow", "#setting-content", settingsOnShowListener); //add listener to monitor what will happen when settings-content shown.

    //hide all the content views first.
    ViewManager.hide('.content-wrapper div.content-box');
    //show the default content (build-content).
    ViewManager.show('#bulid-content');
    //set shownViewId to default content.
    shownViewId = "bulid-content";
    //set tab event to switch content views.
    $('.tab-header ul li a').click(
	    function(event) { 
                $(this).parent().siblings().find("a").removeClass('active'); // Remove "current" class from all tabs
                $(this).addClass('active');
                var currentTab = $(this).attr('href'); // Set variable "currentTab" to the value of href of clicked tab
                $(currentTab).siblings().hide(); // Hide all content divs
                ViewManager.show(currentTab);
                shownViewId = $(currentTab).attr("id");//set shownViewId to currently displayed viewId.
                event.preventDefault();
        }
    );
		
//=======================build page===================================
    
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
    
    function build(selection){
        var url = "/powerbuild/build.ajax";
        if(selection.length === 0){
            return;
        }
            
        DynamicLoad.postJSON(url, {
            selection: selection.shift(),
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
            }else if(!BuildResult.deployed && needDeploy){
                ViewManager.addNotification({
                    type: "error",
                    message: "Deploy error"
                });
            }else{
                build(selection);
            }
        }, function(error){
            ViewManager.addNotification({
                type: "error",
                message: "Internal error:"+error.message,
                timeout: 15000
            });
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
        build(defaultSelection);
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
                setNeedDeploy(false);
                return;
            }
        }
        setNeedDeploy($(this).is(':checked'));
    });
		
		
		
//=======================settings page=================================		
    $('#saveSettings').click(
        function(event) {
		    var ptPath = $('#portalTeamPath').val();
		    var twPath = $('#tomcatWebappsPath').val();
		    var url = "/settings/set.ajax";
		    var settings = {
		            portalTeamPath: ptPath,
		            tomcatWebappsPath: twPath
		        };
		    DynamicLoad.postJSON(url, settings, function(){
		        ViewManager.addNotification({
		            type: "success",
		            message: "Successfully saved settings",
		            timeout: 5000,
		            callback: function(){
		                alert("hello save");
		            }
		        });
		    }, function(error){
		        ViewManager.addNotification({
                    type: "error",
                    message: error.message
                });
		    });
		}
    );
		
//=======================test page====================================
    
//=======================foot page=====================================
        
    $(".close").click(
        function () {
            $(this).parent().fadeTo(400, 0, function () { 
                    $(this).slideUp(400);
                }
            );
            return false;
        }
    );
    
    

	    
	    
});


  
  
  