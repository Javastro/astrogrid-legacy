<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

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
<%@ include file="../navigation.xml" %>

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
    <a href="http://java.sun.com/products/jdbc/">JDBC</a> driver.
</p>

<p>
      The <b>Supported configurations</b> section at the bottom of this
      page shows the RDBMS, JDBC driver and embedded stylesheet
      combinations against which the DSA/Catalog has been known to operate 
      succesfully (with any caveats listed).
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

   <p>For connecting the DSA/catalog installation to the RDBMS
   via JDBC, sample connection settings are given in the 
   <tt>default.properties</tt> file in your installed webapp;  see 
   <a href="./configure_step4.jsp">the next configuration step</a> 
   for more about configuring your webapp properties.</p>

   <p>Note that <strong>this DSA/catalog release</strong> can only 
   wrap <strong>one single database</strong> in your RDBMS, and your 
   JDBC connection URL should provide access directly to that one database.
   See the <tt>default.properties</tt> for examples
   of JDBC access URLs, and/or consult your database administrator.</p> 

<h3>Known issues</h3>

  <ul><li><strong>PostgreSQL JDBC drivers</strong> with versions less than 8.0 
  may fail on queries returning large resultsets (because the webapp runs out 
  of memory).  If using these drivers, we recommend setting tomcat to run with
  plenty of memory, and limiting the maximum number of rows a query can return
  (the <tt>datacenter.max.return</tt> property, see <a href="./configure_step4.jsp">the next configuration step</a> for information about setting properties).
  </li>
  </ul>

<h3>Supported configurations</h3>
      <p>
      The table below shows the RDBMS, JDBC driver and embedded stylesheet
      combinations against which the DSA/Catalog has been known to operate 
      succesfully (with any caveats listed).
      </p>

      <table border="1" cellpadding="2" cellspacing="2">
        <tr>
          <td><strong>RDBMS</strong></td>
          <td><strong>JDBC driver</strong></td>
          <td><strong>XSLT stylesheet</strong></td>
          <td><strong>Comments</strong></td>
        </tr>
        <tr>
          <td><a href="http://hsqldb.sourceforge.net/">HSQLDB</a> 1.8.0</td>
          <td>hsqldb-1.8.0.jar</td>
          <td>HSQLDB-1.8.0.xsl</td>
          <td><font color="green">Works.</font></td>
        </tr>
        <tr>
          <td><a href="http://www.mysql.com/">MySQL</a> 4.1.16</td>
          <td>mysql-connector-java-3.1.12-bin.jar</td>
          <td>MYSQL-4.1.16.xsl</td>
          <td><font color="green">Works.</font></td>
        </tr>
        <tr>
          <td><a href="http://www.postgresql.org/">PostgreSQL</a> 7.2.3</td>
          <td>pg72jdbc2.jar</td>
          <td>POSTGRES-7.2.3.xsl</td>
          <td><font color="orange">Works, but streaming data transfers from 
              RDBMS don't work (so setting low return row limit 
              recommended to avoid DSA running out of memory).</font></td>
        </tr>
        <tr>
          <td><a href="http://www.postgresql.org/">PostgreSQL</a> 8.1.4</td>
          <td>postgresql-8.1-407.jdbc3.jar</td>
          <td>POSTGRES-7.2.3.xsl</td>
          <td><font color="green">Works.</font></td>
        </tr>
        <tr>
          <td><a href="http://www.microsoft.com/sql/">Microsoft SQL Server</a> 8.00</td>
          <td>mssqlserver.jar, msbase.jar, msutil.jar
            (unsure of provenance of these, presume from appropriate distro)</td>
          <td>SQLSERVER-8.00.xsl</td>
          <td><font color="green">Works.</font></td>
        </tr>
        <tr>
          <td><a href="http://www.microsoft.com/sql/">Microsoft SQL Server</a> 2000</td>
          <td>mssqlserver.jar, msbase.jar, msutil.jar
            (from SQL Server 2000 distro.)</td>
          <td>SQLSERVER-8.00.xsl</td>
          <td><font color="green">Works.</font></td>
        </tr>
        <tr>
          <td><a href="http://www.sybase.com">Sybase</a> ASR 15.0</td>
          <td>jconnect55.jar</td>
          <td>SYBASE-ASE-15.0.xsl</td>
          <td><font color="red">Doesn't work</font> (JDBC driver too old)</td>
        </tr>
        <tr>
          <td><a href="http://www.sybase.com">Sybase</a> ASR 15.0</td>
          <td>jconnect60-exclude.jar</td>
          <td>SYBASE-ASE-15.0.xsl</td>
          <td><font color="orange">Works, but TRUNCATE() 
            function not supported.</font></td>
        </tr>
        <tr>
          <td><a href="http://www.sybase.com">Sybase</a> ASR 15.0</td>
          <td>jconn3.jar</td>
          <td>SYBASE-ASE-15.0.xsl</td>
          <td><font color="orange">Works, but TRUNCATE() 
              function not supported.</font></td>
        </tr>
        <!--
        <tr>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
        </tr>
        -->
      </table>

      <p>
        We are always grateful to hear of other combinations of RDBMS,
        driver and XSLT stylesheet that have been shown to work / not work.
        We also welcome community contributions of stylesheets for other 
        RDBMS systems.
     </p>
<h3>References</h3>
<ul>
<li><a href="http://java.sun.com/products/jdbc/">JDBC</a></li>
<li>Sun's database of <a href="http://developers.sun.com/product/jdbc/drivers">JDBC drivers and where to get them</a></li>
<li><a href="http://tomcat.apache.org/tomcat-5.0-doc/">Tomcat 5 documentation</a></li>
</ul>

</body>
</html>
