<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>
	
	<xsl:param name="mainelement" />
	<xsl:param name="criteria_number" />
	<xsl:param name="ErrorMessage" />
	<xsl:param name="publishregistryname" />
	<xsl:param name="searchregistryname" />
	<xsl:param name="queryresult" />

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
				<xsl:call-template name="regquery_form"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="regquery_form">
		<form method="get" action="registryquery.html" name="RegistryQuery">
			<input type="hidden" name="action" value="selectquery" />
			<input type="hidden" name="mainelement">
				<xsl:attribute name="value"><xsl:value-of select="$mainelement"/></xsl:attribute>
			</input>			
			<input type="hidden" name="mainelement">
				<xsl:attribute name="value"><xsl:value-of select="$mainelement"/></xsl:attribute>
			</input>			
			<br />
			<select name="selectitem0">
				<xsl:for-each select="//registryquery/selectitems/selectitem">
					<xsl:element name="option">
						<xsl:attribute name="value"><xsl:value-of select="@name"/></xsl:attribute>
						<xsl:value-of select="@name"/>
					</xsl:element>				  
				</xsl:for-each>
			</select>
			
			<select name="selectitemop0">
				<xsl:for-each select="//registryquery/comparisons/comparison">
					<xsl:element name="option">
						<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
						<xsl:value-of select="@name"/>
					</xsl:element>
				</xsl:for-each>
			</select>
			<input type="text" name="selectitemvalue0"/>
			<xsl:for-each select="//registryquery/criteria_number/criteria_option">
				<br />
				<select>
					<xsl:attribute name="name">selectjointype<xsl:value-of select="@num"/></xsl:attribute>
					<xsl:for-each select="//registryquery/jointypes/jointype">
						<xsl:element name="option">
							<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>				  
					</xsl:for-each>
				</select>
				<select>
					<xsl:attribute name="name">selectitem<xsl:value-of select="@num"/></xsl:attribute>
					<xsl:for-each select="//registryquery/selectitems/selectitem">
						<xsl:element name="option">
							<xsl:attribute name="value"><xsl:value-of select="@name"/></xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>				  
					</xsl:for-each>
				</select>
				<select>
					<xsl:attribute name="name">selectitemop<xsl:value-of select="@num"/></xsl:attribute>
					<xsl:for-each select="//registryquery/comparisons/comparison">
						<xsl:element name="option">
							<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
							<xsl:value-of select="@name"/>
						</xsl:element>				  
					</xsl:for-each>
				</select>
				<input type="text">
					<xsl:attribute name="name">selectitemvalue<xsl:value-of select="@num"/></xsl:attribute>
				</input>
				
			</xsl:for-each>
			<br />
			<input type="submit" name="queryregistry" value="Go Query" />
		</form>
		<br />
		<br />
		<form method="get" action="registryquery.html" name="RegistryQuery">
			<input type="hidden" name="action" value="addcriteria" />
			<input type="hidden" name="criteria_number">
				<xsl:attribute name="value"><xsl:value-of select="$criteria_number"/></xsl:attribute>
			</input>
			<input type="hidden" name="mainelement">
				<xsl:attribute name="value"><xsl:value-of select="$mainelement"/></xsl:attribute>
			</input>			
			<input type="submit" name="addcriteria" value="Add More Criteria" />
		</form>
		<br />
		<br />
		<hr />
		<xsl:for-each select="//registryquery/xmlresults/xmlresult">
			<xsl:if test="$mainelement != 'Registry'" >
				<xsl:if test="@update = 'true'" >
					<form method="post" action="registryupdate.html">
						<input type="hidden" name="updatexml">
							<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
						</input>
						<input type="submit" name="update" value="Update This Entry" />
					</form>
				</xsl:if>
					<form method="post" action="registryupdate.html">
						<input type="hidden" name="createcopy" value="true" />
						<input type="hidden" name="updatexml">
							<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
						</input>
						<input type="submit" name="update" value="Create Copy" />
					</form>
			</xsl:if>
			<xsl:if test="$mainelement = 'Registry'" >
				<form method="post" action="registryharvest.html">
					<input type="hidden" name="action" value="harvestother" />
					<input type="hidden" name="registryxml">
						<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
					</input>
					Havest From Date: <input type="text" name="dateFrom" />
					<input type="submit" name="harvest" value="Harvest Registry" />
				</form>
			</xsl:if>
			<xsl:value-of select="@val"/>
			<br /><hr />
		</xsl:for-each>


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
