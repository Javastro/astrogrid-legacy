<%@ page language="java" import="java.util.*, org.astrogrid.datacenter.metadata.*, org.astrogrid.datacenter.service.HtmlDataServer, org.w3c.dom.*, org.astrogrid.util.* " %>

<%
   String[] searchCols = request.getParameterValues("searchColumns");
   if (searchCols == null) { searchCols = new String[0]; }  //don't have to check for nulls
   String[] resultCols = request.getParameterValues("resultColumns");
   if (resultCols == null) { resultCols = new String[0]; }
   String[] conditionColumns = request.getParameterValues("conditionColumn");
   if (conditionColumns == null) { conditionColumns = new String[0]; }
   String[] conditionOperands = request.getParameterValues("conditionOperand");
   if (conditionOperands == null) { conditionOperands = new String[0]; }
   String[] conditionValues = request.getParameterValues("conditionValue");
   if (conditionValues == null) { conditionValues = new String[0]; }
%>

<Select xmlns="http://tempuri.org/adql" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

<%------------------------------- SELECT ------------------------------------------------%>
<% if (resultCols.length==0) { %>
    <SelectionAll>
<% } else { %>
    <SelectionList>

<%  for (int r=0;r<resultCols.length;r++) { %>
        <ColumnExpr>
            <SingleColumnReference>
                <TableName><%=resultCols[r].substring(0,resultCols[r].indexOf(".")) %></TableName>
                <Name><%=resultCols[r].substring(resultCols[r].indexOf(".")+1) %></Name>
            </SingleColumnReference>
        </ColumnExpr>
<%   } %>

    </SelectionList>
<% } %>

    <TableClause>

<%------------------------------- WHERE ------------------------------------------------%>

<% if (conditionColumns.length > 0) { %>

    <WhereClause>

      <% if (request.getParameter("combine").equals("Intersection")) { %>
         <IntersectionSearch>
      <% } else if (request.getParameter("combine").equals("Union")) { %>
         <UnionSearch>
      <% } else {
          throw new IllegalArgumentException("Unknown (or missing) 'combine' parameter, should be Union or Intersection");
      } %>

      <% for (int condition=0;condition<conditionColumns.length;condition++)
         if ((conditionColumns[condition].length()>0) && (conditionOperands[condition].length()>0)) { %>

        <FirstCondition xsi:type="PredicateSearch">

          <ComparisonPred>

            <FirstExpr xsi:type="ColumnExpr">
              <SingleColumnReference>
                <TableName>
                   <%=conditionColumns[condition].substring(0, conditionColumns[condition].indexOf(".")) %>
                </TableName>
                <Name>
                   <%=conditionColumns[condition].substring(conditionColumns[condition].indexOf(".")+1) %>
                </Name>
              </SingleColumnReference>
            </FirstExpr>

            <Compare>
            <%
                  String op = conditionOperands[condition];
                  if (op.equals("EQ")) out.println("=");
                  else if (op.equals("LT")) out.print("&lt;");
                  else if (op.equals("LTE")) out.print("&lt;=");
                  else if (op.equals("GT")) out.println("&gt;");
                  else if (op.equals("GTE")) out.println("&gt;=");
                  else
                     throw new IllegalArgumentException("Unknown operand "+op);
                  %>
            </Compare>

            <SecondExpr xsi:type="AtomExpr">
               <Value>
                   <NumberLiteral>
                       <ApproxNum>
                           <Value>
                               <%=conditionValues[condition] %>
                           </Value>
                       </ApproxNum>
                   </NumberLiteral>
               </Value>
            </SecondExpr>

          </ComparisonPred>
        </FirstCondition>
      <% } %> <!-- for each condition -->
      
      <% if (request.getParameter("combine").equals("Intersection")) { %>
         </IntersectionSearch>
      <% } else if (request.getParameter("combine").equals("Union")) { %>
         </UnionSearch>
      <% } %>

</WhereClause>
<% } //end if any conditionColumns %>
</TableClause>
</Select>

