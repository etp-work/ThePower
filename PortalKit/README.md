Instruction
=======================

This project is the core part of the ThePower platform, consist of view(HTML + CSS + javascript), Controller(javaEE), Service(java).
We have several Third-Party libraries in this project for using. Such as JQuery, angularJS, jasmine, Spring, Tomcat, JMockit.

* **JQuery** is responsible for DOM control.
* **angularJS** is responsible for data rendering.
* **jasmine** is responsible for js unit test.
* **Spring** is responsible for framework lifecycle.
* **Tomcat** is responsible for websocket management.
* **JMockit** is responsible for java unit test.

Structure
=======================
<pre>
|-- pom.xml
`-- src
    |-- main
    |   |-- java
    |   |   `-- (services and framework of server part)
    |   |-- js
    |   |   |-- dependencies
    |   |   |   `-- (Third-party librayies)
    |   |   |-- features
    |   |   |   `-- (extensible features)
    |   |   |-- fw
    |   |   |   `-- (framework of client part)
    |   |   |-- utility
    |   |       `-- (utilities of client part)
    |   |-- resources
    |       `-- (json files that needed by services)
    |-- test
    |   |-- java
    |   |   `-- (java unit test)
    |   |-- js
    |       `-- (js unit test)
    |-- webapp
        `-- WEB-INF
            |-- default-servlet.xml
            |-- web-related.xml
            |-- web.xml
            |-- resources
            |         |-- css
            |         |   |-- index.css
            |         |   |-- features
            |         |   |-- utility
            |         |-- images
            |-- views
                |-- index.jsp
                |-- common
                |-- templates
                    |-- features
                    |-- utility
                
</pre>

Compile source code by using Ant script
=======================

## Use eclipse as IDE

1. Download the correct version of eclipse and install it. Refer to [Development Tools](https://github.com/etp-work/ThePower/blob/master/README.md#development-tools).
2. Make sure you have `JAVA_HOME`, `M2_HOME` configured in system variables on your laptop.
3. Open *Window* -> *Preferences* -> *Ant* -> *Runtime* -> *Properties*, click *Add Property*, set `deploy.local.dir` to *name* and the value of this property is the absolute path of tomcat's wabapps folder.
4. Open *Window* -> *Show View*, choose *Ant* to add a ant view to the perspective.
5. Drag *PortalKit/build.xml* into ant view, a *PortalKit builder* will be created for you.
6. Two execute options included:
    * Build       : compile the source code and execute both java and js unit test cases.
    * Build_Deploy: compile the source code and execute both java and js unit test cases, deploy the generated war file into a place which you specified in ant configuration by `deploy.local.dir`.
7. Double click the item you want to perform. And the war file will be generated at *PortalKit/target*

## Use windown command line

1. Make sure you have `JAVA_HOME`, `M2_HOME` configured in system variables on your laptop.
2. Modify *PortalKit/build.properties* to set value of `deploy.local.dir` which is the absolute path of tomcat's wabapps folder.
3. Go into *PortalKit* folder, execute command `ant -f build.xml Build`, the source code will be compiled and both java and js unit test cases will be executed.
4. If you intend to deploy this generated war file into your tomcat as extra job compare with step3, you can execute command `ant -f build.xml Build_Deploy`.

