<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<xsl:param name="action" />	
	<xsl:param name="message" />
	<xsl:param name="authID" />
	<xsl:param name="resKey" />	
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
	<xsl:template match="registryquery">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="regadmin_form"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="regadmin_form">
		<xsl:if test="$message != ''" >
			<font color="blue"><xsl:value-of select="$message" /></font>
		</xsl:if>	
		<br />
		<i>Updated and Created attributes will automatically be set when submitted.</i>
		<form method="get" action="registryupdate.html" name="RegistryUpdate">
			<xsl:if test="$action = 'add'" >
				<input type="hidden" name="action" value="add" />
			</xsl:if>
			<xsl:if test="$action = 'update'" >
				<input type="hidden" name="action" value="update" />
			</xsl:if>
			<table border="0" cellspacing="0" cellpadding="0">
			<xsl:for-each select="//registryquery/regitems/regitem">
				<tr>
					<td>
						<xsl:value-of select="@label"/>
					</td>
					<td>
						<input>
							<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
							<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
							<xsl:attribute name="type">text</xsl:attribute>
							<xsl:if test="$action = 'update'" >
								<xsl:if test="$authID = @name" >
									<xsl:attribute name="type">hidden</xsl:attribute>
								</xsl:if>
								<xsl:if test="$resKey = @name" >
									<xsl:attribute name="type">hidden</xsl:attribute>
								</xsl:if>							
							</xsl:if>
						</input>
					</td>
				</tr>
			</xsl:for-each>
			</table>
			<br />
			<input type="submit" name="queryupdate" value="Update Registry" />
		</form>		
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
