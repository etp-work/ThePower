
$(document).ready(function(){
    var resource = $('#resources').val();
    
    var jsFiles = ["bulid-content.js", "deploy-content.js", "test-content.js", "setting-content.js"];
    var jsFileNum = jsFiles.length;

//=======================initialization=================================
    function initialization(){
        jsFileNum--;
        if(jsFileNum !== 0){
            return;
        }
        
        //hide all the content views first.
        ViewManager.hide('.content-wrapper div.content-box');
        //show the default content (build-content).
        ViewManager.show('#bulid-content');
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
    }

    //load all the other used js files.
    for(var i in jsFiles){
        DynamicLoad.loadStaticJS(resource+"/js/"+jsFiles[i], initialization);    
    }
    
//=======================foot part=====================================
        
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


  
  
  