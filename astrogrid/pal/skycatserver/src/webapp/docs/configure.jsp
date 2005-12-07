<%@ page
   import="org.astrogrid.dataservice.service.DataServer"
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
set the environment variable 'org.astrogrid.config.filename' to the appropriate path and
filename, or the environment variable 'org.astrogrid.config.url' to the appropriate URL.
  You can use environment variables in the variable value. For example, using <code>${catalina.home}/conf/datacenter.properties</code>
will cause the datacenter to look in tomcat's configuration directory if you are running tomcat.</p>
<p>If you have several datacenters it will be necessary to do have a separate configuration file
for each datacenter, and you will need to set the
environment variable under each context to a different properties file.</p>
<p>You will need to restart the application after any change to a configuration file.</p>

<h3>Using Environment Variables</h3>
<p>You can set system environment variables from the command line.  For example, you
can use the command <code>setenv org.astrogrid.config.filename=/disk1/webapp/pal.properties</code> under linux.
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

<h1>Connecting to an RDBMS</h1>

<p>Many datasets are stored in Relational Database Management Systems (RDBMSs) such as SQL
Server, Postgres, etc.  PAL includes mechanisms to connect to any RDBMS that provides a
    <a href="http://java.sun.com/products/jdbc/">JDBC</a> driver; it has been tested with the following
   <ul>
      <li><a href="http://www.mysql.com/">MySQL</a> - Open Source SQL database </li>
      <li><a href="http://www.microsoft.com/sql/">SQL Server</a> - Microsoft SQL Server</li>
      <li><a href="http://hsqldb.sourceforge.net/">HSQL</a> - pure-Java
         RDBMS. For testing purposes or serving small databases based on
         textfiles. </li>
   </ul>
   </p>

<h2>Preparing your database</h2>
  <p>
   Many RDBMS have a user Principal system, where different Principals may be provided with different acces privileges to the database.
    If this is the case with your target RDBMS, we recommend that a new user Principal be created for the datacenter service.
   <p />
   The datacenter Principal should only have read / query privileges granted. <b>Do not</b> grant permissions to write to
   tables / create tables / delete tables, or to access other databases or tables than those that are to be published -
   these abilities are not needed and present a security risk.
   <p />
   You may also wish to consider making a duplicate of the original data collection database, or even running a separate
   RDBMS server on a different machine specifically for access from web-services.
   </p>

<h2>Jdbc Plugins</h2>

   <p>You now need to configure PAL to use the JDBC 'plugin', and to handle
   the right 'flavour' of SQL.</p>
   <p>The 'standard' SQL plugin is JdbcPlugin, and using this will work with most
   ODBC compatible databases. Some however take a particular flavour of SQL, and so
   you may want to use a different translator.  See the example default file
   for the keys to set for this.</p>
   <p>If none of these pre-defined translators are suitable, it is possible to write a new translator and plug it in.
   However, this is beyond the scope of this document; refer to SqlMaker.java in the source java doc.</p>

<h2>Install JDBC Driver</h2>
   <p>
   Acquire the jar file containing the JDBC driver for your chosen database and copy it (along with any other jars it depends upon) into the
   <tt>${CATALINA_HOME}/common/lib</tt> directory.  These will be available from the people that provided your database.
   </p>

<h2>JDBC Connection</h2>
   <p>Your database must have an ODBC-compatible connection; this will allow PAL to connect
   to it using a special URL and probably a user id and password.  You will need to
   set up the user id and password on the database ODBC connection so that PAL can
   connect to it using the same ones each time.</p>
   <p>The connection between the datacenter server and database can either be specified as a direct connection (simpler)
   or as a server connection (more efficient and robust). We recommend the second method where possible.</p>
   
   <h3>Direct Connection</h3>
   <p>In this configuration the datacenter manages its own connections to the database.
   You add the above url and user and password values to the configuration
   file (see the example default in WEB-INF/classes/default.properties for
   which keys to use).</p>
   <p>You will also need some JDBC java drivers for the database; these are provided
   by the database vendors as 'jar' files, and will need to be included in the classpath
   (for example, in the common/lib directory of tomcat). You need to add these to the
   configuration file (again, see the example default).</p>
   
   <h3>Server Connection</h3>
   
   <p>Alternatively, you can set up a JNDI datasource using your web service, such
   as the Administrator Tool if you are using Tomcat.</p>
   
   
<h1>Registering</h1>
<p>In order to publish to the Virtual Observatory you need to Register your new service
with a VO-compliant Registry.  This involves 'describing' your data, in a set of XML documents,
so that other VO tools and applications can examine it to see what it contains, and so that they
know how to access the data.
<p>
First you will need to describe your data using Virtual Observatory terms and structures.
You can create a template document from the JDBC, on this page:

<a href='../admin/GenerateMetaDoc'><p><b>Table MetaDoc Generator</b></a></p>

You will need to fill in the UCD and Unit elements at a minimum, but obviously it will help if you can add human-
readable descriptions too.
<p>
Save this file on disk, and then configure this dataservice to find it using the following configuration values:

<code>
datacenter.metadoc.url=(location of your saved file as a url)
</code>

You then need to configure the service to supply the appropriate resource documents, using for example the following:

</code>
datacenter.resource.plugin.1=org.astrogrid.tableserver.metadata.TabularDbResources
datacenter.resource.plugin.2=org.astrogrid.tableserver.metadata.TabularSkyServiceResources
</code>
<p>More details on resource plugins, and how to write your own,
can be found in the <a href='resourcePlugins.jsp'>Resource Plugins</a> documentation.
</p>

<h1>References</h1>
<ul>
<li><a href="http://www.astrogrid.org">Astrogrid Homepage</a>
</li><li><a href="http://java.sun.com/products/jdbc/">JDBC</a>
</li><li><a href="http://servlet.java.sun.com/products/jdbc/drivers">List of known JDBC drivers</a>
</li></ul>




</body>
</html>
