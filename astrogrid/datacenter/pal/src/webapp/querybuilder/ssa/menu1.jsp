<%@ page language="java" import="java.util.*, net.mar.* " %>

<%@ include file="top.html" %>

<h2>SQL by Menu Step 3</h2>

Uncheck any parameters you do not want to select from the database table. NB You must leave 
at least one parameter selected unless you check the <b>count</b> box.
<p>
The upper and lower limit constraints are used to construct the SQL <b>where</b> clause. Again
you must supply at least one operator and value. You can apply constraints to parameters <b>not selected</b>.
<p>
<hr>
<% 
String str = request.getParameter("action");
String extras = request.getParameter("extras");
String from = request.getParameter("from");
String info = request.getParameter("info");
int noTokens=0;
int noParams=0;
String [] extraCols;

%>
<form action="menu2.jsp" method="post">

<table border="0" class="form">
<tr>
<td>

<table border="1">
<tr>
<td align="center" rowspan="3">Parameter</td>
<td rowspan="3">Select</td>
<td colspan="4" align="center"> Constraints </td>
<tr>
<td colspan="2" align="center">Lower Limit</td>
<td colspan="2" align="center">Upper limit</td>
<tr>
<td align="center">oper.</td>
<td align="center">value</td>
<td align="center">oper.</td>
<td align="center">value</td>
<% if (info.equals("full")){ %>
<td align="center">units</td>
<%
}
    StringBuffer sb = new StringBuffer("");
   
    String s="";
	String qcrit="";
	String checked="";
        String units="";
        String par="";    
		      if (request.getParameter("mselect") != null)
	      {
	        String[] values = request.getParameterValues( "mselect" );
	        noParams=values.length;
            for ( int i = 0; i < values.length; ++i )
            { 
            qcrit="";
            checked="";
            par=values[i]+"_units";
            if (request.getParameter(par) != null){
                 units = request.getParameter(par);
              }
            else {
                 units="&nbsp;";
		}
            if (units.equals(" ")){
                 units="&nbsp;";
		}

	                if (values[i].trim().toLowerCase().startsWith("qual")){
	                qcrit="2048";
	                checked="selected";
}           
            %>
<tr>
<td align="right">
<%=values[i]%> 
</td>
<td align="center">
<input type="checkbox" name="select<%=i%>" value="<%=values[i]%>" checked></td>
<td><select name="minOper<%=i%>">
<option value="">
<option value="<%=values[i]%> > ">&gt;      
<option value="<%=values[i]%> >= ">&gt;= 
<option value="<%=values[i]%> = ">= 
</select>
</td>
<td>
<input type="text" size="11" name="minValue<%=i%>" >
</td>
<td><select name="maxOper<%=i%>">
<option value="">
<option <%=checked%> value="<%=values[i]%> < ">&lt;     
<option  value="<%=values[i]%> <= ">&lt;=
<option value="<%=values[i]%> = ">= 
</select>
</td>
<td>
<input type="text" size="11" name="maxValue<%=i%>" value="<%=qcrit%>">
</td>

<% if (info.equals("full")){ %>
<td><%=units%></td>

            <%
}
              
				/*if (i > 0)
				{
					sb.append(",");
				}
				sb.append(values[i]);*/
			}
			str=sb.toString();
		}  %>

<%
		
		if (extras != null){
		StringTokenizer tok = new StringTokenizer(extras,",");
		noTokens=tok.countTokens();
		extraCols=new String[noTokens];
		for (int j = 0; j < noTokens; ++j){
		
		extraCols[j]=tok.nextToken(); 
		extraCols[j]=extraCols[j].trim().toUpperCase(); 
		if (extraCols[j].indexOf(" AS ") > 0){
		extraCols[j]=extraCols[j].substring(0,extraCols[j].indexOf(" AS "));
		extraCols[j]=extraCols[j].trim();
		
		}
		%>
<tr>
<td align="right">
<%=extraCols[j]%> 
</td>
<td align="center"><input type="checkbox" name="select<%=noParams%>" value="<%=extraCols[j]%>" checked></td>
<td><select name="minOper<%=noParams%>">
<option value="">
<option value="<%=extraCols[j]%> > ">&gt;      
<option value="<%=extraCols[j]%> >= ">&gt;= 
<option value="<%=extraCols[j]%> = ">= 
</select>
</td>
<td>
<input type="text" size="11" name="minValue<%=noParams%>">
</td>
<td><select name="maxOper<%=noParams%>">
<option value="">
<option  value="<%=extraCols[j]%> < ">&lt;     
<option  value="<%=extraCols[j]%> <= ">&lt;=
<option value="<%=extraCols[j]%> = ">= 
</select>
</td>


<td>
<input type="text" size="11" name="maxValue<%=noParams%>">
</td>
<tr>

		<%
		noParams=noParams+1;
		}
		}
		
		
		%>

</table>
</td>
</table>
<p>

<% 
if (noParams < 1 ){
%>
<h2>Warning: No parameters have been selected or entered, go back and select at least one</h2> 
</form>
<%@ include file="bottom.html" %>
</font></i><p>
</center> </table> </BODY> 
</html>
<% return;
}
%>

<table cellspacing="3">
<tr>
<td align="right" nowrap><b>Logical operator:</b></td>
<td align="center"><select name=logic>
<option value="and">and
<option value="or">or
</select></td>
<td>the upper and lower limits chosen above will be combined using this operator</td>
<tr>

<td align="right"><b>Or Count only:</b></td>
<td align="center"><input type="checkbox" name="count" value="count"> </td> <td>check this box to get the number of rows
returned by the query (row_count) rather than the rows themselves</td>
</table>

<input type="hidden" name="action" value="menu">
<input type="hidden" name="noParams" value="<%=noParams%>">
<input type="hidden" name="from" value="<%=from%>">
<hr>
<p>
<input class="FontSans" type="submit" name="show" value="Show & Edit SQL" /> or choose
ouput parameters below and execute query.
<p>
<hr>





<table border="0" cellspacing="0" cellpadding="5" >
<tr>

<td>
<table border="0" cellpadding="5" cellspacing="4">
<tr>
<td><b>Database:</b></td>
<td align="left">
<select name="server">

<option value="pmachine">Personal SSA

<option value="amachine">Full SSA

</select>
</td>
</table>
</td>
<tr>


<td><b>Email Address:</b></td>
<td><input type=text name="emailAddress" value="" size="20"> 
the results of long running queries will be sent by email.</td> 
<tr>
<td align="left" valign="middle"><b>Data Format:</b></td>
<td>
<table border="0" cellspacing="0" cellpadding="3"><tr>
<td><input type="radio" name="format" value="HTML" checked="checked" /></td>
<td>HTML table summary (select number of rows returned below)</td></tr><tr>
<td><input type="radio" name="format" value="CSV" /></td>
<td>ASCII File (downloadable with HTML table 
summary on-screen)</td>
<tr><td><input type="radio" name="format" value="FITS" /></td>
<td>FITS File (downloadable with HTML table 
summary on-screen)</td>
<tr><td><input type="radio" name="format" value="VOT" /></td>
<td>VOTable File (downloadable with HTML table 
summary on-screen)</td>
</table>
</td></tr><tr>
<td>&nbsp;</td>
<td>(<em>The number of rows written to the downloadable files is subject to an 
upper limit.</em>)</td><tr>
<td align="left" valign="middle"><b>File Compression:</b></td>
<td><table border="0" cellspacing="0" cellpadding="5" ><tr>
<td><input type="radio" name="compress" value="NONE" /></td>
<td>(none)</td>
<td><input type="radio" name="compress" value="GZIP" checked="checked"/></td>
<td>GZIP</td></tr>
</table>
</td></tr>
<tr> <td align="left" valign="middle"><b>HTML rows:</b></td><td>
<table border="0" cellspacing="0" cellpadding="5" ><tr>
<td><input type="text" name="rows" size="6" maxlength="6" value="30" /></td>
<td>Number of rows written to HTML table summary (maximum 100)</td>
</tr></table></td></tr></table>



 
 <input class="FontSans" type="submit" name="submit" value="Submit" />
 <input class="FontSans" type="reset" value="Reset" />
      
    </form>

<%@ include file="bottom.html" %>

</font></i><p>
</center> </table> </BODY> 
</html>



