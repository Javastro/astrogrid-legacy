<%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       org.apache.axis.utils.XMLUtils,
       org.astrogrid.datacenter.delegate.*,
       org.astrogrid.io.*,
       org.astrogrid.community.User,
       org.astrogrid.datacenter.adql.generated.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%
  URL serviceURL = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath() + "/services/AxisDataServer");

  // lets assume we're doiong a cone search.
  ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher(User.ANONYMOUS,serviceURL.toString(),DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
  double ra = Double.parseDouble(request.getParameter("ra"));
  double dec = Double.parseDouble(request.getParameter("dec"));
  double rad = Double.parseDouble(request.getParameter("rad"));
  InputStream resultsStream = searcher.coneSearch(ra,dec,rad);
  Piper.pipe(new InputStreamReader(resultsStream),out);

%>
