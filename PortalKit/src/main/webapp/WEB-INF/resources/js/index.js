$(document).ready(function(){
    
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
    
        ViewManager.addViewListener("onShow", "bulid-content", buildOnShowListener); //add listener to monitor what will happen when build-content shown.
        ViewManager.addViewListener("onShow", "deploy-content", deployOnShowListener); //add listener to monitor what will happen when deploy-content shown.
        ViewManager.addViewListener("onShow", "setting-content", settingsOnShowListener); //add listener to monitor what will happen when settings-content shown.
		
		$('.content-wrapper div.content-box').hide();
		$('.content-wrapper div.default-tab').show(0, function () {
		    ViewManager.fireViewListener("onShow", $(this).attr("id"));
		    });
		$('.tab-header ul li a.default-tab').addClass('active');
		$('.tab-header ul li a').click( // When a tab is clicked...
				function(event) { 
					$(this).parent().siblings().find("a").removeClass('active'); // Remove "current" class from all tabs
					$(this).addClass('active');
					var currentTab = $(this).attr('href'); // Set variable "currentTab" to the value of href of clicked tab
					$(currentTab).siblings().hide(); // Hide all content divs
					$(currentTab).show(0, function () {
					    ViewManager.fireViewListener("onShow", currentTab.substring(1)); // Show the content div with the id equal to the id of clicked tab
					});
					event.preventDefault();
				}
			);
		
//=======================build page===================================
		$('#setDefaultSelection').click(function(event){
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
		});
		
		
		
//=======================settings page=================================		
		$('#saveSettings').click(function(event) {
		    var ptPath = $('#portalTeamPath').val();
		    var twPath = $('#tomcatWebappsPath').val();
		    var url = "/settings/set.ajax";
		    var settings = {
		            portalTeamPath: ptPath,
		            tomcatWebappsPath: twPath
		    };
		    DynamicLoad.postJSON(url, settings);
		});
		
        
    //Close notification:
        
        $(".close").click(
            function () {
                $(this).parent().fadeTo(400, 0, function () { 
                    $(this).slideUp(400);
                });
                return false;
            }
        );
    
    

	    
	    
});


  
  
  