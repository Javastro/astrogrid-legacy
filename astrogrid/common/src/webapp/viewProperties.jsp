<%@ page session="false" %>

<!--
A web form in which properties of a web-application may be edited.
The properties are read from the deployment descriptor web.xml.
On submitting the form, the new values of the properties are
sent to a separate web-resource which is presumed to update
the depolyment descriptor.
-->

<html>
<head>
<title>Properties</title>
</head>

<body>

<h1>Properties</h1>

<p>
Properties defined as environment entries in the deployment descriptor (<i>web.xml</i>):
</p>

<!-- 
A web form that displays the names, types and current values of the
properties and allows the end-user to change them. Submitting the
form encodes the value of each properties, whether changed or not,
as a form parameter named the same as the property and sends the
request to a separate web-resource that is expected to record the values.
-->
<form method="post" action="updateProperties.jsp">

<!-- 
Build an HTML table of property values by transforming web.xml.
See the Javadoc for the bean class for details.
-->
<jsp:useBean id="tabulator" class="org.astrogrid.common.j2ee.EnvEntriesTabulator"/>
<% tabulator.setContext(application); %>
<jsp:getProperty name="tabulator" property="table"/>

<p>
<input type="submit" value="Update properties"/>
</p>

</form>

<p>
Properties are name-value pairs that you may set to customize this web-application.
Use the form above to change the property values (you cannot remove or add properties).
</p>
<p>
Pushing the "update properties" button writes the property values back to the 
deployment-descriptor file. This means that those values will be used when you next restart the
web application.
</p>

</body>
</html>
