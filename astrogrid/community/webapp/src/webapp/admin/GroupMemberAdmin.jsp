<%@ page import="org.astrogrid.community.common.policy.data.GroupData,
             org.astrogrid.community.common.policy.data.AccountData,
             org.astrogrid.community.common.policy.data.GroupMemberData, 
                 org.astrogrid.community.server.policy.manager.GroupManagerImpl,
                 org.astrogrid.community.server.policy.manager.PolicyManagerImpl,
                 org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver,
                 org.astrogrid.registry.client.query.ResourceData,
                 org.astrogrid.store.Ivorn,
                 java.net.URI,
                 org.astrogrid.community.client.policy.manager.PolicyManagerDelegate,
                 org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.community.server.policy.manager.AccountManagerImpl"
    session="true" %>

<%

//put add account link at the top
//get a list of accounts and put a edit and remove beside them.
//GroupManagerImpl gmi = new GroupManagerImpl();
//AccountManagerImpl ami = new AccountManagerImpl();
PolicyManagerImpl pmi = new PolicyManagerImpl();


String removeGroupMember = request.getParameter("RemoveGroupMember");
String addGroupMember = request.getParameter("AddGroupMember");
String getCommunity = request.getParameter("GetCommunityAccounts");
String info = "";
String ident = null;
String currentCommunity = CommunityIvornParser.getLocalIdent();
String currentCommunityIVO = Ivorn.SCHEME + "://" + CommunityIvornParser.getLocalIdent();
Object[] accounts = null;

PolicyManagerResolver pmr = new PolicyManagerResolver();
if(removeGroupMember != null && removeGroupMember.trim().length() > 0) {
   pmi.delGroupMember(request.getParameter("account"),request.getParameter("group"));
   info = "GroupMember was deleted for account = " + request.getParameter("account") + " with group = " + request.getParameter("group");
}else if(addGroupMember != null && addGroupMember.trim().length() > 0) {
   //System.out.println("Attempting to add group members account = " + request.getParameter("account") + " and group = " + request.getParameter("group"));
      pmi.addGroupMember(request.getParameter("account"),request.getParameter("group"));
      info = "Added Group Member for account = " + request.getParameter("account") + " with group = " + request.getParameter("group");
}else if(getCommunity != null && getCommunity.trim().length() > 0) {
   Ivorn ivorn = new Ivorn(request.getParameter("community"));
   PolicyManagerDelegate pmd = pmr.resolve(ivorn);
   accounts = pmd.getLocalAccounts();
   currentCommunity = ivorn.toUri().getAuthority();
}



//Object[] groups = gmi.getLocalGroups();
Object[] groups = pmi.getLocalGroups();
if(accounts == null)
   accounts = pmi.getLocalAccounts();
   //accounts = ami.getLocalAccounts();
   
//Object[] groupMembers = gmi.getGroupMembers();
Object[] groupMembers = pmi.getGroupMembers();

ResourceData[] communityServices = pmr.resolve();


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
         Group Member administration page, here you can add, edit, or delete accounts to groups.<br />
         
         <% boolean foundMulti = false;
         if(groups == null || groups.length == 0) {
           out.write("<font color='red'>No Groups available cannot add groups</font><br />");
         } else { %>
         <form method="get" />      
            <input type="hidden" name="AddGroupMember" value="true" />
            <strong>Group:</strong>
            <select name="group">
               <% 
               	for(int i = 0;i < groups.length;i++) { 
	        				if(GroupData.MULTI_TYPE.equals(((GroupData)groups[i]).getType())) {	        				
	        				   foundMulti = true;
						%>	        				   
		                  <option value="<%= ((GroupData)groups[i]).getIdent() %>">
	   	                  <%= ((GroupData)groups[i]).getDisplayName() %>
	      	            </option>
	      	         
               <% }  } %>
            </select>
            &nbsp;<br />
            <strong>Account from <%= currentCommunity %> Community:</strong>
            <select name="account">
               <% for(int i = 0;i < accounts.length;i++) { %>
                  <option value="<%= ((AccountData)accounts[i]).getIdent() %>">
                     <%= ((AccountData)accounts[i]).getDisplayName() %>
                  </option>
               <% } %>
            </select><br />
            <%
              if(!foundMulti) {
                out.write("<font color='red'>No MULTI groups found, meaning you cannot add users to MULTI groups.  No submit will be seen.</font><br />");
              } else {  %>
	            <input type="submit" name="AddGroupMemberSubmit" value="Add Group Member" />
	          <% } %>
         </form>
         <% } %>
      <br />
      Get Accounts from another community
         <form method="get" />      
            <input type="hidden" name="GetCommunityAccounts" value="true" />
            <strong>Communities:</strong>
            <select name="community">
               <% for(int i = 0;i < communityServices.length;i++) { %>
                  <option value="<%= ((ResourceData)communityServices[i]).getIvorn() %>">
                     <%= ((ResourceData)communityServices[i]).getTitle() %> -- <%= ((ResourceData)communityServices[i]).getIvorn().toUri().getAuthority() %>
                  </option>
               <% } %>
            </select>            
            <input type="submit" name="GetCommunityAccountsSubmit" value="Get Accounts" />            
         </form>
      
      <%
         if(groupMembers != null && groupMembers.length > 0) {
      %>
      List of group members:<br />
      
      <table>
         <tr>
            <td>
               Group
            </td>
            <td>
               Account (Unique ID)
            </td>
            <td>
               Remove
            </td>
         </tr>
      <%
         }
         GroupMemberData gmd = null;
         if(groupMembers != null && groupMembers.length > 0)
         for(int i = 0;i < groupMembers.length;i++) {
            gmd = (GroupMemberData)groupMembers[i];
      %>
         <tr>
            <form method="get">
               <td>
                  <%= gmd.getGroup().substring((currentCommunityIVO.length() + 1)) %>
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