<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<xsl:param name="ErrorMessage" />	

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the admin element.
		+-->
	<xsl:template match="registrystatus">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="regstatus"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="regstatus">
		<p>
			This screen prints out the last 50 usable messages that has occurred on the registry.<br />
				<xsl:for-each select="//registrystatus/status">
					<xsl:value-of select='.'/> <br />
				</xsl:for-each>
		</p>
	</xsl:template>

	<!--+
	    | Default template, copy all and apply templates.
	    +-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
