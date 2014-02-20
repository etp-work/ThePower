Instruction
==============

**The Power** is not only a toolkit for PortalTeam, but also a platform which can be customized for your own usage. It consist of the following components.

* **PortalKit**: The core part of **The Power**, which provide the capability of service customization, look and feel. Right now, we have a few buildin services, such as "workspace provider service", "maven execution service", "deploy service", "compress service" and so on.
* **PortalKitClient**`deprecated`: A java based native shell for rendering the HTML GUI which provided by **PortalKit**.
* **EmbeddedTomcat**: An embedded tomcat, which is the buildin container for running services provided by **PortalKit**.
* **NativeContainer**: A C# based native shell for rendering the HTML GUI which provided by **PortalKit**, It provide the websocket api as well for some special usage.
* **Installer**: A Inno Setup script based project, which is the installation file creator. By running this project, it will collect all thre resources from above projects and generate a installation file.


![](https://github.com/etp-work/ThePower/blob/master/thepoweer.jpg)


### Project Information

* Multiple languages: C#, java, javascript, HTML, css, Ant, Inno Setup script, Pascal.
* Multiple third-party libraries: Gecko FX, Spring, Junit, Jmockit, lucene, jasmine, angularJS, JQuery, SWT.

We aim to build a very fresh project which consist of many of the newest, best tools and languages. By involved in this project, you would never feel torture from the situation that you don't have time to improve the things you planned to do, or even worse that you are not allowed to do that improvement.
Feel free to pop up your proposal, it's really cool that we can talk to each other about the ideas.


Environment
==============

* JDK 7(or above) installed with correct [configuration] [1].
* Ant 1.8.4(or above) installed with correct [configuration] [2].
* Maven 3.0.4(or above) installed with correct [configuration] [3]. 
* Inno setup 5.5.3-unicode(or above) installed by default options. [Download] [4]
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

* Eclipse IDE for Java EE Developers [download] [5].
* Visual Studio Express 2012 For Window Desktop(or above) [download] [6]
* Dreamweaver 8(or above)`optional`

**Git plugin for Eclipse:**

1.  Open eclipse.
2.  Click *Help* -> *Eclipse Marketplace...*.
3.  Type "EGit" into *Search* -> *Find:* textbox.
4.  Find "EGit - Git Team Provider", and *Install* it.

**Maven plugin for Eclipse:**

1.  Open eclipse.
2.  Click *Help* -> *Eclipse Marketplace...*.
3.  Type "Maven" into *Search* -> *Find:* textbox.
4.  Find "Maven Integration for Eclipse", and *Install* it.

**Properties editor plugin for Eclipse:**

1.  Open eclipse.
2.  Click *Help* -> *Eclipse Marketplace...*.
3.  Type "Properties Editor" into *Search* -> *Find:* textbox.
4.  Find "Properties Editor" developed by Chomakichi, GPL, and *Install* it.


Getting Started
==============

Getting started with eclipse:

1.  Open eclipse.
2.  Import [PortalKit](https://github.com/etp-work/ThePower/blob/master/PortalKit/README.md), [Installer](https://github.com/etp-work/ThePower/blob/master/Installer/README.md) from workstation.
3.  Detail configuration of each project, please refer to their own README.md



[1]: http://docs.oracle.com/javase/7/docs/webnotes/install/windows/jdk-installation-windows.html     "configuration"
[2]: http://ant.apache.org/manual/install.html#sysrequirements                                       "configuration"
[3]: http://maven.apache.org/download.cgi#Installation_Instructions                                  "configuration"
[4]: http://www.jrsoftware.org/isdl.php#stable                                                       "Download"
[5]: http://www.eclipse.org/downloads/                                                               "download"
[6]: http://www.microsoft.com/visualstudio/chs/downloads                                             "download"
