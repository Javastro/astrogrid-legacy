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
	<xsl:template match="registryoptions">
		<!--
		<page>
			<content>
			-->
			<div>
				<xsl:call-template name="regoptions_form"/>
			</div>
			<!--
			</content>
		</page>
		-->
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="regoptions_form">
		<p>
			This is your Options screen to determine your next task for querying or adding to the Registry.<br />
			<i>If your interested on how to do "Updates" you must query the registry and then an appropriate 
			"Update" button will appear if you have permissions or an Administrator</i>
		</p>
		<p>
			Do you Wish to do a Query?
		</p>
		<form method="get" action="registryquery.html" name="RegistryQuery">
				<select name="mainelement">		
				<xsl:for-each select="//registryoptions/regoptions/regoption">
					<xsl:element name="option">
						<xsl:attribute name="value"><xsl:value-of select="@name"/></xsl:attribute>
						<xsl:value-of select="@name"/>
					</xsl:element>				  
				</xsl:for-each>
				</select>
			<br />
			<input type="submit" name="queryregistry" value="Go Query" />
		</form>
		<p>
			Or Do you wish to do an Add?
		</p>
		<form method="get" action="registryupdate.html" name="RegistryUpdate">
			Main Elements: <br />
				<select name="mainelement">
				<xsl:for-each select="//registryoptions/regoptions/regoption">				
					<xsl:if test="@name != 'Registry'" >
						<xsl:element name="option">
							<xsl:attribute name="value"><xsl:value-of select="@name"/></xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>
					</xsl:if>
				</xsl:for-each>
				</select>
			<br />
			<input type="submit" name="addregistry" value="Go Add" />
			<i>Hint: You may query the registry and create a copy of any result found in the registry.</i>
		</form>		
		
		<hr />
		<br />
		<i>Obtain new registry:</i>
		<br />
		Get Registry metadata:<br />
		<form method="get" action="registryharvest.html" name="RegistryHarvest">
			<input type="hidden" name="newregistry" value="true" />
			<input type="hidden" name="addregistries" value="true" />
			<b>Url:</b>
			<input type="text" name="accessurl" />
			<br />
			<input type="submit" name="addregistry" value="Go Harvest" />
		</form>				
		<hr />
		<br />
		Check Registry Status:
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
