<%@ page import="org.astrogrid.mySpace.delegate.*,
                 org.astrogrid.mySpace.delegate.helper.Assist,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Query (Full Form)</title>
</head>

<body>
<h1>Query (Full Form)</h1>

<%
  String[] paramNames={"userId","communityId","credential","query"};
  String userId = request.getParameter("userId");
  String communityId = request.getParameter("communityId");
  String credential = request.getParameter("credential");
  String query = request.getParameter("query");
%>


<%
  URL serviceURL = new URL ("http", request.getServerName(),
    request.getServerPort(), request.getContextPath() +
    "/services/MySpaceManager");
%>

<p>
The end point for this service is: <%=serviceURL%>
</p>

<%
  MySpaceClient client = MySpaceDelegateFactory.createDelegate(
    serviceURL.toString());

  Vector results = client.listDataHoldingsGen(userId, communityId,
    credential, query);
%>

<p>
The following entries satisfied query <code><%=query%></code>:
</p>

<pre>
<%
  int resultsSize = results.size();

  if (resultsSize > 0)
  {  for (int i=0; i<resultsSize; i++)
     {  String xmlString = (String)results.elementAt(i);
        Assist assistant = new Assist();
        Vector summaryList = assistant.getDataItemSummary(xmlString);

        int numEntries = summaryList.size();

        for (int loop = 0; loop < numEntries; loop++)
        {  out.print((String)summaryList.elementAt(loop) + "\n");
        }
     }
  }
  else
  {  out.print("No entries satisfied the query.");
  }

  String firstXmlString = (String)results.elementAt(0);
%>
</pre>

<form action="xmlResults.jsp" method="POST">
<p>
<input name="results" type="hidden" value='<%=firstXmlString%>'>
<INPUT TYPE="submit" NAME="button" VALUE="Display">
a full hierarchical representation of the
<%
  if (resultsSize > 1)
  {
%>
first Vector of the
<%
  }
%>
returned XML.
</p>
</form>

<p>
Return to the <a href="functions.html">MySpace Service Test</a> page.
</p>

</body>
</html>
