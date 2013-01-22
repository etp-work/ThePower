/**
 * This module is only worked for view setting-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    var isInit = false;
    
    function initJQueryBinding(){
        $('#saveSettings').click(function(event){
            alert("hello settings");
        });
    }
    
    function SettingController($http, $scope){
        if(!isInit){
            initJQueryBinding();
        }
    }
    
 // Create a new module
    var switchModule = Lifecycle.getModule("switchModule");
    
    switchModule.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
        when('/setting-content', {templateUrl: 'resources/templates/index-views/setting-content.html', controller: SettingController});
  }]);

    
    
}(window));