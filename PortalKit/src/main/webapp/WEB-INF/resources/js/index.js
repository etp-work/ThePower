/**
 * This JS module is written for view index.jsp. It provider all the common functionalities.
 */
$(document).ready(function(){
    
    
    $('.maincontent .tab-header ul li').first().find('a').addClass('active');
    


    
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