<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/site/explorer/Attic/tree.jsp,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/23 23:21:12 $</cvs:author>
    | <cvs:version>$Revision: 1.5 $</cvs:version>
    | <cvs:log>
    | $Log: tree.jsp,v $
    | Revision 1.5  2003/06/23 23:21:12  dave
    | Updated the page actions
    |
    | Revision 1.4  2003/06/23 11:19:03  dave
    | Added service location to view pages
    |
    | Revision 1.3  2003/06/22 23:29:12  dave
    | Tidied up pages
    |
    | Revision 1.2  2003/06/22 22:29:48  dave
    | Added message, actions and page for move
    |
    | Revision 1.1  2003/06/22 04:03:41  dave
    | Added actions and parsers for MySpace messages
    |
    | </cvs:log>
    |
    | FIXME :
    | Use the tree of containers to change the view path (remove the path edit box).
    | If you select a path that does not esist, it will corrupt the MySpace server.
    | Need to URL encode the item paths, otherwise it breaks the page links.
    |
    |
    +-->
<%@ page session="true" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.util.Iterator" %>

<%@ page import="org.astrogrid.portal.session.AstPortalSession" %>
<%@ page import="org.astrogrid.portal.explorer.AstPortalView"   %>
<%@ page import="org.astrogrid.portal.services.myspace.client.data.DataNode" %>
<%@ page import="org.astrogrid.portal.services.myspace.client.data.DataTreeWalker" %>
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
		// Set our view view path.
		view.setPath(path) ;
		//
		// Clear the selected item.
		view.setItem("") ;
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
	// If we have a view.
	if (null != view)
		{
		//
		// Set the selected item.
		view.setItem(path) ;
		}
	}

//
// If the action is 'delete'.
if ("delete".equals(action))
	{
	//
	// Get the path from our request params.
	String path = request.getParameter("path") ;
	//
	// If we have a view.
	if (null != view)
		{
		//
		// Delete the selected item
		view.deleteItem(path) ;
		//
		// Clear the selected item.
		view.setItem("") ;
		}
	}

//
// If the action is 'close'.
if ("close".equals(action))
	{
	//
	// If we have a view.
	if (null != view)
		{
		//
		// Delete this view
		portal.deleteView(view.getIdent()) ;
		//
		// Redirect back to the index page.
		response.sendRedirect(
			response.encodeRedirectURL("index.jsp")
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
						View : <a href="tree.jsp?view=<%= view.getIdent() %>&action=close">[Close]</a>
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
						</table>
						<br>
						Tree :
						<table border="1">
							<tr>
								<td>Name</td>
								<td>View</td>
								<td>Copy</td>
								<td>Move</td>
								<td>Delete</td>
								<td>Rename</td>
								<td>Create</td>
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
													// Add the indentation.
													for (int i = 0 ; i < level ; i++)
														{
														writer.write("&nbsp;") ;
														writer.write("&nbsp;") ;
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
													//
													// If this node is an item, add an 'item' link to select it.
													if ("2".equals(node.getType()))
														{
														writer.write("<a") ;
														writer.write(" ") ;
														writer.write("href=") ;
														writer.write("\"") ;
														writer.write("tree.jsp") ;
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
														writer.write("item") ;
														writer.write("\"") ;
														writer.write(">") ;
															writer.write("[View]") ;
														writer.write("</a>") ;
														}
													//
													// If this node is a container, add a 'path' link to select it.
													else {
														writer.write("<a") ;
														writer.write(" ") ;
														writer.write("href=") ;
														writer.write("\"") ;
														writer.write("tree.jsp") ;
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
															writer.write("[View]") ;
														writer.write("</a>") ;
														}
												writer.write("</td>") ;

												writer.write("<td>") ;
													//
													// If this node is an item, add an 'copy' link to select it.
													if ("2".equals(node.getType()))
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
														writer.write("item") ;
														writer.write("\"") ;
														writer.write(">") ;
															writer.write("[Copy]") ;
														writer.write("</a>") ;
														}
													//
													// If this isn't an item, then skip it.
													else {
														writer.write("-") ;
														}
												writer.write("</td>") ;

												writer.write("<td>") ;
													//
													// If this node is an item, add an 'move' link to select it.
													if ("2".equals(node.getType()))
														{
														writer.write("<a") ;
														writer.write(" ") ;
														writer.write("href=") ;
														writer.write("\"") ;
														writer.write("move.jsp") ;
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
														writer.write("item") ;
														writer.write("\"") ;
														writer.write(">") ;
															writer.write("[Move]") ;
														writer.write("</a>") ;
														}
													//
													// If this isn't an item, then skip it.
													else {
														writer.write("-") ;
														}
												writer.write("</td>") ;

												writer.write("<td>") ;
													//
													// If this node below the top two levels.
													if (level > 2)
														{
														writer.write("<a") ;
														writer.write(" ") ;
														writer.write("href=") ;
														writer.write("\"") ;
														writer.write("delete.jsp") ;
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
														writer.write("item") ;
														writer.write("\"") ;
														writer.write(">") ;
															writer.write("[Delete]") ;
														writer.write("</a>") ;
														}
													//
													// If this one of the top levels, we can't delete it.
													else {
														writer.write("-") ;
														}
												writer.write("</td>") ;

												writer.write("<td>") ;
													//
													// If this node an item.
													if ("2".equals(node.getType()))
														{
														writer.write("<a") ;
														writer.write(" ") ;
														writer.write("href=") ;
														writer.write("\"") ;
														writer.write("name.jsp") ;
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
														writer.write("item") ;
														writer.write("\"") ;
														writer.write(">") ;
															writer.write("[Name]") ;
														writer.write("</a>") ;
														}
													//
													// If this node isn't an item.
													else {
														writer.write("-") ;
														}
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
														writer.write("create.jsp") ;
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
														writer.write("select") ;
														writer.write("\"") ;
														writer.write(">") ;
															writer.write("[New]") ;
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




