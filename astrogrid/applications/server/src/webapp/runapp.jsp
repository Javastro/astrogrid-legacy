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
<%
    PicoContainer pcontainer = CEAComponentManagerFactory.getInstance().getContainer();
//  ApplicationDescriptionLibrary library = (ApplicationDescriptionLibrary) pcontainer.getComponentInstanceOfType(ApplicationDescriptionLibrary.class);
//  String[] appNames = library.getApplicationNames();
    ApplicationInterface ifc = (ApplicationInterface) session.getValue("interfaceObj");
    ApplicationDescription description = (ApplicationDescription) session.getValue("descriptionObj");
    String executing = request.getParameter("started");
    String executionId = request.getParameter("executionId");
  
    if (!("true".equals(executing))) {
   		//Create Tool
        Tool tool = new Tool();
   		tool.setName(description.getName());
   		tool.setInterface(ifc.getName());
   		Input input = new Input();
   		Output output = new Output();
   		tool.setInput(input);
   		tool.setOutput(output);
   		
   		String[] inputs = ifc.getArrayofInputs();
   		for (int i=0;i<inputs.length;++i) {
	   		ParameterValue pv= new ParameterValue();
	   		pv.setName(inputs[i]);
	   		pv.setValue(request.getParameter(inputs[i]));
			String indirectionParameter = inputs[i] + "Indirect";
			pv.setIndirect(request.getParameter(indirectionParameter) != null);
                        
	   		input.addParameter(pv);
   		}

   		String[] outputs = ifc.getArrayofOutputs();
   		for (int i=0;i<outputs.length;++i) {
	   		ParameterValue pv= new ParameterValue();
	   		pv.setName(outputs[i]);
	   		output.addParameter(pv);
   		}
   		session.putValue("tool",tool);

		ExecutionController cec = (ExecutionController) pcontainer.getComponentInstanceOfType(ExecutionController.class);
		//JobIdentifierType jobstepid = new JobIdentifierType();
		String	jobstepid = "foo";
		executionId = cec.init(tool, jobstepid);
		
		//out.print("Execution Id: "+executionId+"<br/>");
		boolean started = cec.execute(executionId);
		executing="true";
		//out.print((started ? "Started OK" : "Failed to start") + "<br/>");
		}
		%>
<html>
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

		QueryService qs = (QueryService) pcontainer.getComponentInstanceOfType(QueryService.class);
		MessageType message = qs.queryExecutionStatus(executionId);
		ExecutionPhase phase = message.getPhase();
		out.print(phase +"...");

	
		Tool tool = (Tool) session.getValue("tool");
		out.print("<h2>Inputs</h2>");
		Input input = tool.getInput();
		ParameterValue[] pvis = input.getParameter();
   		for (int i=0;i<pvis.length;++i) {
			String indirection = (pvis[i].getIndirect())? "indirect" : "direct";
			out.print(pvis[i].getName()+":- " + pvis[i].getValue()+ " (" + indirection +")<br/>");
   		}
		out.print("<h2>Outputs</h2>");
		Output output = tool.getOutput();
		ParameterValue[] pvos = output.getParameter();
   		for (int i=0;i<pvos.length;++i) {
			out.print(pvos[i].getName()+":- "+pvos[i].getValue()+"<BR/>");
   		}
	%>

<hr />


</body>
</html>
