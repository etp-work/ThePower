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
**Common part**

1. Open *Visual Studio Express 2012 for Desktop*, click *open project...*, choose *NativeContainer -> GriffinsPortalKit.sln*. Click *Open*.
2. In *Solution Panel*, right click *GriffinsPortalKit*, click *Rebuild*.
3. Go into folder `NativeContainer\GriffinsPortalKit\bin\Release`, copy all the stuff except **xulrunner** to `NativeContainer\GriffinsPortalKit\release`.

**By using eclipse as IDE**

1. Make sure you have `JAVA_HOME`, `M2_HOME` configured in system variables on your laptop.
2. Install **Inno Setup**, please refer to [Environment](https://github.com/etp-work/ThePower/blob/master/README.md#environment).
3. Modify system variable `path` by appending value of the installation directory of *inno setup*.
4. Open *Window* -> *Preferences* -> *Ant* -> *Runtime* -> *Properties*, click *Add Property*, set `inno.home` to name and set absolute path of installation directory of **inno setup**.
5. Open *Window* -> *Preferences* -> *Ant* -> *Runtime* -> *Classpath*, choose *Ant Home Entries*, click *Add JARs...*, choose `libs/commons-net-1.4.1.jar`, `libs/jakarta-oro-2.0.8.jar` to add.
6. Open *Window* -> *Show View*, choose *Ant* to add a ant view to the perspective.
7. Drag `Installer/build.xml` into ant view, an `Installer builder` will be created for you.
8. Two execute options included:
    * Compile32: generate the 32-bit supported .exe installation file.
    * Compile64: generate the 64-bit supported .exe installation file.
9. Double click the item you want to perform. And the .exe file will be generated at `Installer/resources/output` folder.

**By using window command line**

1. Make sure you have `JAVA_HOME`, `M2_HOME` configured in system variables on your laptop.
2. Install apache-ant-1.8.4, please refer to [Environment](https://github.com/etp-work/ThePower/blob/master/README.md#environment).
3. Install **Inno Setup**, please refer to [Environment](https://github.com/etp-work/ThePower/blob/master/README.md#environment).
4. Modify system variable `path` by appending value of the installation directory of *inno setup*.
5. Open `Installer/build.properties`, set absolute path of installation directory of *inno setup* to `inno.home`.
6. Copy `libs/commons-net-1.4.1.jar`, `libs/jakarta-oro-2.0.8.jar` to `apache-ant-1.8.4\lib` folder.
7. Execute command `ant -f build.xml Compile64` to generate a 64-bit supported .exe installation file.
8. If you intend to generate a 32-bit supported .exe installation file, you can type in command `ant -f build.xml Compile32`.
9. The .exe installation file will be generated at `Installer/resources/output/`.


