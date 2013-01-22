/**
 * This module is only worked for view deploy-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    
    function DeployController($http, $scope){
    }
    
 // Create a new module
    var switchModule = Lifecycle.getModule("switchModule");
    
    switchModule.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
        when('/deploy-content', {templateUrl: 'resources/templates/index-views/deploy-content.html',   controller: DeployController});
  }])

}(window));
