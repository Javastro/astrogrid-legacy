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
   String query = request.getParameter("AdqlXml");

   FullSearcher delegate = DatacenterDelegateFactory.makeFullSearcher(serviceURL.toString());
   //Reader reader = new StringReader(query);
   //Select select = Select.unmarshalSelect(reader);
   InputStream is = new ByteArrayInputStream(query.getBytes());
   Element elem = XMLUtils.newDocument(is).getDocumentElement();
   DatacenterResults results = delegate.doQuery(delegate.VOTABLE,elem);

   Element votable = results.getVotable();
   XMLUtils.ElementToWriter(votable,out);

%>
