Introduction
===========================
This is an installer project, used to generate executable setup program for both 32bit/64bit windows system.

Workflow
===========================
1. Unzip `Installer/libs/CefSharp-0.12.zip` to `Installer/resources/CefSharp-0.12`, if it is the first time you use this installer to build the .exe file on your laptop.
2. Download `xulrunner-18.0.2.en-US.win32.zip` to `Installer/resources/download`, and unzip it to `Installer/resources/xulrunner`, if it is the first time you use this installer to build the .exe file on your laptop.
3. Compile the source code of **EmbeddedTomcat**, and copy the generated jar file into `Installer/resources/CustomizedTomcat`.
4. Copy `Installer/libs/portal-widget-onekey-war-NO-VERSION.war` into `Installer/resources/CustomizedTomcat`.
5. Compile the source code of **PortalKit**, and copy the generated war file into `Installer/resources/CustomizedTomcat/webapps`.
6. Copy all stuff under `NativeContainer/GriffinsPortalKit/release` into `Installer/resources/shell`.
7. Copy all stuff under `Installer/resources/CefSharp-0.12` into `Installer/resources/shell`.
8. Execute the script under `Installer/resources/scripts` for specific CPU support of windows.
9. The .exe file will be generated in `Installer/resources/output`.

How to run
==========================
**By using eclipse as IDE**

1. Make sure you have `JAVA_HOME`, `M2_HOME` configured in system variables on your laptop.
2. Install **Inno Setup**, please refer to [Environment](https://github.com/etp-work/ThePower/blob/master/README.md#environment).
3. Modify system variable `path` by appending value of the installation directory of *inno setup*.
4. Open *Window* -> *Preferences* -> *Ant* -> *Runtime* -> *Properties*, click *Add Property*, set `inno.home` to name and set absolute path of installation directory of **inno setup**.
5. Open *Window* -> *Preferences* -> *Ant* -> *Runtime* -> *Classpath*, choose *Ant Home Entries*, click *Add JARs...*, choose `libs/commons-net-1.4.1.jar`, `libs/jakarta-oro-2.0.8.jar` to add.
6. Open *Window* -> *Show View*, choose *Ant* to add a ant view to the perspective.
7. Drag PortalKit/build.xml into ant view, a PortalKit builder will be created for you.
6. Run build.xml. Note: there are two options 'compile32/compile64' can be used. You should execute one of them for you operate system.

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
