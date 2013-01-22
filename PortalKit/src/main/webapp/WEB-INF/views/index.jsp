<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/views/common/header_top.jsp"%>
<html ng-app="switchModule">
<head>
<%@ include file="/WEB-INF/views/common/header_head.jsp"%>
<link href="resources/css/index.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="resources/js/index.js"></script>
<c:forEach var="view" items="${viewInfo}" varStatus="status">
    <script type="text/javascript" src="<c:out value='${view.js}'/>"></script>
</c:forEach>
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
                   <li><a href="#/<c:out value='${view.viewId}'/>"><c:out value="${view.viewName}"/></a></li>
                </c:forEach>
            </ul>
            <div class="clear"></div>
        </div>
        
        <div class="content-wrapper">
        <div class="ng-view"></div>
        </div>
        <div class="clear"></div>
    </div>





<!--========================= FOOT & NOTIFICATION ========================================================-->

    <div class="foot">
            <div class="notification error">
                <a href="#" class="close"><img src="resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <span>
                    Error notification. 
                </span>
            </div>
            <div class="notification information">
                <a href="#" class="close"><img src="resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <span class="message">
                    Lorem ipsum dolor sit amet.
                </span>
            </div>
            <div class="notification success">
                <a href="#" class="close"><img src="resources/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <span>
                    Success notification. 
                </span>
            </div>
    </div>


</div>
</body>
</html>
