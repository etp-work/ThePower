Instruction
==============

[The power] is a set of tools that integrated into one, can be used by developers/testers in a easy way. Up to now, contains two parts [PoralKit] and [PortalKitClient].

[The power - PortalKit] It's a web service provider that consist of several services, such as "tree provider", "build service", "deploy service", "compress service"...

[The power - PortalKitClient] It's a java based native client that developed by SWT toolkit, it is suit for windows-32-bit/64-bit OS. The weak point of PortalKitClient is hard to beautify GUI. 

            Note: There is no any new edition released. It's deprecated.

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
