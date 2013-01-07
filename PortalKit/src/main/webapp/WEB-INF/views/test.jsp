<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/views/common/header_top.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/views/common/header_head.jsp"%>
<title>TEST</title>
<script type="text/javascript" src="${resources}/js/test.js"></script>
</head>
<body>
	<form action="/PortalKit/test/redirect/ok.html" method="post">

		<input type="submit" value="TEST_REDIRECT_OK" />
	</form>

	<form action="/PortalKit/test/redirect/error.html" method="post">

		<input type="submit" value="TEST_REDIRECT_ERROR" />
	</form>

	<input id="tapostok" type="button" value="TEST_AJAX_POST_OK" />
	<input id="tagetok" type="button" value="TEST_AJAX_GET_OK" />
	<input id="tapostfail" type="button" value="TEST_AJAX_POST_FAIL" />
	<input id="tagetfail" type="button" value="TEST_AJAX_GET_FAIL" />
	<input id="tonw" type="button" value="TEST_OPEN_NEW_WINDOW" />
	<input id="tugzip" type="button" value="TEST_UNGZIP" />
	<input id="tutar" type="button" value="TEST_UNTAR" />
</body>
</html>
