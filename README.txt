This project can be compiled/viewed with one of the following IDEs or through using the build.xml file against Apache Ant (http://ant.apache.org) - see the build.xml file for a list of available compile & run options.

Eclipse 3.2 - http://www.eclipse.org:
---------------------------------------
1) Open the File Menu
2) Select New 
3) Select Project 
4) Java Project
	4a) Enter Project name
	4b) Select 'Create project from existing source'
	4c) Browse to source folder
	4d) Select Next
	4e) Make sure 'resources' and 'src' folders are included
	4f) Select Finish
5) Java Project from Existing Ant Buildfile
	5a) Find build.xml in source folder
	5b) Make sure 'resources' and 'src' folders are selected
	5c) Select Finish
6) Select Run -> Run...
7) The project under 'Java Application'
8) Arguments tab
9) Enter '-Xms64m -Xmx256m' into the VM Arguments box
10) Enter any program arguments - run the program on its own to find them out
11) Select Project -> Build All
12) Select Run -> Run last launched


JCreator 3.5 - http://www.jcreator.com:
---------------------------------------
1) Open the .jcw file in the source folder
2) Select Build menu -> RunTime Configuration
3) Double-click 'Default'
4) Select 'GameLauncher' under Run options listbox
5) Select 'Default' under Run Application tab
6) Click 'Edit'
7) Select Parameters tab
8) Add '-Xms64m -Xmx256m' to the end of the Parameters field
9) Get back to the main screen (3xOKs)
10) Build -> Compile project
11) Build -> Execute project

If you have the JVM installed and the 'build' folder contains *.class files, try running 'rungame.bat'.

Alternatively, you can just double-click the .jar file in the 'release' directory ;)

Enjoy :o)
