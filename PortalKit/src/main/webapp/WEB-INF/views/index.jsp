<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/views/common/header_top.jsp"%>
<html>
<head>
<%@ include file="/WEB-INF/views/common/header_head.jsp"%>
<link href="${resources}/css/index.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resources}/js/index.js"></script>
<title>The Power</title>
</head>
<body>
<script id="commonBuildTemplate" type="text/x-jsrender">
    <ul class="group"><label class="checkbox"><input name="" type="checkbox" value="" /><span>{{>name}}</span></label>
        {{for subDirs}}
             <li><label class="checkbox"><input name="" type="checkbox"><span>{{>name}}</span></label></li>
    	{{/for}}
    </ul>
</script>

<div class="mainWrapper">

    <div class="header">
        <div class="toolsName">The Power</div>    
    </div>




    <div class="maincontent">
        <div class="tab-header">
            <ul>
                <li><a class="default-tab" href="#bulid-content">Bulid</a></li>
                <li><a>Deploy</a></li>
                <li><a href="#setting-content">Settings</a></li>
            </ul>
            <div class="clear"></div>
        </div>
        
        <div class="content-wrapper">
              <div class="content-box default-tab" id="bulid-content">
            
                    <div class="bulid-feature-header">
                        <li class="active">Folder</li>
                        <li>Path</li>
                    </div>
                    <div class="bulid-feature-content">
                        <div class="bulid-feature-content-folder"></div>
                        <div class="bulid-button-area">
                            <li><input class="small-button config" type="button" value="configuration"/></li>
                            <li><input class="small-button reset" type="button" value="clean all"/></li>
                        <div class="clear"></div>    
                        </div>
                        
                    <div class="clear"></div>
                  </div>    
            
            
                  
                  <div class="main-button-area">
                  <input class="primary-button bulid" type="button" value="Bulid"/> 
                  <div class="check-deploy"><input name="" type="checkbox">Deploy</div>
                  <div class="clear"></div>
                  </div>
                  <div class="clear"></div>
              </div>
          
          
              <div class="content-box" id="setting-content">
                    <span class="tdName">Configuration</span>
                    <div class="table-wrapper">
                    <table  class="setting-table" border="0" cellspacing="0" cellpadding="0">
                        <thead>
                          <tr>
                            <th>Key</th>
                            <th>Value</th>
                          </tr>
                        </thead>
                          <tr>
                            <td title="Path of portal-team repository">Design</td>
                            <td><input id="portalTeamPath" type="text" placeholder="Type something…"></td>
                          </tr>
                          <tr>
                            <td title="Path of webapps under tomcat folder">Deploy</td>
                            <td><input id="tomcatWebappsPath" type="text" placeholder="Type something…"></td>
                          </tr>
                    </table>
            </div>
                    
                    <div class="main-button-area">
                        <input id="saveSettings" class="primary-button save" type="button" value="Save"/>
                        <div class="clear"></div>
                    </div>


               
              </div>
          
          
          
        </div>
        <div class="clear"></div>
    </div>





    <div class="foot">
    </div>

</div>
</body>
</html>
