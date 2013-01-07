Instruction

build.xml
-description
This is a ant script which should be executed within eclipse, in order to compile the portalkit project, and deploy it into tomcat.
1. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "deploy.local.dir" to name and set absolute path of tomcat's wabapps to value.
2. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "maven.home" to name and set absolute path of maven installation folder.
3. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "java.home" to name and set absolute path of JDK installation folder.
4. Open Window -> Show View, choose "Ant" to add a ant view to the perspective.
5. Drag build.xml into ant view.
6. You will see two options under "PortalKit builder" as below:
   build [default]: compile the project without running test case, and deploy it into tomcat.
   build_test     : compile the project with running test case, and deploy it into tomcat.
