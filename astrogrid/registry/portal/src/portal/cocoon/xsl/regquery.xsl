<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	  xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
  xmlns:vc="http://www.ivoa.net/xml/VOCommunity/v0.2"
  xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.2"
  xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.4"
  xmlns:vt="http://www.ivoa.net/xml/VOTable/v0.1"
  xmlns:cs="http://www.ivoa.net/xml/ConeSearch/v0.2"
  xmlns:sia="http://www.ivoa.net/xml/SIA/v0.6"
	>
	
	<xsl:param name="mainelement" />
	<xsl:param name="criteria_number" />
	<xsl:param name="ErrorMessage" />
	<xsl:param name="publishregistryname" />
	<xsl:param name="searchregistryname" />
	<xsl:param name="queryresult" />
	<xsl:param name="fulltable" />	

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
		<form method="post" action="registryquery.html" name="RegistryQuery">
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
		<form method="post" action="registryquery.html" name="RegistryQuery">
			<input type="hidden" name="action" value="addcriteria" />
			<input type="hidden" name="criteria_number">
				<xsl:attribute name="value"><xsl:value-of select="$criteria_number"/></xsl:attribute>
			</input>
			<input type="hidden" name="mainelement">
				<xsl:attribute name="value"><xsl:value-of select="$mainelement"/></xsl:attribute>
			</input>			
			<input type="submit" name="addcriteria" value="Add More Criteria" />
		</form>

		<xsl:if test="//registryquery/xmlresults/xmlresult">
			<table border="1" cellspacing="0" cellpadding="0">
				<tr>
					<td>Actions</td>
					<td>Identifier</td>
					<td>Title</td>
					<td>Description</td>
				</tr>		
		<xsl:for-each select="//registryquery/xmlresults/xmlresult">
			<tr>
				<td>
			<xsl:if test="$mainelement != 'Registry'" >
					<form method="post" action="registryupdate.html">
						<input type="hidden" name="updatexml">
							<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
						</input>
						<input type="submit" name="update" value="Update" />
					</form>
			</xsl:if>
			<xsl:if test="$mainelement = 'Registry'" >
				<form method="post" action="registryharvest.html">
					<input type="hidden" name="action" value="harvestother" />
					<input type="hidden" name="registryxml">
						<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
					</input>
					Havest From Date:<input type="text" name="dateFrom" value="2001-04-02" />
					<input type="submit" name="harvest" value="Harvest" />
				</form>
			</xsl:if>
			<form method="post" action="registrymetadata.xml">
				<input type="hidden" name="xmlcontent">
					<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
				</input>
				<input type="submit" name="update" value="See Full XML" />
			</form>	
			</td>
			<td><xsl:value-of select="@ident"/></td>
			<td><xsl:value-of select="@title"/></td>
			<td><xsl:value-of select="@desc"/></td>
			</tr>			
		</xsl:for-each>
		</table>
		</xsl:if>
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
