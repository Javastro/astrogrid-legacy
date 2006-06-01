<%@ page import="org.astrogrid.store.Ivorn,
                 org.astrogrid.config.SimpleConfig,
                 org.astrogrid.filestore.webapp.FileStoreSelfTest"
    session="false" %>
<html>
<head>
      <title>FileStore Administration</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<%
String testNow = request.getParameter("testnow");
if("true".equals(testNow)) {

  FileStoreSelfTest fit = new FileStoreSelfTest();
  fit.TEST_PROPERTY_PREFIX = "org.astrogrid.filestore.service";
  SimpleConfig.getSingleton().setProperty("org.astrogrid.filestore.service.data.http.html","http://www.astrogrid.org/maven/");
  SimpleConfig.getSingleton().setProperty("org.astrogrid.filestore.service.data.http.jar","http://www.astrogrid.org/maven/org.astrogrid/jars/astrogrid-common-SNAPSHOT.jar");
  
  fit.setUp();
  fit.testImportInit();
  fit.testImportHttp();
  fit.testImportTypeJar();
  fit.testExportInitTransfer();
  fit.testExportInitTransferLocation();
  fit.testExportInitRead();
  
}

%>

By clicking the "Test" button below, will begin a self-test, by importing and exporting test files into the filestore area.
No success messages will show up on this jsp pages only via tomcat logs.  You will see files populated
into your filestore on successfull tests.  Exceptions will be shown on this jsp page.
Also currently only supports 'org.astrogrid.filestore' property, meaning in the  properties or jndi properties all property key's begin with 'org.astrogrid.filestore'
<br />
<i>Hints:  Unable to save info - Normally your repository property is not set correctly.</i>
<br />
<p>
<form method="post">
<input type="hidden" name="testnow" value="true" />
<input type="Submit" name="TestSubmit" value="Test"/>
</form>
</p>

</body>
</html>
