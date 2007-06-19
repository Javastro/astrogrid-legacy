<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid DSA/catalog Documentation</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<!--
1 RELEASE document per component, to include:
Functionality supported
Known bugs and ommissions, with workarounds where possible
How to report bugs (BugZilla, content etc)
-->
<h1>Astrogrid DSA/catalog v1.0</h1>

<h2>Functionality Supported</h2>
<ul>
<li>Input queries in ADQL 0.7.4 and ADQL 1.0 (excluding Region searches and XMATCH).</li>
<li>Standard AstroGrid CEA web service interface</li>
<li>Standard optional IVOA conesearch interface</li>
<li>Standardised, tested JDBC access to at least the following RDBMSs:
  <ul>
    <li>HSQLDB (v. 1.8.0)</li>
    <li>PostgreSQL (v. 8.1.4)</li>
    <li>MySQL (v. 4.1.16)</li>
    <li>Microsoft SQLServer (v. 8.00)</li>
    <li>Sybase ASR (v.15.08.00)</li>
  </ul>
</li>
<li>Log views</li>
<li>Registry metadata views</li>
<li>Server status views</li>
<li>Pluggable architecture for back-end data access</li>
<li>Pluggable architecture for IVOA VOResource generation</li>
</ul>
</p>

<h2>Functionality Not Currently Supported</h2>
<ul>
<li>ADQL Region queries, ADQL Crossmatch </li>
<li>Other service interfaces such as IVOA SkyNode</li>
</ul>

<h2>Migration notes</h2>
<p>
The new DSA/Catalog service has a number of significant changes from the
old skycatserver service, in particular with respect to the plugins and
translation mechanisms used for basic JDBC access.
</p>

<p>
If you are migrating an old skycatserver installation to the new DSA/catalog,
we suggest customising the new default.properties file supplied with the 
DSA/catalog service (by copying in relevant properties from your old 
configuration), rather than attempting to update your old skycatserver 
configuration file.  (DSA/catalog has some additional properties, and some
skycatserver properties are now redundant).
</p>

<p>
Additionally, we advise keeping a backup of your original skycatserver 
installation and any configuration files associated with it, in case you 
should encounter any problems running the DSA/catalog service against 
your RDBMS and/or data.
</p>

<p>
Please contact 
<a href="mailto:astrogrid_dsa@star.le.ac.uk">astrogrid_dsa@star.le.ac.uk</a>
for advice if you find that you cannot migrate an existing skycatserver to 
the new DSA/catalog service.
</p>

<h2>New properties</h2>
<p>
This is an ongoing list of new properties added to the DSA/catalog
configuration, which will not be present in your properties file if you
are upgrading from an older version.
</p>
<p>
Further information about the new properties file can be found in the
<tt>default.properties</tt> file that accompanies each new release;  see
the <a href="./docs/configure_step4.jsp">configuration documentation</a> 
for more details.
</p>

<ul>
<li><strong>Release 2006.3.03pl</strong>
   <ul><li><tt>datacenter.ucd.version</tt><br/>Which UCD standard
   to use when emitting UCD tags in output VOTable (defaults to UCD 1+;
   UCD 1 also supported but deprecated)</li></ul>
</li>
</ul>

<h2>Known bugs</h2>

<p>
If you have found a suspected bug, check the bugzilla pages at 
<a href="http://www.astrogrid.org/bugzilla">http://www.astrogrid.org/bugzilla<a/> for known bugs, for example 
<a href="http://www.astrogrid.org/bugzilla/buglist.cgi?short_desc_type=allwordssubstr&short_desc=&product=DSA%2FCatalog&component=DSA%2Fcatalog+webservice&version=unspecified&long_desc_type=allwordssubstr&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED&emailassigned_to1=1&emailtype1=substring&email1=&emailassigned_to2=1&emailreporter2=1&emailcc2=1&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&changedin=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&newqueryname=&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=">using this query</a>, or
consult <a href="mailto:astrogrid_dsa@star.le.ac.uk">astrogrid_dsa@star.le.ac.uk</a>.
</p>

<p>
You can submit new bugs to the
<a href="http://www.astrogrid.org/bugzilla/enter_bug.cgi">bugzilla entry page</a>.
Before filing a bug, we suggest you run the installation tests to verify that your system is correctly
configured.
</p>

<p>
Please include any error output or logs in the bug report, along with the server endpoint URL and version of the
DSA/catalog server you have installed.
</p>

</div>
</body>
</document>
