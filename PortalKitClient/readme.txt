Instruction

build.xml
-description
This is a ant script which should be executed within eclipse, in order to compile the portalkitclient project into a single jar file.
1. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set deploy.local.dir to name and set absolute path of tomcat's wabapps to value.
2. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "maven.home" to name and set absolute path of maven installation folder.
3. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "java.home" to name and set absolute path of JDK installation folder.
4. Open Window -> Show View, choose "Ant" to add a ant view to the perspective.
5. Drag build.xml into ant view.
6. You will see two options under "PortalKitClient builder" as below:
   build [default]: compile the project into a single jar file which is executable.
   install3pp     : install the swt needed jar into your local repository, this should be run at first.
7. First time when you download the source code, you need to run "install3pp" at first. Then run "build" each time when you want to build a new executable jar file.

setting.cfg
-description
This configuration file is optional, which is used to specify ip, port, url, fullurl.
1. ip: IP address that you want to connect. Default is "localhost".
2. port: Port number that you want to connect with. Default is "8080".
3. url: Url that you want to connect with. Default is "/PortalKit/".
4. fullurl: The url contains protocol, ip, port, and url. If you want to specify your own url, you can choose this option. Note that, if you set fullurl, the other three options will be deprecated.