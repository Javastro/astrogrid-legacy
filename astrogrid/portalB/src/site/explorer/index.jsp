<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/site/explorer/Attic/index.jsp,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/22 23:29:12 $</cvs:author>
    | <cvs:version>$Revision: 1.5 $</cvs:version>
    | <cvs:log>
    | $Log: index.jsp,v $
    | Revision 1.5  2003/06/22 23:29:12  dave
    | Tidied up pages
    |
    | Revision 1.4  2003/06/22 22:29:48  dave
    | Added message, actions and page for move
    |
    | Revision 1.3  2003/06/22 04:03:41  dave
    | Added actions and parsers for MySpace messages
    |
    | </cvs:log>
    |
    | FIXME :
    | Need to use the session user name as the root node.
    | Use a tree of containers to create a new view (remove the path edit box).
    | If you select a path that does not esist, it will corrupt the MySpace server.
    | Need to URL encode the item paths, otherwise it breaks the page links.
    |
    |
    +-->
<%@ page session="true" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Collection" %>

<%@ page import="org.astrogrid.portal.session.AstPortalSession" %>
<%@ page import="org.astrogrid.portal.explorer.AstPortalView"   %>

<%
//
// Our current AstPortalSession.
AstPortalSession portal = null ;
//
// Our current AstPortalView.
AstPortalView view = null ;
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
	//
	// If we created a view.
	if (null != view)
		{
		response.sendRedirect(
			response.encodeRedirectURL("tree.jsp?view=" + view.getIdent())
			) ;
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
						%>
						<br>
						&nbsp;&nbsp;<a href="tree.jsp?view=<%= next.getIdent() %>">[ <%= ("".equals(next.getPath()) ? "/" : next.getPath()) %> ]</a>
						<%
						}
					%>
				</td>
				<td height="350" width="700" valign="top">
					New view :
					<form method="get" action="index.jsp">
						<input name="action" type="hidden" value="create"/>
						<select name="path">
							<option value="">/</option>
							<option value="/clq">/clq</option>
							<option value="/clq/serv2">/clq/serv2</option>
						</select>
						<input name="submit" type="submit" value="Create"/>
					</form>
					Views :
					<table border="1">
						<tr>
							<td>Ident</td>
							<td>Path</td>
						</tr>
						<%
						Iterator views = portal.getCurrentViews() ;
						while (views.hasNext())
							{
							AstPortalView next = (AstPortalView) views.next() ;
							%>
							<tr>
								<td>
									<a href="tree.jsp?view=<%= next.getIdent() %>">[<%= next.getIdent() %>]</a>
								</td>
								<td>
									<%= ("".equals(next.getPath()) ? "/" : next.getPath()) %>
								</td>
							</tr>
							<%
							}
						%>
					</table>
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


