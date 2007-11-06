<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New registry entry</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
    </head>
    <body>
      
    <%@ include file="/style/header.xml" %>
    <%@ include file="/style/navigation.xml" %>
    
    <jsp:useBean id="pa" class="org.astrogrid.registry.registration.PublishingAuthorities" scope="page"/>
    
      <div id='bodyColumn'>
        
        <h1>Creating a new registry entry</h1>
 
        <form action="NewIdentifier" method="post">
          <p>
            Formal name for the new entry:
            ivo://
            <select name="authority">
              <%String[] authorities = pa.getAuthorities();%>
              <%for (int i = 0; i < authorities.length; i++) {%>
              <option value="<%=authorities[i]%>"><%=authorities[i]%></option>
              <%}%>
            </select>
            /
            <input type="text" name="resourceKey" size="32"/>
            (<a href="help/naming.html">help</a></td>)
          </p>
          <p>This entry describes:</p>
          <ul>
            <li><input type="radio" name="xsiType" value="vs:CatalogService"/>A virtual observatory service.</li>
            <li><input type="radio" name="xsiType" value="vs:DataCollection"/>A data collection.</li>
            <li><input type="radio" name="xsiType" value="cea:CeaApplication"/>A CEA application.</li>
            <li><input type="radio" name="xsiType" value="va:Application"/>A non-CEA application.</li>
	    <li><input type="radio" name="xsiType" value="vr:Organisation"/>An organization.</li>
	    <li><input type="radio" name="xsiType" value="vr:Resource"/>None of the above; just a generic resource.</li>
	  </ul>
          <p><input type="submit" value="Create this entry"/></p>
        </form>
        <hr/>
        <p>
          If the drop-down menu in the entry name has no choices, then you
          need to configure the registry before entering resources.
        </p>
      </div>
      
    </body>
</html>
