<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                  org.apache.axis.utils.XMLUtils,                 
                 java.io.*"
    session="false" %>

<html>
<head><title>Add Resource Entry</title>
</head>

<body>

<%
  String resource = request.getParameter("Resource");
  String postregsubmit = request.getParameter("postregsubmit");
  String getregsubmit= request.getParameter("getregsubmit");
  String getregs = request.getParameter("getregs");
  String fullRegistryAddURL = "http://hydra.star.le.ac.uk:8080/astrogrid-registry/addResourceEntry.jsp";
  //String regBas = request.getRequestURL().toString();
  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>

<h1>Adding Entry</h1>

<p>Service returns:

<pre>
<%
   Document doc = null;
   Document result = null;
   RegistryAdminService server = new RegistryAdminService();
   if(resource != null && resource.trim().length() > 0) {
      //RegistryHarvestService server = new RegistryHarvestService();
      
   
      //Document entry = server.harvestFromResource(DomHelper.newDocument(resource));
      result = server.updateResource(DomHelper.newDocument(resource));
      out.write("<p>Attempt at updating Registry, if any errors occurred it will be printed below<br /></p>");
      if (result != null) {
        DomHelper.DocumentToWriter(result, out);
      }
   }else if(postregsubmit != null) {
      String callURL = fullRegistryAddURL + "?getregssubmit=\"true\"&getregs=\"" + regBas + "\"";
      out.write("<p>Attempting to tell hydra full registry about you: </p>");
      URL url = new URL(callURL);
      HttpURLConnection huc = (HttpURLConnection)url.openConnection();
      out.write("<p>Connection opened to hydra and hydra is extracting known registry type entries from here, the response code = " + huc.getResponseCode() + "</p>");
   }else if(getregsubmit != null && getregs != null) {
      String domurl = getregs + "/getRegistries.jsp";
      
      URL urlDom = new URL(domurl);
      System.out.println("the domurl = " + domurl);
      out.write("<p>getregs: " + getregs + "</p><br />");
      out.write("<p>url to grab registries : " + domurl + "</p><br />");
      doc = DomHelper.newDocument(urlDom);
      result = server.updateResource(doc);
      out.write("<p>Attempt at grabbing registries from above url and updating the registry, any errors in the updating of this registry will be below<br /></p>");
         if (result != null) {
        XMLUtils.ElementToWriter(result.getDocumentElement(), out);
         }
         out.write("<p><br /><br />Here were the entries attempted to be updated into the registry (Remember only the Resource elements are placed into the registry):<br /></p>");
         if(doc != null) {
        XMLUtils.ElementToWriter(doc.getDocumentElement(), out);       
         }
   }
   
%>
</pre>

<a href="index.html">Index</a>

</body>
</html>
