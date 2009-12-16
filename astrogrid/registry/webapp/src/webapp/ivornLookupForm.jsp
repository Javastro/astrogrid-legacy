<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.astrogrid.registry.server.query.*,
     				  org.astrogrid.registry.server.*,
      			  org.astrogrid.registry.common.RegistryDOMHelper,
                 java.util.ArrayList"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Access Pages</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</title>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<h1>View Resource</h1>

<p>
Enter the Identifier for the entry you want to view.
</p>

<form action="viewResourceEntry.jsp" method="post">
<p>
Resource identifier
 <br />
 <input name="IVORN" type="text" value="ivo://" size="60"/>
 <input type="submit" name="button" value="Find"/>
</p>
<p>
Examples:<br/>
roe.ac.uk/DSA_6dF/rdbms<br/>
nasa.heasarc/acrs
</p>
</form>
</div>
<%@ include file="/style/footer.xml" %>
</body>
</html>
