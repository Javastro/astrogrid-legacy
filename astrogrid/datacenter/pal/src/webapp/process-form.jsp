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
String query = request.getParameter("query");
if (query != null) { // doing a adql search
	FullSearcher delegate = DatacenterDelegateFactory.makeFullSearcher(serviceURL.toString());
	//Reader reader = new StringReader(query);
	//Select select = Select.unmarshalSelect(reader);
	InputStream is = new ByteArrayInputStream(query.getBytes());
	Element elem = XMLUtils.newDocument(is).getDocumentElement();
	DatacenterResults results = delegate.doQuery(delegate.VOTABLE,elem);

Element votable = results.getVotable();
XMLUtils.ElementToWriter(votable,out);
} else {
  // lets assume we're doiong a cone search.
  ConeSearcher searcher = DatacenterDelegateFactory.makeConeSearcher(User.ANONYMOUS,serviceURL.toString(),DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
  double ra = Double.parseDouble(request.getParameter("ra"));
  double dec = Double.parseDouble(request.getParameter("dec"));
  double rad = Double.parseDouble(request.getParameter("rad"));
  InputStream resultsStream = searcher.coneSearch(ra,dec,rad);
  Piper.pipe(new InputStreamReader(resultsStream),out);

}

%>
