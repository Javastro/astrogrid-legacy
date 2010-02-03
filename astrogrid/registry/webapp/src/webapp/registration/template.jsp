<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.astrogrid.registry.server.http.servlets.helper.JSPHelper,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Element,
                 org.w3c.dom.Document,
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.registry.server.http.servlets.Log4jInit,
                 org.astrogrid.xmldb.client.XMLDBManager,
                 org.astrogrid.registry.common.RegistryDOMHelper,
                 org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.apache.axis.utils.XMLUtils,
                 java.util.*,
                 java.io.*"
   isThreadSafe="false"
   session="false"
%>
<!DOCTYPE HTML  PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Registry Pages</title>
<meta http-equiv="Content-type" content="text/xhtml;charset=iso-8859-1">
<style type="text/css" media="all">
    <%@ include file="/style/astrogrid.css" %>
</style>
<%@ include file="/style/link_options.xml" %>
</head>
<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>
<div id='bodyColumn'>
<br>
<br>
<br>
<br>
<br>
<br>
<center>TBD</center>
<br>
<br>
<br>
<br>
<br>
<script LANGUAGE="JavaScript" type="text/javascript">
        var url = document.location.toString() ; //url
        var e_url = '' ; //edited url
        var p = 0 ; //position
        var p2 = 0 ;//position 2
        p = url.indexOf("//") ;
        e_url = url.substring(p+2) ;
        p2 = e_url.indexOf("/") ;
        var root_url = url.substring(0,p+p2+2)+"<%=request.getContextPath()%>";
</script>
<script LANGUAGE="JavaScript" type="text/javascript">
<!--
        document.write(root_url+"/style/navigation.xml");
// -->
</script>
<br>
<br>
<br>
<br>
</div>
<%@ include file="/style/footer.xml" %>
</body>
</html>
