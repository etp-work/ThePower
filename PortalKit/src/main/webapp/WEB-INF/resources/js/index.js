$(document).ready(function(){

//=======================initialization=================================
//listeners
    function buildOnShowListener(){
        var url = "/powerbuild/getAllTrees.ajax";
        DynamicLoad.loadJSON(url, undefined, function(dirTrees){
                if(!dirTrees){
                    return;
                }
                var scope = angular.element($('.bulid-list')).scope();
                scope.$apply(function(){
                    scope.dirTrees = dirTrees;
                });
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
    //set tab of default content to active state.
    $('#bulid-content').addClass('active');
    //set tab event to switch content views.
    $('.tab-header ul li a').click(
	    function(event) { 
                $(this).parent().siblings().find("a").removeClass('active'); // Remove "current" class from all tabs
                $(this).addClass('active');
                var currentTab = $(this).attr('href'); // Set variable "currentTab" to the value of href of clicked tab
                $(currentTab).siblings().hide(); // Hide all content divs
                ViewManager.show(currentTab);
                event.preventDefault();
        }
    );
		
//=======================build page===================================
    $('#setDefaultSelection').click(
        function(event){
		    var defaultSelection = [];
		    $('.bulid-list .group li label input').each(function (){
		        if($(this).is(':checked')){
		            defaultSelection.push($(this).val());
		        }
		    });
		    var url = "/powerbuild/setDefault.ajax";
		    
		    DynamicLoad.postJSON(url, {
		        selection: defaultSelection
		    }, function (){
                //TODO success callback		        
		    }, function (){
		        //TODO error callback
		    });
		}
   );
    
    $('#resetDefaultSelection').click(
        function(event){
            ViewManager.addNotification("success", "You are right");
        }        
    );
		
		
		
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
		    DynamicLoad.postJSON(url, settings);
		}
    );
		
        
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


  
  
  