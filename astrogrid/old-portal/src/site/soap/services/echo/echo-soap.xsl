<?xml version="1.0"?>
<!--
<cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/site/soap/services/echo/Attic/echo-soap.xsl,v $</cvs:source>
<cvs:date>$Author: dave $</cvs:date>
<cvs:author>$Date: 2003/06/04 11:59:22 $</cvs:author>
<cvs:version>$Revision: 1.1 $</cvs:version>
<cvs:log>
	$Log: echo-soap.xsl,v $
	Revision 1.1  2003/06/04 11:59:22  dave
	Updated site directory structure

	Revision 1.1  2003/06/04 09:12:05  dave
	Reorganised site directories, and added SOAP message test
	
	Revision 1.1  2003/06/03 13:16:47  dave
	Added initial iter 02 code
	
	Revision 1.1  2003/06/01 19:22:09  Dumbledore
	Added test SOAP service and dummy myspace service.
	
</cvs:log>
 -->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	>

	<!--+
	    | Match the request element and create our response.
	    +-->
	<xsl:template match="request">
		<response>
			<!-- Include a copy of the request -->
			<xsl:call-template name="tree-copy"/>
		</response>
	</xsl:template>

	<!-- Recursive copy of request tree -->
	<xsl:template name="tree-copy">
		<xsl:copy>
			<xsl:for-each select="@*|node()">
				<xsl:call-template name="tree-copy"/>
			</xsl:for-each>
		</xsl:copy>
	</xsl:template>

	<!-- Default node match, error and copy tree -->
	<xsl:template match="node()[name() != '']" priority="-1">
		<unknown-element>
			<xsl:attribute name="name">
				<xsl:value-of select="name()"/>
			</xsl:attribute>
			<xsl:call-template name="tree-copy"/>
		</unknown-element>
	</xsl:template>

	<!-- Default attribute match, copy -->
	<xsl:template match="@*">
		<xsl:copy/>
	</xsl:template>


</xsl:stylesheet>
