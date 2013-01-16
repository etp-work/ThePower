/**
 * This JS module is written for view build-content in index.jsp.
 */
(function () {
    
    function setNeedDeploy(param){
        $('#needDeploy').attr("checked", needDeploy);
    }
    
    function setDef4Build(defaultSelection, message){
        var url = "/powerbuild/setDefault.ajax";
        
        DynamicLoad.postJSON(url, {
            selection: defaultSelection
        }, function (){
            ViewManager.addNotification({
                type: "success",
                message: message,
                timeout: 5000
            });        
        }, function (error){
            ViewManager.addNotification({
                type: "error",
                message: error.message
            });
        });
    }
    
    function getBuildSelection(){
        var defaultSelection = [];
        $('.bulid-list .group input').each(function (){
            if($(this).is(':checked')){
                defaultSelection.push($(this).val());
            }
        });
        return defaultSelection;
    }
    
    $('#setDefault4Build').click(
        function(event){
            var defaultSelection = getBuildSelection();
            
            if(defaultSelection.length === 0){//if no anything checked, give a warning.
                ViewManager.addNotification({
                    type: "attention",
                    timeout: 30000,
                    message: "You can't set nothing to default."
                });
                return;
            }
            setDef4Build(defaultSelection, "Successfully saved default selection.");
            event.preventDefault();
        }
   );
    
    $('#resetDefault4Build').click(
        function(event){
            
            $('.bulid-list .group input').each(function (){
                if($(this).is(':checked')){
                    $(this).attr("checked", false);
                }
            });
            setDef4Build([], "Successfully reset default selection.");
        
            event.preventDefault();
       }
    );
    
    function build(selection, needDeploy){
        var url = "/powerbuild/build.ajax";
        if(selection.length === 0){
            return;
        }
        var packagename = selection.shift();
        DynamicLoad.postJSON(url, {
            selection: packagename,
            needDeploy: needDeploy
        }, function(BuildResult){
            if(!BuildResult.success){
                ViewManager.addNotification({
                    type: "error",
                    message: "Build error",
                    callback: function(){
                        //TODO show (BuildResult.message);
                    }
                });
            }else if(!BuildResult.deployed && needDeploy){
                ViewManager.addNotification({
                    type: "error",
                    message: "Deploy error"
                });
            }else{
                var sucMessage = packagename + " build "+(needDeploy ? "+ deploy" : "" )+" successfully.";
                ViewManager.addNotification({
                    type: "success",
                    timeout: 5000,
                    message: sucMessage
                });
                build(selection, needDeploy);
            }
        }, function(error){
            ViewManager.addNotification({
                type: "error",
                message: "Internal error:"+error.message,
                timeout: 15000
            });
        });
    }
    
    $('#buildButton').click(function(event){
        var defaultSelection = [];
        $('.bulid-list .group .child').each(function (){
            if($(this).is(':checked')){
                defaultSelection.push($(this).val());
            }
        });
        var needDeploy = $('#needDeploy').is(':checked');
        build(defaultSelection, needDeploy);
        event.preventDefault();
    });
    
    $('#needDeploy').click(function(event){
        if($(this).is(':checked')){
            var canDeploy = true;
            $('.bulid-list .group .parent[value="Others"]').parent().parent().siblings().find('.child').each(function(){
                if($(this).is(':checked')){
                    canDeploy = false;
                }
            });
            
            if(!canDeploy){
                ViewManager.addNotification({
                    type: "attention",
                    message: "Items in Others can't be deployed.",
                    timeout: 10000
                });
                event.preventDefault();
            }
        }
    });
});
