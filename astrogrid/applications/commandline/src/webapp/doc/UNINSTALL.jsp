<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Command-line-application server - Uninstallation</title>
<%@ include file="../inc/header.jsp" %>
<div id="bodyColumn">
  <div class="contentBox">
    <div class="section"><a name="Deregistration"></a>
      <h2>Deregistration</h2>
      <p> You should deregister any resource for which you intend to cease service.
        For the CEC you should </p>
      <ol>
        <li><em>definitely</em> deregister the service;</li>
        <li><em>possibly</em> deregister the applications.</li>
      </ol>
      <p> You should only deregister the applications if you originally registered them and if
        no other application is providing those applications. </p>
      <p> In each case, deregistration means that you edit a resource document in the registry, changing
        its status to "deleted". Please refer to the <a href="REGISTER.jsp">registration guide</a> for instructions. </p>
    </div>
    <div class="section"><a name="Uninstallation"></a>
      <h2>Uninstallation</h2>
      <p> To uninstall the CL-CEC, you must remove its web-application from Tomcat.
        Use the Tomcat "manager" application to "undeploy" the web-application.
        Please see the Tomcat documentation for details. </p>
    </div>
  </div>
</div>
<%@ include file="../inc/footer.jsp" %>

