<%@ page
   import="org.astrogrid.dataservice.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>PAL Registry-Resource Plugins</title>
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
The datacenter Registry metadata is a 'VoDescription' document consisting of a set
of 'VOResource' elements.  These elements are usually assembled through a set of 'resource
plugins' supplied with PAL, however you can write your own too.
<p>
<h1>Configuration</h1>
<p>Configure the datacenter to use the desired plugins by setting the key <tt>datacenter.resource.plugin.*</tt>
to the relevent classes.  For example:
</p>
<code>
datacenter.resource.plugin.1=org.astrogrid.datacenter.metadata.AuthorityConfigPlugin
datacenter.resource.plugin.2=org.astrogrid.datacenter.metadata.FileResourcePlugin
datacenter.resource.plugin.3=org.astrogrid.datacenter.metadata.CeaResourcePlugin
</code>
<p><i>As a minimum</i>, you will need to supply the CEA Resources and an RdbmsMetadata
(or Queryable?) resource type, for the datacenter to work with other AstroGrid/VO components.
These can either be provided through handwritten documents loaded through, eg,
<tt>FileResourcePlugin</tt>, or by generating them from, eg <tt>CeaResourcePlugin</tt>.

<h1>Writing your own</h1>
Write a class to implement the <tt>org.astrogrid.datacenter.metadata.VoResourcePlugin</tt>
interface.  This should return an array of strings, each one a <tt>&lt;Resource&gt;</tt> element.
Configure your datacenter to use it by adding it to the list of <tt>datacenter.resource.plugin</tt>
keys in the configuration, as given above.

<h1>Standard Plugins</h1>

<h2>FileResourcePlugin</h2>
<p>Reads a set of resource documents in files, given in the configuration by
the key <tt>datacenter.resource.filename.*</tt>, eg:</p>

<code>
datacenter.resource.filename.1=/disk1/webapps/pal-ssa.metadata.xml
</code>

<h2>UrlResourcePlugin</h2>
<p>Reads a set of resource documents at URLs, given in the configuration by
the key <tt>datacenter.resource.url.*</tt>, eg: </p>

<code>
datacenter.resource.url.1=http://astrogrid.roe.ac.uk/pal-ssa/GetMetadata
</code>

<h2>CeaResourcePlugin</h2>
<h2>RdbmsResourcePlugin</h2>

Don't use this...

<h2>AuthorityConfigPlugin</h2>
Loads the authority resource from various configuration file settings.  Needs one
per organisation so not really a datacenter thing...
<div>
</body>
</html>
