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
		<form method="post" action="registryharvest.html" name="UploadMetaData">
			<input type="hidden" name="addmetadatafromurl" />
			Harvest from a URL:
			<input type="text" name="metadata_url" />
			<br />
			<input type="submit" name="Harvest" />			
		</form>
		<br />
		<form method="post" action="registryharvest.html" name="UploadMetaData" enctype="multipart/form-data">
			<input type="hidden" name="addmetadatafromfile" />
			Harvest from a local file:
			<input type="file" name="metadata_file" />
			<br />
			<input type="submit" name="Harvest" />
			<br />
			<i>The above approach is limited possible errors because of time situations.</i>
		</form>
		<br />
		Click below to see an automatic query of all our Registries to perform a harvest.
		<br />
		<form method="post" action="registryquery.html" name="RegistryQuery">
			<input type="hidden" name="action" value="selectquery" />		
			<input type="text" name="registry_identifier" />
			<input type="text" name="selectitem0" value="@xsi:type" />
			<input type="text" name="selectitemop0" value="EQ"/>
			<input type="text" name="selectitemvalue0" value="RegistryType"/>
			<br />
			<input type="submit" name="Query" />
		</form>
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
