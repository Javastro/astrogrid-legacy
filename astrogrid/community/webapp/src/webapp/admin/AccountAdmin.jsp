<%@ page import="org.astrogrid.community.common.policy.data.AccountData,
                 org.astrogrid.community.server.policy.manager.AccountManagerImpl"
    session="false" %>

<%

//put add account link at the top
//get a list of accounts and put a edit and remove beside them.
System.out.println("Creating new AccountManagerImpl");
AccountManagerImpl ami = new AccountManagerImpl();


String removeAccount = request.getParameter("RemoveAccount");
String addAccount = request.getParameter("AddAccount");
String editAccount = request.getParameter("EditAccount");
String info = "";
String ident = null;
AccountData changeAccount = null;
if(removeAccount != null && removeAccount.trim().length() > 0) {
	ident = request.getParameter("ident");
	ident = ident.trim();		
	ami.delAccount(ident);
	info = "Account was deleted for id = " + ident;	
}else if(addAccount != null && addAccount.trim().length() > 0) {
	ident = request.getParameter("ident");
	if(ident == null || ident.trim().length() <= 0) {
		info = "Could not add an account no identifier was provided.";
	}else {
		ident = ident.trim();
		changeAccount = new AccountData(ident);
		changeAccount.setEmailAddress(request.getParameter("email"));		
		changeAccount.setDisplayName(request.getParameter("displayName"));
		changeAccount.setDescription(request.getParameter("description"));
		changeAccount.setHomeSpace(request.getParameter("homespace"));		
		ami.addAccount(changeAccount);
		info = "Account was added for id = " + ident;		
	}
}else if(editAccount != null && editAccount.trim().length() > 0) {
	ident = request.getParameter("ident");
	ident = ident.trim();		
	changeAccount = new AccountData(ident);
	changeAccount.setEmailAddress(request.getParameter("email"));
	changeAccount.setDisplayName(request.getParameter("displayName"));
	changeAccount.setDescription(request.getParameter("description"));
	changeAccount.setHomeSpace(request.getParameter("homespace"));		
	ami.setAccount(changeAccount);	
	info = "Account was updated for id = " + ident;
}
System.out.println("grabbing all local acocounts");
Object[] accounts = ami.getLocalAccounts();
if(accounts != null)
	System.out.println("the accounts size = " + accounts.length);
else 
	System.out.println("the accounts array was null");
%>

<html>
	<head>
		<title>Account Administration</title>
	</head>
	<body>
		<p>
			<strong><font color="blue"><%=info%></font></strong><br />
			Account administration page, here you can add, edit, or delete accounts.
		<br />
		<table>
			<tr>
				<td>
					Identifier and Username
				</td>
				<td>
					Display Name
				</td>
				<td>
					Description
				</td>				
				<td>
					e-mail
				</td>
				<td>
					Home Space
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
						<input type="text" name="email" />
					</td>
					<td>
						<input type="text" name="homespace" />
					</td>
					<td>
						<input type="hidden" name="AddAccount" value="true"/>				
						<input type="submit" name="AddAccountSubmit" value="Add" />
					</td>
				</form>
			</tr>
		</table>		
		<br />
		<hr />
		List of accounts:<br />
		<table>
			<tr>
				<td>
					Identifier and Username
				</td>
				<td>
					Display Name
				</td>
				<td>
					Description
				</td>				
				<td>
					e-mail
				</td>
				<td>
					Home Space
				</td>
				<td>
					Edit
				</td>
				<td>
					Remove
				</td>
			</tr>
		<%
			AccountData ad = null;
			if(accounts != null)
			for(int i = 0;i < accounts.length;i++) {
				ad = (AccountData)accounts[i];
		%>
			<tr>
				<form method="get">
					<td>
						<%=ad.getIdent()%>
					</td>
					<td>
						<input type="text" name="displayName" value="<%=ad.getDisplayName()%>" />
					</td>
					<td>
						<input type="text" name="description" value="<%=ad.getDescription()%>" />
					</td>					
					<td>
						<input type="text" name="email" value="<%=ad.getEmailAddress()%>" />
					</td>
					<td>
						<input type="text" name="homespace" value="<%=ad.getHomeSpace()%>" />
					</td>
					<td>
						<input type="hidden" name="ident" value="<%=ad.getIdent()%>" />					
						<input type="hidden" name="EditAccount" value="true" />
						<input type="submit" name="EditAccountSubmit" value="Edit" />
					</td>
				</form>
				<form method="get">
					<td>

						<input type="hidden" name="RemoveAccount" value="true" />
						<input type="hidden" name="ident" value="<%=ad.getIdent()%>" />
						<input type="submit" name="RemoveAccountSubmit" value="Remove" />
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