<%@ page language="java" import="java.util.* , net.mar.*" %>



<% 
    StringBuffer sb = new StringBuffer("");
    String next="";
    if (request.getParameter("submit") != null){
    next=request.getParameter("submit");
    }
    if (next.equals("Submit")){
    RequestDispatcher 
d=getServletContext().getRequestDispatcher("/SSASQL");
    d.forward(request,response);
    }
    %>
    <%@ include file="top.html" %>
    <%
    SQLMenu sqlm = new SQLMenu();
    String sql="";
    try {
     sql=sqlm.setRequest(request);
    }
    catch (AnException ae)
    { String err=ae.getMessage();%>
    <%=err%>
    <%@ include file="bottom.html" %>
    </font></i><p>        
    </center> </table> </BODY> 
    </html>

    <% return;
    
    }%>
    
    <h2>SSA - SQL Query menu form</h2>
 
 <! www-wfau.roe.ac.uk:8080/wsa/SQLRadial>
 <! grendel12.roe.ac.uk:8080/wsa/SQLRadial>
 
 This forms allows you to submit an SQL query to the SSA database.
 
     <form action="http://surveys.roe.ac.uk:8080/ssa/SSASQL" method="post">
                 

       <input type="hidden" name="action" value="freeform" />  
 <hr>
<table class="form">

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
<td>
<table border="0" cellpadding="5" cellspacing="4">
<tr>
<td valign="top">
<b>SQL statement: </b></td>
<td valign="top">
<textarea name="sqlstmt" cols="65" rows="20">
<%=sql%>
</textarea>
</td>
</table> 
</td>
</table>
<p>

<table border="0" cellspacing="0" cellpadding="5" > <tr>
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

      
      <p>
        <input class="FontSans" type="submit" value="Submit" />
 <input class="FontSans" type="reset" value="reset form" />
      </p>
    </form>

 
 
<%@ include file="bottom.html" %>
</font></i><p>


<a href="http://www.mirror.ac.uk/services/validator/">
<img border=0  SRC="http://www-wfau.roe.ac.uk/sss/images/vh40.gif"
 ALT="Valid HTML 4.0!" HEIGHT=31 WIDTH=88></A>



</center> </table> </BODY> 
</html>
