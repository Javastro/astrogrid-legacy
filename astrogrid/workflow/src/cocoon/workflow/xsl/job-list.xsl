<?xml version="1.0"?>
<xsl:stylesheet	version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<xsl:param name="user-param" />
	<xsl:param name="name" />
	<xsl:param name="community" />	
	<xsl:param name="action" />		

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>



</xsl:stylesheet>
		
	
		
	