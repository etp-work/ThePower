/**
 * This module is only worked for view clean-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    
//=========================================variable=========================================
    var getCleanItemsUrl = "/clean/getCleanItems.ajax";
    var cleanItemUrl = "/clean/cleanItem.ajax";
//=========================================functions========================================
    function cleanOnShowListener(){
        DynamicLoad.loadJSON(getCleanItemsUrl, undefined, function(CleanItems){
            var scope = angular.element($('.bulid-list')).scope();
            scope.$apply(function(){
                scope.widgetCaches = CleanItems.widgetCaches;
                scope.warFiles = CleanItems.warFiles;
            });
            
        }, function(error){
            ViewManager.simpleError(error.message);
        });
    }
    
    function getSelection(){
        var selection = {
                widget: [],
                tomcat: []};
        $('#clean-content .clean-list .Widget input').each(function(){
            if($(this).is(':checked')){
                selection.widget.push($(this).val());
            }
        });
        
        $('#clean-content .clean-list .Tomcat input').each(function(){
            if($(this).is(':checked')){
                selection.tomcat.push($(this).val());
            }
        });
        return selection;
    }
    
    function removeOne(selection, type, callback){
        if(selection.length === 0){
            if(callback){
                callback();
            }
            return;
        }
        var select = selection.shift();
        if(type === "widget"){
            DynamicLoad.postJSON(cleanItemUrl, {
                cleanItem: select,
                cleanType: type
            }, function(data){
                if(data && data.result){
                    ViewManager.simpleSuccess("remove "+select +" successfully.");
                    $("#clean-content .clean-list .Widget li input[value=\""+ select +"\"]").parent().parent().remove();
                    removeOne(selection, type, callback);    
                }else{
                    ViewManager.simpleError("remove "+select +" error.");
                }
                
            }, function(error){
                ViewManager.simpleError("remove "+select +" error : "+error.message);
            });
        }else if(type === "tomcat"){
            DynamicLoad.postJSON(cleanItemUrl, {
                cleanItem: select,
                cleanType: type
            }, function(data){
                if(data && data.result){
                    ViewManager.simpleSuccess("remove "+select +" successfully.");
                    $("#clean-content .clean-list .Tomcat li input[value=\""+ select +"\"]").parent().parent().remove();
                    removeOne(selection, type, callback);
                }else{
                    ViewManager.simpleError("remove "+select +" error.");
                }
            }, function(error){
                ViewManager.simpleError("remove "+select +" error : "+error.message);
            });
        
        }
        
    }
    
    function removeSelection(){
        var selection = getSelection();
        removeOne(selection.widget, "widget", function(){
            removeOne(selection.tomcat, "tomcat");
        });
    }
    
//========================================init listener=====================================
    ViewManager.addViewListener("onShow", "#clean-content", cleanOnShowListener); //add listener to monitor what will happen when settings-content shown.
//================================================event bind================================
    $('#clean-content #cleanButton').click(function(event){
        
        removeSelection();
    });
}(window));
