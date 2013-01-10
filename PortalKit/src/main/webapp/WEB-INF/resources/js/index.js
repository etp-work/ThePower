
function specTreeController($scope) {
    var url = "/power/getAllTrees.ajax";
    DynamicLoad.loadJSON(url, undefined, function(dirTrees){
            if(!dirTrees){
                return;
            }
            $scope.dirTrees = dirTrees;
            $scope.$apply();
    });
 
}

function buildOnShow(){
    
}

function deployOnShow(){
	//TODO
}

function settingsOnShow(){
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

$(document).ready(function(){
    
        ViewManager.addViewListener("onShow", "bulid-content", buildOnShow); //add listener to monitor what will happen when build-content shown.
        ViewManager.addViewListener("onShow", "deploy-content", deployOnShow); //add listener to monitor what will happen when deploy-content shown.
        ViewManager.addViewListener("onShow", "setting-content", settingsOnShow); //add listener to monitor what will happen when settings-content shown.
		
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
        
        $(".notification").click(
            function () {
                $(this).fadeTo(400, 0, function () { 
                    $(this).slideUp(400);
                });
                return false;
         });
	    
	    
});


  
  
  