<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       org.w3c.dom.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.io.*,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.query.ConeQuery,
       org.astrogrid.datacenter.service.HtmlDataServer"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%!
    HtmlDataServer server = new HtmlDataServer();
%><%
   /*
    * Simple image access protocol - similar to cone search but different
    * (and awkward) position format for some wierd reason.
    */
   try {
      String pos = request.getParameter("POS");
      double size = Double.parseDouble(request.getParameter("SIZE"));
      String formatList = request.getParameter("FORMAT");

      int comma = pos.indexOf(",");
      double ra = Double.parseDouble(pos.substring(0,comma));
      double dec = Double.parseDouble(pos.substring(comma+1));
      
      try {
         server.askQuery(Account.ANONYMOUS, new ConeQuery(ra, dec, size), out, formatList);
      } catch (Exception e) {
         LogFactory.getLog(request.getContextPath()).error(e);
         out.write(server.exceptionAsHtml("SIAP; Searching Cone (RA="+ra+", DEC="+dec+", SIZE="+size+", FORMAT="+formatList, e));
      }
      
   } catch (NumberFormatException e) {
      out.write(server.exceptionAsHtml("Input Error", e));
   }


%>

