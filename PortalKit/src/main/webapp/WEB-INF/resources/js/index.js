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
    var viewsInfo = undefined;
    
    //remove a view from viewsInfo with specified viewId.
    function removeViewInfo(viewId){
        for(var i in viewsInfo){
            if(viewsInfo[i].viewId === viewId){
                viewsInfo.splice(i, 1);
                break;
            }
        }
    }

    //find the view from viewsInfo with specified viewId.
    function findViewInfo(viewId){
        if(viewsInfo.length === 0){
            return undefined;
        }
        
        for(var i in viewsInfo){
            if(viewsInfo[i].viewId === viewId){
                return viewsInfo[i];
            }
        }
        //implicitly falsy
    }
    
    //load a specified view for its html,js.
    //callback will be executed after html and js are loaded successfully.
    function loadViewJS(view, callback){
        if(!view){
            return;
        }
        
        var loadJS = function(){
            DynamicLoad.loadStaticJs(view.js, function(){
                if(callback){
                    callback();
                }
                removeViewInfo(view.viewId);
            }, function(error){ViewManager.simpleError("Load js "+view.js+ " error : " +error.message);});
        };
        
        var loadView = function(html){
            var injector = angular.injector(['myModule', 'ng']);
            var compiler = injector.get('compiler');
            $('.maincontent .content-wrapper').append(compiler.compile(html));
            ViewManager.hide(".maincontent .content-wrapper .content-box");
            loadJS();
        };
        
        DynamicLoad.loadStaticHtml(view.templateUrl, loadView, function(error){ViewManager.simpleError("Load view "+view.viewId+ " error : " +error.message);});
    }
    
    function viewsInfoLoad(ViewsInfo){
        viewsInfo = ViewsInfo;
        
        //init default view
        var defaultView = undefined;
        for(var i =0; i < viewsInfo.length; i++){
            if(viewsInfo[i].defaultView){
                defaultView = viewsInfo[i];
                break;
            }
        }
        
        loadViewJS(defaultView, function(){

            ViewManager.show("#" + defaultView.viewId);
            $('.maincontent .content-wrapper').addClass("contentAnimation");
        
        });
    }
  
    //load viewSettings info, in order to append views dynamically.
    DynamicLoad.loadJSON(initUrl, undefined, viewsInfoLoad, function(error){ViewManager.simpleError(error.message);});
    

    
    
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
                    shownViewId = newId;
                    var callbackOnshow = function(){
                        ViewManager.show("#" + shownViewId);
                        $('.maincontent .content-wrapper').addClass("contentAnimation");
                    };
                    
                    var view = findViewInfo(shownViewId);
                    if(view){
                        loadViewJS(view, callbackOnshow);
                    }else{
                        callbackOnshow();
                    }
                   
                    return false;
    });
    
});