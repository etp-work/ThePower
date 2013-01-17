/**
 * This JS module is written for view test-content in index.jsp.
 */
(function () {
    
  //listeners
    function testOnShowListener(){
        //TODO
    }
    
    ViewManager.addViewListener("onShow", "#test-content", testOnShowListener); //add listener to monitor what will happen when deploy-content shown.
    
    
});
