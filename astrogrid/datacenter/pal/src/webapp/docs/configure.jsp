<%@ page
   import="org.astrogrid.datacenter.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>PAL Documentation</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Configuring your Datacenter</h1>

<h3>Using a 'Properties' File</h3>
<p>A properties file is an ordinary text file with a list of keys and values, like this:
<verbatim>
   datacenter.name=SomeExample
   datacenter.url=http://somehost.ac.uk/PAL/
</verbatim>
Any line starting with <tt>#</tt>
are comments and are ignored by the application.</p>

<p>When initially installed, a datacenter reads its configurations from the
'default.properties' file in its context's WEB-INF/classes directory. This
configures the datacenter to use a 'sample' dataset, so you can run
test queries on it before connecting up to a real dataset. This file
is overwritten whenever you upgrade PAL, so it is useful as a template but we
do not recommend you edit it directly.</p>
<p>Instead, create a new file (or edit the existing one if you have other
Astrogrid components already installed) 'astrogrid.properties'
and make sure this is in your classpath; a suitable place on Tomcat might be
the common/classes directory so it is not overwritten on updates.</p>
<p>If you want to have individual configuration files for each component, then create it and
set the environment variable 'astrogrid.config.filename' to the appropriate path and
filename, or the environment variable 'astrogrid.config.url' to the appropriate URL.
  You can use environment variables in the variable value. For example, using <code>${catalina.home}/conf/datacenter.properties</code>
will cause the datacenter to look in tomcat's configuration directory if you are running tomcat.</p>
<p>If you have several datacenters it will be necessary to do have a separate configuration file
for each datacenter, and you will need to set the
environment variable under each context to a different properties file.</p>
<p>You will need to restart the application after any change to a configuration file.</p>

<h3>Using Environment Variables</h3>
<p>You can set system environment variables from the command line.  For example, you
can use the command <code>setenv astrogrid.config.filename=/disk1/webapp/pal.properties</code> under linux.
<p>If you are using Tomcat, you can also change environment variables by editing the 'Environment Entries'
as a Tomcat Administrator</p>

<h3>Editing the Service Container (eg Tomcat) configuration files</h3>
<p>You can also edit the service container's configuration file server.xml to
define system environment variables for individual contexts.

<h2>Multiple PALs</h2>
<p>When installing several PALs to connect to several databases from one server,
you can add many 'contexts' that actually point to one single webapp.
This means you can have many contexts with
their own configuration files, with one webapp to update, and updates to that
webapp will not disturb any changes you have made to the contexts.
</p>
<p>
For example, in Tomcat add this to your server container's server.xml:
<pre>
    &lt;Context path="/pal-6df"
             docBase="/usr/bin/tomcat/webapps/pal-SNAPSHOT"
               debug="9" reloadable="true"&gt;

       &lt;Environment name="org.astrogrid.config.filename"
                      value="/usr/bin/tomcat/conf/pal-6df.properties"
                        type="java.lang.String" /&gt;
        &lt;/Context&gt;
</pre>
</p>
<p>This creates a context 'pal-6df' that actually forwards all requests to the
'pal-SNAPSHOT' webapp but with the environment variables set as given.  You can
then edit the pal-6df context's environment variables with the user-friendly Admin
application and they will not be overwritten when you update 'pal-SNAPSHOT'.
<p>
<i>(Is this explanation sufficient?)</i>

<h1>Configuring to publish your data</h1>
<p>See also:
<ul>
<li> <a href="configureRdbms.jsp">Connecting to a Database</a>

<li> <a href="configureWarehouse.jsp">Creating a Warehouse</a>

<li> <a href="configureFits.jsp">Publishing FITS files</a>

<li> <a href="querierPlugins.jsp">Writing your own data plugins</a>

<li> <a href="resourcePlugins.jsp">Writing your own Registry-resource plugins</a>
</ul>


<h1>References<h1>
<ul>
<li><a href="http://www.astrogrid.org">Astrogrid Homepage</a>
</li></ul>



</body>
</html>

