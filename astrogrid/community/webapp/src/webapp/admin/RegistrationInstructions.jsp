<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="true"
%>

<%
HttpServletRequest http = (HttpServletRequest) request;
StringBuffer vosi = http.getRequestURL();
vosi.append("/../../VOSI/capabilities");
%>

<html>
    <head>
        <title>Service registration</title>
        <style type="text/css" media="all">
            @import url("../style/astrogrid.css");
        </style>
        </title>
    </head>
<body>
  
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Registration</h1>

<p>
When setting up community for the first time, you need to register 
it in an IVO resource-registry. Please follow this procedure.
(This presumes that you're using an AstroGrid registry.)
</p>

<ol>
  
  <li>
    Go to <a href="<%=vosi%>">the service-metadata page</a> of the community 
    and copy its URL.
  </li>
  
  <li>
    Go to the web interface of your <em>publishing</em> registry.
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
  The <a href="<%=vosi%>">service-metadata page</a> of the community
  displays like a web page but is actually an XML document. Show the page
  source if you want to see what is sent to the registry. These metadata are
  mainly URLs for different services in the community web-application.
</p>

</body>
</html>
