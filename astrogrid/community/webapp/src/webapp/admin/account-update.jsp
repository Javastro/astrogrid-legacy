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
      
      <h1>Update user account</h1>
      
      <p>
        Your community database is  <jsp:include page="database"/>.
      </p>
      
      <jsp:useBean id="account" scope="page" class="org.astrogrid.community.webapp.AccountBean"/>
      <jsp:setProperty name="account" property="userName"/>
      
      <h2>Basic account</h2>
      <form action="account-update" method="post">
        <input type="hidden" name="userName" value="<jsp:getProperty name="account" property="userName"/>"/>
        <table>
          <tr>
            <td>User name:</td>
            <td><jsp:getProperty name="account" property="userName"/></td>
          </tr>
          <tr>
            <td>Common name:</td>
            <td><input name="commonName" size="32" value="<jsp:getProperty name="account" property="commonName"/>"/></td>
          </tr>
          <tr>
            <td>Description:</td>
            <td><input name="description" size="32" value="<jsp:getProperty name="account" property="description"/>"/></td>
          </tr>
          <tr>
            <td>Email address:</td>
            <td><input name="email" size="32" value="<jsp:getProperty name="account" property="email"/>"/></td>
          </tr>
        </table>
        <input type="submit" value="Update basic account"/>
      </form>
      
      <h2>Credentials</h2>
        <form action="account-update" method="post">
          <input type="hidden" name="userName" value="<jsp:getProperty name="account" property="userName"/>"/>
          <p>User's password: <input name="password" type="password" size="16"/></p>
          <p><input type="submit" value="Reset password"/></p>
        </form>
        <p>
          If you reset a password here, you should allocate a new certificate to
          the user in order that the user's credentials carry the same password.
        </p>
        <p>
         Your certificate authority is <jsp:include page="ca"/>.
        </p>
        <form action="ca" method="post">
          <input type="hidden" name="userName" value="<jsp:getProperty name="account" property="userName"/>"/>
          <input type="hidden" name="commonName" value="<jsp:getProperty name="account" property="commonName"/>"/>
          <p>Distinguished name: <jsp:getProperty name="account" property="distinguishedName"/></p>
          <p><input type="submit" value="Allocate new certificate"/></p>
        </form>
        <p>
          If the distinguished name is null, then no certificate has been allocated.
          The user must have a common name and password, and the certificate authority
          must be enabled, before you can allocate a certificate.
        </p>
      
      <h2>VOSpace</h2>
      <form action="account-update" method="post">
        <input type="hidden" name="userName" value="<jsp:getProperty name="account" property="userName"/>"/>
        <p>Home space: <input name="homeSpace" size="64" value="<jsp:getProperty name="account" property="homeSpace"/>"/></p>
        <p><input type="submit" value="Set new home-space"/></p>
      </form>
      <p>
        If the home space is blank then no space has been allocated.
        You need to set the basic account-details before allocating home space.
      </p>
      <p>
        If you set home space to the special value "new", the community will
        allocate space in its preferred VOSpace; it will then replace "new"
        with the correct location. If you set home space to anything else, 
        the community will assume that the space is already allocated and will 
        just record the value you give it.
      </p>
      
      <h2>Remove the account</h2>
      <form action="account-update" method="post">
        <input type="hidden" name="userName" value="<jsp:getProperty name="account" property="userName"/>"/>
        <input type="hidden" name="delete" value="true"/>
         <p><input type="submit" value="Delete this account"/></p>
      </form>
      
    </div>
    
   </body>  
</html>