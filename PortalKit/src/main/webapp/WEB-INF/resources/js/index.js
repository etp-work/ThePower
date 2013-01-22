/**
 * This JS module is written for view index.jsp. It provider all the common functionalities.
 */
$(document).ready(function(){
    
    //init the first tab to active.
    $('.tab-header ul li').first().find('a').addClass('active');
    
    
    $('.tab-header ul li a').click(
            function(event) { 
                    $(this).parent().siblings().find("a").removeClass('active'); // Remove active class from all the other tabs
                    $(this).addClass('active');
            }
        );


    
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