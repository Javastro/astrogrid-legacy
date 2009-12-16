<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.astrogrid.store.Ivorn,
                 org.astrogrid.registry.common.RegistryValidator,
                 org.astrogrid.registry.server.http.servlets.helper.JSPHelper,
                 junit.framework.AssertionFailedError,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,                 
                 java.util.*,
                 org.apache.axis.utils.XMLUtils, 
                 org.apache.commons.fileupload.*, 
                 java.io.*"
    session="false" %>

<%
  boolean validateError = false;
  boolean doValidate = false;
  Document doc = null;
  boolean update = false;
  String errorTemp = "";
  if(request.getParameter("Index").trim().length() > 0) {
	  doc = DomHelper.newDocument(request.getParameter("Index").trim());
	  update = true;
  }
%>

<html>
<head>
<title>Adding Index</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>


<p>Service returns:</p>

<pre>
   <font color="red"><%=errorTemp %></font>
<%
   if(update) {
      Document result = null;
	  IAdmin server = JSPHelper.getAdminService(request);
      out.write("<p>Attempt at updating Registry Indexes, if any errors occurred it will be printed below<br /></p>");
      result = server.updateIndex(doc);
	      if (result != null) {
	        DomHelper.DocumentToWriter(result, out);
	      }
   }else {
     out.write("Did not find anything to add/update");
   }
%>
</pre>
</div>
<%@ include file="/style/footer.xml" %>

</body>
</html>
