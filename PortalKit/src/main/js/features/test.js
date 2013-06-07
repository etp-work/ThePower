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
            } else if(command.type == "TESTCASE_START"){
                disableElements(true);
                var id = command.area + "." + command.casename;
                var element = $("#test-content .us-list .childB[value=\""+id+"\"]").parent().siblings('.status');
                element.removeClass("s-success");
                element.removeClass("s-error");
                element.addClass("s-working");
            } else if(command.type == "TESTCASE_END"){
                var id = command.area + "." + command.casename;
                var element = $("#test-content .us-list .childB[value=\""+id+"\"]").parent().siblings('.status');
                element.removeClass("s-working");
                if(command.result === "OK"){
                    element.addClass("s-success");
                } else {
                    element.addClass("s-error");
                }
                disableElements(false);
            }
        };
        window.wsconn.onclose = function(event) {};
        window.wsconn.onerror = function(event) {};
    }
    
    function disableElements(isDisable){
        $('#test-content .test').attr("disabled", isDisable);
        $('#test-content #needRemoteControl').attr("disabled", isDisable);
        $('#test-content #reloadButton').attr("disabled", isDisable);
        $('#test-content #portal').attr("disabled", isDisable);
    }
    
//========================================init listener=====================================
    
    function buildViewOnShow(){
        
        DynamicLoad.postJSON(dataUrl, {
                targetIP: undefined,
                targetPort: undefined,
                targetContextPath: undefined
            }, function(devices){
            if(!devices || devices.length === 0){
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
                enableDevTool:needDevTool(),
                enableRemoteControl:needRemoteControl()
            }));
        }
        setTimeout(buildViewOnShow, 5000); // Wait 5s for portal load and refresh test cases
    }
    
    function needDevTool(isNeeded){
        if(isNeeded === undefined){
            return $('#test-content #needDevTool').is(':checked');
        }
        $('#test-content #needDevTool').attr("checked", isNeeded);
    }
    
    function needRemoteControl(isNeeded){
        if(isNeeded === undefined){
            return $('#test-content #needRemoteControl').is(':checked');
        }
        $('#test-content #needRemoteControl').attr("checked", isNeeded);
    }
    
    $('#test-content #portal').change(function() {
        loadPortal();
        var portaltype = $(this).val();
        if("" !== portaltype){
            $('#test-content #reloadButton').attr("disabled", false);
        }else{
            $('#test-content #reloadButton').attr("disabled", true);
        }
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
    
    $('#test-content #needRemoteControl').change(function() {
        if(window.wsconn){
            window.wsconn.send(JSON.stringify({
                from:window.portalID,
                to:window.nativeID,
                type:"ENABLEREMOTECONTROL",
                enableRemoteControl:needRemoteControl()
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

    function getCases(){
        var cases = [];
        $('#test-content .us-list .childB').each(function (){
            if($(this).is(':checked')){
                cases.push($(this).attr("caseId"));
            }
        });
        return cases;
    }
    
    $('#test-content .test').click(function(event){
        var cases = getCases();
        $('#test-content .test-feature-content .us-list .status').attr("class", "status");
        DynamicLoad.postJSON(startUrl, {
            targetIP: undefined,
            targetPort: undefined,
            targetContextPath: undefined,
            cases:cases
        }, function(){});
    });
    
}(window));
