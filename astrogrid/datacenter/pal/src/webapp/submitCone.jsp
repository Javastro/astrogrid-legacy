<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       java.net.URL,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.query.ConeQuery,
       org.astrogrid.store.Agsl,
       org.astrogrid.datacenter.queriers.TargetIndicator,
       org.astrogrid.datacenter.service.HtmlDataServer"
   isThreadSafe="false"
   session="false" %>
<html>
<head><title>Submitting Query</title></head>
<body>
<%@ page language="java" %>
<%!
    HtmlDataServer server = new HtmlDataServer();
%>
<%
   /*
    * Non-blocking cone search
    */
   String param_ra = request.getParameter("RA");
   String param_dec = request.getParameter("DEC");
   String param_sr = request.getParameter("SR");
   String resultsFormat = request.getParameter("Format");
   String resultsTarget = request.getParameter("Target");

   try {
      double ra = Double.parseDouble(param_ra);
      double dec = Double.parseDouble(param_dec);
      double sr = Double.parseDouble(param_sr);

      TargetIndicator target =server.makeTarget(resultsTarget, out);

      if (resultsTarget == null) {
         server.askQuery(Account.ANONYMOUS, new ConeQuery(ra, dec, sr), out, resultsFormat);
      }
      else {
         String id = server.submitQuery(Account.ANONYMOUS, new ConeQuery(ra, dec, sr), target, resultsFormat);
      
         URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
         //indicate status
         out.write("Cone Query has been submitted, and assigned ID "+id+".  <a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
         //redirect to status
         out.write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+statusUrl+"?ID="+id+"'>");
      }
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Searching Cone (RA="+param_ra+", DEC="+param_dec+", SR="+param_sr+") -> "+resultsTarget,th));
   }
      

%>
</body>
</html>

