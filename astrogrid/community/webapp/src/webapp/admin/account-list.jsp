<%@ page import="org.astrogrid.community.common.policy.data.AccountData,
                 org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.community.server.policy.manager.AccountManagerImpl,
                 org.astrogrid.community.server.sso.CredentialStore,
                 java.util.List,
                 java.security.cert.X509Certificate"
    session="true" %>
    
    
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
    
      <h1>User accounts</h1>
      
      <p>
        Your community database is  <jsp:include page="database"/>.
      </p>
    
      <h2>Add an account</h2>
      <form action="new-account" method="post">
        <table>
          <tr>
            <td>User name:</td>
            <td><input name="userName" size="32"/></td>
          </tr>
          <tr>
            <td>Common name:</td>
            <td><input name="commonName" size="32"/></td>
          </tr>
          <tr>
            <td>Description:</td>
            <td><input name="description" size="32"/></td>
          </tr>
          <tr>
            <td>Email address:</td>
            <td><input name="email" size="32"/></td>
          </tr>
          <tr>
            <td>Password:</td>
            <td><input type="password" name="password" size="32"/></td>
          </tr>
        </table>
        <input type="submit" value="Add account"/>
      </form>
      
      <h2>Existing accounts</h2>
      <p>
        Blank distinguished-name means no credentials have been generated
        for that account.
      </p>
      <p>
        Blank home-space means that account has no allocated home-space.
      </p>
      <p>
        All details may be changed by following the edit links.
      </p>
    
      <table border="1">
      <tr>
        <th>User name</td>
        <th>Common Name</th>
        <th>Distinguished name</th>         
        <th>Description</th>          
        <th>e-mail</th>
        <th>Home Space</th>
        <th></th>
      </tr>
    
      <%
      String[] accounts = new AccountManagerImpl().getUserNames();
      for(int i = 0;i < accounts.length;i++) {
      %>
      <jsp:useBean id="account" scope="page" class="org.astrogrid.community.webapp.AccountBean"/>
      <jsp:setProperty name="account" property="userName" value="<%=accounts[i]%>"/>
      <tr>
        <td><%=accounts[i]%></td>
        <td><jsp:getProperty name="account" property="commonName"/></td>
        <td><jsp:getProperty name="account" property="distinguishedName"/></td>             
        <td><jsp:getProperty name="account" property="description"/></td>             
        <td><jsp:getProperty name="account" property="email"/></td>
        <td><jsp:getProperty name="account" property="homeSpace"/></td>
        <td><a href="account-update.jsp?userName=<%=accounts[i]%>">edit</a></td>
      </tr>
      <%
      }
      %>
      </table>
    </div>
  </body>  
</html>