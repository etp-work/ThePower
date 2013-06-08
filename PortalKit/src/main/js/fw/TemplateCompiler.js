(function (window) {
    'use strict';
    
    //this part provides the capbility that data render can be used within the dynamic loaded html templates.
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
    
}(window));