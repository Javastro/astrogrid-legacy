<?xml version="1.0"?>
<!--
<cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/site/soap/generator/Attic/soap-envelope.xsl,v $</cvs:source>
<cvs:date>$Author: dave $</cvs:date>
<cvs:author>$Date: 2003/06/04 09:12:05 $</cvs:author>
<cvs:version>$Revision: 1.1 $</cvs:version>
<cvs:log>
	$Log: soap-envelope.xsl,v $
	Revision 1.1  2003/06/04 09:12:05  dave
	Reorganised site directories, and added SOAP message test

	Revision 1.1  2003/06/03 13:16:47  dave
	Added initial iter 02 code
	
	Revision 1.2  2003/06/01 19:22:09  Dumbledore
	Added test SOAP service and dummy myspace service.
	
	Revision 1.1  2003/05/30 15:27:41  Dumbledore
	Added SOAP request generator.
	
</cvs:log>
 -->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>
	<!--+
	    | Match the top level element and wrap it in the correct SOAP envelope.
	    +-->
	<xsl:template match="/">
		<SOAP-ENV:Envelope
			xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
			SOAP-ENV:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
			<SOAP-ENV:Body>
				<xsl:apply-templates/>
			</SOAP-ENV:Body>
		</SOAP-ENV:Envelope>
	</xsl:template>

	<!-- Default, copy all and apply templates -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
