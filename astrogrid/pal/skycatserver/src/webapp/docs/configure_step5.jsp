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
<h4><a href="./configure_step4.jsp">&nbsp;&nbsp;&lt;&lt; Previous step</a></h4>
</td>
<td align="center" width="33%">
<h4><a href="../configure.jsp">^Index^</a></h4>
</td>
<td align="right" width="33%">
<h4><a href="./configure_step6.jsp">Next step &gt;&gt;&nbsp;&nbsp;</a></h4>
</td>
</tr>
</table>


<h2>Step 5: Prepare your metadoc file</h2>
<p>
Once you have <a href="./configure_step4.jsp"</a>customised your configuration</a>, you need to prepare a "metadoc file" to describe your data.
</p>

<p>In order to publish to the Virtual Observatory you need to register your 
new service with a VO-compliant Registry.  This involves 'describing' your 
data, in a set of XML documents,
so that other VO tools and applications can examine it to see what it contains,
and so that they
know how to access the data.
</p>

<p>
An important part of this VO description is configured using a "metadoc" file,
  which describes the data being supplied by this DSA/catalog installation.
  This metadoc file is a bit like a database schema, but needs to contain
  additional information to help human astronomers and VO tooling make sense
  of your data.
  </p>


<h3>Generate a skeleton metadoc file</h3>
<p>
To assist you, this DSA/catalog installation can create a skeleton metadoc
file for you, using information extracted from your RDBMS:
</p>

<h4>&nbsp;&nbsp;&nbsp;<a href='../admin/GenerateMetaDoc'>Run the skeleton metadoc file generator &gt;&gt;</a></h4>

<p><strong>Note that this generator will only work if you have completed
<a href="./configure_step4.jsp">customising your configuration</a>, such that 
your DSA/catalog is now successfully configured to connect to your own RDBMS 
database.</strong>
</p>
<p>
If the generator fails, check your configuration settings.
The self-test pages may be of some help as well, although the self-tests
are NOT all expected to pass until you have produced a valid metadoc file.
</p>

<p>Also, note that your browser may mangle the XML produced by the metadoc
file generator when displaying it as a web page;  the safest way to extract 
the skeleton metadoc file is to view and save the page 
XML <strong>source</strong> (not the browser rendering of the XML source).
</p>

<p>
Once you have saved the skeleton metadoc file to a location of your choice
(preferably outside the DSA/catalog webapp), you should revisit your
configuration properties and set the <tt>datacenter.metadoc.file</tt> 
to point to it (see the notes in your properties file for more).
</p>
<p><strong>At this point, your DSA/catalog should be <a href="./configure_step6.jsp">passing all of its self-tests again</a>.</strong>
If it isn't, check your configuration file for misconfigured properties.
</p>

<h3>Customise your skeleton metadoc file</h3>
<p>
Now that you have created a skeleton metadoc file and configured your 
DSA/catalog installation to use it, the technical demands of customising
your configuration are complete;  your DSA/catalog installation is
self-consistent.
</p>

<p>
However, the skeleton metadoc file is not very informative;  it doesn't 
contain enough "metadata" to make your exported data tables very useful to 
astronomers accessing them via the Virtual Observatory.  It is therefore
necessary to manually edit the skeleton metadoc file to insert extra
metadata to further explain your data.
</p>

<p>
<ul>
<li>Adding Unified Content Descriptors (<a href="http://cdsweb.u-strasbg.fr/doc/UCD/inVizieR.htx">UCDs</a>) where possible (leave the &lt;UCD&gt; tags present but empty if a UCD is not defined for this kind of column).  
The IVOA provides information about 
<a href="http://cdsweb.u-strasbg.fr/UCD/old/">the UCD1 controlled vocabulary</a>
and 
<a href="the UCD1+ controlled vocabulary">http://www.ivoa.net/Documents/latest/UCDlist.html</a> online.
(NB: Please supply both UCD v.1 and UCD v.1+ tags, as not all VO 
tooling has yet migrated to UCD v1+.)
</li>

<li>Adding human-readable descriptions between the <Description> tag pairs</li>

<li>Adding descriptions of the coordinate system (where relevant, see example below)</li>

<li>Removing irrelevant elements (e.g. &lt;ErrorColumn&gt; tag pairs for 
columns with no associated error column)</li>
</ul>

For example, the skeleton metadoc might contain this:
</p>
<div class="boxedCode">
<pre>
&lt;Column ID='catalogue.POS_EQ_RA' indexed='false'&gt;
     &lt;Name&gt;POS_EQ_RA&lt;Name&gt;
     &lt;Datatype&gt;float&lt;Datatype&gt;
     &lt;Description&gt; &lt;Description&gt;
     &lt;Units&gt; &lt;Units&gt;
     &lt;DimEq&gt; &lt;DimEq&gt;
     &lt;Scale&gt; &lt;Scale&gt;
     &lt;UCD version='1'&gt; &lt;UCD&gt;
     &lt;UCD version='1+'&gt; &lt;UCD&gt;
     &lt;ErrorColumn&gt; &lt;ErrorColumn&gt;
&lt;Column&gt;
</pre>
</div>
<p>
which should be customised to be more informative:
</p>
<div class="boxedCode">
<pre>
&lt;Column ID="catalogue.POS_EQ_RA" indexed="true"&gt;
    &lt;Name&gt;POS_EQ_RA&lt;Name&gt;
    &lt;Datatype&gt;float&lt;Datatype&gt;
    &lt;Description&gt;Right ascension&lt;Description&gt;
    &lt;Units&gt;deg&lt;Units&gt;
    &lt;UCD version="1"&gt;POS_EQ_RA_MAIN&lt;UCD&gt;
    &lt;UCD version="1+"&gt;pos.eq.ra;meta.main&lt;UCD&gt;
    &lt;SkyPolarPos&gt;
       &lt;Coordinate&gt;RA&lt;Coordinate&gt;
       &lt;Equinox&gt;J2000&lt;Equinox&gt;
    &lt;SkyPolarPos&gt;
&lt;Column&gt;
</div>

<p>
<strong>If you are a sysadmin, you may need an astronomer to help with
this step</strong>;  producing useful metadoc requires familiarity with
astronomical rather than technical features of the data.
</p>

<h3>Metadoc examples</h3>
<p>
For reference and assistance, here are some example metadoc files
that have been prepared for existing astronomy datasets:
</p>

<ul>
<li><h4><a href="first-dsa-metadoc.xml">XML metadoc</a> (or <a href="first-dsa-metadoc.txt">plaintext version</a>) for the <a href="http://sundog.stsci.edu/">FIRST</a> survey at the VLA</h4></li>
<li><h4><a href="int-wfs-obs-metadoc.xml">XML metadoc</a> (or <a href="int-wfs-obs-metadoc.txt">plaintext version</a>) for <a href="http://www.ast.cam.ac.uk/~wfcsur/">INT WFS Observation Catalogue</a></h4></li>
<li><h4><a href="int-wfs-object-metadoc.xml">XML metadoc</a> (or <a href="int-wfs-object-metadoc.txt">plaintext version</a>) for <a href="http://www.ast.cam.ac.uk/~wfcsur/">INT WFS Merged Object Catalogue</a></h4></li>
</ul>

</body>
</html>



