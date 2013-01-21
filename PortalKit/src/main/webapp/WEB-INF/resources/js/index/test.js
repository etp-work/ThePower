/**
 * This module is only worked for view test-content which included in
 * index.jsp.

(function(window) {
    'use strict';
    
    function TestController($http, $scope){
        alert("test");
    }
    
 // Create a new module
    var switchModule = Lifecycle.getModule("switchModule");
    
    switchModule.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
        when('/test-content', {templateUrl: 'resources/templates/index-views/test-content.html',   controller: TestController});
  }])

}(window));
 */