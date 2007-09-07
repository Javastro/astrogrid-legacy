<%@ page import="java.io.*,
      java.net.URL,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.dataservice.service.*,
       org.astrogrid.dataservice.service.servlet.Register"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title>Register <%=DataServer.getDatacenterName() %></title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='navigation.xml' %>

<div id='bodyColumn'>

<body>

<%
   String phaseTwo = request.getParameter("PhaseTwo");
   String regParam = request.getParameter("RegistryUrl");
   if ("true".equals(phaseTwo)) 
   {
%>

<!-- CONTENT FOR COMPLETED REGISTRATION -->
<h1>Registration results for <%=DataServer.getDatacenterName() %></h1>

<%= new Register().doRegistration(regParam) %>

<%
  }
  else 
  {
%>

<!-- PRE-REGISTRATION PAGE-->
  <h1>Registering <%= DataServer.getDatacenterName() %></h1>

<%
    boolean usingTestPlugin = false;

    // Optionally enable registration (DISABLED when SampleStarsPlugin in use)
    String thePlugin = ConfigFactory.getCommonConfig().getString(
                   "datacenter.querier.plugin");
    if ( (thePlugin != null) && (thePlugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) ) {
       usingTestPlugin = true;
    }

    if (usingTestPlugin == true) { %>
        <p><strong>This DSA/catalog installation is currently using the default
         test SampleStars plugin, which is for local testing purposes only.</strong></p>
         
        <p>You can only register this DSA/catalog installation once you have 
        configured it to access a real data set of your own, rather than the
        SampleStars internal test data.
        </p>

        <% }
        else {
        %>
  <p>
  You can send the Registry details from this datacenter to a remote Registry using this form:
         <form action="register.jsp" method="POST">
          <p>
            Registry URL <input type="text" name="RegistryUrl" size="60" value='<%= ConfigFactory.getCommonConfig().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY, "[WARNING: Property "+RegistryDelegateFactory.ADMIN_URL_PROPERTY+" not set]") %>'/>
            (leave blank to register on the
            <a href='<%= ConfigFactory.getCommonConfig().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY, "[WARNING: Property "+RegistryDelegateFactory.ADMIN_URL_PROPERTY+" not set]") %>'>Default Registry</a>)
            <br />
          </p>
           <p>
             <input type="hidden" name='PhaseTwo' value="true"  />
             <!-- Tell self to go to phase two on submit -->
             <input type="submit" name='Submit' value="Register"  />
           </p>
      </form>
      <p>
      However if there are problems with this, you can click on the XML link to
      the left to get the XML document, copy it into your clip board, then go to
      the entry forms of the relevent Registry and paste it in.
      <p>
<% 

   //put a link to the default registry page
   URL registerUrl = ConfigFactory.getCommonConfig().getUrl(RegistryDelegateFactory.ADMIN_URL_PROPERTY, null);
   if (( registerUrl != null)) {
      String context = registerUrl.getPath().substring(1);
      int endindex = context.indexOf("/");
      if (endindex != -1) {   // Prevent page crashing for erroneous config URL
         context = context.substring(0, endindex);
      }
      //I think the port gets shortcut because it's the same as the hosts....
      //URL indexUrl = new URL(registerUrl.getProtocol(), registerUrl.getHost(), registerUrl.getPort(), context);
      //URL entryFormUrl = new URL(registerUrl.getProtocol(), registerUrl.getHost(), registerUrl.getPort(), context+"/admin/entryForm.html");
      String indexUrl = registerUrl.getProtocol()+"://"+registerUrl.getAuthority()+":"+registerUrl.getPort()+"/"+context+"/";
      String entryFormUrl = registerUrl.getProtocol()+"://"+registerUrl.getAuthority()+":"+registerUrl.getPort()+"/"+context+"/admin/entryForm.html";
      
      out.write("<p>Default Registry ");
      out.write("<a href='"+indexUrl+"'>Main Page</a> and <a href='"+entryFormUrl+"'>Entry Form</a>");
      out.write("</p>");
   }
 }
%>

<%
 }
%>
</div>

<%@ include file='../footer.xml' %>
</body>
</html>
