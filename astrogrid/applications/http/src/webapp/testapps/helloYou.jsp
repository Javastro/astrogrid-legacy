<%@ page session="false" %>
<%-- echos someone's name --%>
<%
  String name = request.getParameter("name");
%>
Hello <%=name%>
  
