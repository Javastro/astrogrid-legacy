This package builds a copy of Globus Toolkit 3 (GT3) core into a WAR for easier
installation in a web container such as Jakarta-Tomcat.

To begin, you need a clean copy of GT3 core, obtainable from www.globus.org.
You only need the Java core, not the supporting packages.  You also need a copy
of Apache ant.

You must create, in the directory where you find this README file, the file
build.properties and in it make these settings:

ogsa.home=<GT3 directory>
web.app.name=<name of web application>

replacing <GT3 directory> with the absolute path to the directory where you put
GT3 and <name of web application> with your choice of name for the web
application in which GT3 core will run.  Example:

ogsa.home=/export/home/globus-3.0.2
web.app.name=ogsa

The conventional web-app for running GT3 is called ogsa, but you can pick any
name. Note that you create and define a web-app by building and deploying this
WAR file. You are not being asked to give the name of an existing web-app to
which GT3 will be added and, indeed, you must choose a web-app name that is not
already used in your intended web container. If you were to use the name of an
existing web-app, then both old and new web-apps would be damaged if you
deployed the WAR to the container.

Now change directory to the directory holding this README and give the command:

 ant
 
This builds the WAR and leaves it in the sub-directory "build" of the current
directory.

You can now deploy the WAR to your web container using that container's normal
method. E.g. for Tomcat, either copy the WAT to Tomcat's webapps directory and
restart Tomcat or load the WAR via a web browser using Tomcat's "manager"
interface.
