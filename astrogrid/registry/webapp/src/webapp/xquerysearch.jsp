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

<html>
<head>
<title>Advanced Query of Registry</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<%

%>

<h1>Query Registry</h1>

<p>
   Put in your XQuery String in the text box and hit submit.  This is 'raw' XQuery meaning you need to declare
   your own namespaces (if needed).  There is nothing given to you on the xquery by the server.  You may elect to use
   the special reserver word 'RootResource' to be translated to the root node hence vr:Resource or vor:Resource.
   <br />
   <i>Once you hit submit it takes you to an XML page of your results, you will need to hit back button to get back to the menu or XQuery page.</i><br />
</p>
<br />
<form action="xqueryresults.jsp" method="post">
<p>
<input type="hidden" name="performquery" value="true" />
<textarea name="ResourceXQuery" rows="30" cols="90"></textarea>
</p>
<p>
<input type="submit" name="button" value="Submit"/><br />
</p>
</form>
</div>
<%@ include file="/style/footer.xml" %>

</body>
</html>
