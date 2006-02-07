<%@ page import="org.astrogrid.registry.server.query.*,
				     org.astrogrid.registry.server.*,
  				     org.astrogrid.registry.common.RegistryDOMHelper,
	  				  org.astrogrid.registry.server.http.servlets.helper.JSPHelper,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.config.SimpleConfig,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Element,                 
                 java.net.*,
                 java.util.*,
                 org.apache.commons.fileupload.*,                  
                 java.io.*"
    session="false" %>
<%
   ISearch server = JSPHelper.getQueryService(request);
            
%>
<html>
<head>
<title>XForms</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>
</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<h1>XForms for New Resource</h1>

<p>
   This page is for creating a brand new resource using XForms or alternative you can load a
   previously saved xml resource. <br />
</p>

<p>
Load a Resource xml document:<br />
<form enctype="multipart/form-data" method="post" action="XFormsProcessor.jsp">
<br />
Resource Type: 
<select name="mapType">
	<option value="default">Resource</option>
	<option value="Organisation">-Organisation</option>
	<option value="Authority">-Authority</option>
	<option value="DataCollection">-DataCollection</option>
	<option value="TabularDB">--TabularDB</option>
	<option value="Service">-Service</option>
	<option value="Registry">--Registry</option>
	<option value="SkyService">--SkyService</option>
	<option value="TabularSkyService">---TabularSkyService</option>
	<option value="ConeSearch">---ConeSearch</option>
	<option value="SimpleImageAccess">---SimpleImageAccess</option>
</select>

<input type="file" name="docfile" />
<input type="hidden" name="addFromFile" value="true" />
<input type="submit" name="uploadFromFile" value="upload" />
</form>
</p>

<p>
<br />
New Resources

<form method="post" action="XFormsProcessor.jsp">
<input type="hidden" name="keywordquery" value="true" />
<br />
Resource Type: 
<select name="mapType">
	<option value="default">Resource</option>
	<option value="Organisation">-Organisation</option>
	<option value="Authority">-Authority</option>
	<option value="DataCollection">-DataCollection</option>
	<option value="TabularDB">--TabularDB</option>
	<option value="Service">-Service</option>
	<option value="Registry">--Registry</option>
	<option value="SkyService">--SkyService</option>
	<option value="TabularSkyService">---TabularSkyService</option>
	<option value="ConeSearch">---ConeSearch</option>
	<option value="SimpleImageAccess">---SimpleImageAccess</option>
</select>

<input type="submit" name="xformsnewsubmit" value="Submit" />
</form>
</p>

</body>
</html>