<?xml version="1.0"?>
<!--
<cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/site/tree/styles/Attic/tree-navigation.xsl,v $</cvs:source>
<cvs:date>$Author: dave $</cvs:date>
<cvs:author>$Date: 2003/06/04 11:59:22 $</cvs:author>
<cvs:version>$Revision: 1.1 $</cvs:version>
<cvs:log>
	$Log: tree-navigation.xsl,v $
	Revision 1.1  2003/06/04 11:59:22  dave
	Updated site directory structure

	Revision 1.1  2003/06/03 13:16:47  dave
	Added initial iter 02 code
	
	Revision 1.2  2003/05/25 11:29:04  Dumbledore
	Fixed bug in the cvs log header.
	
</cvs:log>
 -->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<!-- Match the page header -->
	<xsl:template match="/page/header">
		<!-- Copy the header element -->
		<xsl:copy>
			<!-- Add the tree view style -->
			<xsl:comment>Style elements required by treeview</xsl:comment>
			<style>
				<![CDATA[
					SPAN.TreeviewSpanArea A
						{
						font-size: 10pt; 
						font-family: verdana,helvetica; 
						text-decoration: none;
						color: black
						}

					SPAN.TreeviewSpanArea A:hover
						{
						color: '#820082';
						}

				]]>
			</style>
			<!-- Process the rest of the original header element -->
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>

	<!-- Match a treeview block -->
	<xsl:template match="treeview">
		<!-- Add the tree view scripts -->
		<xsl:comment>Script elements required by treeview</xsl:comment>
		<script src="/cocoon/astrogrid/treeview/scripts/ua.js"/>
		<script src="/cocoon/astrogrid/treeview/scripts/ftiens4.js"/>

		<!-- Add the tree view link -->
		<xsl:comment>Removing this link will make the script stop from working</xsl:comment>
		<a style="font-size:7pt;text-decoration:none;color:silver" href="http://www.treemenu.net/" target="_blank">JavaScript Tree Menu</a>

		<!-- Generate the tree view config -->
		<xsl:comment>Treeview configuration</xsl:comment>
		<script language="javascript">
			<!-- Configure the tree -->
			<![CDATA[
				USETEXTLINKS = 1 ;
				STARTALLOPEN = 0 ;
				USEFRAMES = 0 ;
				USEICONS = 1 ;
				WRAPTEXT = 0 ;
				PERSERVESTATE = 1 ;
				ICONPATH = "/cocoon/astrogrid/treeview/icons/" ;
				HIGHLIGHT = 1 ;
			]]>
			<!-- Create the root element -->
			<![CDATA[
				foldersTree = gFld("<b>MyRootNode</b>", "home") ;
			]]>
			<!-- Process the treeview elements -->
			<xsl:apply-templates select="element">
				<xsl:with-param name="parent">foldersTree</xsl:with-param>
			</xsl:apply-templates>
		</script>

		<!-- Add the tree view span -->
		<xsl:comment>Treeview span</xsl:comment>
		<span class="TreeviewSpanArea">
			<script>initializeDocument()</script>
			<noscript>
				A tree for site navigation will open here if you enable JavaScript in your browser.
			</noscript>
		</span>
	</xsl:template>

	<!-- Match a treeview element -->
	<xsl:template match="treeview//element">
		<xsl:param name="parent"/>
		<xsl:param name="element"><xsl:number level="multiple" format="a_a"/></xsl:param>
		<![CDATA[
			]]>
			<xsl:value-of select="$element"/><![CDATA[ = insFld(]]><xsl:value-of select="$parent"/><![CDATA[, gFld("]]><xsl:value-of select="@display"/><![CDATA[", "]]><xsl:value-of select="@href"/><![CDATA[")) ;
		]]>
		<!-- Process any child elements -->
		<xsl:apply-templates select="element">
			<xsl:with-param name="parent"><xsl:value-of select="$element"/></xsl:with-param>
		</xsl:apply-templates>
		<!-- Process any element fields -->
		<xsl:apply-templates select="field">
			<xsl:with-param name="parent"><xsl:value-of select="$element"/></xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>

	<!-- Match a treeview field -->
	<xsl:template match="treeview//field">
		<xsl:param name="parent"/>
		<![CDATA[
			insDoc(]]><xsl:value-of select="$parent"/><![CDATA[, gLnk("S", "]]><xsl:value-of select="@display"/><![CDATA[", "]]><xsl:value-of select="@href"/><![CDATA[")) ;
		]]>
	</xsl:template>

	<!-- Default, copy all and apply templates -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
