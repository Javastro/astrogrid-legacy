<%-- The store browser page is a frontend to an individual store --%>
<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.*,
                 org.astrogrid.community.User,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<%-- Which store --%>
<% String store = request.getParameter("store"); %>

<%-- Current path --%>
<% String path = request.getParameter("path"); %>

<html>
<head>
<title>Store File Browser</title>
</head>
<body>
<hr>
<div id='address' style='top'>
Current Path:  <%= path %>  (Store <%= store %> )
</div>
<hr>
<div id='toolbar' style='top'>
  <img src='Back.gif' alt='Back'/> *
  <img src='Next.gif' alt='Next'/> *
  <img src='Up.gif' alt='Up'/> |
  <img src='Copy.gif' alt='Copy To'/> *
  <img src='Move.gif' alt='Move To'/> *
  <img src='Delete.gif' alt='Delete'/> |
  <img src='NewFolder.gif' alt='New Folder'/>
  |
  <img src='Refresh.gif' alt='Refresh'/>
</div>
<hr>
<div id='view' align='center' style='float:left'>
   <table border=1>
   <tr><td><a href='browser.jsp?store=myspace:<%= new URL("http", request.getServerName(), request.getServerPort(), request.getContextPath()).toString() %>/services/Manager'>This</a></td></tr>
   <tr><td><a href='browser.jsp?store=myspace:http://cadairidris.star.le.ac.uk:8080/astrogrid-mySpace-Itn05_release/services/Manager'>Cadairidris Itn05</a></td></tr>
   <tr><td><a href='browser.jsp?store=myspace:http://twmbarlwm.star.le.ac.uk:8888/astrogrid-mySpace-SNAPSHOT/services/Manager'>Twmbarlwm Snapshot</a></td></tr>
   <tr><td>VoSpace</td></tr>
   <tr><td>MySpace</td></tr>
   <tr><td>Storepoints</td></tr>
   </table>
</div>
  <div id='files'>
    <%@ include file='listFiles.jsp' %>
  </div>
<div id='footer' style='bottom'>
<hr>
AstroGrid 2002-2004
</div>
</body>
</html>
