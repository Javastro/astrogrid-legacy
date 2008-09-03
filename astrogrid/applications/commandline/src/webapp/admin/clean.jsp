<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="org.astrogrid.applications.component.*, org.astrogrid.applications.description.registry.*" errorPage="" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>CEA Working Directory Clean</title>
<%@ include file="../inc/header.jsp" %>
<h2>CEA Cleaning </h2>
<%! String message; %>
<form action="clean.jsp"  method="post" enctype="application/x-www-form-urlencoded" name="cleanwork" id="cleanwork">
<table  border="0" cellspacing="5" cellpadding="2">
  <tr>
    <td align="left">Delete working directories older than</td>
    
    <td><input name="ndays" id="ndays" size="3" value="7"/> days
     </td>
  </tr>
<tr><td colspan="2">
<input name="Delete" type="submit" value="Delete" />
</td></tr>
</table>
</form>
<% if(request.getParameter("ndays") != null)
{

message =  CEAComponentContainer.getInstance().getControlService().deleteOldRuntimeWorkFiles(java.lang.Integer.parseInt(request.getParameter("ndays")));
%>
Deleting directories older than <%= request.getParameter("ndays") %> days resulted in
<%= message %>

<%
 } 
%>
<%@ include file="../inc/footer.jsp" %>
