<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/site/explorer/Attic/move.jsp,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/23 11:19:03 $</cvs:author>
    | <cvs:version>$Revision: 1.3 $</cvs:version>
    | <cvs:log>
    | $Log: move.jsp,v $
    | Revision 1.3  2003/06/23 11:19:03  dave
    | Added service location to view pages
    |
    | Revision 1.2  2003/06/22 23:29:12  dave
    | Tidied up pages
    |
    | Revision 1.1  2003/06/22 22:29:48  dave
    | Added message, actions and page for move
    |
    | </cvs:log>
    |
    | FIXME :
    | If you select a path that does not esist, it will corrupt the MySpace server.
    | If you try to move from and to the same location, it will corrupt the MySpace server.
    | If you try to move to an existing item name, it will corrupt the MySpace server.
    | If you select a destination level less than two, it will corrupt the MySpace server.
    | Need to URL encode the item paths, otherwise it breaks the page links.
    |
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
// If the action is 'path'.
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
		// Set our view path.
		view.setPath(path) ;
		//
		// Set our destination path.
		view.setDestPath(path) ;
		}
	}

//
// If the action is 'item'.
if ("item".equals(action))
	{
	//
	// Get the path from our request params.
	String path = request.getParameter("path") ;
	//
	// If we have a view
	if (null != view)
		{
		//
		// Select the item.
		view.setItem(path) ;
		//
		// Convert the path into an abstract file.
		File file = new File(path) ;
		//
		// Set the destination name.
		view.setDestName(file.getName()) ;
		//
		// Set the destination path.
		view.setDestPath(file.getParent()) ;
		//
		// Set the view path
		view.setPath(file.getParent()) ;
		}
	}

//
// If the action is 'move'.
if ("Move".equals(action))
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
		// Perform the move.
		StatusNode status = view.moveItem() ;
		//
		// If the operation worked.
		if ("SUCCESS".equals(status.getStatus()))
			{
			//
			// Redirect back to the view page.
			response.sendRedirect(
				response.encodeRedirectURL("tree.jsp?view=" + view.getIdent() + "&path=" + view.getDestPath() + "/" + view.getDestName() + "&action=item")
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

//
// If the action is 'cancel'.
if ("Cancel".equals(action))
	{
	//
	// If we have a view
	if (null != view)
		{
		//
		// Redirect back to the view page.
		response.sendRedirect(
			response.encodeRedirectURL("tree.jsp?view=" + view.getIdent() + "&path=" + view.getItem() + "&action=item")
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
						Item :
						<table border="1">
							<tr>
								<td>Path</td>
								<td><%= view.getItem() %></td>
							</tr>
							<tr>
								<td>Dest</td>
								<td><%= view.getDestPath() %></td>
							</tr>
							<tr>
								<form method="get">
									<td>Name</td>
									<td>
										<input name="view"   type="hidden" value="<%= view.getIdent() %>"/>
										<input name="name"   type="text"   value="<%= view.getDestName() %>"/>
										<input name="action" type="submit" value="Move"/>
										<input name="action" type="submit" value="Cancel"/>
									</td>
								</form>
							</tr>
						</table>
						<br>
						Tree :
						<table border="1">
							<tr>
								<td>Name</td>
								<td>Ident</td>
								<td>Type</td>
								<td>View</td>
							</tr>
							<%
							if (null != view)
								{
								//
								// Request the tree nodes.
								DataNode tree = view.getTree() ;
								//
								// Create a tree node walker.
								DataTreeWalker walker = new JspTreeWalker(out, view)
									{
									public void action(DataNode node, boolean more, int index, int level)
										{
										try {
											writer.write("<tr>") ;
												writer.write("<td>") ;
													//
													// Add the indentation
													for (int i = 0 ; i < level ; i++)
														{
														writer.write("&nbsp;") ;
														writer.write("&nbsp;") ;
														}
													//
													// If this is an item.
													if ("2".equals(node.getType()))
														{
														writer.write("-") ;
														}
													//
													// If this is not an item (container or blank).
													else {
														writer.write("+") ;
														}
													writer.write("&nbsp;") ;
													//
													// If this is the currently selected path.
													if (node.getPath().equals(view.getPath()))
														{
														writer.write("<b>") ;
														writer.write(node.getName()) ;
														writer.write("</b>") ;
														}
													//
													// If this is the currently selected item.
													else if (node.getPath().equals(view.getItem()))
														{
														writer.write("<b>") ;
														writer.write(node.getName()) ;
														writer.write("</b>") ;
														}
													//
													// If this is not the current anything.
													else {
														writer.write(node.getName()) ;
														}
												writer.write("</td>") ;
												writer.write("<td>") ;
													writer.write("[") ;
													writer.write(node.getIdent()) ;
													writer.write("]") ;
												writer.write("</td>") ;
												writer.write("<td>") ;
													writer.write("[") ;
													writer.write(node.getType()) ;
													writer.write("]") ;
												writer.write("</td>") ;
												writer.write("<td>") ;
													//
													// If this node is a container below the top two levels.
													if ("1".equals(node.getType()) && level > 1)
														{
														writer.write("<a") ;
														writer.write(" ") ;
														writer.write("href=") ;
														writer.write("\"") ;
														writer.write("copy.jsp") ;
														writer.write("?") ;
														writer.write("view") ;
														writer.write("=") ;
														writer.write(view.getIdent()) ;
														writer.write("&") ;
														writer.write("path") ;
														writer.write("=") ;
														writer.write(node.getPath()) ;
														writer.write("&") ;
														writer.write("action") ;
														writer.write("=") ;
														writer.write("path") ;
														writer.write("\"") ;
														writer.write(">") ;
															writer.write("[V]") ;
														writer.write("</a>") ;
														}
													//
													// If this node isn't a container.
													else {
														writer.write("-") ;
														}
												writer.write("</td>") ;
											writer.write("</tr>") ;
											}
										catch (IOException ouch)
											{
											// FIXME ....
											}
										}
									} ;
								//
								// Walk the results tree.
								walker.walk(tree) ;
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




