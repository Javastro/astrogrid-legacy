<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.queriers.query.ConeQuery,
       org.astrogrid.datacenter.service.DataServer"
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

   try {
      server.askQuery(Account.ANONYMOUS, new ConeQuery(ra, dec, sr), out);
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Searching Cone (RA="+ra+", DEC="+dec+", SR="+sr, th));
   }
      

%>

