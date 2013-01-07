<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/views/common/header_top.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/views/common/header_head.jsp"%>
<title>error</title>
</head>
<body>
<c:out value="${exception.message}"></c:out>
</body>
</html>
