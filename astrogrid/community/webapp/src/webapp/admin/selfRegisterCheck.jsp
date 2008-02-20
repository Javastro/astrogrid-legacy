<%@ page import="org.astrogrid.config.SimpleConfig, 
    javax.naming.*,
    org.w3c.dom.*,
    org.astrogrid.util.DomHelper,
    org.astrogrid.registry.client.RegistryDelegateFactory,
    org.astrogrid.registry.client.admin.RegistryAdminService"
    isThreadSafe="false"
    session="true"
%>
<html>
    <head>
        <title>Service registration</title>
        <style type="text/css" media="all">
            @import url("../style/astrogrid.css");
        </style>
    </head>
    <body>
        <%@ include file="header.xml" %>
        <%@ include file="navigation.xml" %>
        <div id='bodyColumn'>
            <h1>Self-registration</h1>
            <p>
                This is a set of generated Resources for the Registry make corrections and hit submit to send to the registry:
                After hitting submit, any errors will be reported just below this line or an exception will be shown:
            </p>
            <p>
                <%
                if (request.getParameter("addFromText") != null && "true".equals(request.getParameter("addFromText")))
                    {
                    RegistryAdminService ras = RegistryDelegateFactory.createAdmin(); 
                    String resource = request.getParameter("Resource");
                    Document entries = DomHelper.newDocument(resource);
                    Document errors = ras.update(entries);
                    if(errors != null)
                        {
                        out.write(DomHelper.ElementToString(errors.getDocumentElement()));
                        }
                    else {
                        out.write("Success in registering resources");
                        }
                    }
                %>
            </p>
            <p>
                Target Registry endpoint is : <code><%= SimpleConfig.getSingleton().getString("org.astrogrid.registry.admin.endpoint") %></code>
            </p>
            <form method="post">
                <p>
                    <input type="hidden" name="addFromText" value="true" />
                    <textarea name="Resource" cols='100' rows='20'>
                      <%@ include file="MetaDataXML0.10.jsp" %>
                    </textarea>
                </p>
                <p>
                    <input name="button" value="Register" type="submit" />
                </p>
            </form>
        </div>
    </body>
</html>
