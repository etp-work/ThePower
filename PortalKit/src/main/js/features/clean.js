/**
\ * This module is only worked for view clean-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    
//=========================================variable=========================================
    var getCleanItemsUrl = "/clean/getCleanItems.ajax";
    var cleanItemUrl = "/clean/cleanItem.ajax";
    var widgetCaches = [];
    var warFiles = [];
//=========================================functions========================================
    
    
    function setDisableElements(isDisable){
        $('#clean-content #refreshButton').attr("disabled", isDisable);
        $('#clean-content #cleanButton').attr("disabled", isDisable);
    }
    
    //rebind click event clean-list checkbox items
    function rebindSelection(){

        $('#clean-content .clean-list .parentB').on("click", function(event){
                    var isChecked = $(this).is(':checked');
                    $(this).parent().parent().siblings().find('.childB').attr("checked", isChecked);
        });
        $('#clean-content .clean-list .childB').on("click", function(event){
                    var isChecked = $(this).is(':checked');
                    var parent = $(this).parent().parent().parent().find('.parentB');
                    if(isChecked){
                        parent.attr("checked", true);
                    }else{
                        var isAllFalse = true;
                        $(this).parent().parent().siblings().find('.childB').each(function(){
                            if($(this).is(':checked')){
                                isAllFalse = false;
                            }
                        });
                        if(isAllFalse){
                            parent.attr("checked", false);
                        }
                    }
                }
        );
    }
    
    function cleanOnShowListener(){
        DynamicLoad.loadJSON(getCleanItemsUrl, undefined, function(CleanItems){
            
            $('#clean-content .clean-list input').attr("checked", false);
            
            $('#clean-content .clean-list .parentB').off("click");
            $('#clean-content .clean-list .childB').off("click");
            
            $('#clean-content .clean-list input').off("click");
            $('#clean-content #cleanButton').attr("disabled", true);
            
            widgetCaches = CleanItems.widgetCaches;
            warFiles = CleanItems.warFiles;
            
            var scope = angular.element($('#clean-content .clean-list')).scope();
            scope.$apply(function(){
                scope.widgetCaches = widgetCaches;
                scope.warFiles = warFiles;
            });
            
            if(widgetCaches.length > 0 || warFiles.length > 0){
                $('#clean-content .clean-list input').on("click", function(event){
                    var isAllUnChecked = true;
                    $('#clean-content .clean-list input').each(function(){
                        if($(this).is(':checked')){
                            isAllUnChecked = false;
                        }
                    });
                    $('#clean-content #cleanButton').attr("disabled", isAllUnChecked);
                });
                rebindSelection();
            }
            
        });
    }
    
    function deleteElementFromArray(array, text){
        for(var i = 0; i < array.length; i++){
            if(array[i] === text){
                array.splice(i, 1);
                return;
            }
        }
    }
    
    function deleteFromWidgetCaches(cache){
        deleteElementFromArray(widgetCaches, cache);
    }
    
    function deleteFromWarFiles(file){
        deleteElementFromArray(warFiles, file);
    }
    
    //get all the selection in both widget and tomcat.
    function getSelection(){
        var selection = {
                widget: [],
                tomcat: []};
        $('#clean-content .clean-list .Widget .childB').each(function(){
            if($(this).is(':checked')){
                selection.widget.push($(this).val());
            }
        });
        
        $('#clean-content .clean-list .Tomcat .childB').each(function(){
            if($(this).is(':checked')){
                selection.tomcat.push($(this).val());
            }
        });
        return selection;
    }
    
    //remove one stuff whatever it is widget or tomcat.
    function removeOne(selection, type, callback){
        if(selection.length === 0){
            if(callback){
                callback();
            }
            return;
        }
        var select = selection.shift();
        setDisableElements(true);
        if(type === "widget"){
            DynamicLoad.postJSON(cleanItemUrl, {
                cleanItem: select,
                cleanType: type
            }, function(data){
                setDisableElements(false);
                if(data && data.result){
                    ViewManager.simpleSuccess("remove "+select +" successfully.");
//                    $("#clean-content .clean-list .Widget li input[value=\""+ select +"\"]").parent().parent().remove();
                    deleteFromWidgetCaches(select);
                    var scope =  angular.element($('#clean-content .clean-list')).scope();
                    scope.$apply(function(){
                        scope.widgetCaches = widgetCaches;
                    });
                    removeOne(selection, type, callback);
                }else{
                    ViewManager.simpleError("remove "+select +" error.");
                }
                
            }, function(error){
                setDisableElements(false);
                ViewManager.simpleError("remove "+select +" error : "+error.message);
            });
        }else if(type === "tomcat"){
            DynamicLoad.postJSON(cleanItemUrl, {
                cleanItem: select,
                cleanType: type
            }, function(data){
                setDisableElements(false);
                if(data && data.result){
                    ViewManager.simpleSuccess("remove "+select +" successfully.");
//                    $("#clean-content .clean-list .Tomcat li input[value=\""+ select +"\"]").parent().parent().remove();
                    deleteFromWarFiles(select);
                    var scope =  angular.element($('#clean-content .clean-list')).scope();
                    scope.$apply(function(){
                        scope.warFiles = warFiles;
                    });
                    removeOne(selection, type, callback);
                }else{
                    ViewManager.simpleError("remove "+select +" error.");
                }
            }, function(error){
                setDisableElements(false);
                ViewManager.simpleError("remove "+select +" error : "+error.message);
            });
        
        }
        
    }
    
    //remove selected items
    function removeSelection(){
        var selection = getSelection();
        removeOne(selection.widget, "widget", function(){
            removeOne(selection.tomcat, "tomcat", function(){
                var selection = getSelection();
                $('#clean-content .clean-list .Widget .parentB').attr("checked", selection.widget.length > 0);
                $('#clean-content .clean-list .Tomcat .parentB').attr("checked", selection.tomcat.length > 0);
            });
        });
    }
   
    
    //do filter the specified text within a specified range of elements.
    function doFilter(text){
        ViewManager.filter("#clean-content .clean-list ul li", "div input[type=\"checkbox\"]", text);
    }
    
//========================================init listener=====================================
    ViewManager.addViewListener("onShow", "#clean-content", cleanOnShowListener); //add listener to monitor what will happen when settings-content shown.
//================================================event bind================================

    $('#clean-content #cleanButton').click(function(event){
        $('#clean-content .list-header #quickSearch').val("");
        doFilter("");
        removeSelection();
    });
    
    $('#clean-content #refreshButton').click(function(event){
        $('#clean-content .list-header #quickSearch').val("");
        doFilter("");
        cleanOnShowListener();
    });
    
    $('#clean-content .list-header #quickSearch').keyup(function(event) {
        var text = $(this).val();
        doFilter(text);
    });
    
    
}(window));
