<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>
	
	<xsl:param name="credential" />		
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
	<xsl:template match="credentials">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="credentials_form"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="credentials_form">
		<p>
			This page deals with setting up your credentials for doing a particular task on astrogrid.  Only certain groups are 
			allowed access to various resouces.  Such as running registry queries, running job queries, access to certain datacenters.
			Choose a group below that would allow you access resources.  This group becomes your credential for running this particular 
			task.  If a group that you belong to was originally setup by another community, you may choose the community and all the
			groups that you are assigned to will be added to your groups list for you to choose.
		</p>
		<strong>Your current credentials are set at: <xsl:value-of select="$credential" /></strong>
		<form method="get" name="Credentials">
			<select name="credential">
				<xsl:for-each select="//credentials/groups/group">
					<xsl:element name="option">
						<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
						<xsl:value-of select="@name"/>
					</xsl:element>				  
				</xsl:for-each>
			</select>
			<br />
			<input type="submit" name="setgroup" value="Set" />
			<br />
			<strong>You can add more groups to your credentials if you are assigned to a group in another community.</strong>
			<br />
			<select name="community">
				<xsl:for-each select="//credentials/communities/community">
					<xsl:element name="option">
						<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
						<xsl:value-of select="@name"/>
					</xsl:element>
				</xsl:for-each>
			</select>
			<br />
			<input type="submit" name="communitygroups" value="Get Groups" />
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
