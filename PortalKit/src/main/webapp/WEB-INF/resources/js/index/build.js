/**
 * This module is only worked for view build-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    
    function BuildInfoController($http, $scope){
        var url = "/PortalKit/powerbuild/getAllTrees.ajax";
        $http.get(url).success(function(data, status, headers, config) {
            $scope.dirTrees = data
          });
    }
    
 // Create a new module
    var switchModule = angular.module('switchModule', []);
    
    switchModule.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
        when('/build-content', {templateUrl: 'resources/templates/index-views/build-content.html', controller: BuildInfoController});
  }]);

}(window));