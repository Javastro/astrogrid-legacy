<%@ page import="org.astrogrid.community.common.policy.data.GroupData,
                 org.astrogrid.community.common.policy.data.ResourceData,
                 org.astrogrid.community.common.policy.data.PolicyPermission,            
                 org.astrogrid.community.server.policy.manager.GroupManagerImpl,
                 org.astrogrid.community.server.policy.manager.PolicyManagerImpl,
                 org.astrogrid.community.server.policy.manager.ResourceManagerImpl,                 
                 org.astrogrid.community.server.policy.manager.PermissionManagerImpl,
                 org.astrogrid.community.resolver.policy.manager.PolicyManagerResolver,
                 org.astrogrid.store.Ivorn,
                 java.net.URI,
                 org.astrogrid.community.client.policy.manager.PolicyManagerDelegate,
                 org.astrogrid.community.common.ivorn.CommunityIvornParser,             
                 org.astrogrid.community.server.policy.manager.AccountManagerImpl"
    session="false" %>

<%

//put add account link at the top
//get a list of accounts and put a edit and remove beside them.
PolicyManagerImpl pmi = new PolicyManagerImpl();
//PermissionManagerImpl pmi = new PermissionManagerImpl();
//GroupManagerImpl gmi = new GroupManagerImpl();
//AccountManagerImpl ami = new AccountManagerImpl();
//ResourceManagerImpl rmi = new ResourceManagerImpl();
//PermissionManagerImpl pmi = new PermissionManagerImpl();

String removePermission = request.getParameter("RemovePermission");
String addPermission = request.getParameter("AddPermission");
String getCommunity = request.getParameter("GetCommunityGroups");
String currentCommunity = CommunityIvornParser.getLocalIdent();

String info = "";
String ident = null;
Object[] groups = null;
PolicyManagerResolver pmr = new PolicyManagerResolver();
if(removePermission != null && removePermission.trim().length() > 0) {
   pmi.delPermission(request.getParameter("resource"),request.getParameter("group"),request.getParameter("action"));
   info = "Permission was deleted for group = " + request.getParameter("group") + " with resource = " + request.getParameter("resource");
}else if(addPermission != null && addPermission.trim().length() > 0) {
     if((request.getParameter("resource") == null || request.getParameter("resource").trim().length() <= 0) ||
       (request.getParameter("group") == null || request.getParameter("group").trim().length() <= 0) ||
       (request.getParameter("action") == null || request.getParameter("action").trim().length() <= 0) ) {
         info = "No adding of permission; You must have a Action, Resource, and Group for adding a permission.";
     } else {
         pmi.addPermission(request.getParameter("resource"),request.getParameter("group"),request.getParameter("action"));
         info = "Permission was added for group = " + request.getParameter("group") + " with resource = " + request.getParameter("resource");
         //PolicyPermission pptest2 = pmi.getPermission(request.getParameter("resource"),request.getParameter("group"),request.getParameter("action"));
         //if(pptest2 != null) {
           //System.out.println("the pptest2 is not null");
         //}
      }
}else if(getCommunity != null && getCommunity.trim().length() > 0) {
   Ivorn ivorn = new Ivorn(request.getParameter("community"));
   PolicyManagerDelegate pmd = pmr.resolve(ivorn);
   groups = pmd.getLocalGroups();
   currentCommunity = ivorn.toUri().getAuthority();
}

if(groups == null)
   groups = pmi.getLocalGroups();
//   groups = gmi.getLocalGroups();
   
//Object[] groupMembers = gmi.getGroupMembers();
//Object[] resources = rmi.getResources();
//Object[] permissions = pmi.getPermissions();
Object[] groupMembers = pmi.getGroupMembers();
Object[] resources = pmi.getResources();
Object[] permissions = pmi.getPermissions();

//System.out.println("the size of permissions = " + permissions.length);

org.astrogrid.registry.client.query.ResourceData[] communityServices = pmr.resolve();

%>
<html>
<head>
      <title>Permission Administration</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

      <p>
         <strong><font color="blue"><%=info%></font></strong><br />
         Permission Member administration page, here you can add, edit, or delete accounts to groups.
         <%
         	if(groups == null || groups.length == 0 || resources == null || resources.length == 0) {
	         	if(groups == null || groups.length == 0) {
	         		out.write("<br /><font color='red'>No groups found for adding permissions</font><br />");
	         	}
	         	if(resources == null || resources.length == 0) {
	         		out.write("<br /><font color='red'>No resources found for adding permissions</font><br />");
	         	}         
         	}
         else { %>
         <form method="get" />
            <input type="hidden" name="AddPermission" value="true" />
            <strong>Groups from <%= currentCommunity %> Community:</strong>
            <select name="group">
               <% 
               boolean foundMulti = false;                              
               for(int i = 0;i < groups.length;i++) { 
	        				if(GroupData.MULTI_TYPE.equals(((GroupData)groups[i]).getType())) {	        				
	        				   foundMulti = true;            
					%>	        				      
                  <option value="<%= ((GroupData)groups[i]).getIdent() %>">
                     <%= ((GroupData)groups[i]).getDisplayName() %>
                  </option>
               <% } } %>
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
				<%
              if(!foundMulti) {
                out.write("<font color='red'>No MULTI groups found, meaning you cannot add permissions to MULTI groups." +
                            " Which is all that is allowed at this moment, later it can be single/individual groups." +
                            " No submit will be seen.</font><br />");
              } else {  %>
            	<input type="submit" name="AddPermissionSubmit" value="Add Permission" />
              <% } %>
         </form>
         <% } %>
      <br />
      Get Groups from another community
         <form method="get" />      
            <input type="hidden" name="GetCommunityGroups" value="true" />
            <strong>Communities:</strong>
            <select name="community">
               <% for(int i = 0;i < communityServices.length;i++) { %>
                  <option value="<%= ((org.astrogrid.registry.client.query.ResourceData)communityServices[i]).getIvorn() %>">
                     <%= ((org.astrogrid.registry.client.query.ResourceData)communityServices[i]).getTitle() %> -- <%= ((org.astrogrid.registry.client.query.ResourceData)communityServices[i]).getIvorn().toUri().getAuthority() %>
                  </option>
               <% } %>
            </select>            
            <input type="submit" name="GetCommunityGroupsSubmit" value="Get Groups" />
         </form>
      <%
        if(permissions != null && permissions.length > 0) { 
      %>
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
          }
         PolicyPermission pp = null;
        if(permissions != null && permissions.length > 0) 
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