<%@ page contentType="text/html" import="java.net.*, java.util.*, org.apache.soap.*, org.apache.soap.rpc.*"%>

<%

	String usecase_result = null;
	String usecase_result1 = null;
	String XMLString = null;
	String usecase_intro = null;
	String query = null;
	String query_string = null;
	String query_string1 = null;

	usecase_result = "";
	usecase_result1 = "";

	query_string = "";
	query_string1 = "";
	XMLString = "";
	usecase_intro = "";

	String ls_usecase = (String) request.getParameter("useCase");
	String queryElement = (String) request.getParameter("element");
	String queryValue = (String) request.getParameter("queryValue");
        query = "<selection item='" + queryElement + "' itemOp='EQ' value='" + queryValue + "'/>";
	query = "<query><selectionSequence>" + query + "</selectionSequence></query>";

	
/* Call to Registry Service:  */

	Call call = new Call ();
        call.setTargetObjectURI("urn:org.astrogrid.registry.RegistryInterface");
	call.setMethodName ("submitQuery");
	call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
	Vector params = new Vector();
	params.addElement (new Parameter("usecase", String.class, query, null));
	call.setParams (params);
	URL url = new URL("http://msslxy.mssl.ucl.ac.uk:8080/soap/servlet/rpcrouter");
	Response resp = call.invoke(url, "");
	if(resp.generatedFault())
	{
	  Fault fault = resp.getFault();
	  usecase_result = " Fault code: " + fault.getFaultCode();
	  usecase_result = " Fault Description: " + fault.getFaultString();
	}
	else
	{
          Parameter result = resp.getReturnValue();
          XMLString = (String) result.getValue();
	  usecase_result1 = XMLString.replaceAll("<", "&lt");
	  usecase_result = usecase_result1.replaceAll(">", "&gt \n");
	  query_string1 = query.replaceAll("<", "&lt");
	  query_string = query_string1.replaceAll(">", "&gt \n");	  
	}


%>

<html>
<head>
<title>Astrogrid: sample registry web service</title>
</head>

<body bgcolor="white">
<hr width="100%">
<table border="0">
<tr>
<td>
<img src="AGlogo.gif">
</td>
<td>
<h1>Astrogrid Registry Web Service</h1>
<p>
<b>First Draft: Elizabeth Auden, MSSL, 3 June 2003</b>
</td>

</tr>
</table>

<hr width="100%">

<P>
This demo displays the results of an XQuery registry query for Astrogrid Iteration 2:


<br/>

Please fill in the query information to send to the registry.

<form method="post" action="TestRegistry3.jsp">

        <b>Query Element: </b> <input type="textfield" name="element"> 
        <b>Query value: </b> <input type="textfield" name="queryValue">
	<input type="submit" value="Query Registry" />
</form>

<p>
<b>QUERY: </b>
<p>
<pre>
<%= query_string %>
</pre>
<p>
<b>USECASE RESULT: </b>
<p>
<pre>
<%= usecase_result %>
</pre>
<% 
	usecase_result = null;
	usecase_result1 = null; 
	XMLString = null;  

%>
</body>
</html>