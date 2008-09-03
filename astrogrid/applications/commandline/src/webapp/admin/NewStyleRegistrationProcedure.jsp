<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="true"
%>

<%
String registryUi = 
    SimpleConfig.getSingleton().getProperty("org.astrogrid.registry.query.endpoint")
    + "/../../";
%>

<%
HttpServletRequest http = (HttpServletRequest) request;
StringBuffer vosi = http.getRequestURL();
vosi.append("/../../VOSI/capabilities");
%>

<html>
    <head>
        <title>New-style registration</title>
<%@ include file="../inc/header.jsp" %>

<div id='bodyColumn'>

<h1>New-style registration</h1>

<p>
  To make a new-style registration of your application server, follow this procedure.
  (This presumes that you're using an AstroGrid registry.)
</p>

<ol>
  
  <li>
    Go to <a href="<%=vosi%>">the service-metadata page</a> of the application 
    server and copy its URL.
  </li>
  
  <li>
    Go to 
    <a href="<%=registryUi%>">the web interface of your publishing registry</a>.
  </li>
  
  <li>
    Create a new resource in the registry (the "submit new resource" link in the
    menu); use the virtual-observatory-service type.
  </li>
  
  <li>
    Fill in the core metadata.
  </li>
  
  <li>
    Select the edit link in the summary of the new resource. On the next page,
    choose the "edit service metadata" link.
  </li>
  
  <li>
    In the next page, in the "service capabilities" box, paste the URL you copied
    above.
  </li>
  
</ol>

<h2>Notes</h2>

<p>
  This procedure applies to an AstroGrid publishing-registry. If you need to
  register in some other kind of registry, then you'll need to work out a
  different method. Capturing the XML text of the service metadata (see
  notes below) will help.
</p>
<p>
  The registration is a combination of the "core metadata", which you enter
  directly into the registry, and the "service metadata", which the registry
  reads from the community service. <em>You need both for the community to be
  usable.</em> If you forget to load the service metadata, the registry will
  not complain, but no clients will be able to find your community.
</p>
<p>
  If the link above doesn't take you to the registry UI, it's probably because
  you haven't told the application server which registry to use. Check the 
  value of the setting <i>org.astrogrid.registry.admin.endpoint</i> in the 
  application-server configuration.
</p>
<p>
  The <a href="<%=vosi%>">the service-metadata page</a> of the application
  server displays like a web page but is actually an XML document. Show the page
  source if you want to see what is sent to the registry. These metadata are
  mainly URLs for different services in the application-server
  web-application.
</p>
<p>
  The new-style registration was first supported in version 2008.0 of the 
  registry. If you have an older registry then you need to upgrade that first.
</p>
</div>
<%@ include file="../inc/footer.jsp" %>
