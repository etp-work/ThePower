<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/views/common/header_top.jsp"%>
<html ng-app="myModule">
<head>
<%@ include file="/WEB-INF/views/common/header_head.jsp"%>
<link href="resources/css/index.css" rel="stylesheet" type="text/css">
<c:forEach var="view" items="${viewInfo}" varStatus="status">
    <c:if test="${view.css != ''}">
         <link href="<c:out value='${view.css}'/>" rel="stylesheet" type="text/css">
    </c:if>
</c:forEach>
<title>The Power</title>
</head>
<body>

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

<div class="help">help</div>
<div class="version">V 1.0.0</div>

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
