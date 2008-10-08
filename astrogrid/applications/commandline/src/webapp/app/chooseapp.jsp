<%@ page import="java.io.*,
                                 java.net.*,
                 java.util.*,   
                  org.astrogrid.workflow.beans.v1.*,
                 org.astrogrid.applications.service.v1.cea.*,
                 org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType,
                 org.astrogrid.applications.manager.ExecutionController,
                 org.astrogrid.applications.manager.QueryService,
                                 org.astrogrid.config.*,
                                 org.astrogrid.applications.*,
                                 org.astrogrid.applications.description.*,
                                 org.astrogrid.community.User "
     %>
<%@page import="org.astrogrid.applications.component.CEAComponentContainer"%>
<%@page import="org.astrogrid.applications.component.CEAComponents"%>
<%@page import="org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;"%><html>
<head>
<title>Test Run Application</title>
<%@ include file="../inc/header.jsp" %>
<div id='bodyColumn'>

<h2>Test Run Application</h2>
This page allows you to select and run an application served by this CEA server.  <strong>Warning:</strong> use with care - this might
not be a good idea if your application consumes/produces large amounts of data or is long-running.
<h3>Application</h3>
<%
  CEAComponents pcontainer = CEAComponentContainer.getInstance();
  ApplicationDescriptionLibrary library = pcontainer.getApplicationDescriptionLibrary();
  String[] appIds = library.getApplicationNames();
  String[] appNames = new String[appIds.length];
  for (int i=0;i<appIds.length;++i) {
      appNames[i] = library.getDescription(appIds[i]).getName();
  }
  if (appNames.length==0) {
  	out.print("There are no applications installed in this server");
  	return;
  }
  String selectedApp = request.getParameter("application");
  selectedApp = selectedApp !=null ? selectedApp :  appNames[0];
%>
Currently selected application is <%=selectedApp%>.
<form name="chooseapp" action="chooseapp.jsp">
<select onchange="javascript: document.chooseapp.submit()" name="application">
<%
	for (int i=0;i<appNames.length;++i) {
	    out.print("<option value='");
	    out.print(appNames[i].replaceAll("\\s",""));
	    out.print("' ");
		if (appNames[i].replaceAll("\\s","").equals(selectedApp)) {
		   out.print("selected='true'");
		} 
		out.print(">"+appNames[i]+"</option>");
	}
%>
</select>
</form>
<h3>Interface</h3>
<%
  ApplicationDescription description = library.getDescriptionByShortName(selectedApp);
  ApplicationInterface[] interfaces = description.getInterfaces();
  if (interfaces.length==0) {
  	out.print("There are no interfaces installed for this application");
  	return;
  }
  String selectedInterface = request.getParameter("interface");
  selectedInterface = selectedInterface !=null ? selectedInterface :  interfaces[0].getId();
  session.putValue("interfaceObj", description.getInterface(selectedInterface));
  session.putValue("descriptionObj", description);
%>
Currently selected interface is <%=selectedInterface%>.
<form name="chooseinterface" action="../xforms/tool.jsp">
<input type="hidden" name="application"  value="<%=selectedApp%>"/>
<select  name="interface">
<%
	for (int i=0;i<interfaces.length;++i) {
		if (interfaces[i].getId().equals(selectedInterface)) {
		   out.print("<option selected='true'>"+interfaces[i].getId()+"</option>");
		} else {
		   out.print("<option>"+interfaces[i].getId()+"</option>");
		}
	}
%>
</select>
<input type="submit" value="Set Parameters"/>
<h3>Description</h3>
<p><%=description.getDescription()%></p>
</form>
 
</div>
<%@ include file="../inc/footer.jsp" %>
