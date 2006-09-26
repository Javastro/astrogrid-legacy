<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>DSA/catalog Documentation</title>
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

<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td align="left" width="33%">
<h4><a href="./configure_step2.jsp">&nbsp;&nbsp;&lt;&lt; Previous step</a></h4>
</td>
<td align="center" width="33%">
<h4><a href="../configure.jsp">^Index^</a></h4>
</td>
<td align="right" width="33%">
<h4><a href="./configure_step4.jsp">Next step &gt;&gt;&nbsp;&nbsp;</a></h4>
</td>
</tr>
</table>

<h2>Step 3: Prepare your RDBMS connection</h2>

<p>Many datasets are stored in 
Relational Database Management Systems (RDBMSs) such as SQL
Server, Postgres, etc.  DSA/catalog includes mechanisms to connect to any 
RDBMS that provides a
    <a href="http://java.sun.com/products/jdbc/">JDBC</a> driver; it has been tested with the following systems:
   <ul>
      <li><a href="http://www.mysql.com/">MySQL</a> - Open Source SQL database (version 4.1.16)</li>
      <li><a href="http://www.postgresql.org/">PostgreSQL</a> - Open Source SQL database (version 7.2.3, version 8.1.4)</li>
      <li><a href="http://www.microsoft.com/sql/">SQL Server</a> - Microsoft SQL Server (version 8.00) </li>
      <li><a href="http://hsqldb.sourceforge.net/">HSQLDB</a> - pure-Java
         RDBMS (version 1.8.0). For testing purposes or serving small 
         databases based on textfiles. </li>
   </ul>
   </p>


<h3>Preparing your database</h3>
  <p>
   Many RDBMS have a user Principal system, where different Principals may be provided with different acces privileges to the database.
    If this is the case with your target RDBMS, we recommend that a new user Principal be created for the datacenter service.
   </p>
<p>
   The datacenter Principal should only have read / query privileges granted. <b>Do not</b> grant permissions to write to
   tables / create tables / delete tables, or to access other databases or tables than those that are to be published -
   these abilities are not needed and present a security risk.
   </p>

<p>
   You may also wish to consider making a duplicate of the original data collection database, or even running a separate
   RDBMS server on a different machine specifically for access from web-services.
   </p>

<h3>Installing the JDBC driver</h3>
   <p>
   Acquire the jar file(s) containing the JDBC driver for your chosen database;
  this should be available from your database supplier.  Copy the jar file(s) 
   into the <tt>$TOMCAT_HOME/common/lib</tt> directory</tt> of your tomcat 
  installation (or, alternatively, into the <tt>WEB-INF/lib</tt> directory 
 of your installed DSA/catalog webapp). 
  </p>

  <p>
  <strong>Now restart tomcat</strong> so that
  new jar files are added to your webapp's classpath.
   </p>

<h3>Configuring the JDBC connection</h3>
   <p>Your database must have an ODBC-compatible connection; this will allow 
   the DSA/catalog webapp to connect
   to it using a special URL and (probably) a user ID and password.  
   </p>
   
   <p>
   You will need to
   set up a lasting user ID and password on the database ODBC connection 
   so that DSA/catalog can connect to it using these settings.</p>

   <p>The connection between the DSA/catalog installation and the database can 
either be specified as a direct connection (simpler) or as a server connection 
(more efficient and robust).</p>
   
   <h4>Direct Connection</h4>
   <p>In this configuration, the DSA/catalog installation manages its own 
   connections to the database.
   </p>
   <p>Sample direct-connection settings are given in the 
   <tt>default.properties</tt> file in your installed webapp;  see 
   <a href="./configure_step4.jsp">the next configuration step</a> 
   for more about configuring your webapp properties.</p>

   <h4>Server Connection</h4>
   
   <p>In this configuration, the DSA/catalog installation uses tomcat's
   JNDI Resources mechanism to connect to the database.
   </p>

   <p> 
   The DSA/catalog 
   installation looks for a JNDI datasource named <tt>dsa-datasource</tt>
   <font color="red">(note that in some previous installations this used to
       be called <tt>pal-datasource</tt>, so you may need to check your
      JNDI settings).</font>
   If it finds one, it will use it in preference to any direct connection 
   settings in its configuration file.
   </p>

   <p>To use a server connection, you will therefore need to create a 
   JNDI datasource named <tt>dsa-datasource</tt> within your DSA/catalog
   webapp's environment.  Please consult the following tomcat documentation 
   for more information about setting up JNDI datasources:
   </p>
   <ul>
   <li><a href="http://tomcat.apache.org/tomcat-5.0-doc/jndi-resources-howto.html">The Apache Jakarta Tomcat 5 Servlet/JSP Container JNDI Resources HOW-TO</a></li>
   </ul>
   

<h3>Known issues</h3>

  <ul><li><strong>PostgreSQL JDBC drivers</strong> with versions less than 8.0 
  may fail on queries returning large resultsets (because the webapp runs out 
  of memory).  If using these drivers, we recommend setting tomcat to run with
  plenty of memory, and limiting the maximum number of rows a query can return
  (the <tt>datacenter.max.return</tt> property, see <a href="./configure_step4.jsp">the next configuration step</a> for information about setting properties).
  </li>
  </ul>

<h3>References</h3>
<ul>
<li><a href="http://java.sun.com/products/jdbc/">JDBC</a></li>
<li><a href="http://servlet.java.sun.com/products/jdbc/drivers">List of known JDBC drivers</a></li>
<li><a href="http://tomcat.apache.org/tomcat-5.0-doc/">Tomcat 5 documentation</a></li>
</ul>

</body>
</html>
