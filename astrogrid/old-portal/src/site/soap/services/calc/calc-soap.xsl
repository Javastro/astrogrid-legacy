<?xml version="1.0"?>
<!--
<cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/site/soap/services/calc/Attic/calc-soap.xsl,v $</cvs:source>
<cvs:date>$Author: dave $</cvs:date>
<cvs:author>$Date: 2003/06/04 09:12:05 $</cvs:author>
<cvs:version>$Revision: 1.1 $</cvs:version>
<cvs:log>
	$Log: calc-soap.xsl,v $
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
	xmlns:calc="astrogrid:soap/calc"
	xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
	>

	<!--+
	    | Match the request element and create our response.
	    +-->
	<xsl:template match="request">
		<response>
			<!-- Include a copy of the request -->
			<xsl:call-template name="tree-copy"/>
			<result>
				<xsl:apply-templates/>
			</result>
		</response>
	</xsl:template>

	<!--+
	    | Match and skip the soap envelope ...
	    +-->
	<xsl:template match="SOAP-ENV:*">
		<xsl:comment>Processing SOAP element</xsl:comment>
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match and skip the request headers ...
	    +-->
	<xsl:template match="headers">
		<xsl:comment>Processing request headers</xsl:comment>
	</xsl:template>

	<!--+
	    | Match the request content ...
	    +-->
	<xsl:template match="content">
		<xsl:comment>Processing request content</xsl:comment>
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the calculation call and generate our result.
	    +-->
	<xsl:template match="calc:add">
		<xsl:value-of select="i1"/>
		<xsl:text>+</xsl:text>
		<xsl:value-of select="i2"/>
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
	<xsl:template match="node()[name() != '']" priority="-10">
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
