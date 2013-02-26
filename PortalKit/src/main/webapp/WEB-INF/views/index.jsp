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
<script type="text/javascript" src="resources/js/index.js"></script>
<title>The Power</title>
</head>
<body>
<div class="mainWrapper">

    <div class="header">
        <div class="toolsName"><img class="logo" src="resources/images/logo_24.png"/>THE POWER</div>    
    </div>

    <div class="maincontent">
        <div class="tab-header">
            <ul>
                <c:forEach var="view" items="${viewInfo}" varStatus="status">
                   <c:if test="${view.defaultView == true}">
                       <li><a class="active" href="#/<c:out value='${view.viewId}'/>"><c:out value="${view.viewName}"/></a></li>
                   </c:if>
                   <c:if test="${view.defaultView == false}">
                       <li><a href="#/<c:out value='${view.viewId}'/>"><c:out value="${view.viewName}"/></a></li>
                   </c:if>
                </c:forEach>
            </ul>
            <div class="clear"></div>
        </div>
        
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
