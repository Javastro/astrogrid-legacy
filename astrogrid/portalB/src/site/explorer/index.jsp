<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page session="true" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="org.astrogrid.portal.session.AstPortalSession" %>
<%@ page import="org.astrogrid.portal.explorer.AstPortalView"   %>
<%
//
// Our current AstPortalSession.
AstPortalSession portal = null ;
//
// Load our AstSession from our HttpSession.
portal = (AstPortalSession) session.getAttribute(AstPortalSession.HTTP_SESSION_TAG) ;
//
// If we havn't got one yet.
if (null == portal)
	{
	//
	// Create a new AstSession.
	portal = new AstPortalSession() ;
	//
	// Add it to our HttpSession.
	session.setAttribute(AstPortalSession.HTTP_SESSION_TAG, portal) ;
	}

//
// Our current AstPortalView.
AstPortalView view = null ;

//
// Get our view ident from our request params.
String ident = request.getParameter("ident") ;

//
// If we have an ident.
if (null != ident)
	{
	//
	// Try and get the view from our session.
	view = portal.getView(ident) ;
	}

//
// Get the action from our request params.
String action = request.getParameter("action") ;

//
// If the action is 'create'.
if ("create".equals(action))
	{
	//
	// Get the path from our request params.
	String path = request.getParameter("path") ;
	//
	// Create a new view.
	view = portal.createView(path) ;
	}

%>
<html>
	<head>
	</head>
	<body>
		<table border="1" height="450" width="800">
			<tr>
				<td height="100" width="100">
					Logo
				</td>
				<td height="100" width="700">
					Banner
				</td>
			</tr>
			<tr>
				<td height="350" width="100" valign="top">
					Menu :
					<br>
					<a href="/astrogrid/index.jsp">Home</a>
					<br>
					<b><a href="/astrogrid/explorer/index.jsp">Explorer</a></b>
				</td>
				<td height="350" width="700" valign="top">
<p>
	<table border="1">
		<tr>
			<td>Portal ident</td>
			<td><%= portal.getIdent() %></td>
		</tr>
		<tr>
			<td>Request ident</td>
			<td><%= ident %></td>
		</tr>
	</table>
</p>
<p>
	Views :
	<table border="1">
		<tr>
			<td>Ident</td>
			<td>Link</td>
		</tr>
		<%
		Iterator views = portal.getCurrentViews() ;
		while (views.hasNext())
			{
			AstPortalView next = (AstPortalView) views.next() ;
			%>
			<tr>
				<td><%= next.getIdent() %></td>
				<td>
				<a href="index.jsp?ident=<%= next.getIdent() %>">[<%= next.getIdent() %>]</a>
				</td>
			</tr>
			<%
			}
		%>
	</table>
</p>
<p>
	New view :
	<form method="get" action="index.jsp">
		<input name="action" type="hidden" value="create"/>
		<input name="path"   type="text"   value="*"/>
		<input name="go"     type="submit" value="Create"/>
	</form>
</p>
<p>
	View :
	<table border="1">
		<%
		if (null != view)
			{
			%>
			<tr>
				<td>Ident</td>
				<td><%= view.getIdent() %></td>
			</tr>
			<tr>
				<td>Path</td>
				<td><%= view.getPath() %></td>
			</tr>
			<%
			}
		%>
	</table>
</p>



				</td>
			</tr>
			<tr>
				<td height="100" width="100">
					Blank
				</td>
				<td height="100" width="700">
					Footer
				</td>
			</tr>
		</table>
	</body>
</html>