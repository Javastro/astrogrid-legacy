<%@ include file="top.html" %>

<% 
String ra="";
if (request.getParameter("ra") != null && !request.getParameter("ra").equals("")){
ra=request.getParameter("ra");
}
String dec="";
if (request.getParameter("dec") != null && !request.getParameter("dec").equals("")){
dec=request.getParameter("dec");
}
String size="";
String radius="";
if (request.getParameter("radius") != null && !request.getParameter("radius").equals("")){
radius=request.getParameter("radius");
try {
if (Float.parseFloat(radius) > 15) {
size="30";
}
else {
size=String.valueOf((Float.parseFloat(radius)*2));
}
}
catch (Exception e)
    {
    };
    }
    
String sysB="";
String sysJ="";
String sysG="";
if (request.getParameter("sys") != null && !request.getParameter("sys").equals("")){
String sys=request.getParameter("sys");
if (sys.equalsIgnoreCase("J")) {
sysJ="selected";
}
if (sys.equalsIgnoreCase("B")) {
sysB="selected";
}
if (sys.equalsIgnoreCase("G")) {
sysG="selected";
}
}
%>


<h2>Image/pixel-map extraction</h2>

This form provides access to the SuperCOSMOS pixel data<p>

<form action="http://www-wfau.roe.ac.uk/~sss/cgi-bin/pixel_ssa.cgi" method=POST>
<hr>
<table class="form" border="0">
<tr>
<td>
<table border="2" cellspacing="3" cellpadding="3" >
<tr>
<td nowrap align="right"><b>RA</b> or <b>Galactic Long.:</b></td>
<td><input type="text" name=ra size=15 value="<%=ra %>"></td>
<td rowspan="2" > sexagesimal format or decimal degrees </td>
<tr>
<td nowrap align="right"><b>Dec</b> or <b>Galactic Lat.:</b></td>
<td><input type="text" name=dec size=15 value="<%=dec %>"></td>
<tr>
<td nowrap align="right"><b>Coordinate System:</b></td>
<td >
<select name="equinox" >
<option value="2" <%=sysJ%>> J2000
<option value="1" <%=sysB%>> B1950
<option value="3" <%=sysG%>> Galactic
</select>
</td>
<td>&nbsp;</td>

<tr>
<td align="right"><b>Size of extracted box:</b></td>
<td><input type="text" name=size size="5" value="<%=size%>	"></td>
<td> in arcminutes (maximum 30) </td>
<tr>
<td align="right"><b>Survey/Waveband:<b></td>
<td><select name="waveband">
<option value="J">B : UKST Blue: -90 &lt; Dec &lt; +2.5
<option value="R">R2 : UKST Red: -90 &lt; Dec &lt; +2.5
<option value="I">I : UKST near IR -90 &lt; Dec &lt; +2.5    
<option value="E">R1 : ESO Red: -90 &lt; Dec &lt; -17.5
<option value="P">R1 : POSS I Red: -20.5 &lt; Dec &lt; +2.5
</select> </td>
<td>&nbsp;</td>
</table>
</td>
</table>


<p>
Do you wish to see a GIF image of the result (takes more time)? 
<input type="radio" name="gif" value="0" checked>no
<input type="radio" name="gif" value="1">yes
<p>

Assuming the required area of sky, in the colour/survey requested, is
available on-line a link to a gzipped FITS image is returned.
<p>
<hr>

When you have completed the form, click on the Submit button below.<p>
   <input type=submit value="Submit">   <input type=reset  value="Clear">

</form>







<%@ include file="bottom.html" %>

</font></i><p>
</center> </table> </BODY> 
</html>