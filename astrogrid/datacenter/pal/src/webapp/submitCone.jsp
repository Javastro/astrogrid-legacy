<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       java.net.URL,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.query.ConeQuery,
       org.astrogrid.store.Agsl,
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
   String param_sendTo = request.getParameter("SENDTO");
   String param_ra = request.getParameter("RA");
   String param_dec = request.getParameter("DEC");
   String param_sr = request.getParameter("SR");
   
   try {
      double ra = Double.parseDouble(param_ra);
      double dec = Double.parseDouble(param_dec);
      double sr = Double.parseDouble(param_sr);

      Agsl agsl = new Agsl(param_sendTo);

      String id = server.submitQuery(Account.ANONYMOUS, new ConeQuery(ra, dec, sr), agsl, "VOTABLE");
      
      URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
      //redirect to status
      out.write("<a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Searching Cone (RA="+param_ra+", DEC="+param_dec+", SR="+param_sr+") -> "+param_sendTo,th));
   }
      

%>
</body>
</html>

