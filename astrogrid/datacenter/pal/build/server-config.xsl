<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns:wsdd="http://xml.apache.org/axis/wsdd/">
<!-- very simple stylesheet to insert service section of deploy.wsdd -->
<xsl:output method="xml" indent="yes" />
<xsl:template match="/">
<deployment xmlns="http://xml.apache.org/axis/wsdd/" xmlns:java="http://xml.apache.org/axis/wsdd/providers/java">
 <globalConfiguration>
  <parameter name="adminPassword" value="admin"/>
  <!--
  <parameter name="attachments.Directory" value="/var/lib/tomcat4/webapps/axis/WEB-INF/attachments"/>
  -->
  <parameter name="attachments.implementation" value="org.apache.axis.attachments.AttachmentsImpl"/>
  <parameter name="sendXsiTypes" value="true"/>
  <parameter name="sendMultiRefs" value="true"/>
  <parameter name="axis.development.system" value="true"/>
  <parameter name="sendXMLDeclaration" value="true"/>
  <parameter name="axis.sendMinimizedElements" value="true"/>
  <requestFlow>
   <handler type="java:org.apache.axis.handlers.JWSHandler">
    <parameter name="scope" value="session"/>
   </handler>
   <handler type="java:org.apache.axis.handlers.JWSHandler">
    <parameter name="scope" value="request"/>
    <parameter name="extension" value=".jwr"/>
   </handler>
  </requestFlow>
 </globalConfiguration>
 <handler name="LocalResponder" type="java:org.apache.axis.transport.local.LocalResponder"/>
 <handler name="URLMapper" type="java:org.apache.axis.handlers.http.URLMapper"/>
 <handler name="Authenticate" type="java:org.apache.axis.handlers.SimpleAuthenticationHandler"/>
 <xsl:apply-templates select="//wsdd:service" />
 <service name="Version" provider="java:RPC">
  <parameter name="allowedMethods" value="getVersion"/>
  <parameter name="className" value="org.apache.axis.Version"/>
 </service>
 <transport name="http">
  <requestFlow>
   <handler type="URLMapper"/>
   <handler type="java:org.apache.axis.handlers.http.HTTPAuthHandler"/>
  </requestFlow>
 </transport>
 <transport name="local">
  <responseFlow>
   <handler type="LocalResponder"/>
  </responseFlow>
 </transport>
</deployment>
</xsl:template>

<!-- copy-all template -->
<xsl:template match="node()|@*">
        <xsl:copy> <!-- was:  name="{name()}" -->
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
</xsl:template>

</xsl:stylesheet>
