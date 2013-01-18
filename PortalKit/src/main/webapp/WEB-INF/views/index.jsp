<%@ page language="java" contentType="text/html;charset=utf-8"%>
<%@ include file="/WEB-INF/views/common/header_top.jsp"%>
<html ng-app>
<head>
<%@ include file="/WEB-INF/views/common/header_head.jsp"%>
<link href="${resources}/css/index.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resources}/js/index.js"></script>
<title>The Power</title>
</head>
<body>
<input id="resources" type="hidden" value="${resources}"/>
<div class="mainWrapper">

    <div class="header">
        <div class="toolsName"><img class="logo" src="${resources}/images/logo_24.png"/>THE POWER</div>    
    </div>




    <div class="maincontent">
        <div class="tab-header">
            <ul>
                <li><a class="active" href="#bulid-content">Bulid</a></li>
                <li><a href="#deploy-content">Deploy</a></li>
                <li><a href="#test-content">Test</a></li>
                <li><a href="#setting-content">Set</a></li>
            </ul>
            <div class="clear"></div>
        </div>
        
        <div class="content-wrapper">
        
        <!-- ========================================= Bulid ================================================-->
        
        
              <div class="content-box" id="bulid-content">
            
                    <div class="bulid-feature-header">
                    <li ><a class="active" href="#common-use">common</a></li>
                    <li><a href="#environment">environment</a></li>
                    <div class="clear"></div>
                    </div>
                    
                    <div class="bulid-feature-content">

                        <div class="bulid-list">
                            <ul class="group" ng-repeat="dirTree in dirTrees"><label class="category"><div class="lightpoint"><input name="" class="parent" type="checkbox" value="{{dirTree.name}}" ng-checked="dirTree.checked" /><label></label></div><span>{{dirTree.name}}</span></label>
                                <li ng-repeat="subTree in dirTree.subDirs"><div class="lightpoint"><input name="" class="child" type="checkbox" ng-checked="subTree.checked" value="{{subTree.name}}"><label></label></div><span>{{subTree.name}}</span><div class="status"><img src="${resources}/images/loading.gif"/></div></li>
                            </ul>
                        </div>
                        <div class="config-button-area">
                            <li><input id="setDefault4Build" class="small-button config" disabled="disabled" type="button" value="Set Default"/></li>
                            <li><input id="resetDefault4Build" class="small-button reset" disabled="disabled" type="button" value="Reset"/></li>
                        <div class="clear"></div>    
                        </div>
                        
                    <div class="clear"></div>
                  </div>    
            
            
                  
                  <div class="main-button-area">
                  <input id="buildButton" class="primary-button bulid" disabled="disabled" type="button" value="Bulid"/> 
                  <div class="check-deploy"><input id="needDeploy" name="" disabled="disabled" type="checkbox">Deploy</div>
                  <div class="clear"></div>
                  </div>
                  <div class="clear"></div>
              </div>
              
   <!-- ========================================= Deploy ================================================-->
              
              <div class="content-box" id="deploy-content">
                <span class="tdName">Sources Files</span>
                <div class="table-wrapper">                 
                    
                    
                    <div class="war-input">
                    <input class="input-deploy"type="text" placeholder="Type something…">
                    <input class="primary-button check" type="button" value="Check"/>
                    <div class="clear"></div>
                    </div>
                    
                    <div class="war-list-wrapper">

                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 01</li>
                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 02</li>
                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 03</li>
                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 04</li>
                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 05</li>
                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 06</li>
                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 07</li>
                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 08</li>
                        <li><div class="lightpoint"><input name="" type="checkbox"><label></label></div>list 09</li>
                        
                    </div>
                </div>
                <div class="main-button-area">
                    <input class="primary-button save" type="button" value="Deploy"/>
                </div>
              </div>     
              
 <!-- ========================================= Test ================================================--> 
             <div class="content-box" id="test-content">
                    <span class="tdName">User Story List</span>
                    
                    <div class="test-feature-content">
                        <div class="us-list">
                        
                            <ul class="group"><label class="category"><input name="" type="checkbox" value="" />User story 01</label>
                              <li><input name="" type="checkbox">list 01</li>
                              <li><input name="" type="checkbox">list 02</li>
                            </ul>
                            
                            <ul class="group"><label class="category"><input name="" type="checkbox" value="" />User story 02</label>
                              <li><input name="" type="checkbox">list 01</li>
                              <li><input name="" type="checkbox">list 02</li>
                            </ul>
                        
                        </div>
                        <div class="config-button-area">
                            <li><input id="setDefault4Test" class="small-button config" type="button" value="Set Default"/></li>
                            <li><input class="small-button reset" type="button" value="Reset"/></li>
                        <div class="clear"></div>    
                        </div>
                        <div class="clear"></div>
                    </div>
                    
                    <div class="main-button-area">
                        <input class="primary-button test" type="button" value="Test"/>
                    </div>
             </div>      
              
          
   <!-- ========================================= Settings ================================================-->
           
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
                            <td><input class="input-setting" id="portalTeamPath" type="text" placeholder="Type something…"></td>
                          </tr>
                          <tr>
                            <td title="Path of webapps under tomcat folder">Deploy</td>
                            <td><input class="input-setting" id="tomcatWebappsPath" type="text" placeholder="Type something…"></td>
                          </tr>
                    </table>
            </div>
                    
                    <div class="main-button-area">
                        <input id="saveSettings" class="primary-button save" type="button" disabled="disabled" value="Save"/>
                    </div>


               
              </div>
          
          
          
        </div>
        <div class="clear"></div>
    </div>





<!--========================= FOOT & NOTIFICATION ========================================================-->

    <div class="foot">
            <div class="notification error">
                <a href="#" class="close"><img src="${resources}/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <span>
                    Error notification. 
                </span>
            </div>
            <div class="notification information">
                <a href="#" class="close"><img src="${resources}/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <span class="message">
                    Lorem ipsum dolor sit amet.
                </span>
            </div>
            <div class="notification success">
                <a href="#" class="close"><img src="${resources}/images/icons/cross_grey_small.png" title="Close this notification" alt="close" /></a>
                <span>
                    Success notification. 
                </span>
            </div>
    </div>


</div>
</body>
</html>
