/**
 * This module is only worked for view clean-content which included in
 * index.jsp.
 */
(function(window) {
    'use strict';
    
//=========================================variable=========================================
    var getCleanItemsUrl = "/clean/getCleanItems.ajax";
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
    
//========================================init listener=====================================
    ViewManager.addViewListener("onShow", "#clean-content", cleanOnShowListener); //add listener to monitor what will happen when settings-content shown.
//================================================event bind================================
    
}(window));
