<%@ page import="org.astrogrid.community.common.policy.data.GroupData,
				 org.astrogrid.community.common.policy.data.AccountData,
				 org.astrogrid.community.common.policy.data.GroupMemberData, 
                 org.astrogrid.community.server.policy.manager.GroupManagerImpl,
                 org.astrogrid.community.server.policy.manager.AccountManagerImpl"
    session="false" %>

<%

//put add account link at the top
//get a list of accounts and put a edit and remove beside them.
GroupManagerImpl gmi = new GroupManagerImpl();
AccountManagerImpl ami = new AccountManagerImpl();

String removeGroupMember = request.getParameter("RemoveGroupMember");
String addGroupMember = request.getParameter("AddGroupMember");
String info = "";
String ident = null;
if(removeGroupMember != null && removeGroupMember.trim().length() > 0) {
	gmi.delGroupMember(request.getParameter("account"),request.getParameter("group"));
	info = "GroupMember was deleted for account = " + request.getParameter("account") + " with group = " + request.getParameter("group");
}else if(addGroupMember != null && addGroupMember.trim().length() > 0) {
	System.out.println("Attempting to add group members account = " + request.getParameter("account") + " and group = " + request.getParameter("group"));
		gmi.addGroupMember(request.getParameter("account"),request.getParameter("group"));
		info = "Added Group Member for account = " + request.getParameter("account") + " with group = " + request.getParameter("group");
}
Object[] groups = gmi.getLocalGroups();
Object[] accounts = ami.getLocalAccounts();
Object[] groupMembers = gmi.getGroupMembers();

%>

<html>
	<head>
		<title>Group Administration</title>
	</head>
	<body>
		<p>
			<strong><font color="blue"><%=info%></font></strong><br />
			Group Member administration page, here you can add, edit, or delete accounts to groups.
		<br />
			<form method="get" />		
				<input type="hidden" name="AddGroupMember" value="true" />
				<strong>Group:</strong>
				<select name="group">
					<% for(int i = 0;i < groups.length;i++) { %>
						<option value="<%= ((GroupData)groups[i]).getIdent() %>">
							<%= ((GroupData)groups[i]).getDisplayName() %>
						</option>
					<% } %>
				</select>
				&nbsp;
				<strong>Account:</strong>
				<select name="account">
					<% for(int i = 0;i < accounts.length;i++) { %>
						<option value="<%= ((AccountData)accounts[i]).getIdent() %>">
							<%= ((AccountData)accounts[i]).getDisplayName() %>
						</option>
					<% } %>
				</select>
				<input type="submit" name="AddGroupMemberSubmit" value="Add Group Member" />
			</form>
		<br />
		List of group members:<br />
		
		<table>
			<tr>
				<td>
					Group
				</td>
				<td>
					Account
				</td>
				<td>
					Remove
				</td>
			</tr>
		<%
			GroupMemberData gmd = null;
			for(int i = 0;i < groupMembers.length;i++) {
				gmd = (GroupMemberData)groupMembers[i];
		%>
			<tr>
				<form method="get">
					<td>
						<%= gmd.getGroup() %>
					</td>
					<td>
						<%= gmd.getAccount() %>
					</td>
					<td>
						<input type="hidden" name="group" value="<%=gmd.getGroup()%>" />
						<input type="hidden" name="account" value="<%=gmd.getAccount()%>" />						
						<input type="hidden" name="RemoveGroupMember" value="true" />
						<input type="submit" name="RemoveGroupMemberSubmit" value="Remove" />
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