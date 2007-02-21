<%@ page import="org.astrogrid.registry.server.query.*,
                 org.w3c.dom.*,
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.config.SimpleConfig,
 	  				  org.astrogrid.registry.server.http.servlets.helper.JSPHelper,                 
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Edit Registry Entry</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<%
   String resource = "";
   Document resourceDoc = null;
   if(request.getParameter("IVORN") != null && request.getParameter("IVORN").trim().length() > 0) {
       ISearch server = JSPHelper.getQueryService(request);
	   resourceDoc = server.getQueryHelper().getResourceByIdentifier(request.getParameter("IVORN"));
	   if (resourceDoc != null) {
	     StringBuffer resContent = new StringBuffer(DomHelper.ElementToString(((Element)(resourceDoc.getDocumentElement().getElementsByTagNameNS("*","Resource").item(0)))));
	     if(server.getContractVersion().equals("0.1")) {    	  
			String temp = resContent.substring(0,resContent.indexOf(">"));
			int tempIndex = resContent.indexOf("created=\"");
			int tempIndex2;
			if(tempIndex != -1 && tempIndex < temp.length()) {
				tempIndex2 = resContent.indexOf("T",tempIndex);
				tempIndex = (resContent.indexOf("\"",tempIndex +  5));
				if(tempIndex2 != -1 && tempIndex2 < tempIndex) {
					resContent.replace(tempIndex2,tempIndex,"");
				}
				temp = resContent.substring(0,resContent.indexOf(">"));
			}//if
		
			tempIndex = resContent.indexOf("updated=\"");
			if(tempIndex != -1 && tempIndex < temp.length()) {
				tempIndex2 = resContent.indexOf("T",tempIndex);
				tempIndex = (resContent.indexOf("\"",tempIndex + 5));
				if(tempIndex2 > 0 && tempIndex2 < tempIndex) {
					resContent.replace(tempIndex2,tempIndex,"");
				}//if
			}//if
		}//if
	    resource = resContent.toString();
	   }//if
	}//if
%>

<h1>Add/Update Entry</h1>
<p>
Here you can update the resources in various ways. If the Resources are already there then it will be updated, 
Be sure your on the correct 'Contract' version (see menu).
Validation is now always turned on and checked before going into the Registry.<br />
You may checkmark the 'Validate' box to have it validated before it is ever sent and checked at the Server.
<i>schemaLocations are not particularly needed for known ivoa schemas, but they will be preserved if in the XML and may be required for
validation on extensions. schemaLocations may also be desirable if you use other applications for validation via the XML database using WebDav or other interfaces.</i>
</p>

<p>
There is a range of various ways to insert or update records.  
At the bottom of this page are various way of submitting towards Update web service calls.<br />
<b>
For jsp pages the version number is used from your current contract session settings.
See menu 'Current Contract' to make sure your on the correct version. <br />
</b>
</p>
<br />
<p>
<i>Warning for 0.10 Resources: You may have a created or updated date on the Resource element that is a dateTime, it should be a 'date' i.e. 2004-04-28</i>
</p>

Upload from a local file:
<form enctype="multipart/form-data" method="post" action="addResourceEntry.jsp">
<input type="checkbox" name="validate" value="true">Validate</input>
<input type="file" name="docfile" />
<input type="hidden" name="addFromFile" value="true" />
<input type="submit" name="uploadFromFile" value="upload" />
</form>
<br />
Upload from a url:
<form method="post" action="addResourceEntry.jsp">
<input type="checkbox" name="validate" value="true">Validate</input>
<input type="text" name="docurl" />
<input type="hidden" name="addFromURL" value="true" />
<input type="submit" name="uploadFromURL" value="upload" />

</form>

Upload from text:<br />
<form action="addResourceEntry.jsp" method="post">
<input type="checkbox" name="validate" value="true">Validate</input>
<input type="hidden" name="addFromText" value="true" />
<p>
<textarea name="Resource" rows="30" cols="90">
<%= resource %>
</textarea>
</p>
<p>
<input type="submit" name="button" value="Submit"/>
</p>
</form>

<p>
The samples below are more for updating/inserting entries via the Update web service call.
<br />
</p>
<pre>
Multiple Resources (1.0): <br />
&lt;ri:VOResources xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" from="1" numberReturned="5" more="false" &gt;
&lt;!-- You can place as many ri:Resource elements --&gt;
&lt;ri:Resource xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0" .....
&lt;ri:Resource%gt;
&lt;/ri:VOResources&gt;
<br />
Singe Resource (1.0)  - (Remember you can use the Multiple Resources way as well):
&lt;ri:Resource xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0" .....
&lt;ri:Resource%gt;
<br />
Multiple Resources (0.10):
&lt;ri:VOResource xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v0.1" .....
&lt;!-- Now place 1 to many 'Resource' elements here, Notice the ri namespace only Resource element has this. --&gt;
&lt;ri:Resource xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v0.1" .....
&lt;ri:Resource%gt;
&lt;ri:VOResources%gt;
<br />
Singe Resource (0.10) - (Remember you can use the Multiple Resources way as well):
&lt;ri:Resource xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v0.1" .....
&lt;ri:Resource%gt;
<br />

</pre>

</body>
</html>
