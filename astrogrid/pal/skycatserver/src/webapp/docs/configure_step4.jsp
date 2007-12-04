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
<h4><a href="./configure_step3.jsp">&nbsp;&nbsp;&lt;&lt; Previous step</a></h4>
</td>
<td align="center" width="33%">
<h4><a href="../configure.jsp">^Index^</a></h4>
</td>
<td align="right" width="33%">
<h4><a href="./configure_step5.jsp">Next step &gt;&gt;&nbsp;&nbsp;</a></h4>
</td>
</tr>
</table>

<h2>Step 4: Customise your configuration</h2>
<p>Once you have reassured yourself that the installed DSA/catalog is 
<a href="./configure_step2.jsp">working properly in its default mode</a> 
and have <a href="./configure_step3.jsp">prepared your database 
connection</a>, you can customise your DSA/catalog configuration
to provide access to your own RDBMS and data.
</p>


<h3>Properties files</h3>
<p>The default DSA/catalog configuration settings are found in the following
file:
</p>
<pre>
$DSA_CATALOG_HOME/WEB-INF/classes/default.properties
</pre>
<p>
where <tt>$DSA_CATALOG_HOME</tt> is the toplevel directory of your 
DSA/catalog installation, to be found in the <tt>$TOMCAT_HOME/webapps</tt>
directory.
</p>

<p>
This "properties file" is an ordinary text file with a list of keys and 
values, like this:
<div class="boxedCode">
<pre>
   datacenter.name=SomeExample
   datacenter.url=http://somehost.ac.uk/dsa-catalog/
</pre>
</div>
Lines starting with the <tt>#</tt> or <tt>!</tt> characters
are treated as comments and are ignored by the application.
</p>

<p>
<strong>The <tt>default.properties</tt> file is extensively commented
and is intended to be self-documenting;</strong>  work through it carefully,
    following the instructions and examples within the file, to customise 
    your DSA/catalog configuration by one of the two methods below.
</p>

<div class="boxedCode">
<h3>IMPORTANT NOTE</h3>
<p>
There is one property you <strong>SHOULD NOT</strong> attempt to configure yet:
<pre>
datacenter.metadoc.file={/full/path/to/your.metadoc.xml}
</pre>
<p>
This property will be configured AFTER you have completed the next
step in the configuration process, 
<a href="./configure_step5.jsp">preparing your metadoc file</a>.
</p>
</div>

<h4>Simple reconfiguration (not recommended)</h4>
<p>
The simplest way to reconfigure your DSA/catalog installation is
to edit the <tt>default.properties</tt> file directly.
However, if you do this, we recommend 
<strong><font color="red">KEEPING A BACKUP COPY OF IT SOMEWHERE OUTSIDE OF THE DSA/catalog 
WEBAPP</font></strong>.  If you don't do this, your configuration information 
<strong><font color="red">WILL BE LOST</font></strong> if the webapp is undeployed or upgraded.
</p>

<p>Once you have finished, <strong>reload the DSA/catalog webapp, or 
restart tomcat,</strong> to reload the configuration for your DSA/catalog 
webapp.
</p>

<h4>Persistent reconfiguration (recommended)</h4>
<p>
Alternatively, we recommend taking a copy of the <tt>default.properties</tt>
file, renaming it to something more informative (e.g. "usnob-dsa.properties"),
and  putting it somewhere outside the installed webapp.  You can then
tell tomcat where to find it by creating an environment entry file for your 
installed DSA-catalog webapp in the $TOMCAT_HOME/conf/Catalina/localhost</tt>
directory, as follows:

<p>
Create a file:
</p>
<pre>
    $TOMCAT_HOME/conf/Catalina/localhost/$ASTROGRID_DSA.xml
</pre>
<p>
(where <tt>$ASTROGRID_DSA</tt> is the name of your installed webapp in 
 the $TOMCAT_HOME/webapps directory)
and put the following contents in it:
</p>

<div class="boxedCode">
<p>
<tt>
&lt;?xml version='1.0' encoding='utf-8'?&gt;<br/>
&lt;Context displayName="Astrogrid DSA/catalog" docBase="<font color="green">$TOMCAT_HOME</font>/webapps/</font><font color="blue">$ASTROGRID_DSA</font>" path="/<font color="blue">$ASTROGRID_DSA</font>" workDir="work/Catalina/localhost/<font color="blue">$ASTROGRID_DSA</font>"&gt;<br/>
<br/>
&lt;Environment description="Configuration filename" name="org.astrogrid.config.filename" override="false" type="java.lang.String" value="<font color="red">/path/to/properties/$PROPERTIES_FILENAME</font>"/&gt;<br/>
<br/>
&lt;/Context&gt;
</tt>
</p>
</div>

<p><strong>IMPORTANT:</strong> Change the <font color="blue">blue elements</font> to match the name
    of your installed webapp in the $TOMCAT_HOME/webapps directory, 
    the <font color="red">red element</font> to match the full path to your
    new properties file, and the <font color="green">green</font> element 
    to match the full path to your tomcat installation).
</p>

<p>Once you have finished, <strong>restart tomcat</strong> to reload the
configuration for your DSA/catalog webapp.
</p>

</body>
</html>
