<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = "."; // For the navigation include %>

<html>
<head>
<title>AstroGrid DSA/catalog Documentation</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
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
<h1>Astrogrid DSA/catalog release notes</h1>

<h2>Functionality Supported</h2>
<ul>
<li>Input queries in ADQL 0.7.4 and ADQL 1.0 (excluding Region searches and XMATCH).</li>
<li>Standard AstroGrid CEA web service interface, supporting multiple interfaces (currently ADQL querying, asynchronous "conesearch" and simple "multicone" crossmatch)</li>
<li>Standard optional IVOA conesearch interface</li>
<li>Standardised, tested JDBC access to at least the RDBMSs listed
<a href="docs/configure_step3.jsp">on this page</a>.
</li>
<li>Log views</li>
<li>Registry metadata views</li>
<li>Server status views</li>
<li>Pluggable architecture for back-end data access</li>
<li>Pluggable architecture for IVOA VOResource generation</li>
</ul>

<h2>Functionality Not Currently Supported</h2>
<ul>
<li>ADQL Region queries, advanced joins etc</li>
<li>Other service interfaces such as IVOA SkyNode</li>
</ul>

<h2>Upgrading notes</h2>
<p>
If you are upgrading a very old installation,
we suggest customising the new default.properties file supplied with the 
DSA/catalog service (by copying in relevant properties from your old 
configuration), rather than attempting to update your old 
configuration file.  For updates to newer services, the self-tests 
should warn you if additional properties need to be added.
</p>

<p>
Additionally, we advise keeping a backup of your original DSA/catalog 
installation and any configuration files associated with it, in case you 
should encounter any problems running a newer DSA/catalog service against 
your RDBMS and/or data.
</p>

<p>
Please contact 
<a href="mailto:astrogrid_dsa@star.le.ac.uk">astrogrid_dsa@star.le.ac.uk</a>
for advice if you find that you cannot upgrade an old installation to 
the newest DSA/catalog service.
</p>

<h2>New properties</h2>
<p>
New properties are regularly added to the DSA/catalog
configuration, which will not be present in your properties file if you
are upgrading from an older version.  Additionally, sometimes old 
properties are deprecated.
</p>
<p>
The installation self-tests should warn you of any missing or redundant
properties in your configuration file.
Further information about any new properties can be found in the
<tt>default.properties</tt> file that accompanies each new release;  see
the <a href="./docs/configure_step4.jsp">configuration documentation</a> 
for more details.
</p>

<h2>Bug reporting</h2>

<p>
If you have found a suspected bug, check the bugzilla pages at 
<a href="http://www.astrogrid.org/bugzilla">http://www.astrogrid.org/bugzilla<a/>
for known bugs, for example using
<a href="http://www.astrogrid.org/bugzilla/buglist.cgi?short_desc_type=allwordssubstr&short_desc=&product=DSA%2FCatalog&component=DSA%2Fcatalog+webservice&version=unspecified&long_desc_type=allwordssubstr&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&bug_status=NEW&bug_status=ASSIGNED&bug_status=REOPENED&emailassigned_to1=1&emailtype1=substring&email1=&emailassigned_to2=1&emailreporter2=1&emailcc2=1&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&changedin=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&newqueryname=&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=">this query</a>,
or consult <a href="mailto:astrogrid_dsa@star.le.ac.uk">astrogrid_dsa@star.le.ac.uk</a>.
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

<h2>Change log</h2>
<pre>
<%@ include file="docs/changelog.txt" %>
</pre>


</div>
</body>
</document>
