<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/cocoon/common/xsl/Attic/astrogrid.xsl,v $</cvs:source>
    | <cvs:date>$Author: pjn3 $</cvs:date>
    | <cvs:author>$Date: 2003/10/28 16:27:55 $</cvs:author>
    | <cvs:version>$Revision: 1.11 $</cvs:version>
    | <cvs:log>
    | $Log: astrogrid.xsl,v $
    | Revision 1.11  2003/10/28 16:27:55  pjn3
    | Job Monitor renamed Job Manager
    |
    | Revision 1.10  2003/10/10 04:37:25  eca
    | Edited serveral pages to update registry browser / admin:
    | 
    | portalB/src/cocoon/sitemap.xmap 
    | portalB/src/cocoon/common/xsl/astrogrid.xsl
    | portalB/src/cocoon/common/xsl/agtemplate.xsl
    | portalB/src/cocoon/common/xsl/registrytemplate.xsl
    | portalB/src/cocoon/html/agindex.html
    | portalB/src/cocoon/html/agregistry.html
    | portalB/src/cocoon/html/agregadmin.html
    | portalB/src/cocoon/registry/xsl/*.xsl
    | portalB/src/cocoon/registry/xsp/*.xsp
    | 
    | 10 October 2003 by Elizabeth Auden
    |
    | Revision 1.9  2003/10/08 14:11:23  eca
    | Edited portalB/src/cocoon/common/xsl/astrogrid.xsl to include reg admin
    | 8 October 2003 by Elizabeth Auden
    |
    | Revision 1.8  2003/09/14 21:11:26  KevinBenson
    | Several small things cleared up on the portal interacting with the community.
    | Cleared up some validation bugs and made sure only Admin users can do certain
    | admin features.  Plus redirecting to the secure url
    |
    | Revision 1.7  2003/09/10 06:16:51  KevinBenson
    | *** empty log message ***
    |
    | Revision 1.6  2003/09/05 08:21:44  KevinBenson
    | Okay did a few things here.  First the topcat plug in had a small xsp mistake causing the select boxes to have some unneeded data in it.
    | Also made a common QueryAction.java file that will just set the current session Credential in the Hashmap so it can be displayed on all pages.
    | Modified the sitemap.xmap to verify the credential is working. I still need to go back and correct some things in it.
    |
    | Revision 1.5  2003/08/26 10:43:18  KevinBenson
    | small things to have the community prototype going
    |
    | Revision 1.4  2003/07/25 08:13:56  KevinBenson
    | Okay DataQuery is now under cocoon.  Yeah.
    |
    | Revision 1.1  2003/07/23 15:31:46  KevinBenson
    | These files are not ready yet, but they are getting there.
    |
    | Revision 1.3  2003/07/03 16:43:45  dave
    | Fixed styles in page navigation
    |
    | Revision 1.2  2003/07/03 13:30:38  dave
    | Fixed http link on ivoa logo
    |
    | Revision 1.1  2003/06/30 00:04:53  dave
    | Added initial astrogrid style
    |
    | Revision 1.6  2003/06/29 02:45:22  dave
    | Fixed display styles in explorer and add VOTable transform
    |
    | Revision 1.5  2003/06/27 03:17:38  dave
    | Simplified page path in sitemap
    |
    | Revision 1.4  2003/06/27 02:55:11  dave
    | Added images to tree nodes
    |
    | Revision 1.2  2003/06/27 02:43:18  dave
    | Added images to tree nodes
    |
    | Revision 1.1  2003/06/26 14:15:10  dave
    | Added explorer pages and actions to Cocoon
    |
    | </cvs:log>
    | 
    +-->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<!--+
	    | The explorer page names, set from the Cocoon sitemap.
	    +-->
	<xsl:param name="explorer-page">explorer</xsl:param>
	<xsl:param name="votable-page">votable</xsl:param>
	<!--+
	    | The sitemap page names, set from the Cocoon sitemap.
	    +-->
	<xsl:param name="home-page">index.html</xsl:param>
	<xsl:param name="current-page">test.html</xsl:param>
	<xsl:param name="credential" />
	<xsl:param name="credential-page">agcredentials.html</xsl:param>
	<xsl:param name="myspace-page">agmyspace.html</xsl:param>
	<xsl:param name="help-page">agmyspacehelp.html</xsl:param>
	<xsl:param name="registry-page">agregistry.html</xsl:param>
	<xsl:param name="regadmin-page">agregadmin.html</xsl:param>
	<xsl:param name="query-page">agdataquery.html</xsl:param>
	<xsl:param name="admin-page">agdadministration.html</xsl:param>
	<xsl:param name="title-name">Astrogrid</xsl:param>	
	<xsl:param name="monitor-page">agjobmonitor.html</xsl:param>
	<xsl:param name="tools-page">agtools.html</xsl:param>
	<xsl:param name="logout-page">aglogout.html</xsl:param>

	<xsl:template match="/page">  
		<html>
			<head>
				<title>
					AstroGrid Portal
				</title>
				<style>
					<xsl:text>
						table.menu      { background-color: rgb(204,204,204); color: rgb(51,51,255); font-style:normal; font-weight:normal; font-family:arial, serif}
						tr.menu         { background-color: rgb(204,204,204); color: rgb(51,51,255); font-style:normal; font-weight:normal; font-family:arial, serif}
						td.menu         { background-color: rgb(204,204,204); color: rgb(51,51,255); font-style:normal; font-weight:normal; font-family:arial, serif}

						a.menu:link     { background-color: rgb(204,204,204); color: rgb(51,51,255); font-style:normal; font-weight:bold; font-family:arial, serif}
						a.menu:active   { background-color: rgb(204,204,204); color: rgb(51,51,255); font-style:normal; font-weight:bold; font-family:arial, serif}
						a.menu:visited  { background-color: rgb(204,204,204); color: rgb(51,51,255); font-style:normal; font-weight:bold; font-family:arial, serif}
						a.menu:hover    { background-color: rgb(204,204,204); color: rgb(51,51,255); font-style:normal; font-weight:bold; font-family:arial, serif}

						table.info      { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
						tr.info         { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
						td.info         { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}

						table.tree      { background:#ffffff; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
						tr.tree         { background:#ffffff; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
						td.tree         { background:#ffffff; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}

						a.tree:link     { background:#ffffff; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif; text-decoration: none}
						a.tree:active   { background:#ffffff; color:red;   font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif; text-decoration: none}
						a.tree:visited  { background:#ffffff; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif; text-decoration: none}
						a.tree:hover    { background:#ffffff; color:red;   font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif; text-decoration: none}

						tr.title      { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:bold;   font-family:arial, serif}
						td.title      { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:bold;   font-family:arial, serif}

						table.data    { background:#FFFFFF; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
						td.data-light { background:#DDDDDD; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
						td.data-dark  { background:#C0C0C0; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}

					</xsl:text>
				</style>
			</head>
			<body>
			<!-- Logo and Banner -->
			<table cellpadding="2" cellspacing="0" border="0" style="width: 100%;">
				<tbody>
					<tr align="center" valign="middle">
						<td style="height: 100px; width: 140px;">
							<a href="http://www.astrogrid.org">
								<img src="aglogo.png" title="AstroGrid Home" alt="" style="border: 0px solid ; width: 140px; height: 100px;"/>
							</a>
						</td>
						<td bgcolor="#CCCCCC" style="color: #0000FF;">
							<font size="+4" face="arial, helvetica, sans-serif">
								AstroGrid Virtual Observatory
							</font> 
							<br/>
							<font size="+3" face="arial, helvetica, sans-serif">
								<xsl:if test="$title-name != ''" >
									<xsl:value-of select="$title-name" />
								</xsl:if>
							</font>
							<br/>
						</td>
					</tr>
				</tbody>
			</table>
			<!-- Separator bar -->
			<table cellpadding="2" cellspacing="0" border="0" style="text-align: left; width: 100%;">
				<tbody>
					<tr bgcolor="#0000FF">
						<td>
							<br/>
						</td>
					</tr>
				</tbody>
			</table>
			<!-- Menu and main page-->
			<table cellpadding="2" cellspacing="0" border="0" style="height: 80%; width: 100%;">
				<tbody>
					<tr align="center" valign="top" style="color: rgb(51,51,255);">
						<!-- Menu -->
							<td style="background-color: rgb(204,204,204); width: 140px;">
								<br/>
								<a class="menu">
									<xsl:attribute name="href">
										<xsl:value-of select="$home-page"/>
									</xsl:attribute>
									Home
								</a>
								<br/>
								<br/>
								<a class="menu">
									<xsl:attribute name="href">
										<xsl:value-of select="$registry-page"/>
									</xsl:attribute>
									Browse Registry
								</a>
								<br/>
								<br/>
								<a class="menu">
									<xsl:attribute name="href">
										<xsl:value-of select="$regadmin-page"/>
									</xsl:attribute>
									Registry Admin
								</a>								
								<br/>
								<br/>
									<a class="menu">
										<xsl:attribute name="href">
											<xsl:value-of select="$query-page"/>
										</xsl:attribute>
										Data Query
									</a>
								
								<xsl:if test="$current-page != 'agmyspace.html'" >
									<br/>
									<br/>
									<a class="menu">
										<xsl:attribute name="href">
											<xsl:value-of select="$myspace-page"/>
										</xsl:attribute>
										Browse MySpace
									</a>
								</xsl:if>
								<xsl:if test="$current-page = 'agmyspace.html'" >
									<br/>
									<br/>
									<!-- Add the MySpace menu links -->
									<xsl:apply-templates select="menu"/>
								</xsl:if>
								<br/>
								<br/>
								<a class="menu">
									<xsl:attribute name="href">
										<xsl:value-of select="$monitor-page"/>
									</xsl:attribute>
									Job Manager
								</a>
								<br/>
								<br/>
								<a class="menu">
									<xsl:attribute name="href">
										<xsl:value-of select="$tools-page"/>
									</xsl:attribute>
									Tools
								</a>
								<br/>
								<br/>
								<a class="menu">
									<xsl:attribute name="href">
										<xsl:value-of select="$admin-page"/>
									</xsl:attribute>
									Admin
								</a>								
								<br/>
								<br/>
								<a class="menu">
									<xsl:attribute name="href">
										<xsl:value-of select="$logout-page"/>
									</xsl:attribute>
									Logout
								</a>
								<br/>
								<br/>
							</td>
							<!-- Main page -->
							<td style="vertical-align: top;" rowspan="2">
								<table cellpadding="2" cellspacing="2" border="0" style="width: 100%; margin-left: auto; margin-right: auto;">
									<tbody>
										<tr align="center" valign="middle" bgcolor="#000000">
											<td width="0" height="0" colspan="0" rowspan="0" style="color: white; font-family: arial, helvetica, sans-serif; font-weight: bold; height: 30px; width: 200px; ">
												<xsl:if test="$title-name != ''" >
												  	<xsl:value-of select="$title-name" />
												</xsl:if>
											</td>
											<td width="0" height="0" colspan="0" rowspan="0" style="color: white; font-family: arial, helvetica, sans-serif; font-weight: bold; height: 30px; width: 200px; ">
												<a style="color: #FFFFFF; ">
													<xsl:attribute name="href">
														<xsl:value-of select="$help-page"/>
													</xsl:attribute>
													Help
												</a>
											</td>
											<td>
											</td>
										</tr>

										<tr align="left" valign="top">
											<td rowspan="1" colspan="3">
												<!-- Add the MySpace explorer content -->
												<xsl:apply-templates select="content"/>
											</td>
										</tr>
										<xsl:if test="$credential != ''">
										<tr align="left" valign="bottom">
											<td>
												Current Credential: <xsl:value-of select="$credential"/>
												<br />
												<a style="color: blue; ">
													<xsl:attribute name="href">
														<xsl:value-of select="$credential-page"/>
													</xsl:attribute>
													Credentials
												</a>
												<xsl:attribute name="href">
													
												</xsl:attribute>
												
											</td>
											<td>
												
											</td>
											<td>

											</td>
										</tr>
										</xsl:if>
									</tbody>
								</table>
								<br/>
							</td>
						</tr>
						<!-- IVOA logo and link -->
						<tr bgcolor="#CCCCCC">
							<td align="center" style="color: #FF9900; font-size-adjust: -2; width: 140px;">
								Member of the
								<br/>
								<a href="http://www.ivoa.net">
									<img src="ivoalogo.png" title="IVOA Alliance" alt="IVOA Alliance" style="border: 0px solid ; width: 140px; height: 77px;"/>
								</a>
								<br/>
								International Virtual Observatory Alliance
							</td>
						</tr>
					</tbody>
				</table>
			</body>
		</html>
	</xsl:template> 

	<!-- Match the page navigation -->
	<xsl:template match="/page/menu">
		<table border="0" class="menu" cellpadding="0" cellspacing="0">
			<xsl:apply-templates select="link"/>
		</table>
	</xsl:template>

	<!-- Match a menu link -->
	<xsl:template match="link[@type = 'menu']">
		<!-- Add a table row for this item -->
		<tr class="menu">
			<td class="menu">
				<xsl:for-each select="ancestor::link">
					<xsl:text>-- </xsl:text>
				</xsl:for-each>
				<a class="menu">
					<xsl:attribute name="href">
						<xsl:value-of select="href/base"/>
						<xsl:for-each select="href/param">
							<xsl:choose>
								<xsl:when test="position() = 1">
									<xsl:text>?</xsl:text>
								</xsl:when>
								<xsl:otherwise>
									<xsl:text>&amp;</xsl:text>
								</xsl:otherwise>
							</xsl:choose>
							<xsl:value-of select="@name"/>
							<xsl:text>=</xsl:text>
							<xsl:value-of select="text()"/>
						</xsl:for-each>
					</xsl:attribute>
					<xsl:choose>
						<!-- If this link is selected -->
						<xsl:when test="@selected = 'true'">
							<span style="font-style:italic; font-weight:bold">
								<xsl:value-of select="display/text()"/>
							</span>
						</xsl:when>
						<!-- If this link is NOT selected -->
						<xsl:otherwise>
							<xsl:value-of select="display/text()"/>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</td>
		</tr>
		<!-- Process any child items -->
		<xsl:apply-templates select="link"/>
	</xsl:template>

	<!-- Generate a tree node link -->
	<xsl:template match="link[@type = 'node']">
		<a class="tree">
			<xsl:attribute name="href">
				<xsl:value-of select="href/base"/>
				<xsl:for-each select="href/param">
					<xsl:choose>
						<xsl:when test="position() = 1">
							<xsl:text>?</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>&amp;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:value-of select="@name"/>
					<xsl:text>=</xsl:text>
					<xsl:value-of select="text()"/>
				</xsl:for-each>
			</xsl:attribute>
			<img valign="center" border="0">
				<xsl:attribute name="src">
					<xsl:value-of select="image/@src"/>
				</xsl:attribute>
			</img>
			<!-- If we have some text -->
			<xsl:if test="string-length(normalize-space(text)) > 0">
				<xsl:choose>
					<!-- If this link is selected -->
					<xsl:when test="@selected = 'true'">
						<span style="font-weight:bold">
							<xsl:value-of select="text"/>
						</span>
					</xsl:when>
					<!-- If this link is NOT selected -->
					<xsl:otherwise>
						<xsl:value-of select="text"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
		</a>
	</xsl:template>

	<!-- Generate a generic href link -->
	<xsl:template name="link" match="link">
		<a>
			<xsl:attribute name="href">
				<xsl:value-of select="href/base"/>
				<xsl:for-each select="href/param">
					<xsl:choose>
						<xsl:when test="position() = 1">
							<xsl:text>?</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>&amp;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:value-of select="@name"/>
					<xsl:text>=</xsl:text>
					<xsl:value-of select="text()"/>
				</xsl:for-each>
			</xsl:attribute>
			<xsl:choose>
				<!-- If this link is selected -->
				<xsl:when test="@selected = 'true'">
					<span style="font-weight:bold">
						<xsl:text>[</xsl:text>
						<xsl:value-of select="display/text()"/>
						<xsl:text>]</xsl:text>
					</span>
				</xsl:when>
				<!-- If this link is NOT selected -->
				<xsl:otherwise>
					<xsl:text>[</xsl:text>
					<xsl:value-of select="display/text()"/>
					<xsl:text>]</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</a>
	</xsl:template>

	<!-- Match the page content -->
	<xsl:template match="/page/content">
		<!-- Process the page content -->
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Default, copy all and apply templates -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
