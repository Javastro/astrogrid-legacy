<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.astrogrid.store.Ivorn,
                 org.astrogrid.registry.common.RegistryValidator,
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
  boolean isMultipart = FileUpload.isMultipartContent(request);
//System.out.println("the ismultipart = " + isMultipart + " doc url = " + request.getParameter("docurl") + " and addFromURL = " + request.getParameter("addFromURL"));
//System.out.println("validate = " + request.getParameter("validate"));
 if(isMultipart) {
   DiskFileUpload upload = new DiskFileUpload();  
   List /* FileItem */ items = upload.parseRequest(request);
   Iterator iter = items.iterator();
   while (iter.hasNext()) {
      FileItem item = (FileItem) iter.next();
       if (!item.isFormField()) {
         doc = DomHelper.newDocument(item.getInputStream());
         update = true;
       }else {
         //System.out.println("FIELd name = " + item.getFieldName());
         if("validate".equals(item.getFieldName())) {
            if("true".equals(item.getString())) {
               doValidate = true;
            }
         }
       }
   }
   //update = true;
   if(doValidate) {
      try {
         RegistryValidator.isValid(doc);
      }catch(AssertionFailedError afe) {
            update = false;
            validateError = true;
            errorTemp = afe.getMessage();
      }
   }//if

   
  } else if(request.getParameter("addFromURL") != null &&
     request.getParameter("addFromURL").trim().length() > 0) {
      doc = DomHelper.newDocument(new URL(request.getParameter("docurl")));
      update = true;
      if(request.getParameter("validate") != null &&
         request.getParameter("validate").equals("true")) {      
         try {
            RegistryValidator.isValid(doc);
         }catch(AssertionFailedError afe) {
            update = false;
            validateError = true;
            errorTemp = afe.getMessage();
         }
       }//if
      
  } else if(request.getParameter("addFromText") != null &&
     request.getParameter("addFromText").trim().length() > 0) {
    doc = DomHelper.newDocument(request.getParameter("Resource"));
     update = true;
      if(request.getParameter("validate") != null &&
         request.getParameter("validate").equals("true")) {     
         try {
            RegistryValidator.isValid(doc);
         }catch(AssertionFailedError afe) {
            update = false;
            validateError = true;
            errorTemp = afe.getMessage();
         }     
      }//if
  }
%>

<html>
<head>
<title>Edit Registry Entry</title>
<style type="text/css" media="all">
          @import url("style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>


<p>Service returns:</p>

<pre>
   <font color="red"><%=errorTemp %></font>
<%
   if(update) {
      Document result = null;
      RegistryAdminService server = new RegistryAdminService();   
      result = server.updateResource(doc);
      out.write("<p>Attempt at updating Registry, if any errors occurred it will be printed below<br /></p>");
      if (result != null) {
        DomHelper.DocumentToWriter(result, out);
      }
   }
%>
</pre>

</body>
</html>