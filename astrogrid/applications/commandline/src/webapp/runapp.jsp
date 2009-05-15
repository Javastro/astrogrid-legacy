<%@ page import="java.io.*,
                                 java.net.*,
                 java.util.*,   
                 org.astrogrid.applications.manager.ExecutionController,
                 org.astrogrid.applications.manager.QueryService,
                                  org.astrogrid.applications.description.*,
                                 org.astrogrid.applications.component.CEAComponentContainer,
                                 org.astrogrid.applications.component.CEAComponents
                                 "
     %>
<%@page import="org.astrogrid.applications.description.execution.MessageType"%>
<%@page import="net.ivoa.uws.ExecutionPhase"%>
<%@page import="org.astrogrid.applications.description.execution.Tool"%>
<%@page import="org.astrogrid.applications.description.execution.ListOfParameterValues"%>
<%@page import="org.astrogrid.applications.description.execution.ParameterValue"%>
<%@page import="org.astrogrid.applications.uws.UWSUtils"%>
<%@page import="org.astrogrid.security.SecurityGuard"%><html>
<%
    CEAComponents pcontainer = CEAComponentContainer.getInstance();
//  ApplicationDescriptionLibrary library = (ApplicationDescriptionLibrary) pcontainer.getComponentInstanceOfType(ApplicationDescriptionLibrary.class);
//  String[] appNames = library.getApplicationNames();
    ApplicationInterface ifc = (ApplicationInterface) session.getValue("interfaceObj");
    ApplicationDescription description = (ApplicationDescription) session.getValue("descriptionObj");
    String executing = request.getParameter("started");
    String executionId = request.getParameter("executionId");
  
    if (!("true".equals(executing))) {
   		//Create Tool
        Tool tool = new Tool();
   		tool.setId(description.getId());
   		tool.setInterface(ifc.getId());
   		ListOfParameterValues input = tool.getInput();
   		ListOfParameterValues output = tool.getOutput();
   		tool.setInput(input);
   		tool.setOutput(output);
   		
   		String[] inputs = ifc.getArrayofInputs();
   		for (int i=0;i<inputs.length;++i) {
   			String value = request.getParameter(inputs[i]);
   			if (value != null && !(value.equals(""))) { // Ignore unset parameters
 	   			ParameterValue pv = new ParameterValue();
	   			pv.setId(inputs[i]);
	   			pv.setValue(value);
				String indirectionParameter = inputs[i] + "Indirect";
				pv.setIndirect(request.getParameter(indirectionParameter) != null);
	   			input.addParameter(pv);
	   		}
   		}

   		String[] outputs = ifc.getArrayofOutputs();
   		for (int i=0;i<outputs.length;++i) {
	   		ParameterValue pv= new ParameterValue();
	   		pv.setId(outputs[i]);
	   		output.addParameter(pv);
   		}
   		session.putValue("tool",tool);

		ExecutionController cec = pcontainer.getExecutionController();
		//JobIdentifierType jobstepid = new JobIdentifierType();
		String	jobstepid = "foo";
		SecurityGuard sec = UWSUtils.createSecurityGuard(request);
		executionId = cec.init(tool, jobstepid, sec);
		
		//out.print("Execution Id: "+executionId+"<br/>");
		boolean started = cec.execute(executionId, sec);
		executing="true";
		//out.print((started ? "Started OK" : "Failed to start") + "<br/>");
		}
		%>
<head>
<title>Test Application</title>
<script language="JavaScript">
<!--

var sURL = "runapp.jsp?started=<%=executing%>&executionId=<%=executionId%>";

function doLoad()
{
    // the timeout value should be the same as in the "refresh" meta-tag
    setTimeout( "refresh()", 2*1000 );
}
<!--
function refresh()
{
    //  This version does NOT cause an entry in the browser's
    //  page view history.  Most browsers will always retrieve
    //  the document from the web-server whether it is already
    //  in the browsers page-cache or not.
    //  
    window.location.replace( sURL );
}
//-->
</script>
</head>
<body bgcolor=#ffffff onload="doLoad()">

<h1>Executing Application</h1>

   <%

		QueryService qs =  pcontainer.getQueryService();
		MessageType message = qs.queryExecutionStatus(executionId);
		ExecutionPhase phase = message.getPhase();
		out.print(phase +"...");

	
		Tool tool = (Tool) session.getValue("tool");
		out.print("<h2>Inputs</h2>");
		ListOfParameterValues input = tool.getInput();
		ParameterValue[] pvis = input.getParameter();
   		for (int i=0;i<pvis.length;++i) {
			String indirection = (pvis[i].isIndirect())? "indirect" : "direct";
			out.print(pvis[i].getId()+":- " + pvis[i].getValue()+ " (" + indirection +")<br/>");
   		}
		out.print("<h2>Outputs</h2>");
		ListOfParameterValues output = tool.getOutput();
		ParameterValue[] pvos = output.getParameter();
   		for (int i=0;i<pvos.length;++i) {
			out.print(pvos[i].getId()+":- "+pvos[i].getValue()+"<BR/>");
   		}
	%>

<hr />


</body>
</html>
