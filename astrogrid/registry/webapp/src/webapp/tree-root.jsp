<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
      <title>Tree view of IVOA registry</title>
      <style type="text/css" media="all">
        @import url("<%=request.getContextPath()%>/style/astrogrid.css");
      </style>
    </head>
    <body>
      <%@ include file="/style/header.xml" %>
      <%@ include file="/style/navigation.xml" %>
      <div id="bodyColumn">
        <div class="contentBox">
          <h1>Tree view of IVOA registry</h1>
          <p>
            This is a browseable view of the IVOA registry as a tree
            structure. Links ending in a slash lead to branches; 
            links with no slash at the end lead to resource documents for the
            things registered.
          </p>
          <p>
            The branches are represented as web pages
            with further links while the resource documents are in XML. If you
            get a resource document with a utility such as <i>wget</i> you
            get the actual XML text from the registry. If you view it in a web
            browser you see a view of the text turned into HTML for ease of
            reading; use the <i>show source</i> function in your browser to
            see the original XML.
          </p>
          <ul>
            <li><a href="<%=request.getContextPath()%>/tree/">ivo://</a></li>
          </ul>
        </div>
      </div>
    </body>
</html>