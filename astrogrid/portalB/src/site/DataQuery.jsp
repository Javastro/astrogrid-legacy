<%

/*
	java.util.ArrayList dataSets = request.getDataSetsInformation();
	QueryBuilder qb = session.getValue("QueryBuilder");
	
*/
	
	 

%>
<html>
<title>Data Query</title>
<body>

<h1>Query Chooser</h1>
<Form action="QueryBuilder" method="post">
<select name="DataSetName">
	<option value="DS1">DataSet1</option>
	<option value="DS2">DataSet2</option>
	<%
	//loop through dataSets showing all the DataSet names.
	%>
</select>
<select name="ReturnColumn">
	<option value="Col1">Column1</option>
	<option value="Col2">Column2</option>
	<%
	//loop through dataSets columns.
	%>
	
</select>
<input type="submit" name="AddSelection" value="Add Selection" />
<input type="submit" name="RemoveSelection" value="Remove Selection" />
<HR />
<h1>Query Filter</h1>
<select name="DataSetName">
	<option value="DS1">DataSet1</option>
	<option value="DS2">DataSet2</option>
	<%
		//loop through the added selection criteria datasets.
	%>
</select>
<select name="FilterColumn">
	<option value="Fi1">FilterColumn1</option>
	<option value="Fi2">FilterColumn2</option>
	<%
	//loop through dataSets columns.
	%>
</select>
<select name="Operator">
	<option value="Equal">Equal</option>
	<option value="Less Than">Less Than</option>
</select>
<input type="text" name="value" />
<input type="submit" name="AddCriteria" value="Add Criteria" />
<input type="submit" name="RemoveCriteria" value="Remove Criteria" />

<HR>
<h1>The Query</h1>

<!--label of the query another words the session.getValue("QueryString");-->

<input type="submit" name="ClearQuery" value="Clear Query" />
<input type="submit" name="SubmitQuery" value="Send Query" />
</Form>
</body>

</html>