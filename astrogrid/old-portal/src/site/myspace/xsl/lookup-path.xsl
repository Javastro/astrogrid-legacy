<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/site/myspace/xsl/Attic/lookup-path.xsl,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/03 13:16:47 $</cvs:author>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    | $Log: lookup-path.xsl,v $
    | Revision 1.1  2003/06/03 13:16:47  dave
    | Added initial iter 02 code
    |
    | Revision 1.1  2003/06/03 12:37:42  Dumbledore
    | Mid way through changes to the site structure
    |
    | </cvs:log>
    | 
    | XSL template to convert MySpace SOAP request into XInclude element.
    | Looks for the request/query/path element in the SOAP message.
    | 
    +-->
<xsl:stylesheet
	version="1.0" 
	xmlns:xi="http://www.w3.org/2001/XInclude"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
	>

	<!--+
	    | Skip the soap envelope ...
	    +-->
	<xsl:template match="SOAP-ENV:*">
		<xsl:comment>Processing SOAP element</xsl:comment>
		<xsl:apply-templates/>
	</xsl:template>

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
	    | Match and the request headers ...
	    +-->
	<xsl:template match="request/headers">
		<xsl:comment>Processing request headers</xsl:comment>
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the request content ...
	    +-->
	<xsl:template match="request/content">
		<xsl:comment>Processing request content</xsl:comment>
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the request query ...
	    +-->
	<xsl:template match="request/content//query">
		<xsl:comment>Processing request query</xsl:comment>
		<xi:include>
			<xsl:attribute name="href">
				<xsl:text>xml/myspace/lookup-path.xml#xpointer(/results/result[@path='</xsl:text><xsl:value-of select="path"/><xsl:text>'])</xsl:text>
			</xsl:attribute>
		</xi:include>
	</xsl:template>

	<!--+
	    | Match the request user ...
	    +-->
	<xsl:template match="request/content//user">
		<xsl:comment>Processing request user</xsl:comment>
	</xsl:template>

	<!--+
	    | Match the request select ...
	    +-->
	<xsl:template match="request/content//select">
		<xsl:comment>Processing request select</xsl:comment>
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
	<xsl:template match="@*" priority="-1">
		<xsl:copy/>
	</xsl:template>

</xsl:stylesheet>
