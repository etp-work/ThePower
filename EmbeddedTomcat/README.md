Instruction

build.xml

-description

This is a ant script which can be executed within eclipse or command line, in order to compile the EmbeddedTomcat project.

Preparation in Eclipse

1. Make sure you have JAVA_HOME, M2_HOME set in system variables on your laptop.
 
2. Open Window -> Show View, choose "Ant" to add a ant view to the perspective.

3. Drag build.xml into ant view.

4. You will see two options under "EmbeddedTomcat builder" as below:

   build [default]    : compile the project, and generate EmbeddedTomcat.jar into target folder.

   
Preparation in Command line

1. Make sure you have JAVA_HOME, M2_HOME set in system variables on your laptop.
 
2. Execute command "ant -f build.xml"

3. jar file will be generated in target folder.