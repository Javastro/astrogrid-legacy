<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/site/explorer/Attic/create.jsp,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/24 10:43:25 $</cvs:author>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    | $Log: create.jsp,v $
    | Revision 1.2  2003/06/24 10:43:25  dave
    | Fixed bugs in DataTreeWalker and tree page
    |
    | Revision 1.1  2003/06/23 23:21:12  dave
    | Updated the page actions
    |
    | </cvs:log>
    |
    +-->
<%@ page session="true" %>
<%@ page import="java.io.File" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="org.astrogrid.portal.session.AstPortalSession" %>
<%@ page import="org.astrogrid.portal.explorer.AstPortalView"   %>
<%@ page import="org.astrogrid.portal.services.myspace.client.data.DataNode" %>
<%@ page import="org.astrogrid.portal.services.myspace.client.data.DataTreeWalker" %>
<%@ page import="org.astrogrid.portal.services.myspace.client.status.StatusNode" %>
<%!
//
// Local class to integrate a TreeNodeWalker into a JSP page.
// FIXME : Move this into a custom tag at some point.
abstract class JspTreeWalker
	extends DataTreeWalker
	{
	//
	// Internal reference to our view.
	protected AstPortalView view ;
	//
	// Internal reference to our output writer.
	protected JspWriter writer ;
	//
	// Public constructor.
	public JspTreeWalker(JspWriter writer, AstPortalView view)
		{
		this.view   = view   ;
		this.writer = writer ;
		}
	}
%>
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
// Get our current AstPortalView.
AstPortalView view = portal.getView(request.getParameter("view")) ;

//
// Get the action from our request params.
String action = request.getParameter("action") ;

//
// If the action is 'select'.
if ("path".equals(action))
	{
	//
	// Get the path from our request params.
	String path = request.getParameter("path") ;
	//
	// If we have a view.
	if (null != view)
		{
		//
		// Set our destination path.
		view.setDestPath(path) ;
		}
	}

//
// If the action is 'create'.
if ("create".equals(action))
	{
	//
	// Get the name from our request params.
	String name = request.getParameter("name") ;
	//
	// If we have a view
	if (null != view)
		{
		//
		// Set the destination name.
		view.setDestName(name) ;
		//
		// Create the new container
		StatusNode status = view.createContainer(view.getDestName(), view.getDestPath()) ;
		//
		// If the operation worked.
		if ("SUCCESS".equals(status.getStatus()))
			{
			//
			// Clear the selected item.
			view.setItemPath("") ;
			//
			// Redirect back to the view page.
			response.sendRedirect(
				response.encodeRedirectURL("tree.jsp?view=" + view.getIdent())
				) ;
			}
		//
		// If the operation didn't work
		else {
			//
			// FIXME : Need to display something usefull to the user ...
			//
			}
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
								<b><a href="tree.jsp?view=<%= next.getIdent() %>">[ <%= ("".equals(next.getPath()) ? "/" : next.getPath()) %> ]</a></b>
								<%
								}
							else {
								%>
								<br>
								&nbsp;&nbsp;
								<a href="tree.jsp?view=<%= next.getIdent() %>">[ <%= ("".equals(next.getPath()) ? "/" : next.getPath()) %> ]</a>
								<%
								}
							}
						}
					%>
				</td>
				<td height="350" width="700" valign="top">
					<%
					if (null != view)
						{
						%>
						View :
						<table border="1">
							<tr>
								<td>Ident</td>
								<td><%= view.getIdent() %></td>
							</tr>
							<tr>
								<td>Path</td>
								<td><%= ("".equals(view.getPath()) ? "/" : view.getPath()) %></td>
							</tr>
							<tr>
								<td>Service</td>
								<td><%= view.getMySpaceLocation() %></td>
							</tr>
						</table>
						<br>
						<b>Create container</b> :
						<table border="1">
							<tr>
								<td>Path</td>
								<td><%= view.getDestPath() %></td>
							</tr>
							<tr>
								<form method="get">
									<td>Name</td>
									<td>
										<input name="view"   type="hidden" value="<%= view.getIdent() %>"/>
										<input name="name"   type="text"   value="<%= (null != view.getDestName()) ? view.getDestName() : "NewContainer" %>"/>
										<input name="action" type="hidden" value="create"/>
										<input name="submit" type="submit" value="Create"/>
									</td>
								</form>
							</tr>
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




