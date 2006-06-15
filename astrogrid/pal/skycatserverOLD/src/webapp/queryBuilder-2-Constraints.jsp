<%@ page language="java"
    import="java.util.*, java.io.*,
    org.astrogrid.dataservice.metadata.*,
    org.astrogrid.dataservice.service.DataServer,
    org.w3c.dom.* "
    %>
<!-- %@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %> -->
<!-- returns 'checked' if the given value is in the list of values -->
<%!
   public String getChecked(String values[], String value) {

      if (values != null) {
         for (int i=0;i<values.length;i++) {
            if (values[i].equals(value)) {
               return "checked";
            }
         }
      }
      return "";
   }
%>

<!-- returns 'selected' if the first value is in the second array at the index point.
   returns empty otherwise (or if the index point is out of range) e -->
<%!
   public String getSelected(String value1, String[] values, int index) {

      if ((value1 != null) && (values != null) && (index<values.length) && (value1.equals(values[index]))) {
            return "selected";
      }
      return "";
   }
%>


<%
   String[] searchCols = request.getParameterValues("searchColumn");
   if (searchCols == null) { searchCols = new String[0]; }  //don't have to check for nulls
   String[] resultCols = request.getParameterValues("resultColumn");
   if (resultCols == null) { resultCols = new String[] {"*"}; }
   String[] conditionColumns = request.getParameterValues("conditionColumn");
   if (conditionColumns == null) { conditionColumns = new String[1]; }
   String[] conditionOperands = request.getParameterValues("conditionOperand");
   if (conditionOperands == null) { conditionOperands = new String[0]; }
   String[] conditionValues = request.getParameterValues("conditionValue");
   if (conditionValues == null) { conditionValues = new String[0]; }
   
   if (request.getParameter("AddCondition") != null) {
      //add blank condition column, operand and value
      String[] newCols = new String[conditionColumns.length+1];
      System.arraycopy(conditionColumns, 0, newCols, 0, conditionColumns.length);
      conditionColumns = newCols;
   }

   if (conditionValues.length < conditionColumns.length) {
      String[] newValues = new String[conditionColumns.length];
      System.arraycopy(conditionValues, 0, newValues, 0, conditionValues.length);
      for (int i = conditionValues.length; i <newValues.length; i++) {
         newValues[i] = ""; //rather than null
      }
      conditionValues = newValues;
   }
   
%>
<head>
<title>Query Builder (p2) for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<h1>Query Builder (2 - Constraints)</h1>

This form helps you construct a query without messing about with SQL or ADQL.

 <form method="get" onSubmit="
   this.action=formAction;
   return true;
   ">

<h2>Constraints</h2>
Enter below the constraints you want to put on each of the values.

<p>These will be combined in an
<input type='radio' name='combine' value='Intersection' <%=getChecked(request.getParameterValues("combine"), "Intersection") %> /> intersection (AND),
or a <input type='radio' name='combine' value='Union' <%=getChecked(request.getParameterValues("combine"), "Union") %> /> union (OR).

<p>

<table border="1" cellspacing="3" cellpadding="3">
<%
   //show all conditions, plus one row for adding a new condition
   for (int con=0;con<conditionColumns.length;con++) {
//      if ((conditionColumns[con] != null) && (conditionColumns[con].length() > 0)) {//not empty
%>
         <tr>
         <!-- Drop down list to select table/column -->
         <td>
           <select name="conditionColumn">
            <option value=""> <!-- won't test this condition if this one selected -->
            <% for (int col=0;col<searchCols.length;col++) { %>
              <option value=<%=searchCols[col] %> <%=getSelected(searchCols[col], conditionColumns, con) %> ><%=searchCols[col] %>
            <% } %>
           </select>
         </td>
         <!-- Operand -->
         <td>
           <select name="conditionOperand">
               <option <%=getSelected("", conditionOperands, con) %> value=""> <!-- won't test this condition if this one selected -->
               <option <%=getSelected("GT", conditionOperands, con) %> value="GT">&gt;
               <option <%=getSelected("GTE", conditionOperands, con) %> value="GTE">&gt;=
               <option <%=getSelected("EQ", conditionOperands, con) %> value="EQ">=
               <option <%=getSelected("LT", conditionOperands, con) %> value="LT">&lt;
               <option <%=getSelected("LTE", conditionOperands, con) %> value="LTE">&lt;=
           </select>
         </td>
         <td>
            <input type='text' name='conditionValue' value='<%=conditionValues[con] %>' >
         </td>
         </tr>
<%
  //     } // end if
   } //end con for loop
%>
   <tr>
      <td>
      <input type='submit' value='Add Condition' name='AddCondition' onclick='formAction="queryBuilder-2-Constraints.jsp";'>
      </td>
   </tr>
</table>
<br>
<p>
<input type='submit' value='Next' onclick='formAction="adqlXmlFromQueryBuilder.jsp";'>
</p>

<!-- Preserve all page parameters -->
<%
   for(int i = 0; i < searchCols.length; i++) {
      out.print("  <input type='hidden' name='searchColumn' value='"+searchCols[i]+"' />");
   }
   for(int i = 0; i < resultCols.length; i++) {
      out.print("  <input type='hidden' name='resultColumn' value='"+resultCols[i]+"'  />");
   }
%>

</form>


<hr>
<h2>Debug</h2>
Parameter names in this request:
<p>
<pre>
<%
   Enumeration e = request.getParameterNames();
   while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      String[] values = request.getParameterValues(key);
      out.print(" " + key + " = ");
      for(int i = 0; i < values.length; i++) {
         out.print(values[i] + " ");
      }
      out.println();
   }
%>
</pre>



</div>
<%@ include file="footer.xml" %>

</body>
</html>



