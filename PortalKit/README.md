Instruction

build.xml
-description
This is a ant script which should be executed within eclipse, in order to compile the PortalKit project, and deploy it into tomcat.
1. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "deploy.local.dir" to name and set absolute path of tomcat's wabapps to value.
2. Make sure you have JAVA_HOME, M2_HOME set in system variables on your laptop. 
3. Open Window -> Show View, choose "Ant" to add a ant view to the perspective.
4. Drag build.xml into ant view.
5. You will see two options under "PortalKit builder" as below:
   build_deploy_nontest    : compile the project without running test case, and deploy it into tomcat.
   build_deploy_test       : compile the project with test case running, and deploy it into tomcat.
   build_nontest[default]  : compile the project without running test case.
   build_test              : compile the project with test case running.
