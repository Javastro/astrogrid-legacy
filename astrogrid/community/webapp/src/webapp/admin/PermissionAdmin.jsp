<%@ page import="org.astrogrid.community.common.policy.data.GroupData,
				 org.astrogrid.community.common.policy.data.ResourceData,
				 org.astrogrid.community.common.policy.data.PolicyPermission,				 
                 org.astrogrid.community.server.policy.manager.GroupManagerImpl,
                 org.astrogrid.community.server.policy.manager.ResourceManagerImpl,                 
				 org.astrogrid.community.server.policy.manager.PermissionManagerImpl,
                 org.astrogrid.community.server.policy.manager.AccountManagerImpl"
    session="false" %>

<%

//put add account link at the top
//get a list of accounts and put a edit and remove beside them.
GroupManagerImpl gmi = new GroupManagerImpl();
AccountManagerImpl ami = new AccountManagerImpl();
ResourceManagerImpl rmi = new ResourceManagerImpl();
PermissionManagerImpl pmi = new PermissionManagerImpl();

String removePermission = request.getParameter("RemovePermission");
String addPermission = request.getParameter("AddPermission");
String info = "";
String ident = null;
if(removePermission != null && removePermission.trim().length() > 0) {
	pmi.delPermission(request.getParameter("resource"),request.getParameter("group"),request.getParameter("action"));
	info = "Permission was deleted for group = " + request.getParameter("group") + " with resource = " + request.getParameter("resource");
}else if(addPermission != null && addPermission.trim().length() > 0) {
		pmi.addPermission(request.getParameter("resource"),request.getParameter("group"),request.getParameter("action"));
		info = "Permission was added for group = " + request.getParameter("group") + " with resource = " + request.getParameter("resource");
}
Object[] groups = gmi.getLocalGroups();
Object[] groupMembers = gmi.getGroupMembers();
Object[] resources = rmi.getResources();
Object[] permissions = pmi.getPermissions();

%>

<html>
	<head>
		<title>Permission Administration</title>
	</head>
	<body>
		<p>
			<strong><font color="blue"><%=info%></font></strong><br />
			Permission Member administration page, here you can add, edit, or delete accounts to groups.
		<br />
			<form method="get" />		
				<input type="hidden" name="AddPermission" value="true" />
				<strong>Group:</strong>
				<select name="group">
					<% for(int i = 0;i < groups.length;i++) { %>
						<option value="<%= ((GroupData)groups[i]).getIdent() %>">
							<%= ((GroupData)groups[i]).getDisplayName() %>
						</option>
					<% } %>
				</select>
				<br />
				<strong>Resources:</strong>
				<select name="resource">
					<% for(int i = 0;i < resources.length;i++) { %>
						<option name="resource" value="<%= ((ResourceData)resources[i]).getIdent() %>">
							<%= ((ResourceData)resources[i]).getDescription() %>
						</option>
					<% } %>
				</select>
				<br />
				<input type="text" name="action" value="read" /><br />
				<input type="submit" name="AddPermissionSubmit" value="Add Permission" />
			</form>
		<br />
		List of permissions:<br />
		<table>
			<tr>
				<td>
					Group
				</td>
				<td>
					Resource
				</td>
				<td>
					Action
				</td>
				<td>
					Remove
				</td>
			</tr>
		<%
			PolicyPermission pp = null;
			for(int i = 0;i < permissions.length;i++) {
				pp = (PolicyPermission)permissions[i];
		%>
			<tr>
				<form method="get">
					<td>
						<%= pp.getGroup() %>
					</td>
					<td>
						<%= pp.getResource() %>
					</td>
					<td>
						<%= pp.getAction() %>
					</td>					
					<td>
						<input type="hidden" name="group" value="<%=pp.getGroup()%>" />
						<input type="hidden" name="resource" value="<%=pp.getResource()%>" />						
						<input type="hidden" name="action" value="<%=pp.getAction()%>" />												
						<input type="hidden" name="RemovePermission" value="true" />
						<input type="submit" name="RemovePermissionSubmit" value="Remove" />
					</td>
				</form>
			</tr>	
		<%
			}
		%>
		</table>
		<br />
		<a href="index.html">Administration Index</a>
		
		</p>	
	</body>	
</html>