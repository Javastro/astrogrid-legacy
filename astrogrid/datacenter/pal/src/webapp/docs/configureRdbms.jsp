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
   Many RDBMS have a user account system, where different accounts may be provided with different acces privileges to the database.
    If this is the case with your target RDBMS, we recommend that a new user account be created for the datacenter service.
   <p />
   The datacenter account should only have read / query privileges granted. <b>Do not</b> grant permissions to write to
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
You can get an initial template of your database by clicking <a href='../admin/getRdbmsMetadata.jsp'>here</a>.
You will need to fill in the UCD and Unit elements at a minimum, but obviously it will help if you can add human-
readable descriptions too.
<p>
Save this file on disk, and then configure your datacenter to serve that as metadata
using the FileResourcePlugin by setting the following configuration values:

<code>
datacenter.resource.plugin.1=org.astrogrid.datacenter.metadata.FileResourcePlugin
datacenter.resource.filename.1={location of your saved file}
datacenter.resource.plugin.2=org.astrogrid.datacenter.metadata.CeaResourcePlugin
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

