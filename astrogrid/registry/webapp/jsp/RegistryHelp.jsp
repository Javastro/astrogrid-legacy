<html>
<%@ page import="java.io.InputStream,
                 java.io.IOException,
                 javax.xml.parsers.SAXParser,
                 javax.xml.parsers.SAXParserFactory,
                 org.astrogrid.registry.server.query.RegistryService,
                 org.astrogrid.util.DomHelper,
                 org.w3c.dom.Document"
   session="false" %>
<head>
<title>Registry Install Help</title>
</head>
<body bgcolor='#ffffff'>
<%!

    /**
     * Get a string providing install information.
     * TODO: make this platform aware and give specific hints
     */
    public String queryFromIVO(String authorityID, String resKey,JspWriter out) {

         String selectQuery = "<query><selectionSequence>" +
         "<selection item='searchElements' itemOp='EQ' value='all'/>" +
         "<selectionOp op='$and$'/>" +
         "<selection item='AuthorityID' itemOp='EQ' value='" + authorityID + "'/>";
         if(resKey != null && resKey.length() > 0) {
            selectQuery += "<selectionOp op='AND'/>" + 
            "<selection item='ResourceKey' itemOp='EQ' value='" + resKey + "'/>";
         }
         selectQuery += "</selectionSequence></query>";
         Document doc = DomHelper.newDocument(selectQuery);
         RegistryService rs = new RegistryService();
         Document resultDoc = rs.submitQuery(doc);
         out.write(DomHelper.DocumentToString(resultDoc));
    }





    /**
     * probe for a resource existing,
     * @param out
     * @param resource
     * @param errorText
     * @throws Exception
     */
    int wantResource(JspWriter out,
                      String resource,
                      String errorText) throws Exception {
        if(!resourceExists(resource)) {
            out.write("<p><b>Warning</b>: could not find resource "+resource
                        +"<br>"
                        +errorText);
            return 0;
        } else {
            out.write("found "+resource+"<br>");
            return 1;
        }
    }


    /**
     *  get servlet version string
     *
     */

    public String getServletVersion() {
        ServletContext context=getServletConfig().getServletContext();
        int major = context.getMajorVersion();
        int minor = context.getMinorVersion();
        return Integer.toString(major) + '.' + Integer.toString(minor);
    }


    %>
<html><head><title>Axis Happiness Page</title></head>
<body>
<h1>Registry help page</h1>
<h2>Examining webapp configuration</h2>

<p>
<h3>Query Registry by Ivo</h3>
<%
    int needed=0,wanted=0;


    //should we search for a javax.wsdl file here, to hint that it needs
    //to go into an approved directory? because we dont seem to need to do that.
%>
<%
//    out.write("</h3>");
    %>
    <p>
    <hr/>
    <hr/>
</body>
</html>


