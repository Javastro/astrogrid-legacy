<%@ page
   import="org.astrogrid.datacenter.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>PAL Installation </title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>


<!-- packrat requirments
1 INSTALL document per component, to include:
Where and how to download the installation kit
How to deploy and configure
How to administer (stop/start/restart)
How to trouble-shoot
-->
<h1>Introduction</h1>
<p>
This document describes how to publish a data collection using an Astrogrid Datacenter Server.
To do this, carry out the following steps:
<ul>
<li>Install the system pre-requisites
</li><li>Download PAL
</li><li>Deploy PAL
</li><li>Configure the database
</li><li>Configure the connection to the data collection
</li><li>Create a  description of the data collection
</li><li>Restart the servlet container
</li><li>Test the installation
</li><li>Register the data collection with the VO
</li></ul>
</p>

<h1>Pre-requisites</h1>
<p>
<a href="pre-requisites.jsp">This page</a> describes the software components that must be installed and configured before installing the Datacenter Server.
The Datacenter Server relies upon the following software components, which must be
installed and configured before installing the Datacenter Server.
</p>


<h1>Download PAL</h1>
<p>
The publishers astrogrid library is distributed as a J2EE Web Application (.war) file.
<!--To add a dependency on PAL to your own project, add the following to your <a href="http://maven.apache.org">maven</a> <tt>project.xml</tt>
<source>
   &lt;dependencies&gt;
   ...
   &lt;dependency&gt;
   &lt;groupId&gt;astrogrid-datacenter&lt;/groupId&gt;
   &lt;artifactId&gt;pal&lt;/artifactId&gt;
   &lt;version&gt;SNAPSHOT&lt;/version&gt;
   &lt;type&gt;war&lt;/type&gt;
   &lt;/dependency&gt;
   ...
   &lt;dependencies&gt;
</source> -->
</p>
<p>
The latest snapshot of PAL can be downloaded directly from<br />
<a href="http://www.astrogrid.org/maven/org.astrogrid/wars/pal-SNAPSHOT.war">here</a>. You can find
other versions, jar files, etc for the AstroGrid project <a href="http://www.astrogrid.org/maven/build/">here</a>
</p>

<h1>Deploy PAL</h1>

You can use the Tomcat manager to install PAL (see the Tomcat manager instructions)
or you can do it by hand - Copy <tt>pal.war</tt> to the <tt>webapps</tt> directory of your tomcat installation and
restart your server:

<source>
$ cp pal-SNAPSHOT.war ${CATALINA_HOME}/webapps
$ ${CATALINA_HOME}/bin/shutdown.sh
$ ${CATALINA_HOME}/bin/startup.sh
</source>
<p>
Once tomcat has been restarted, the pal web application will be unpackaged into the directory <tt>webapps/pal-SNAPSHOT</tt>. If installed on your
machine in the default location, the web application root will be <a href="http://localhost:8080/pal-SNAPSHOT">http://localhost:8080/pal-SNAPSHOT</a>.
</p>

<h1>Configure PAL</h1>
<p>

<a href="configuration.jsp">This page</a> describes in detail how to configure
your datacenter. However once PAL has been installed, it should be possible to
run the <a href="../self-test.jsp">installation tests</a> and some cone <a href="../coneForm.jsp">queries</a>
 on it, as it contains a simple SQL database by default.
<p/>
Read the configuration page before going on, as you
will need to make a new configuration file to connect to your data.
</p>
</section>

<h1>Describe your data</h1>

<p>For your data to be published to the VO, it must be capable of presenting an
XML document that describes that data.  This allows the VO's automatic tools
to analyse what is available.</p>
<p>The <a href="configuration.jsp">config pages</a> describe how to make these.  Once this
is done and you are happy with the results shown on the <a href="../viewMetadata.jsp">metadata</a> pages,
click on the 'Register' link in the <a href='../admin/register.jsp'>admin</a> pages
</p>

</section>

<h1>Verify the installation</h1>
<p>
Pal comes packaged with a series of self-test scripts. To run these, visit the web application root,
and navigate to the <i>self-test</i> page. Inspect the result from each script - no
errors should appear on the output.
</p>


<h1>Logging</h1>
<p>
The datacenter uses 'commons-logging' to log messages; on a Tomcat server this is
routed to the server's <a href="http://jakarta.apache.org/log4j/docs/">Log4J</a> logger.
A default log4j properties file is included in the distribution, and can be found at
<tt>/WEB-INF/classes/log4j.properties</tt> in the web application.
<p/>
By default, the properties file causes INFO and higher messages to go to std.out (these
can be found in the catalina.out file when using Tomcat), INFO and higher messages are
stored in pal.log, and DEBUG and higher messages are stored in a rolling file (it is
size limited) called pal-debug.log.  These latter two are found in ${CATALINA_HOME}/logs/ when
running under Tomcat.
</p>

<h1>Running test queries</h1>
<p>
The datacenter web application contains simple web forms (see the navigation links on the main <a href='../index.jsp'>index page</a>)
to submit queries to the datacenter service.

<p />
In addition, the client delegate jar contains simple command-line tool that will fire a adql query at the server and display the VOTABLE response. You need
to write an adql query that conforms to the adql schema (downloadable from the web application) and save it to a file.

<source>
$ java org.astrogrid.datacenter.ui.DatacenterCommander adql <i>&lt;url-endpoint&gt;</i> <i>&lt;query-xml-file&gt;</i>
</source>

</p>


<h1>References<h1>
<ul>
<li><a href="http://www.astrogrid.org">Astrogrid Homepage</a>
</li><li><a href="http://java.sun.com">Java Homepage</a>
</li><li><a href="http://jakarta.apache.org/tomcat">Jakarta Tomcat Homepage</a>
</li><li><a href="http://ws.apache.org/axis/">Apache Axis Homepage</a>
</li></ul>

</div>
</body>
</html>

