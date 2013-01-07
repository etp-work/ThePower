<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/views/common/header_top.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/views/common/header_head.jsp"%>
<link href="${resources}/css/index.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resources}/js/index.js"></script>
<title>PowerBuild</title>
</head>
<body>
<div class="mainWrapper">

    <div class="header">
        <div class="toolsName">B.Tools</div>    
    </div>




    <div class="maincontent">
        <div class="tab-header">
            <ul>
                <li class="active">Bulid</li>
                <li>Deploy</li>
                <li>Settings</li>
            </ul>
            <div class="clear"></div>
        </div>
        
        <div class="tab-content">
            <div class="bulid-content">
                <div class="bulid-feature-header">
                    <li class="active">Folder</li>
                    <li>Path</li>
                </div>
                <div class="bulid-feature-content">
                    <div class="bulid-feature-content-folder">
                        <ul class="group"><label class="checkbox"><input name="" type="checkbox" value="" />Plug-in</label>
                          <li><label class="checkbox"><input name="" type="checkbox">list 01</label></li>
                          <li><label class="checkbox"><input name="" type="checkbox">list 02</label></li>
                        </ul>
                        
                        <ul class="group"><label class="checkbox"><input name="" type="checkbox" value="" />Plug-in</label>
                          <li><label class="checkbox"><input name="" type="checkbox">list 01</label></li>
                          <li><label class="checkbox"><input name="" type="checkbox">list 02</label></li>
                        </ul>

                        <ul class="group"><label class="checkbox"><input name="" type="checkbox" value="" />Plug-in</label>
                          <li><label class="checkbox"><input name="" type="checkbox">list 01</label></li>
                          <li><label class="checkbox"><input name="" type="checkbox">list 02</label></li>
                        </ul>

                        <ul class="group"><label class="checkbox"><input name="" type="checkbox" value="" />Plug-in</label>
                          <li><label class="checkbox"><input name="" type="checkbox">list 01</label></li>
                          <li><label class="checkbox"><input name="" type="checkbox">list 02</label></li>
                        </ul>

                        <ul class="group"><label class="checkbox"><input name="" type="checkbox" value="" />Plug-in</label>
                          <li><label class="checkbox"><input name="" type="checkbox">list 01</label></li>
                          <li><label class="checkbox"><input name="" type="checkbox">list 02</label></li>
                        </ul>

                        <ul class="group"><label class="checkbox"><input name="" type="checkbox" value="" />Plug-in</label>
                          <li><label class="checkbox"><input name="" type="checkbox">list 01</label></li>
                          <li><label class="checkbox"><input name="" type="checkbox">list 02</label></li>
                        </ul>

                        

                        
                    </div>
                    <div class="bulid-button-area">
                        <li><input class="configbutton" type="button" value="configuration"/></li>
                        <li><input class="configbutton" type="button" value="clean all"/></li>
                        <div class="clear"></div>
                    </div>
                    
              </div>
              <input class="button-bulid" type="button" value="Bulid"/> <label class="main-button"><input name="" type="checkbox">Deploy</label>
              <div class="clear"></div>
                
          </div>
        </div>
        <div class="clear"></div>
    </div>





    <div class="foot">
    </div>

</div>
</body>
</html>
