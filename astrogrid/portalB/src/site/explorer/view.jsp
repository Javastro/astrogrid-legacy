<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<%@ page session="true" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="org.astrogrid.portal.session.AstPortalSession" %>
<%@ page import="org.astrogrid.portal.explorer.AstPortalView"   %>
<%@ page import="org.astrogrid.portal.services.myspace.client.DataItemRecord" %>

<%
//
// Our current AstPortalSession.
AstPortalSession portal = null ;
//
// Load our AstSession from our HttpSession.
portal = (AstPortalSession) session.getAttribute(AstPortalSession.HTTP_SESSION_TAG) ;
//
// If we havn't got a portal session yet.
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
// If the action is 'path'.
if ("path".equals(action))
	{
	//
	// Get the path from our request params.
	String path = request.getParameter("path") ;
	//
	// Update our view
	if (null != view)
		{
		view.setPath(path) ;
		}
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
					<a href="/astrogrid/index.jsp">Home</a>
					<br>
					<b><a href="/astrogrid/explorer/index.jsp">Explorer</a></b>
					<%
					Iterator iter = portal.getCurrentViews() ;
					while (iter.hasNext())
						{
						AstPortalView next = (AstPortalView) iter.next() ;
						if (null != view)
							{
							if ((null != view) ? (view.getIdent().equals(next.getIdent())) : false)
								{
								%>
								<br>
								&nbsp;&nbsp;
								<b><a href="view.jsp?ident=<%= next.getIdent() %>">[ <%= next.getPath() %> ]</a></b>
								<%
								}
							else {
								%>
								<br>
								&nbsp;&nbsp;
								<a href="view.jsp?ident=<%= next.getIdent() %>">[ <%= next.getPath() %> ]</a>
								<%
								}
							}
						}
					%>
				</td>
				<td height="350" width="700" valign="top">
					View :
					<%
					if (null != view)
						{
						%>
						<table border="1">
							<tr>
								<td>Ident</td>
								<td><%= view.getIdent() %></td>
							</tr>
							<tr>
								<form method="get" action="view.jsp">
									<td>Path</td>
									<td>
										<input name="action" type="hidden" value="path"/>
										<input name="ident"  type="hidden" value="<%= view.getIdent() %>"/>
										<input name="path"   type="text"   value="<%= view.getPath() %>"/>
										<input name="submit" type="submit" value="Update"/>
									</td>
								</form>
							</tr>
						</table>
						<br>
						Data :
						<table border="1">
							<tr>
								<td>Name</td>
								<td>Ident</td>
							</tr>
							<%
							if (null != view)
								{
								view.initMySpaceService() ;
								view.lookupDataHoldersDetails() ;
								Iterator items = view.getDataItems().iterator() ;
								while (items.hasNext())
									{
									DataItemRecord item = (DataItemRecord) items.next() ;
									%>
									<tr>
										<td><%= item.getName() %></td>
										<td><%= item.getIdent() %></td>
									</tr>
									<%
									}
								}
							%>
						</table>
						<%
						}
					%>
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




