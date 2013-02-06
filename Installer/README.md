Description
This is an installer project, used to generate executable setup program for windows system both 32bit/64bit.


Preparation
1. Download 'Inno Setup' from http://www.jrsoftware.org/download.php/is-unicode.exe.
2. Install the downloaded is-unicode.exe, by all default choices.
3. Modify system variable path by appending value of the installation directory of 'inno setup'.
4. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "inno.home" to name and set absolute path of installation directory of 'inno setup'.
5. Open Window -> Preferences -> Ant -> Runtime -> Classpath, choose 'Ant Home Entries', click 'Add JARs...', choose libs/commons-net-1.4.1.jar, libs/jakarta-oro-2.0.8.jar to add.
6. Run build.xml. Note: there are two options 'compile32/compile64' can be used. You should execute one of them for you operate system.
7. The generated setup file located at resources/output/