<%
String file = request.getParameter("file");
response.setContentType("application/x-java-jnlp-file");
 %>
<?xml version="1.0" encoding="utf-8"?>
<jnlp spec="1.0+" codebase="http://surveysl.roe.ac.uk:8080/ssa/tmp_radial/"
      >
  <information>
    <title>Topcat</title>
    <vendor>Starlink</vendor>
    <homepage href="http://www.starlink.ac.uk/topcat/"/>
    <description>Generic table viewer/editor</description>
<icon href="http://surveys.roe.ac.uk/ssa/topcat.gif"/>
    <offline-allowed/>
  </information>
  <security>
    <all-permissions/>
  </security>
  <resources>
    <j2se version="1.4+" max-heap-size="256m"/>
    <jar href="http://surveys.roe.ac.uk:8080/ssa/topcat.jar"/>
  </resources>
  <application-desc main-class="uk/ac/starlink/topcat/TableViewer">
<argument><%=file%></argument>

  </application-desc>
</jnlp>
