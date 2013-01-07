var listeners = {
	onShow: [],
	onHide: []
};
function fireListeners(type, currentTab){
    if(type === "onShow"){
	var list = listeners.onShow;
	for(var i in list){
	    list[i](currentTab);
	}
    }else if(type === "onHide"){
	var list = listeners.onHide;
	for(var i in list){
	    list[i](currentTab);
	}
    }
}

function addListener(type, listener){
    if(type === "onShow"){
	listeners.onShow.push(listener);
    }else if(type === "onHide"){
	listeners.onHide.push(listener);
    }
}

function removeListener(type, listener){
    if(type === "onShow"){
	
    }else if(type === "onHide"){
	
    }
}

function buildOnShow(currentTab){
    if(currentTab === "#bulid-content"){
	
    }
}

function deployOnShow(currentTab){
    if(currentTab === "#deploy-content"){
	//TODO
    }
}

function settingsOnShow(currentTab){
    if(currentTab === "#setting-content"){
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
}

$(document).ready(function(){
    
		addListener("onShow",buildOnShow); //add listener to monitor what will happen when build-content shown.
		addListener("onShow",deployOnShow); //add listener to monitor what will happen when deploy-content shown.
		addListener("onShow",settingsOnShow); //add listener to monitor what will happen when settings-content shown.
		
		$('.content-wrapper div.content-box').hide();
		$('.content-wrapper div.default-tab').show(0, function (){
		    fireListeners("onShow", "#"+$(this).attr("id"));
		});
		$('.tab-header ul li a.default-tab').addClass('active');
		$('.tab-header ul li a').click( // When a tab is clicked...
				function(event) { 
					$(this).parent().siblings().find("a").removeClass('active'); // Remove "current" class from all tabs
					$(this).addClass('active');
					var currentTab = $(this).attr('href'); // Set variable "currentTab" to the value of href of clicked tab
					$(currentTab).siblings().hide(); // Hide all content divs
					$(currentTab).show(0,function (){
					    fireListeners("onShow", currentTab);
					}); // Show the content div with the id equal to the id of clicked tab
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
	    
	    



});


  
  
  