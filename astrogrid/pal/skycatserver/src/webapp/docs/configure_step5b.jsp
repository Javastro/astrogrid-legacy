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
<h4><a href="./configure_step5.jsp">&nbsp;&nbsp;&lt;&lt; Previous step</a></h4>
</td>
<td align="center" width="33%">
<h4><a href="../configure.jsp">^Index^</a></h4>
</td>
<td align="right" width="33%">
<h4><a href="./configure_step6.jsp">Next step &gt;&gt;&nbsp;&nbsp;</a></h4>
</td>
</tr>
</table>


<h2>Step 5b: Customise your skeleton metadoc file</h2>
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
The <strong>most important</strong> changes you will need to make are as follows:
</p>
<ol>
<li>Setting the ID attribute and Name tag for the Catalog element 
to match, respectively, the <strong>system name</strong> and 
<strong>publication name</strong> of your database, and Name tag <em>(see comments below re system and publication names.)</em></li>

<li>Adding human-readable descriptions between the &lt;Description&gt; tag pairs</li>

<li>Adding Unified Content Descriptors (<a href="http://cdsweb.u-strasbg.fr/doc/UCD/inVizieR.htx">UCDs</a>) where possible (leave the &lt;UCD&gt; tags present but empty if a UCD is not defined for this kind of column).  
The IVOA provides information about 
<a href="http://cdsweb.u-strasbg.fr/UCD/old/">the UCD1 controlled vocabulary</a>
and 
<a href="the UCD1+ controlled vocabulary">http://www.ivoa.net/Documents/latest/UCDlist.html</a> online.
(NB: Please supply both UCD v.1 and UCD v.1+ tags, as not all VO 
tooling has yet migrated to UCD v1+.)
</li>

<li>Enabling conesearch for the relevant table(s) in your dataset, if 
required, using the &lt;ConeSettings&gt; tag.</li>
</ol>

<p>
<strong>If you are a sysadmin, you may need an astronomer to help with
this step</strong>;  producing useful metadoc requires familiarity with
astronomical rather than technical features of the data.
</p>

<p>
Note also that the DSA metadoc system differentiates between 
<strong>system names</strong> and <strong>publication names</strong> for 
catalogs (databases), tables and columns.  The <strong>system name</strong>
must match the actual name of your database, table or column in the RDBMS,
and "ID" attributes in the metadoc require system names.  The 
<strong>publication name</strong>, used in registering your resource, can
be different from the system name; the &lt;Name&gt; tags in the 
metadoc specify publication names.  By default, the publication name is the 
same as the system name and this is perfectly acceptable.
</p>

<h3>Brief example</h3>
<p>
For example, you might get the following column entry from the skeleton metadoc generator:
</p>
<div class="boxedCode">
<pre>
&lt;Column ID='POS_EQ_RA' indexed='false'&gt;
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
which you could customise to be more informative:
</p>
<div class="boxedCode">
<pre>
&lt;Column ID="POS_EQ_RA" indexed="true"&gt;
    &lt;Name&gt;RA&lt;Name&gt;
    &lt;Datatype&gt;float&lt;Datatype&gt;
    &lt;Description&gt;Right ascension&lt;Description&gt;
    &lt;Units&gt;deg&lt;Units&gt;
    &lt;UCD version="1"&gt;POS_EQ_RA_MAIN&lt;UCD&gt;
    &lt;UCD version="1+"&gt;pos.eq.ra;meta.main&lt;UCD&gt;
    &lt;SkyPolarCoord&gt;RA&lt;SkyPolarPos&gt;
&lt;Column&gt;
</div>

(see the detailed metadoc analysis below for more explanation).

<h3>Sample metadoc files </h3>
<p>
For reference and assistance, here are some example metadoc files
that have been prepared for existing datasets:
</p>

<ul>
<li><h4><a href="first-dsa-metadoc.xml">XML metadoc</a> (or <a href="first-dsa-metadoc.txt">plaintext version</a>) for the <a href="http://sundog.stsci.edu/">FIRST</a> survey at the VLA</h4></li>
<li><h4><a href="int-wfs-obs-metadoc.xml">XML metadoc</a> (or <a href="int-wfs-obs-metadoc.txt">plaintext version</a>) for <a href="http://www.ast.cam.ac.uk/~wfcsur/">INT WFS Observation Catalogue</a></h4></li>
<li><h4><a href="int-wfs-object-metadoc.xml">XML metadoc</a> (or <a href="int-wfs-object-metadoc.txt">plaintext version</a>) for <a href="http://www.ast.cam.ac.uk/~wfcsur/">INT WFS Merged Object Catalogue</a></h4></li>
</ul>


<h3>Detailed metadoc analysis</h3>
<p>
The box below shows which elements of the skeleton metadoc WILL, MAY and WON'T need changing, and the changes required.
The key to the colour coding is as follows:
<blockquote>
<div class="redback"><strong>Red: </strong></span>WILL need to edit this</div/>
<div class="yellowback"><strong>Yellow: </strong></span>MAY need to edit this</div/>
<div class="greenback"><strong>Green: </strong></span>WON'T need to edit this</div/>
</blockquote>
</p>

<div class="boxedCode">
<tt>
<%@ include file="./annmeta.xml" %>
</tt>
</div>


</body>
</html>



