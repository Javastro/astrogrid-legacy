<%@ page import = "java.util.*" %>
<%@ page import = "org.astrogrid.portal.query.DataSetInformation" %>
<%@ page import = "org.astrogrid.portal.query.DataSetColumn" %>

<%
	java.util.ArrayList dataSets = (ArrayList)session.getAttribute("DataSetArrayList");
	Iterator info;
%>

<html>
<head>
<title>Data Query</title>

<script>
  <%if(dataSets != null && dataSets.size() > 0) {%>
	var dsColArray = new Array(<%=dataSets.size()%>);
<%}%>
<%
  int tempIncrement = 0;
  int tempColIncrement = 0;
  String val;
  if(dataSets != null && dataSets.size() > 0) {
  for(int i = 0;i < dataSets.size();i++) {
  	tempIncrement = i;
  	DataSetInformation dsVal = (DataSetInformation)dataSets.get(i);
  	%>
  	dsColArray[<%=i%>] = new Array(<%=dsVal.getDataSetColumns().size()%>)
  	<%
  	for(int j = 0;j < dsVal.getDataSetColumns().size(); j++) {
  	  tempColIncrement = j;
  	  DataSetColumn dsCol = (DataSetColumn)dsVal.getDataSetColumns().get(j);
  	  val = dsCol.getType();
  	  if(val != null && val.length() > 0) {val += "-" + dsCol.getName();}
  	  else{val = dsCol.getName();}
  	  
%>
 dsColArray[<%=tempIncrement%>][<%=tempColIncrement%>] = "<%=dsCol.getType()%>-<%=dsCol.getName()%>";
<%
}}
%>
//updateCols(0,document.forms[0].ReturnColumn);
<%
}
%>

function updateCols(sIndex,colObj) {
//alert(sIndex);
while(colObj.length > 0) { colObj.options[colObj.length-1] = null; }
for(var i = 0;i < dsColArray[sIndex].length;i++) {
   //alert(dsColArray[dsObj.selectedIndex].length);
   //alert(dsColArray[dsObj.selectedIndex][i]);
   colObj.options[colObj.length] = new Option(dsColArray[sIndex][i],dsColArray[sIndex][i],false,false);
   //colObj.options[colObj.length] = new Option(dsColArray[dsObj.selectedIndex][i],dsColArray[dsObj.selectedIndex][i],false,false);
}

}

</script>

</head>
<body  <%if(dataSets != null && dataSets.size() > 0) {%> onLoad="updateCols(0,document.forms[0].ReturnColumn);updateCols(0,document.DataQueryForm.FilterColumn)"<%}%>> 


<%
String errorMessage = (String)session.getAttribute("ErrorMessage");
if (errorMessage != null) {
%>
  <font color="red"><%=errorMessage%></font>
<%}%>

<h1>Query Chooser</h1>
<Form action="/DataQuery/DataQuery" name="DataQueryForm" method="post">
<select name="DataSetName" <%if(dataSets != null && dataSets.size() > 0) {%>onChange="updateCols(document.DataQueryForm.DataSetName.selectedIndex,document.DataQueryForm.ReturnColumn)"<%}%>>
	<%
	  if(dataSets != null) {
		info = dataSets.iterator();
		while( info.hasNext() ) {
		  DataSetInformation dsInfo = (DataSetInformation)info.next();
	%>
	<option value="<%=dsInfo.getName()%>"><%=dsInfo.getName()%></option>
	<%}}%>	
	<option value="USNOB">USNOB</option>
</select>

<!---<input type="text" name="ReturnColumn" />--->

<select name="ReturnColumn">
	<option value="COLUMN-ID">COLUMN-ID</option>
	<option value="COLUMN-ID">COLUMN-ID</option>
</select>

<input type="submit" name="AddSelection" value="Add Selection" />
<input type="submit" name="RemoveSelection" value="Remove Selection" />
<HR />
<h1>Query Filter</h1>

<%
  Integer criteriaNumber = (Integer)session.getAttribute("CriteriaNumber");
  if(criteriaNumber != null && criteriaNumber.intValue() > 0) {
%>
<select name="JoinType">
	<option value="AND">AND</option>
	<option value="OR">OR</option>
</select>
<select name="LinkTo">
	<option value="-1">NONE</option>
	<%for(int k = 0;k < criteriaNumber.intValue();k++) {%>
		<option value="<%=k%>"><%=k%></option>
	<%}%>
</select>
<%
}//if
%>
<select name="DataSetNameCriteria" <%if(dataSets != null && dataSets.size() > 0) {%>onChange="updateCols(document.DataQueryForm.DataSetNameCriteria.selectedIndex,document.DataQueryForm.FilterColumn)"<%}%>>
	<%
	  if(dataSets != null) {
		info = dataSets.iterator();
		while( info.hasNext() ) {
		  DataSetInformation dsInfo = (DataSetInformation)info.next();
	%>
	<option value="<%=dsInfo.getName()%>"><%=dsInfo.getName()%></option>
	<%}}%>	
	<option value="USNOB">USNOB</option>
</select>

<select name="FilterColumn">
	<option value="Fi1">FilterColumn1</option>
	<option value="Fi2">FilterColumn2</option>
</select>

<input type="text" name="FunctionValues" VALUE="Only for Functions" />

<br />


<select name="Operator">
	<option value="EQUALS">EQUALS</option>
	<option value="LESS_THAN">Less Than</option>
	<option value="LESS_THAN_OR_EQUALS">Less Than Or Equals</option>
	<option value="GREATER_THAN">Greater Than</option>
	<option value="GREATER_THAN_OR_EQUALS">Greater Than Or Equals</option>
	<option value="LESS_THAN">Less Than</option>	
	<option value="NONE">NONE</option>
</select>
<input type="text" name="Value" />
<input type="submit" name="AddCriteria" value="Add Criteria" />
<input type="submit" name="RemoveCriteria" value="Remove Criteria" />

<HR />
<h1>The Query</h1>

<% 
	String queryString = (String)session.getAttribute("QueryString");
	if(queryString != null) {
%>
<%=queryString%>
<%}%>

<br />
<input type="submit" name="ClearQuery" value="Clear Query" />
<input type="submit" name="SubmitQuery" value="Send Query" />
<br />
<%
	String sentQueryString = (String)session.getAttribute("QueryStringSent");
	if(sentQueryString != null) {
%>
<%=sentQueryString%>
<% } %>
</Form>
</body>

</html>