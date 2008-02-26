<%@ page import="org.astrogrid.community.common.policy.data.AccountData,
                 org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.store.Ivorn,                 
                 org.astrogrid.config.SimpleConfig,
                 org.astrogrid.community.server.security.manager.SecurityManagerImpl,
                 org.astrogrid.community.server.policy.manager.PolicyManagerImpl,                 
                 org.astrogrid.community.server.policy.manager.AccountManagerImpl"
    session="true" %>
    
    


<%

//put add account link at the top
//get a list of accounts and put a edit and remove beside them.
//System.out.println("Creating new AccountManagerImpl");
//AccountManagerImpl ami = new AccountManagerImpl();
PolicyManagerImpl ami = new PolicyManagerImpl();
SecurityManagerImpl smi = new SecurityManagerImpl();


String removeAccount = request.getParameter("RemoveAccount");
String addAccount = request.getParameter("AddAccount");
String editAccount = request.getParameter("EditAccount");
String currentCommunity = Ivorn.SCHEME + "://" + CommunityIvornParser.getLocalIdent();
String info = "";
String ident = null;
AccountData changeAccount = null;
boolean passwordSet = false;
String passwordTemp = null;
String loginName = null;
String accountIvorn = null;

if(removeAccount != null && removeAccount.trim().length() > 0) {
   accountIvorn = request.getParameter("userLoginName").trim();
   ami.delAccount(accountIvorn);
   //MyProxyBean myProxy = new MyProxyBean();
   //myProxy.setUserLoginName("foo");
   //info = "Account was deleted for id = " + accountIvorn +". " +
   //       myProxy.getDeleteCredentialsResult();
}

else if(addAccount != null && addAccount.trim().length() > 0) {
   ident = request.getParameter("userLoginName");
   passwordTemp = request.getParameter("userPassword");
   if(ident == null || ident.trim().length() <= 0 ||
       request.getParameter("userCommonName") == null || 
       request.getParameter("userCommonName").trim().length() <= 0 ||
       passwordTemp == null || passwordTemp.trim().length() <= 0) {   
      info = "Could not add an account no username, password or display name was provided.";
   }else {
      ident = ident.trim();
      String homespace = request.getParameter("homespace");
      if(homespace != null && homespace.trim().length() <= 0) {
      	homespace = null;
      }
      accountIvorn = "ivo://" + ident + "@" + CommunityIvornParser.getLocalIdent();
      changeAccount = new AccountData(accountIvorn);
      changeAccount.setEmailAddress(request.getParameter("email"));     
      changeAccount.setDisplayName(request.getParameter("userCommonName"));
      changeAccount.setDescription(request.getParameter("description"));
      changeAccount.setHomeSpace(homespace);    
      ami.addAccount(changeAccount);
      info = "Account was added for id = " + ident;
      passwordSet = smi.setPassword(accountIvorn, passwordTemp.trim());
      if(passwordSet) {
         info += " And password set. ";
      }else {
         info += " Error on setting password. ";
      }
   }
}else if(editAccount != null && editAccount.trim().length() > 0) {
   accountIvorn = request.getParameter("userLoginName").trim();
   changeAccount = new AccountData(accountIvorn);
   changeAccount.setEmailAddress(request.getParameter("email"));
   changeAccount.setDisplayName(request.getParameter("userCommonName"));
   changeAccount.setDescription(request.getParameter("description"));
   changeAccount.setHomeSpace(request.getParameter("homespace"));    
   ami.setAccount(changeAccount);   
   info = "Account was updated for id = " + ident;
   passwordTemp = request.getParameter("userPassword");
   if(passwordTemp != null && passwordTemp.trim().length() > 0) {
      passwordSet = smi.setPassword(accountIvorn,passwordTemp.trim());
      if(passwordSet) {
         info += " And password set. ";
      }else {
         info += " Error on setting password. ";
      }
   }//if
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
         Account administration page, here you can add, edit, or delete accounts.<br />
         On Add Accounts: Leave Homespace blank, to have your account added to Myspace, otherwise it is assumed you already
         have an account on myspace and it does not need to add your new account to the Myspace service.
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
               <strong>
                 Password
               </strong>
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
                  <input type="text" name="userLoginName" />
               </td>
               <td>
                  <input type="text" name="userCommonName" />
               </td>
               <td>
                  <input type="password" name="userPassword" />
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
      <%
      if(accounts != null && accounts.length > 0) {
      %>
      List of accounts:<br />
      <i>* Blank Passwords means no change in Password and will remain the same.
           Passwords can only be set they cannot be seen. </i><br />
      <table>
         <tr>
            <td>
               Username
            </td>
            <td>
               Display Name
            </td>
            <td>
                 Password
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
          }
         AccountData ad = null;
         if(accounts != null && accounts.length > 0)
         for(int i = 0;i < accounts.length;i++) {
            ad = (AccountData)accounts[i];
            String userName = new CommunityIvornParser(ad.getIdent()).getAccountName();
      %>
         <tr>
            <form method="get">
               <td>
                  <%=userName%>
               </td>
               <td>
                  <input type="text" name="userCommonName" value="<%=ad.getDisplayName()%>" />
               </td>
               <td>
                  <input type="password" name="userPassword" />
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
                  <input type="hidden" name="userLoginName" value="<%=ad.getIdent()%>" />               
                  <input type="hidden" name="EditAccount" value="true" />
                  <input type="submit" name="EditAccountSubmit" value="Edit" />
               </td>
            </form>
            <form method="get">
               <td>

                  <input type="hidden" name="RemoveAccount" value="true" />
                  <input type="hidden" name="userLoginName" value="<%=ad.getIdent()%>" />
                  <input type="submit" name="RemoveAccountSubmit" value="Remove" />
               </td>
            </form>
            <form method="post" action="ca">
              <td>
                <input type="hidden" name="username" value="<%=userName%>"/>
                <input type="hidden" name="commonname" value="<%=ad.getDisplayName()%>"/>
                <input type="submit" name="GenerateCredentialsSubmit" value="Issue ID certificate"/>
              </td>
            </form>
         </tr> 
      <%
         }
      %>
      </table>
   </body>  
</html>