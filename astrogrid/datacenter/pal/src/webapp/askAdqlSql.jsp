<%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       org.apache.axis.utils.XMLUtils,
       org.astrogrid.datacenter.delegate.*,
       org.astrogrid.datacenter.sql.SQLUtils,
       org.astrogrid.io.*,
       org.astrogrid.datacenter.adql.generated.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%
   URL serviceURL = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath() + "/services/AxisDataServer");
   String query = request.getParameter("AdqlSql");

   FullSearcher delegate = DatacenterDelegateFactory.makeFullSearcher(serviceURL.toString());

   Element queryBody = SQLUtils.toQueryBody(query);
   
   DatacenterResults results = delegate.doQuery(delegate.VOTABLE,queryBody);

   Element votable = results.getVotable();
   XMLUtils.ElementToWriter(votable,out);

%>
