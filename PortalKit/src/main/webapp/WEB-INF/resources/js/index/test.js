/**
 * This module is only worked for view test-content which included in
 * index.jsp.
 */
    
(function(window) {
    'use strict';
    
  //=========================================variable=====================================
    var viewId = "test-content";
    var dataUrl = "/test/data.ajax";
    
//========================================init listener=====================================
    
    function buildViewOnShow(){
        
        DynamicLoad.loadJSON(dataUrl, undefined, function(devices){
            if(!devices || devices.length === 0){
                Lifecycle.setState(Lifecycle.NO_CONFIGURATION);
                return;
            }
            
            var scope = angular.element($('.us-list')).scope();
            scope.$apply(function(){
                var testsuites = {};
                if(devices.length > 0){
                    var device = devices[0];
                    for(var i=0; i<device.results.length; i++){
                        var testcase = device.results[i];
                        var testSuiteId = testcase.testSuiteId;
                        var ids = testSuiteId.split(".");
                        var suiteId = ids[ids.length - 1];
                        if(!testsuites[suiteId]) {
                            testsuites[suiteId] = {
                                suiteId: suiteId,
                                cases: []
                            };
                        }
                        testsuites[suiteId].cases.push(testcase);
                    }
                }
                scope.testsuites = testsuites;
            });
          
            Lifecycle.setState(viewId, Lifecycle.NORMAL);
        });
    }
    
    ViewManager.addViewListener("onShow", "#"+viewId, buildViewOnShow);

    //================================================event bind======================
    
    $('#test-content #portal').change(function() {
        var portaltype = $('#test-content #portal').val();
        startPortal(portaltype);
    });

}(window));
