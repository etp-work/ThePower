Introduction
===========================
This is an installer project, used to generate executable setup program for both 32bit/64bit windows system.

Workflow
===========================
1. Unzip *Installer/libs/CefSharp-0.12.zip* to *Installer/resources/CefSharp-0.12*, unless if it is the first time you use it to build the .exe file on your laptop.
2. 



Preparation for eclipse

1. Make sure you have JAVA_HOME, M2_HOME set in system variables on your laptop.

2. Download 'Inno Setup' from http://www.jrsoftware.org/download.php/is-unicode.exe.

3. Install the downloaded is-unicode.exe, by all default choices.

4. Modify system variable path by appending value of the installation directory of 'inno setup'.

5. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "inno.home" to name and set absolute path of installation directory of 'inno setup'.

6. Open Window -> Preferences -> Ant -> Runtime -> Classpath, choose 'Ant Home Entries', click 'Add JARs...', choose libs/commons-net-1.4.1.jar, libs/jakarta-oro-2.0.8.jar to add.

7. Run build.xml. Note: there are two options 'compile32/compile64' can be used. You should execute one of them for you operate system.

8. The generated setup file located at resources/output/


Preparation for command line

1.  Make sure you have JAVA_HOME, M2_HOME set in system variables on your laptop.

2.  Download apache-ant-1.8.4 from http://archive.apache.org/dist/ant/binaries/apache-ant-1.8.4-bin.zip.

3.  Upzip apache-ant-1.8.4-bin.zip into %CUSTOMIZED%.(You decide yourself what folder is %CUSTOMIZED%)

4.  Config ant by following http://ant.apache.org/manual/index.html.

5.  Download 'Inno Setup' from http://www.jrsoftware.org/download.php/is-unicode.exe.

6.  Install the downloaded is-unicode.exe, by all default choices.

7.  Modify system variable path by appending value of the installation directory of 'inno setup'.

8.  Open build.properties, set absolute path of installation directory of 'inno setup' to "inno.home".

9.  Copy libs/commons-net-1.4.1.jar, libs/jakarta-oro-2.0.8.jar to %CUSTOMIZED%\apache-ant-1.8.4\lib.

10.  Execute command "ant -f build.xml compile64/compile32" to generate a 64-bit/32-bit executable file.

11. The generated setup file located at resources/output/


Usage

1. To use this build.xml, you need rebuild source code of NativeContainer within Visual Stuido Express 2012.

2. Copy all the files in NativeContainer\GriffinsPortalKit\bin\Release into NativeContainer\GriffinsPortalKit\release.(Do not do anything else, like copy xulRunner, CefSharp into this release folder. Those such things will be done by this script automatically)

3. Execute this build.xml in ANT environment.
