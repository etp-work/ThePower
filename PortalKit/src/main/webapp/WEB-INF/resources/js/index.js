/**
 * This JS module is written for view index.jsp. It provider all the common functionalities.
 */
(function(angular){
    angular.module('myModule', []).
    factory('compiler', function($compile) {
      return {
        compile: function(html){
            var scope = angular.element($('.content-wrapper')).scope();
            return $compile(html)(scope);
        }
      };
    });
   
  angular.injector(['myModule', 'ng']);
}(angular))

$(document).ready(function(){
    
    var initUrl = "/init/getViewsInfo.ajax";
    var shownViewId = undefined;
  
    function viewsInfoLoading(ViewsInfo){
        if(!ViewsInfo|| !ViewsInfo.length){
            //error handle
        }
        var count = ViewsInfo.length;

        var showViews = function(){
             var showId = undefined;
             for(var i =0; i < ViewsInfo.length; i++){
                 if(ViewsInfo[i].defaultView){
                     showId = ViewsInfo[i].viewId;
                     break;
                 }
             }
             $('.content-wrapper').addClass("contentAnimation");
             ViewManager.show("#"+showId);
        };
        
        var loadJs = function(){
            for(var i = 0; i < ViewsInfo.length; i++){
                var viewInfo = ViewsInfo[i];
                DynamicLoad.loadStaticJs(viewInfo.js, function(){count--; if(count === 0){showViews();}}, function(error){});
            }
        };
        
        var loadView = function(view){
            count--;
            var injector = angular.injector(['myModule', 'ng']);
            var compiler = injector.get('compiler');
            $('.maincontent .content-wrapper').append(compiler.compile(view));
            ViewManager.hide(".maincontent .content-wrapper .content-box");
            if(count === 0){
                count = ViewsInfo.length;
                loadJs();
            }
        };
        
        for(var i = 0; i < ViewsInfo.length; i++){
            var viewInfo = ViewsInfo[i];
            DynamicLoad.loadStaticHtml(viewInfo.templateUrl, loadView, function(error){});
        }
    }
    
    DynamicLoad.loadJSON(initUrl, undefined, viewsInfoLoading, function(error){});
    

    
    
    $('.tab-header ul li a').click(
            function(event) {
                    var newId = $(this).attr("href").substring(2);
                    if(newId === shownViewId){
                        return false;
                    }
                    $(this).parent().siblings().find("a").removeClass('active'); // Remove active class from all the other tabs
                    $(this).addClass('active');
                    $('.content-wrapper').removeClass("contentAnimation");
                    ViewManager.hide(".maincontent .content-wrapper .content-box");
                    shownViewId = newId
                    ViewManager.show("#" + shownViewId);
                    $('.content-wrapper').addClass("contentAnimation");
                    return false;
    });
    
});