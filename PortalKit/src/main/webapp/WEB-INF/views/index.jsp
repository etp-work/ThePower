<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html ng-app="myModule">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="resources/css/index.css" rel="stylesheet" type="text/css">
<c:forEach var="view" items="${viewInfo}" varStatus="status">
    <c:if test="${view.css != ''}">
         <link href="<c:out value='${view.css}'/>" rel="stylesheet" type="text/css">
    </c:if>
</c:forEach>
<title>The Power</title>
</head>
<body oncontextmenu="return false">

<div class="left-nav">

        <div class="header">
                <img class="logo" src="resources/images/LOGO_40.png"/>   
        </div>
        
        <div class="tab-header">
            <ul>
                <c:forEach var="view" items="${viewInfo}" varStatus="status">
                    <c:if test="${view.defaultView == true}">
                        <li><a class="icon<c:out value='${view.viewName}'/> active" href="#/<c:out value='${view.viewId}'/>"><c:out value="${view.viewName}"/></a></li>
                    </c:if>
                    <c:if test="${view.defaultView == false}">
                        <li><a class="icon<c:out value='${view.viewName}'/>" href="#/<c:out value='${view.viewId}'/>"><c:out value="${view.viewName}"/></a></li>
                    </c:if>
                </c:forEach>
            </ul>
            <div class="clear"></div>
        </div>

</div>

<div class="help" title="Not implemented yet.">help</div>
<div class="version">V <c:out value="${portalKitInfo.version}"/></div>

<div class="mainWrapper">


    <div class="maincontent">
        <div class="content-wrapper">
        </div>
        <div class="clear"></div>
    </div>





<!--========================= FOOT & NOTIFICATION ========================================================-->

    <div class="foot">
    </div>


</div>
</body>
</html>
<script type="text/javascript" src="resources/js/dependencies/jquery.js"></script>
<script type="text/javascript" src="resources/js/dependencies/jsrender.js"></script>
<script type="text/javascript" src="resources/js/dependencies/json2.js"></script>
<script type="text/javascript" src="resources/js/dependencies/angular.js"></script>
<script type="text/javascript" src="resources/js/fw/TemplateCompiler.js"></script>
<script type="text/javascript" src="resources/js/fw/DynamicLoad.js"></script>
<script type="text/javascript" src="resources/js/utility/ViewManager.js"></script>
<script type="text/javascript" src="resources/js/fw/Lifecycle.js"></script>