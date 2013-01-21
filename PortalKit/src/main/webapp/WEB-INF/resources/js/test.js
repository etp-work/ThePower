/**
 * This JS module is written for view build-content in index.jsp.
 */
(function () {
    
    function PhoneListCtrl($http, $scope){
        var url = "/PortalKit/powerbuild/getAllTrees.ajax";
        $http.get(url).success(function(data, status, headers, config) {
            $scope.dirTrees = data
          });
    }
    
 // Create a new module
    var switchModule = angular.module('switchModule', []);
    
    switchModule.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
        when('/', {templateUrl: 'resources/templates/build-content.html',   controller: PhoneListCtrl}).
        otherwise({redirectTo: '/phones'});
  }])
    
}());