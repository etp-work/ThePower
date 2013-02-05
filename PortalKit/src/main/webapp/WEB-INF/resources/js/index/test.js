/**
 * This module is only worked for view test-content which included in
 * index.jsp.
 */
    
(function(window) {
    'use strict';
    
  //=========================================variable=====================================
    var viewId = "test-content";
    var dataUrl = "/test/data.ajax?targetIP=150.236.48.198&targetPort=18080";
    
    function TestController($http, $scope){
    }
    
 // Create a new module
    var switchModule = Lifecycle.getModule("switchModule");
    
    switchModule.config(['$routeProvider', function($routeProvider) {
        $routeProvider.
        when('/test-content', {templateUrl: 'resources/templates/index-views/test-content.html',   controller: TestController});
    }]);
    
    $('#test-content #portal').change(function() {
        var portaltype = $('#test-content #portal').val();
        startPortal(portaltype);
    });

//========================================init listener=====================================
    
    function buildViewOnShow(){
        if(Lifecycle.getState(viewId) !== Lifecycle.LOADED && Lifecycle.getState(viewId) !== Lifecycle.NEWCONFIG){
            return;
        }
        
        DynamicLoad.loadJSON(dataUrl, undefined, function(devices){
            if(!devices || devices.length === 0){
                Lifecycle.setState(Lifecycle.NO_CONFIGURATION);
                return;
            }
            
            var scope = angular.element($('.us-list')).scope();
            scope.$apply(function(){
                scope.devices = devices;
            });
          
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
        });
    }
    
    ViewManager.addViewListener("onShow", "#"+viewId, buildViewOnShow);
    
}(window));
