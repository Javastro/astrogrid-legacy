<html>
<%@ page import="java.io.InputStream,
                 java.io.IOException,
                 org.astrogrid.config.*,
                 javax.xml.parsers.SAXParser,
                 javax.xml.parsers.SAXParserFactory"
   session="false" %>
<body>
<h1>PAL Configuration dump</h1>
<%
   try {
      //force load
      SimpleConfig.getProperty("Dummy",null);
      ((FailbackConfig) SimpleConfig.getSingleton()).dumpConfig(out);
   } catch (Throwable t) {
     out.println(t.getMessage());
   }
   
%>
</body>
</html>
