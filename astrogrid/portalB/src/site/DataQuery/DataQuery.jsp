<%@ page import = "java.util.*" %>
<%@ page import = "org.astrogrid.portal.query.DataSetInformation" %>
<%@ page import = "org.astrogrid.portal.query.DataSetColumn" %>

<%
	Iterator info;
	java.util.ArrayList dataSets = (ArrayList)application.getAttribute("DataSetArrayList");
	String userName = "";
	String community = "";
	if(request.getParameter("username") != null) {
		userName = request.getParameter("username");		
		}else if((String)session.getAttribute("username") != null) {
			userName = (String)session.getAttribute("username");
		}
		if(request.getParameter("community") != null) {
			community = request.getParameter("community");
		}else if((String)session.getAttribute("community") != null) {
			community = (String)session.getAttribute("community");
		}	
	if(dataSets == null || dataSets.size() <= 0 ) {
%>
	<jsp:forward page="/DataQuery">
		<jsp:param name="username"  value = "<%=userName%>" />
		<jsp:param name="community" value = "<%=community %>" />
	</jsp:forward>
<%}%>


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

<jsp:include page="DataQueryHeader.html" />

<table cellpadding="0" cellspacing="0" border="0" style="width: 100%;">
	<tr>
		<td style="vertical-align: top;">
			<jsp:include page="DataQueryMenu.html" />
		</td>
        <td align="left" width="100%" style="vertical-align: top;" rowspan="2">
        	<table cellpadding="2" cellspacing="2" border="0" style="width: 100%; margin-left: auto; margin-right: auto;">
        		<tbody>
                	<tr align="center" valign="middle" bgcolor="#000000">
                    	<td  width="0" height="0" rowspan="0" colspan="0"
                    	     style="color: white; font-family: arial, helvetica, sans-serif; 
                           	 font-size-adjust: -2; font-weight: bold; height: 30px; width: 200px; ">
                                 Data Query
                        </td>
                        <td width="0" height="0" rowspan="0" colspan="0"
                            style="color: white; font-family: arial, helvetica, sans-serif; 
                           	font-size-adjust: -2; font-weight: bold; height: 30px; width: 200px; ">
                            <a href="DataQueryHelp.html" style="color: #FFFFFF; ">Help</a>
                        </td>
                        <td width="70%">&nbsp;</td>
                    </tr>
                    <tr align="left" valign="top">
                    	<td rowspan="1" colspan="3">
							<%
								String errorMessage = (String)session.getAttribute("ErrorMessage");
								if (errorMessage != null) {
							%>
									<font color="red"><%=errorMessage%></font>
							<%}%>
							<h1>Query Chooser</h1>
							<Form action="/DataQuery/DataQuery" name="DataQueryForm" method="post">
								<input type="hidden" name="username" value="<%=userName %>" />
								<input type="hidden" name="community" value="<%=community %>" />
								<b>Information you want returned:</b>
								<select name="DataSetName" <%if(dataSets != null && dataSets.size() > 0) {%>onChange="updateCols(document.DataQueryForm.DataSetName.selectedIndex,document.DataQueryForm.ReturnColumn)"<%}%>>
								<%
									if(dataSets != null) {
										info = dataSets.iterator();
										while( info.hasNext() ) {
											DataSetInformation dsInfo = (DataSetInformation)info.next();
								%>
									<option value="<%=dsInfo.getName()%>"><%=dsInfo.getName()%></option>
								<%}}%>	
								</select>
								<select name="ReturnColumn">
									<option value="COLUMN-ID">COLUMN-ID</option>
									<option value="COLUMN-ID">COLUMN-ID</option>
								</select>
								<br />
								<input type="submit" name="AddSelection" value="Add Selection" />
								<input type="submit" name="RemoveSelection" value="Remove Selection" />
								<HR />
								<h1>Query Filter</h1>
								<%
									Integer criteriaNumber = (Integer)session.getAttribute("CriteriaNumber");
									if(criteriaNumber != null && criteriaNumber.intValue() > 0) {
								%>
										Use to place a criteria inside a parenthesis place inside the {?}:
										<select name="LinkTo">
											<option value="-1">NONE</option>
											<%for(int k = 0;k < criteriaNumber.intValue();k++) {%>
												<option value="<%=k%>"><%=k%></option>
											<%}%>
										</select>
										<i>* this is required when removing a criteria</i>
										<br />
										<b>Choose the join operation for your next criteria:</b>
										<select name="JoinType">
											<option value="AND">AND</option>
											<option value="OR">OR</option>
										</select>
								<%}%>
								<br />
								<b>Choose a Data set name that matches with your Selection:</b>
								<select name="DataSetNameCriteria" <%if(dataSets != null && dataSets.size() > 0) {%>onChange="updateCols(document.DataQueryForm.DataSetNameCriteria.selectedIndex,document.DataQueryForm.FilterColumn)"<%}%>>
								<%
									if(dataSets != null) {
										info = dataSets.iterator();
										while( info.hasNext() ) {
											DataSetInformation dsInfo = (DataSetInformation)info.next();
								%>
											<option value="<%=dsInfo.getName()%>"><%=dsInfo.getName()%></option>
								<%}}%>	
								</select>
								<br />
								<b>Choose a Column, UCD, Function to filter on:</b>
								<select name="FilterColumn">
									<option value="Fi1">FilterColumn1</option>
									<option value="Fi2">FilterColumn2</option>
								</select>
								<br />
								<b>Pick a Operator you desire to filter against the value:</b>
								<select name="Operator">
									<option value="EQUALS">EQUALS</option>
									<option value="LESS_THAN">Less Than</option>
									<option value="LESS_THAN_OR_EQUALS">Less Than Or Equals</option>
									<option value="GREATER_THAN">Greater Than</option>
									<option value="GREATER_THAN_OR_EQUALS">Greater Than Or Equals</option>
									<option value="LESS_THAN">Less Than</option>	
									<option value="NONE">NONE</option>
								</select>
								<i>* A NONE is provided in case certain functions do not require an operator</i>
								<br />
								Only to be used for Functions, this is a comma seperated values list:
								<input type="text" name="FunctionValues" />
								<i>* ex:  44,56.98,22.33</i>
								<br />
								Please input the value to filter against:
								<input type="text" name="Value" />
								<br />
								<input type="submit" name="AddCriteria" value="Add Criteria" />
								<input type="submit" name="RemoveCriteria" value="Remove Criteria" />
								<% 
									String queryString = (String)session.getAttribute("QueryString");
									if(queryString != null) {
								%>
										<hr />
										<h1>The Query</h1>
										<%=queryString%>
										<br />
										<input type="submit" name="ClearQuery" value="Clear Query" />
										<input type="submit" name="SubmitQuery" value="Send Query" />
								<%}%>
								<br />
								<%
									String sentQueryString = (String)session.getAttribute("QueryStringSent");
									if(sentQueryString != null) {
								%>
										<%=sentQueryString%>
								<% } %>
							</Form>
							<hr />
							<br />
							<br />
							<i>A look at the last XML string sent to the webservice. Can be seen by doing a "View Source" and looking at the bottom.</i>
							<%
								sentQueryString = (String)session.getAttribute("LastWebServiceXML");
								if(sentQueryString != null) {
							%>
									<!---<%=sentQueryString%>--->
							<%}%>
                        </td>
                     </tr>
                  </tbody>
               </table>
            </td>
         </tr> 
        </tbody>
       </table>
	</body>
</html>