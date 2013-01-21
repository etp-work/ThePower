/**
 * This module is only worked for view deploy-content which included in
 * index.jsp.

(function(window) {
    'use strict';
    
    function DeployController($http, $scope){
        alert("deploy");
    }
    
 // Create a new module
    var switchModule = angular.module('switchModule', []);
    
    switchModule.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
        when('/deploy-content', {templateUrl: 'resources/templates/index-views/deploy-content.html',   controller: DeployController});
  }])

}(window));
 */