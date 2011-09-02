<%@ page session="false" %>
<%-- echos someone's name --%>
<%
  String name = request.getParameter("name");
%>
<?xml version="1.0" encoding="UTF-8"?>
<root>
<some>
<xml>
Hello <%=name%>
</xml>
</some>
</root>
