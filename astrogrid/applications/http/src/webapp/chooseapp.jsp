<%@ page import="java.io.*,
                                 java.net.*,
                 java.util.*,   
                 org.picocontainer.*,
                 org.astrogrid.workflow.beans.v1.*,
                 org.astrogrid.applications.beans.v1.parameters.*,
                 org.astrogrid.applications.service.v1.cea.*,
                 org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType,
                 org.astrogrid.applications.manager.ExecutionController,
                 org.astrogrid.applications.manager.QueryService,
                 org.astrogrid.applications.beans.v1.cea.castor.MessageType,
                 org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase,
                                 org.astrogrid.config.*,
                                 org.astrogrid.applications.*,
                                 org.astrogrid.applications.description.*,
                                 org.astrogrid.applications.component.CEAComponentManagerFactory,
                                 org.astrogrid.community.User "
     %>
<html>
<head>
<title>Test Application</title>
</head>
<body bgcolor=#ffffff>

<h1>Test Application</h1>
This page allows you to select and run an application served by this CEA server.  <strong>Warning:</strong> use with care - this might
not be a good idea if your application consumes/produces large amounts of data or is long-running.
<h2>Application</h2>
<%
  PicoContainer pcontainer = CEAComponentManagerFactory.getInstance().getContainer();
  ApplicationDescriptionLibrary library = (ApplicationDescriptionLibrary) pcontainer.getComponentInstanceOfType(ApplicationDescriptionLibrary.class);
  String[] appNames = library.getApplicationNames();
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
		if (appNames[i].equals(selectedApp)) {
		   out.print("<option selected='true'>"+appNames[i]+"</option>");
		} else {
		   out.print("<option>"+appNames[i]+"</option>");
		}
	}
%>
</select>
</form>
<h2>Interface</h2>
<%
  ApplicationDescription description = library.getDescription(selectedApp);
  ApplicationInterface[] interfaces = description.getInterfaces();
  if (interfaces.length==0) {
  	out.print("There are no interfaces installed for this application");
  	return;
  }
  String selectedInterface = request.getParameter("interface");
  selectedInterface = selectedInterface !=null ? selectedInterface :  interfaces[0].getName();
  session.putValue("interfaceObj", description.getInterface(selectedInterface));
  session.putValue("descriptionObj", description);
%>
Currently selected interface is <%=selectedInterface%>.
<form name="chooseinterface" action="chooseinputs.jsp">
<input type="hidden" name="application"  value="<%=selectedApp%>"/>
<select  name="interface">
<%
	for (int i=0;i<interfaces.length;++i) {
		if (interfaces[i].getName().equals(selectedInterface)) {
		   out.print("<option selected='true'>"+interfaces[i].getName()+"</option>");
		} else {
		   out.print("<option>"+interfaces[i].getName()+"</option>");
		}
	}
%>
</select>
<input type="submit" value="Select"/>
<h2>Description</h2>
<%=description.getAppDescription()%>
</form>
 


</body>
</html>
