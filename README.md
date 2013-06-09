Instruction
==============

**The Power** is not only a toolkit for PortalTeam, but also a platform which can be customized for your own usage. It consist of the following components.

* **PortalKit**: The core part of **The Power**, which provide the capability of service customization, look and feel. Right now, we have a few buildin services, such as "workspace provider service", "maven execution service", "deploy service", "compress service" and so on.
* **PortalKitClient**(deprecated): A java based native shell for rendering the HTML GUI which provided by **PortalKit**.
* **EmbeddedTomcat**: An embedded tomcat, which is the buildin container for running services provided by **PortalKit**.
* **NativeContainer**: A C# based native shell for rendering the HTML GUI which provided by **PortalKit**, It provide the websocket api as well for some special usage.
* **Installer**: A Inno Setup script based project, which is the installation file creator. By running this project, it will collect all thre resources from above projects and generate a installation file.

Environment
==============

* JDK 7(or above) installed with correct [configuration] [1].
* Ant 1.8.4(or above) installed with correct [configuration] [2].
* Maven 3.0.4(or above) installed with correct [configuration] [3]. 
* Inno setup 5.5.3-unicode(or above) installed by default options.
* Git 1.8.0(or above) with following configuration. Those commands should be executed under *workstation* folder, which doesn't impact the configuration for other projects.

        git config --local user.name "<name>"
        git config --local user.email "<email>"
        git config --local core.excludesfile $HOME/.gitignore
        git config --local core.autocrlf input
        git config --local color.ui true
        git config --local gui.encoding utf-8
        git config --local push.default tracking
        git config --local branch.autosetupmerge always
        git config --local branch.autosetuprebase always
        git config --local alias.co checkout
        git config --local alias.st status
        git config --local alias.br branch

Development Tools
==============

* Eclipse 4(or above), [download] [4].
* Visual Studio Express 2012 For Window Desktop(or above) [download] [5]
* Dreamweaver 8(or above)`optional`






[1]: http://docs.oracle.com/javase/7/docs/webnotes/install/windows/jdk-installation-windows.html     "configuration"
[2]: http://ant.apache.org/manual/install.html#sysrequirements                                       "configuration"
[3]: http://maven.apache.org/download.cgi#Installation_Instructions                                  "configuration"
[4]: http://www.eclipse.org/downloads/                                                               "download"
[5]: http://www.microsoft.com/visualstudio/chs/downloads                                             "download"
