<%@ page import="java.io.*,
		 java.net.URL,
		 org.w3c.dom.*,
		 org.apache.axis.utils.XMLUtils,
		 org.astrogrid.datacenter.delegate.*,
		 org.astrogrid.datacenter.adql.generated.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%

String query = request.getParameter("query");
URL serviceURL = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath() + "/services/AxisDataServer");
AdqlQuerier delegate = DatacenterDelegateFactory.makeAdqlQuerier(serviceURL.toString());
Reader reader = new StringReader(query);
Select select = Select.unmarshalSelect(reader);
DatacenterResults results = delegate.doQuery(delegate.VOTABLE,select);
Element votable = results.getVotable();
XMLUtils.ElementToWriter(votable,out);
%>
