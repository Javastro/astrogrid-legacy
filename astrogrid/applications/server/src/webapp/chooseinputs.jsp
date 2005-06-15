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
<%
  String selectedApp = request.getParameter("application");
  String selectedInterface = request.getParameter("interface");
%>



<h1>Test Application</h1>
<p>Selected application: <%=selectedApp%>:<%=selectedInterface%></p>
<%
  ApplicationDescription description = (ApplicationDescription) session.getValue("descriptionObj");
  ApplicationInterface ifc = description.getInterface(selectedInterface);    
  session.putValue("interfaceObj", ifc);
%>

<h2>Inputs: </h2>
<form action="runapp.jsp">
<%
  String[] inputs = ifc.getArrayofInputs();
  for (int i=0;i<inputs.length;++i) {
    String input = inputs[i];  // Name of text-entry control for value/URL
    String inputIndirect = input + "Indirect"; // Name of checkbox control for indirection
    String displayName = description.getParameterDescription(input).getDisplayName();
    String paramDescription = description.getParameterDescription(input).getDisplayDescription();
%>
	<p>
		<%=input%> (<%=displayName%>): <%=paramDescription%><br/>
		Indirect?<input type="checkbox" name="<%=inputIndirect%>"/><br/>
		(If indirect, enter the URL for the parameter value below; otherwise enter the value.)<br/>
   		<textarea rows="3" cols="40" name="<%=input%>"><%=description.getParameterDescription(input).getDefaultValue()%></textarea><br/>
	</p>
<%
  }
%>
   	
	<input type="submit" value="execute"/>
</form>
   
<h2>Outputs:</h2>
<%
   		String[] outputs = ifc.getArrayofOutputs();
   		for (int i=0;i<outputs.length;++i) {
   			out.println(outputs[i]+"<br/>");
   		}
  		
   %>
 

<hr />


</body>
</html>
