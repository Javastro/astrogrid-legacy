<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.service.DataServer,
       org.astrogrid.datacenter.adql.generated.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%!
    DataServer server = new DataServer();
%><%

   /*
    * NVO Compliant cone search
    */
   double ra = Double.parseDouble(request.getParameter("RA"));
   double dec = Double.parseDouble(request.getParameter("DEC"));
   double sr = Double.parseDouble(request.getParameter("SR"));

   /* Using delegate
   URL serviceURL = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath() + "/services/AxisDataServer");

   ConeSearcher delegate = DatacenterDelegateFactory.makeConeSearcher(serviceURL.toString());

   InputStream results = delegate.coneSearch(Double.parseDouble(ra), Double.parseDouble(dec), Double.parseDouble(sr));
    */
   
   /* Direct server call - and why not? */
   try {
      String results = server.searchCone(Account.ANONYMOUS, ra, dec, sr);

      Piper.bufferedPipe(new StringReader(results), (Writer) out);
   } catch (Exception e) {
      LogFactory.getLog("searchCone").error("searchCone.jsp failed ra="+ra+" dec="+dec+", sr="+sr, e);
      //this should probably be a VOtable error...
      out.println("<html>");
      out.println("<head><title>Cone Search Error</title></head>");
      out.println("<body>");
      out.println("<H1>SERVER ERROR</H1>");
      out.println("<pre>");
      e.printStackTrace(new PrintWriter(out));
      out.println("</pre>");
      out.println("</body>");
      out.println("</html>");
   }
      

%>

