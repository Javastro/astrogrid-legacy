<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/cocoon/explorer/xsl/Attic/votable.xsl,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/29 02:45:22 $</cvs:author>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    | $Log: votable.xsl,v $
    | Revision 1.1  2003/06/29 02:45:22  dave
    | Fixed display styles in explorer and add VOTable transform
    |
    | </cvs:log>
    | 
    +-->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:vot="http://vizier.u-strasbg.fr/VOTable"
	>

	<!--+
	    | Match the top level element.
	    +-->
	<xsl:template match="/">
		<page>
			<style>
				<![CDATA[
					table.info    { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
					tr.info       { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
					td.info       { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}

					tr.title      { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:bold;   font-family:arial, serif}
					td.title      { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:bold;   font-family:arial, serif}

					table.data    { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
					td.data-light { background:#DDDDDD; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
					td.data-dark  { background:#C0C0C0; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
				]]>
			</style>
			<menu>
			</menu>
			<content>
				<xsl:apply-templates select="VOTABLE"/>
			</content>
			<debug>
				<link type="action">
					<display>001</display>
					<href>
						<base/>
						<param name="cocoon-view">001</param>
					</href>
				</link>
				<xsl:text> </xsl:text>
				<link type="action">
					<display>002</display>
					<href>
						<base/>
						<param name="cocoon-view">002</param>
					</href>
				</link>
				<xsl:text> </xsl:text>
				<link type="action">
					<display>003</display>
					<href>
						<base/>
						<param name="cocoon-view">003</param>
					</href>
				</link>
			</debug>
		</page>
	</xsl:template>

	<!--+
	    | Process the start of the VOTABLE data.
	    +-->
	<xsl:template match="/VOTABLE">
		<!-- Process headers -->
		<p>
			<table class="info" border="0" cellpadding="4" cellspacing="0">
				<tr class="info">
					<td class="info" valign="top">Description</td>
					<td class="info" valign="top">
						<xsl:apply-templates select="DESCRIPTION"/>
					</td>
				</tr>
				<xsl:if test="DEFINITIONS">
					<tr class="info">
						<td class="info" valign="top">Definitions</td>
						<td class="info" valign="top">
							<xsl:apply-templates select="DEFINITIONS"/>
						</td>
					</tr>
				</xsl:if>
			</table>
		</p>
		<!-- Process the rest of the data -->
		<p>
			<xsl:apply-templates select="RESOURCE"/>
		</p>
	</xsl:template>

	<!--+
	    | Process a DESCRIPTION element.
	    +-->
	<xsl:template match="/VOTABLE/DESCRIPTION">
		<xsl:value-of select="text()"/>
	</xsl:template>

	<!--+
	    | Process a DEFINITIONS element.
	    +-->
	<xsl:template match="/VOTABLE/DEFINITIONS">
		<xsl:for-each select="*">
			<xsl:value-of select="name()"/>
			<xsl:text> : </xsl:text>
			<xsl:for-each select="@*">
				<xsl:value-of select="name()"/>
				<xsl:text>=</xsl:text>
				<xsl:value-of select="."/>
				<xsl:if test="position() != last()">
					<xsl:text>, </xsl:text>
				</xsl:if>
			</xsl:for-each>
			<xsl:if test="position() != last()">
				<br/>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!--+
	    | Process a RESOURCE element.
	    +-->
	<xsl:template match="RESOURCE">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Process a RESOURCE TABLE element.
	    +-->
	<xsl:template match="RESOURCE/TABLE">
		<table class="data" border="0" cellpadding="2" cellspacing="1">
			<!-- Generate the column headers -->
			<tr class="title">
				<xsl:for-each select="FIELD">
					<td class="title">
						<!-- Choose the column name -->
						<xsl:choose>
							<!-- Use the name if it is there -->
							<xsl:when test="@name">
								<xsl:value-of select="@name"/>
							</xsl:when>
							<!-- If not, use the ID -->
							<xsl:when test="@ID">
								<xsl:value-of select="@ID"/>
							</xsl:when>
							<!-- Otherwise, just put a dummy label -->
							<xsl:otherwise>
								<xsl:text>Unknown</xsl:text>
							</xsl:otherwise>
						</xsl:choose>
					</td>
				</xsl:for-each>
			</tr>
			<!-- Generate the data -->
			<xsl:apply-templates select="DATA"/>
		</table>
	</xsl:template>

	<xsl:template match="RESOURCE/TABLE/DATA">
		<xsl:apply-templates select="TABLEDATA"/>
	</xsl:template>

	<xsl:template match="RESOURCE/TABLE/DATA/TABLEDATA">
		<xsl:apply-templates select="TR"/>
	</xsl:template>

	<xsl:template match="RESOURCE/TABLE/DATA/TABLEDATA/TR">
		<xsl:variable name="toad">
			<xsl:choose>
				<xsl:when test="(position() mod 2) = 0">
					<xsl:text>data-light</xsl:text>
				</xsl:when>
				<xsl:when test="(position() mod 2) = 1">
					<xsl:text>data-dark</xsl:text>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<tr>
			<xsl:apply-templates select="TD">
				<xsl:with-param name="frog">
					<xsl:value-of select="$toad"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</tr>
	</xsl:template>

	<xsl:template match="RESOURCE/TABLE/DATA/TABLEDATA/TR/TD">
		<xsl:param name="frog"/>
		<td>
			<xsl:attribute name="class"><xsl:value-of select="$frog"/></xsl:attribute>
			<xsl:value-of select="text()"/>
		</td>
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
