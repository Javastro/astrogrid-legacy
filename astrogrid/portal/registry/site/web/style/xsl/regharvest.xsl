<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<xsl:param name="action" />	
	<xsl:param name="message" />
	<xsl:param name="registryXML" />
	<xsl:param name="harvestResult" />		
	<xsl:param name="addregistry" />
	<xsl:param name="errorMessage" />

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the admin element.
		+-->
	<xsl:template match="harvest">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="regharvest_form"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="regharvest_form">
		<b>Harvest Information:</b><br />
		<xsl:if test="$message != ''" >
			<font color="blue"><xsl:value-of select="$message" /></font>
			<br />
		</xsl:if>	
		<xsl:if test="$errorMessage != ''" >
			<font color="red"><xsl:value-of select="$errorMessage" /></font>
			<br />
		</xsl:if>		
		<br />
		<a href="registryquery.html?mainelement=Registry">Query Registries</a>
		<br />
		<xsl:if test="$harvestResult != ''" >
			<i>Raw XML of the Harvest Result:</i> <br />
			<xsl:value-of select="$harvestResult" />
		</xsl:if>
		<xsl:if test="$addregistry != 'true'">
			<xsl:if test="$errorMessage != ''" >
				<form method="get" action="registryharvest.html" name="RegistryUpdate">
					<input type="hidden" name="registryXML">
						<xsl:attribute name="value"><xsl:value-of select="$registryXML" /></xsl:attribute>
					</input>
					<input type="text" name="dateFrom" />
					<input type="submit" name="harvestSubmit" value="Harvest Try Again" />
				</form>		
			</xsl:if>
		</xsl:if>
		<br /><br />		
		<a href="registrystatus.html">Link to Registry Status</a>
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
