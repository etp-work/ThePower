$(document).ready(function() {
    $("#tapostok").click(function(event) {
	    DynamicLoad.postJSON("/test/ajax/post/ok.ajax", {
	               testId : 001,
	               testName : "nanfeng"
	       }, function(data) {
	              alert("testId = " + data.testId + "||testName = " + data.testName);
	       }, function(obj) {
	              alert("shibai type = " + obj.type + "||msg = " + obj.message);
	       });
    });

    $("#tagetok").click(function(event) {
	DynamicLoad.getJSON("/test/ajax/get/ok.ajax", {
	    testId : 1,
	    testName : "nanfeng"
	}, function(data) {
	    alert("testId = " + data.testId + "||testName = " + data.testName);
	}, function(obj) {
	    alert("shibai type = " + obj.type + "||msg = " + obj.message);
	});
    });

    $("#tapostfail").click(function(event) {
	DynamicLoad.postJSON("/test/ajax/post/error.ajax", {
	    testId : 1,
	    testName : "nanfeng"
	}, function(data) {
	    alert("testId = " + data.testId + "||testName = " + data.testName);
	}, function(obj) {
	    alert("shibai type = " + obj.type + "||msg = " + obj.message);
	});
    });

    $("#tagetfail").click(function(event) {
	DynamicLoad.getJSON("/test/ajax/get/error.ajax", {
	    testId : 1,
	    testName : "nanfeng"
	}, function(data) {
	    alert("testId = " + data.testId + "||testName = " + data.testName);
	}, function(obj) {
	    alert("shibai type = " + obj.type + "||msg = " + obj.message);
	});
    });

    $("#tonw").click(function(event) {
	window.open("/PortalKit/","newwindow", "height=100,width=400,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
    });
    
    $("#tugzip").click(function(event) {
        DynamicLoad.postJSON("/test/ajax/post/ungzip.ajax", {}, function(data) {
            alert("result = " + data.result);
        }, function(obj) {
            alert("shibai type = " + obj.type + "||msg = " + obj.message);
        });
    });
    
    $("#tutar").click(function(event) {
        DynamicLoad.postJSON("/test/ajax/post/untar.ajax", {}, function(data) {
            alert("result = " + data.result);
        }, function(obj) {
            alert("shibai type = " + obj.type + "||msg = " + obj.message);
        });
    });
});