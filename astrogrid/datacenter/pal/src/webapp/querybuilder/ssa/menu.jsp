<%@ page language="java" %>
<%@ include file="top.html" %>
<link href="http://surveys.roe.ac.uk/ssa/www/wsaserver.css" rel="stylesheet" type="text/css">
<h2>SQL by Menu Step 2</h2>
 <form action="http://surveys.roe.ac.uk:8080/ssa/menu1.jsp" method="post">
 In this section you should select the parameters you wish to extract from the table
 and/or apply constraints to.<p>
 <font class="i">Indexed variables</font> are highlighted, searches making
use of indexed quantities will execute faster.<p>
 You must select or enter at least one parameter.
 <p>
 <hr>
 <p>
 <b>Select:</b> 
<p>
<% 
String table = request.getParameter("from");
String info = request.getParameter("info");
String includeFile="SSA_"+table+"_"+info+".html";


%>
<p>
<jsp:include page="<%=includeFile%>" flush="true" /> 

<p>
Extras columns: If you wish to generate extra parameters from arithmetic combinations of those listed above enter them as a comma separated list eg classMagB-classMagR2,lassMagR2-classMagI 


<p><input type="text" name="extras" size="60"   />
<br>

<p>
<input type="hidden" name="from" value="<%=table%>">
<input type="hidden" name="info" value="<%=info%>">
        <input class="FontSans" type="submit" value="Next" />
 <input class="FontSans" type="reset" value="Reset" />
      </p>

</form>


<%@ include file="bottom.html" %>

</font></i><p>
</center> </table> </BODY> 
</html>




