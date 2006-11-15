<?xml version="1.0" encoding="iso-8859-1"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="org.astrogrid.applications.component.*, org.astrogrid.applications.description.registry.*" errorPage="" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>CEA Registry Manipulation</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>
<body>
<h2>CEA Registry Entry
</h2>
<form action="displayRegistry.jsp"  method="get" name="jobsubmit">

<table width="90%" border="0" cellspacing="5" cellpadding="2">
  <tr>
    <td>
      <div align="right">Endpoint</div>
    </td>
    <input name="endpoint" id="endpoint" size="80" default=""/>
    <td>
     </td>
  </tr>
  <tr>
    <td>
      <div align="right">AuthorityID</div>
    </td>
    <td>
    <input name="authorityid" id="authorityid" size="80" default=""/>
    </td>
  </tr>
  <tr>
    <td>
      <div align="right">Server ID</div>
    </td>
    <td>
    <input name="serverid" id="serverid" size="80" default=""/>
    </td>
  </tr>
  <tr>
    <td>
      <div align="right">email address to notify</div>
    </td>
    <td>
      <input name="email" type="text" id="email" size="40" />
    </td>
  </tr>
<tr><td>
<input name="Submit" type="submit" value="submit and run job" />
<input name="workflow" type="submit" value="create workflow" />
</td></tr>
</table>
</form>

</body>
</html>
