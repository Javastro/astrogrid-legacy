<%
  String q = (String)session.getAttribute("foo");
%>
<html>
  <head>
  </head>
  <body>
    <h1>Registry browser</h1>
      <p>
	The string has the value <%=q%>.
    <form action="/Registry/Registry" action="post">
      <button type="submit">submit</button>
    </form>
  </body>
</html>
