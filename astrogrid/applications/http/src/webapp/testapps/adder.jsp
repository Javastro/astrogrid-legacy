<%@ page session="false" %>
<%-- Takes two get params x and y and returns their sum --%>
<%
  String param_x=request.getParameter("x");
  String param_y=request.getParameter("y");
  int x,y;
  try {
	   x = Integer.parseInt(param_x);
	   y = Integer.parseInt(param_y);
  } catch (NumberFormatException e) {
  	out.println("Error");
  	return;
  }
  int z = x + y;
%>
<%=z%>
  
