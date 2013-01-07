$(document).ready(function(){
		 
		$('.content-wrapper div.content-box').hide();
		$('.content-wrapper div.default-tab').show();
		$('.tab-header ul li a.default-tab').addClass('active');
		$('.tab-header ul li a').click( // When a tab is clicked...
				function() { 
					$(this).parent().siblings().find("a").removeClass('active'); // Remove "current" class from all tabs
					$(this).addClass('active');
					var currentTab = $(this).attr('href'); // Set variable "currentTab" to the value of href of clicked tab
					$(currentTab).siblings().hide(); // Hide all content divs
					$(currentTab).show(); // Show the content div with the id equal to the id of clicked tab
					return false; 
				}
			);
		
	
	



});


  
  
  