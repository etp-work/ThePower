Instruction
==============

**The Power** is not only a toolkit for PortalTeam, but also a platform which can be customized for your own usage. It consist of the following components.

* **PortalKit**: The core part of **The Power**, which provide the capability of service customization, look and feel. Right now, we have a few buildin services, such as "workspace provider service", "maven execution service", "deploy service", "compress service" and so on.

* **PortalKitClient**(deprecated): A java based native shell for rendering the HTML GUI which provided by **PortalKit**.

* **EmbeddedTomcat**: An embedded tomcat, which is the buildin container for running services provided by **PortalKit**.

* **NativeContainer**: A C# based native shell for rendering the HTML GUI which provided by **PortalKit**, It provide the websocket api as well for some special usage.

* **Installer**: A Inno Setup script based project, which is the installation file creator. By running this project, it will collect all thre resources from above projects and generate a installation file.

[How to use]
==============

[PortalKit] It's a web service war file, should deployed into tomcat. (Only support tomcat for now)


[Configuration]
==============

--First time when you clone this repository, you need to run below commands under workstation folder one by one.

[Commands]

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



