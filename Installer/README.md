Description
This is an installer project, used to generate executable setup program for windows system both 32bit/64bit.


Preparation for eclipse

1. Download 'Inno Setup' from http://www.jrsoftware.org/download.php/is-unicode.exe.

2. Install the downloaded is-unicode.exe, by all default choices.

3. Modify system variable path by appending value of the installation directory of 'inno setup'.

4. Open Window -> Preferences -> Ant -> Runtime -> Properties, click "Add Property", set "inno.home" to name and set absolute path of installation directory of 'inno setup'.

5. Open Window -> Preferences -> Ant -> Runtime -> Classpath, choose 'Ant Home Entries', click 'Add JARs...', choose libs/commons-net-1.4.1.jar, libs/jakarta-oro-2.0.8.jar to add.

6. Run build.xml. Note: there are two options 'compile32/compile64' can be used. You should execute one of them for you operate system.

7. The generated setup file located at resources/output/


Preparation for command line

1.  Download apache-ant-1.8.4 from http://archive.apache.org/dist/ant/binaries/apache-ant-1.8.4-bin.zip.

2.  Upzip apache-ant-1.8.4-bin.zip into %CUSTOMIZED%.(You decide yourself what folder is %CUSTOMIZED%)

3.  Config ant by following http://ant.apache.org/manual/index.html.

4.  Download 'Inno Setup' from http://www.jrsoftware.org/download.php/is-unicode.exe.

5.  Install the downloaded is-unicode.exe, by all default choices.

6.  Modify system variable path by appending value of the installation directory of 'inno setup'.

7.  Open build.properties, set absolute path of installation directory of 'inno setup' to "inno.home".

8.  Copy libs/commons-net-1.4.1.jar, libs/jakarta-oro-2.0.8.jar to %CUSTOMIZED%\apache-ant-1.8.4\lib.

9.  Execute command "ant -f build.xml compile64/compile32" to generate a 64-bit/32-bit executable file.

10. The generated setup file located at resources/output/