<%@ page import="org.astrogrid.community.common.policy.data.GroupData,
				 org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.community.server.policy.manager.PolicyManagerImpl,				 
                 org.astrogrid.store.Ivorn,				 
                 org.astrogrid.community.server.policy.manager.GroupManagerImpl"
    session="true" %>

<%

//put add account link at the top
//get a list of accounts and put a edit and remove beside them.
//GroupManagerImpl gmi = new GroupManagerImpl();
PolicyManagerImpl gmi = new PolicyManagerImpl();


String removeGroup = request.getParameter("RemoveGroup");
String addGroup = request.getParameter("AddGroup");
String editGroup = request.getParameter("EditGroup");
String info = "";
String ident = null;
GroupData changeGroup = null;
String currentCommunity = Ivorn.SCHEME + "://" + CommunityIvornParser.getLocalIdent();
if(removeGroup != null && removeGroup.trim().length() > 0) {
	ident = request.getParameter("ident");
	ident = ident.trim();		
	gmi.delGroup(ident);
	info = "Group was deleted for id = " + ident;	
}else if(addGroup != null && addGroup.trim().length() > 0) {
	ident = request.getParameter("ident");
	if(ident == null || ident.trim().length() <= 0 ||
	   request.getParameter("displayName") == null || 
	   request.getParameter("displayName").trim().length() <= 0) {
		info = "Could not add a group no username or display name was provided.";
	}else {
		ident = ident.trim();
		if(request.getParameter("displayName") == null || 
	   		request.getParameter("displayName").trim().length() <= 0) {
			info = "Could not add a group no username or display name was provided.";
		}else {
			changeGroup = new GroupData(currentCommunity + "/" + ident);
			changeGroup.setDisplayName(request.getParameter("displayName"));
			changeGroup.setDescription(request.getParameter("description"));
			changeGroup.setType(GroupData.MULTI_TYPE);		
			gmi.addGroup(changeGroup);
			info = "Group was added for id = " + ident;		
		}
	}
}else if(editGroup != null && editGroup.trim().length() > 0) {
	ident = request.getParameter("ident");
	ident = ident.trim();
	changeGroup = new GroupData(ident);
	changeGroup.setDisplayName(request.getParameter("displayName"));
	changeGroup.setDescription(request.getParameter("description"));
	changeGroup.setType(GroupData.MULTI_TYPE);		
	gmi.setGroup(changeGroup);	
	info = "Group was updated for id = " + ident;
}
Object[] groups = gmi.getLocalGroups();
if(groups != null)
	System.out.println("the size of groups = " + groups.length);
%>
<html>
<head>
		<title>Group Administration</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="beans.xml" %>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

		<p>
			<strong><font color="blue"><%=info%></font></strong><br />
			Group administration page, here you can add, edit, or delete groups.
		<table>
			<tr>
				<td>
					<strong>
					Username
					</strong>
				</td>
				<td>
					<strong>
					Display Name
					</strong>
				</td>
				<td>
					Description
				</td>				
				<td>
					Add
				</td>
			</tr>		
			<tr>
				<form method="get" />
					<td>
						<input type="text" name="ident" />
					</td>
					<td>
						<input type="text" name="displayName" />
					</td>
					<td>
						<input type="text" name="description" />
					</td>					
					<td>
						<input type="hidden" name="AddGroup" value="true"/>				
						<input type="submit" name="AddGroupSubmit" value="Add" />
					</td>
				</form>
			</tr>
		</table>		
		<br />
		<%
 		  if(groups != null && groups.length > 0) {
 		%>
		List of groups:<br />
		<table>
			<tr>
				<td>
					Username
				</td>
				<td>
					Display Name
				</td>
				<td>
					Description
				</td>				
				<td>
					Edit
				</td>
				<td>
					Remove
				</td>
			</tr>
		<%
		    }
			GroupData gd = null;
			if(groups != null && groups.length > 0)
			for(int i = 0;i < groups.length;i++) {
				gd = (GroupData)groups[i];
				if(GroupData.MULTI_TYPE.equals(gd.getType())) {
		%>
			<tr>
				<form method="get">
					<td>
						<%=gd.getIdent().substring((currentCommunity.length()+1))%>
					</td>
					<td>
						<input type="text" name="displayName" value="<%=gd.getDisplayName()%>" />
					</td>
					<td>
						<input type="text" name="description" value="<%=gd.getDescription()%>" />
					</td>					
					<td>
						<input type="hidden" name="ident" value="<%=gd.getIdent()%>" />					
						<input type="hidden" name="EditGroup" value="true" />
						<input type="submit" name="EditGroupSubmit" value="Edit" />
					</td>
				</form>
				<%
					if("admin".endsWith(gd.getIdent()) || "guest".endsWith(gd.getIdent())) {
				%>
					<td>
					  Cannot Remove
					</td>
				<%
				  } else {
				%>
				<form method="get">				
					<td>
						<input type="hidden" name="RemoveGroup" value="true" />
						<input type="hidden" name="ident" value="<%=gd.getIdent()%>" />
						<input type="submit" name="RemoveGroupSubmit" value="Remove" />
					</td>
				</form>	
				<%
				}
				%>				
			</tr>	
		<%
			}//if
		  }//for
		%>
		</table>
		<br />
		<a href="index.html">Administration Index</a>

		</p>	
	</body>	
</html>