/**
 * This module is only worked for view test-content which included in
 * index.jsp.
 */
    
(function(window) {
    'use strict';
    
//=========================================variable=====================================
    var viewId = "test-content";
    var dataUrl = "/test/data.ajax";
    var startUrl = "/test/start.ajax";
    
//=========================================functions========================================
    
    //do filter the specified text within a specified range of elements.
    function doFilter(text){
        ViewManager.filter("#test-content .test-feature-content .us-list ul li", "div input[type=\"checkbox\"]", text);
    }
    
    if (window.wsuri) {
        window.wsconn = new window.WebSocket(window.wsuri);
        window.wsconn.onopen = function(event) {
            window.wsconn.send("ID:"+window.portalID); 
        };
        window.wsconn.onmessage = function(event) {
            var command = JSON.parse(event.data);
            if(command.type == "TESTSUITE_UPDATE"){
                var scope = angular.element($('.us-list')).scope();
                scope.$apply(function(){
                    scope.testsuites = command.testsuites;
                });
            }
            
            if(command.type == "TESTCASE_START"){
                var element = $("#test-content .us-list .childB[value=\""+command.casename+"\"]").parent().siblings('.status');
                element.addClass("s-working");
            }
            
            if(command.type == "TESTCASE_END"){
                var element = $("#test-content .us-list .childB[value=\""+command.casename+"\"]").parent().siblings('.status');
                element.removeClass("s-working");
                if(command.result === "OK"){
                    element.addClass("s-success");
                } else {
                    element.addClass("s-error");
                }
            }
        };
        window.wsconn.onclose = function(event) {};
        window.wsconn.onerror = function(event) {};
    }
    
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
    
    function loadPortal(){
        var portaltype = $('#test-content #portal').val();
        if(window.wsconn && portaltype){
            window.wsconn.send(JSON.stringify({
                from:window.portalID,
                to:window.nativeID,
                type:"STARTPORTAL",
                portaltype:portaltype,
                enableDevTool:needDevTool()
            }));
        }
    }
    
    function needDevTool(isNeeded){
        if(isNeeded === undefined){
            return $('#test-content #needDevTool').is(':checked');
        }
        $('#test-content #needDevTool').attr("checked", isNeeded);
    }
    
    $('#test-content #portal').change(function() {
        loadPortal();
    });
    
    $('#test-content #needDevTool').change(function() {
        if(window.wsconn){
            window.wsconn.send(JSON.stringify({
                from:window.portalID,
                to:window.nativeID,
                type:"ENABLEDEVTOOL",
                enableDevTool:needDevTool()
            }));
        }
    });
    
    $('#test-content .list-header #quickSearch').keyup(function(event) {
        var text = $(this).val();
        doFilter(text);
    });
    
    $('#test-content #reloadButton').click(function() {
        loadPortal();
    });

}(window));
