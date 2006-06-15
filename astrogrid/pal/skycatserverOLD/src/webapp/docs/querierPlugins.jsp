<%@ page
   import="org.astrogrid.dataservice.service.DataServer"
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

<h1>Introduction</h1>
<p>
The datacenter server is designed so that different back-end implementations can be plugged into the system.
This 'coarse-level' plugin has a very simple interface and requires the plugin to handle all aspects of an
individual query.
<p>Several plugins for common datasets (such as RDBMS, or FITS file collections) are provided with PAL.
Some, such as JdbcPlugin, allow you to create 'finer' plugins that
handle just a small aspect of the query, such as creating specialised SQL strings for non-standard databases.
</p>
<h1>Requirements</h1>
<p>
A back-end implementation must implement the interface <tt>org.astrogrid.datacenter.queriers.QuerierPlugin</tt>,
or extend an implemting class such as <tt>org.astrogrid.datacenter.queriers.DefaultPlugin</tt>. The compiled
class, and all other libraries it depends upon must be available on the web-application's classpath.
<p>
The configuration key <tt>datacenter.querier.plugin</tt> must be set to the new class, for example:

<code>
datacenter.querier.plugin=org.astrogrid.datacenter.queriers.test.SampleStarsPlugin
</code>

</p>

<h1>Development</h1>
<p>
It is best to refer directly to the package documentation.  The classes of interest are in the packages:
<tt>org.astrogrid.datacenter.queriers</tt>
<p />

<h1>Examples</h1>
<h2>Writing a querier for another JDBC database</h2>
<p>The standard JDBC database querier <tt>org.astrogrid.datacenter.queriers.sql.JdbcPlugin</tt>
should be suitable for most database flavours,
given an appropriate JDBC driver. Situations where it is not applicable split into:

<p><b>The generated SQL is not compatible with my database:</b>
Extend the <tt>org.astrogrid.datacenter.queriers.sql.SqlMaker</tt> to provide new translation rules for the offending
ADQL clauses. The query translator to use can be specified in the system configuration file.</p>

<p><b>My database requires some special initialization:</b>
 Subclass the standard JdbcQuerier to perform the initialization.

</p>

<h2>Adapter/Proxy Servers</h2>
<p>Another possible form of back end is one that does not retrieve data from a local store, but instead acts as an adapter for an existing
datacenter - possibly another web service. An adapter server can integrate a legacy web service into Astrogrid without requiring changes to
the existing service. Its task is to translate the ADQL queries it receives into a form understood by the legacy service, perform the call to the service
and then massage any results into the format expected by the VO client.

<p />
See the cds-delegate example back-end for an example of an adapter server.

</p>

<h2>XMLDatabases</h2><p>
Shouldn't be too hard. Would need to transform the ADQL into whatever query language the XMLDB uses - maybe XQuery, and then transform
results of query again into a VOTABLE for output.
</p>

<h2>Anything else?</h2>
<p>
We'd be interested to hear of your experiences integrating other back-end systems into PAL. We'd also appreciate code donations, to
build up a larger library of back-end implementations.
</p>
</div>

</body>
</html>
