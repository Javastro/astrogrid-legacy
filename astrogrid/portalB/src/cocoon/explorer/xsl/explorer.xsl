<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/cocoon/explorer/xsl/Attic/explorer.xsl,v $</cvs:source>
    | <cvs:date>$Author: KevinBenson $</cvs:date>
    | <cvs:author>$Date: 2003/09/03 07:47:56 $</cvs:author>
    | <cvs:version>$Revision: 1.9 $</cvs:version>
    | <cvs:log>
    | $Log: explorer.xsl,v $
    | Revision 1.9  2003/09/03 07:47:56  KevinBenson
    | Finishing up the topcat plugin, still needs a few more testing and has one snag that needs to be resolved, but it is about 90-95% there.
    |
    | Revision 1.8  2003/06/30 14:23:15  dave
    | Disabled access to root node in the tree
    |
    | Revision 1.7  2003/06/30 12:42:31  dave
    | Added user name session attribute
    |
    | Revision 1.6  2003/06/30 00:04:53  dave
    | Added initial astrogrid style
    |
    | Revision 1.5  2003/06/29 02:45:22  dave
    | Fixed display styles in explorer and add VOTable transform
    |
    | Revision 1.4  2003/06/27 03:17:38  dave
    | Simplified page path in sitemap
    |
    | Revision 1.3  2003/06/27 02:43:18  dave
    | Added images to tree nodes
    |
    | Revision 1.2  2003/06/27 00:04:36  dave
    | Added Cocoon 2.0 binary and updated MySpace jar
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
	    | The current action, set from the Cocoon action.
	    +-->
	<xsl:param name="action"/>
	<xsl:param name="confirm"/>
	<!--+
	    | The page names, set from the Cocoon sitemap.
	    +-->
	<xsl:param name="explorer-page">explorer</xsl:param>
	<xsl:param name="votable-page">votable</xsl:param>
	<xsl:param name="plot-page">plot</xsl:param>

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the explorer element.
		+-->
	<xsl:template match="explorer">
		<page>
			<!-- Add our page menu -->
			<menu>
				<!-- Add our top level item -->
				<link type="menu" selected="true">
					<display>Browse MySpace</display>
					<href>
						<base><xsl:value-of select="$explorer-page"/></base>
					</href>
					<!-- Add the new-view item -->
					<link type="menu">
						<!-- If this action is selected -->
						<xsl:if test="$action = 'create-view'">
							<xsl:attribute name="selected">true</xsl:attribute>
						</xsl:if>
						<display>New view</display>
						<href>
							<base><xsl:value-of select="$explorer-page"/></base>
							<param name="action">create-view</param>
						</href>
					</link>
					<!-- Add a list of our current views -->
					<xsl:call-template name="menu-views"/>
				</link>
			</menu>
			<!-- Add our page content -->
			<content>
				<!-- Check the page action -->
				<xsl:choose>
					<!-- Display the hello message -->
					<xsl:when test="$action = 'hello'">
						<b>Hello !!</b>
					</xsl:when>
					<!-- Display the create view form -->
					<xsl:when test="$action = 'create-view'">
						<xsl:call-template name="create-view"/>
					</xsl:when>
					<!-- Display our view tree -->
					<xsl:otherwise>
						<xsl:apply-templates select="view"/>
					</xsl:otherwise>
				</xsl:choose>
			</content>

			<!--+
			    | These elements are for debug only.
			    +-->
			<debug>
				<link type="action">
					<display>001</display>
					<href>
						<base><xsl:value-of select="$explorer-page"/></base>
						<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
						<param name="cocoon-view">001</param>
					</href>
				</link>
				<xsl:text> </xsl:text>
				<link type="action">
					<display>002</display>
					<href>
						<base><xsl:value-of select="$explorer-page"/></base>
						<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
						<param name="cocoon-view">002</param>
					</href>
				</link>
				<xsl:text> </xsl:text>
				<link type="action">
					<display>003</display>
					<href>
						<base><xsl:value-of select="$explorer-page"/></base>
						<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
						<param name="cocoon-view">003</param>
					</href>
				</link>
			</debug>

		</page>
	</xsl:template>

	<!--+
	    | Generate our list of views for our menu.
	    +-->
	<xsl:template name="menu-views">
		<!-- Match each of our session views -->
		<xsl:for-each select="//explorer/session/views/view">
			<!-- Add a menu item for each view -->
			<xsl:element name="link">
				<xsl:attribute name="type">menu</xsl:attribute>
				<!-- If this the current view -->
				<xsl:if test="//explorer/view/@ident = @ident">
					<xsl:attribute name="selected">true</xsl:attribute>
				</xsl:if>
				<display>
					<xsl:value-of select="@ident"/>
				</display>
				<href>
					<base>
						<xsl:value-of select="$explorer-page"/>
					</base>
					<param name="AST-VIEW">
						<xsl:value-of select="@ident"/>
					</param>
				</href>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<!--+
	    | Process our current view.
	    +-->
	<xsl:template match="//explorer/view">
		<table class="info" border="0"  cellpadding="0" cellspacing="0" width="650">
			<tr class="info">
				<td class="info" width="100">Service name</td>
				<td class="info" colspan="2">
					<xsl:value-of select="@service"/>
				</td>
			</tr>
			<tr class="info">
				<td class="info" width="100">Explorer path</td>
				<td class="info" width="500" align="left">
					<xsl:value-of select="@path"/>
				</td>
				<td class="info" width="50" align="right">
					<link type="action">
						<display>Close</display>
						<href>
							<base><xsl:value-of select="$explorer-page"/></base>
							<param name="action">delete-view</param>
							<param name="confirm">true</param>
							<param name="AST-VIEW">
								<xsl:value-of select="@ident"/>
							</param>
						</href>
					</link>
				</td>
			</tr>
			<tr class="info">
				<td class="info" width="100">Current item</td>
				<td class="info" width="550" align="left">
					<xsl:value-of select="current/@path"/>
					<xsl:if test="current/@type = '2'">
						<xsl:text> </xsl:text>
						<link type="action">
							<display>View</display>
							<href>
								<base>
									<xsl:value-of select="$votable-page"/>
								</base>
								<param name="data">
									<xsl:value-of select="current/@uri"/>
								</param>
								<param name="AST-VIEW">
									<xsl:value-of select="@ident"/>
								</param>
							</href>
						</link>
						<xsl:text> </xsl:text>
						<link type="action">
							<display>Plot</display>
							<href>
								<base>
									<xsl:value-of select="$plot-page"/>
								</base>
								<param name="url">
									<xsl:value-of select="current/@uri"/>
								</param>
							</href>
						</link>						
					</xsl:if>
				</td>
			</tr>
			<tr class="info">
				<td class="info" width="100">Selected action</td>
				<td class="info" width="550" align="left">
					<xsl:if test="selected/@action != 'null'">
						<xsl:value-of select="selected/@action"/>
					</xsl:if>
				</td>
			</tr>
			<tr class="info">
				<td class="info" width="100">Selected item</td>
				<td class="info" width="550" align="left">
					<xsl:if test="selected/@path != ''">
						<xsl:value-of select="selected/@path"/>
					</xsl:if>
				</td>
			</tr>
		</table>
		<hr/>
		<!-- Check the page action -->
		<xsl:choose>
			<!-- Display the delete item form -->
			<xsl:when test="$action = 'create-folder'">
				<xsl:call-template name="create-folder"/>
			</xsl:when>
			<!-- Display the delete item form -->
			<xsl:when test="$action = 'delete-item'">
				<xsl:call-template name="delete-item"/>
			</xsl:when>
			<!-- Display the rename item form -->
			<xsl:when test="$action = 'rename-item'">
				<xsl:call-template name="rename-item"/>
			</xsl:when>
			<!-- Display our tree -->
			<xsl:otherwise>
				<xsl:apply-templates select="tree"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!--+
	    | Process our tree.
	    +-->
	<xsl:template match="//explorer/view/tree">
		<table class="tree" border="0" cellpadding="0" cellspacing="0">
			<xsl:apply-templates select="node"/>
		</table>
	</xsl:template>

	<!--+
	    | Process our tree nodes.
	    +-->
	<xsl:template match="//explorer/view/tree//node">
		<tr class="tree" valign="middle">
			<td class="tree" align="left" valign="bottom">
				<!-- Add the indentation -->
				<xsl:call-template name="node-indent"/>
				<!-- Add the node link -->
				<xsl:choose>
					<!-- If this is a folder -->
					<xsl:when test="@type = '1'">

						<xsl:choose>
							<!-- If this is the top node -->
							<xsl:when test="@level = 0">
								<img border="0" src="explorer/images/root.icon.gif"/>
							</xsl:when>
							<!-- If this is not the top node -->
							<xsl:otherwise>
								<link type="node">
									<!-- If this matches the explorer path -->
									<xsl:if test="@path = //explorer/view/@path">
										<xsl:attribute name="selected">true</xsl:attribute>
									</xsl:if>
									<image src="explorer/images/cont.icon.gif"/>
									<text><xsl:value-of select="@name"/></text>
									<href>
										<base><xsl:value-of select="$explorer-page"/></base>
										<param name="action">explorer-path</param>
										<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
										<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
									</href>
								</link>
							</xsl:otherwise>
						</xsl:choose>

					</xsl:when>
					<!-- If this is an item -->
					<xsl:when test="@type = '2'">
						<link type="node">
							<!-- If this matches the current path -->
							<xsl:if test="@path = //explorer/view/current/@path">
								<xsl:attribute name="selected">true</xsl:attribute>
							</xsl:if>
							<image src="explorer/images/item.icon.gif"/>
							<text><xsl:value-of select="@name"/></text>
							<href>
								<base><xsl:value-of select="$explorer-page"/></base>
								<param name="action">current-path</param>
								<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
								<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
							</href>
						</link>
					</xsl:when>
				</xsl:choose>
			</td>
			<td class="tree" width="5">
				<xsl:text> </xsl:text>
			</td>
			<td class="tree" align="left" valign="bottom">
				<xsl:call-template name="node-actions"/>
			</td>
		</tr>
		<!-- Process any child nodes -->
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Generate the indentation for a tree node.
	    +-->
	<xsl:template name="node-indent">
		<!-- Indent for each parent node in the tree -->
		<xsl:for-each select="ancestor::node">
			<xsl:call-template name="node-parent"/>
		</xsl:for-each>
		<!-- Add the symbol for this node -->
		<xsl:choose>
			<!-- If this node is a container -->
			<xsl:when test="@type = '1'">

				<xsl:choose>
					<!-- If this is the top node -->
					<xsl:when test="@level = 0">
<!--
						<img border="0" src="explorer/images/cont.node.top.gif"/>
-->
					</xsl:when>
					<!-- If this is not the top node -->
					<xsl:otherwise>
						<xsl:choose>
							<!-- If this node is not the last in its group -->
							<xsl:when test="@more = 'true'">
								<img border="0" src="explorer/images/cont.node.mid.gif"/>
							</xsl:when>
							<!-- If this node is the last in its group -->
							<xsl:otherwise>
								<img border="0" src="explorer/images/cont.node.end.gif"/>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:otherwise>
				</xsl:choose>

			</xsl:when>
			<!-- If this node is an item -->
			<xsl:when test="@type = '2'">
				<xsl:choose>
					<!-- If this node is not the last in its group -->
					<xsl:when test="@more = 'true'">
						<img border="0" src="explorer/images/item.node.mid.gif"/>
					</xsl:when>
					<!-- If this node is the last in its group -->
					<xsl:otherwise>
						<img border="0" src="explorer/images/item.node.end.gif"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!--+
	    | Generate the indentation for a parent tree node.
	    +-->
	<xsl:template name="node-parent">
		<xsl:choose>
			<!-- If the PARENT node has more siblings -->
			<xsl:when test="@more = 'true'">
				<img border="0" src="explorer/images/tree.bar.gif"/>
			</xsl:when>
			<!-- If the PARENT node is the last in its group -->
			<xsl:otherwise>
				<img border="0" src="explorer/images/tree.gap.gif"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!--+
	    | Generate the actions for a tree node.
	    +-->
	<xsl:template name="node-actions">
		<xsl:choose>
			<!-- If this is an item -->
			<xsl:when test="@type = '2'">
				<!-- Add the cut-item action -->
				<xsl:text> </xsl:text>
				<link type="action">
					<display>
						<xsl:text>Cut</xsl:text>
					</display>
					<href>
						<base><xsl:value-of select="$explorer-page"/></base>
						<param name="action">cut-item</param>
						<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
						<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
					</href>
				</link>
				<!-- Add the copy-item action -->
				<xsl:text> </xsl:text>
				<link type="action">
					<display>
						<xsl:text>Copy</xsl:text>
					</display>
					<href>
						<base><xsl:value-of select="$explorer-page"/></base>
						<param name="action">copy-item</param>
						<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
						<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
					</href>
				</link>
				<!-- Add the rename-item action -->
				<xsl:text> </xsl:text>
				<link type="action">
					<display>
						<xsl:text>Rename</xsl:text>
					</display>
					<href>
						<base><xsl:value-of select="$explorer-page"/></base>
						<param name="action">rename-item</param>
						<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
						<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
					</href>
				</link>
				<!-- Add the delete-item action -->
				<xsl:text> </xsl:text>
				<link type="action">
					<display>
						<xsl:text>Delete</xsl:text>
					</display>
					<href>
						<base><xsl:value-of select="$explorer-page"/></base>
						<param name="action">delete-item</param>
						<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
						<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
					</href>
				</link>
			</xsl:when>
			<!-- If this is a container -->
			<xsl:when test="@type = '1'">
				<!-- If this is below the top two levels -->
				<xsl:if test="@level > 1">
					<!-- If we have a cut item to paste -->
					<xsl:if test="//explorer/view/selected/@action = 'cut-item'">
						<!-- Add the paste-item action -->
						<xsl:text> </xsl:text>
						<link type="action">
							<display>
								<xsl:text>Paste</xsl:text>
							</display>
							<href>
								<base><xsl:value-of select="$explorer-page"/></base>
								<param name="action">paste-item</param>
								<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
								<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
							</href>
						</link>
					</xsl:if>
					<!-- If we have a copy item to paste -->
					<xsl:if test="//explorer/view/selected/@action = 'copy-item'">
						<!-- Add the paste-item action -->
						<xsl:text> </xsl:text>
						<link type="action">
							<display>
								<xsl:text>Paste</xsl:text>
							</display>
							<href>
								<base><xsl:value-of select="$explorer-page"/></base>
								<param name="action">paste-item</param>
								<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
								<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
							</href>
						</link>
					</xsl:if>
					<!-- Add the new-folder action -->
					<xsl:text> </xsl:text>
					<link type="action">
						<display>
							<xsl:text>New folder</xsl:text>
						</display>
						<href>
							<base><xsl:value-of select="$explorer-page"/></base>
							<param name="action">create-folder</param>
							<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
							<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
						</href>
					</link>
				</xsl:if>
				<!-- If this is below the top three levels -->
				<xsl:if test="@level > 2">
					<!-- Add the delete-item action -->
					<xsl:text> </xsl:text>
					<link type="action">
						<display>
							<xsl:text>Delete</xsl:text>
						</display>
						<href>
							<base><xsl:value-of select="$explorer-page"/></base>
							<param name="action">delete-item</param>
							<param name="AST-VIEW"><xsl:value-of select="//explorer/view/@ident"/></param>
							<param name="AST-PATH"><xsl:value-of select="@href-path"/></param>
						</href>
					</link>
				</xsl:if>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

	<!--+
	    | Generate the create view form.
	    +-->
	<xsl:template name="create-view">
		<form method="get">
			<input type="hidden" name="action"   value="create-view"/>
			<input type="hidden" name="confirm"  value="true"/>
			<table class="info" border="0">
				<tr class="info">
					<td class="info">
						Service :
					</td>
					<td class="info">
						<select name="AST-SERVICE">
							<xsl:for-each select="//explorer/options/services/service">
								<xsl:element name="option">
									<xsl:attribute name="value">
										<xsl:value-of select="@url"/>
									</xsl:attribute>
									<xsl:if test="@default = 'true'">
										<xsl:attribute name="selected">
											<xsl:value-of select="true"/>
										</xsl:attribute>
									</xsl:if>
									<xsl:value-of select="@name"/>
								</xsl:element>
							</xsl:for-each>
						</select>
					</td>
				</tr>
				<tr class="info">
					<td class="info">
					</td>
					<td class="info">
						<input type="submit" value="Create"/>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>

	<!--+
	    | Generate the create folder form.
	    +-->
	<xsl:template name="create-folder">
		<form method="get">
			<input type="hidden" name="AST-VIEW">
				<xsl:attribute name="value">
					<xsl:value-of select="//explorer/view/@ident"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="AST-PATH">
				<xsl:attribute name="value">
					<xsl:value-of select="//explorer/view/current/@path"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="action"   value="create-folder"/>
			<input type="hidden" name="confirm"  value="true"/>
			<table class="info" border="0">
				<tr class="info">
					<td class="info">
						Path
					</td>
					<td class="info">
						<xsl:value-of select="//explorer/view/current/@path"/>
					</td>
				</tr>
				<tr class="info">
					<td class="info">
						Name
					</td>
					<td class="info">
						<input type="text" name="AST-NAME" value="New folder"/>
					</td>
				</tr>
				<tr class="info">
					<td class="info">
					</td>
					<td class="info">
						<input type="submit" value="Create"/>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>

	<!--+
	    | Generate the rename item form.
	    +-->
	<xsl:template name="rename-item">
		<form method="get">
			<input type="hidden" name="AST-VIEW">
				<xsl:attribute name="value">
					<xsl:value-of select="//explorer/view/@ident"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="AST-PATH">
				<xsl:attribute name="value">
					<xsl:value-of select="//explorer/view/selected/@path"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="action"   value="rename-item"/>
			<input type="hidden" name="confirm"  value="true"/>
			<table class="info" border="0">
				<tr class="info">
					<td class="info">
						Path
					</td>
					<td class="info">
						<xsl:value-of select="//explorer/view/selected/@path"/>
					</td>
				</tr>
				<tr class="info">
					<td class="info">
						Name
					</td>
					<td class="info">
						<input type="text" name="AST-NAME">
							<xsl:attribute name="value">
								<xsl:value-of select="//explorer/view/selected/@name"/>
							</xsl:attribute>
						</input>
					</td>
				</tr>
				<tr class="info">
					<td class="info">
					</td>
					<td class="info">
						<input type="submit" value="Rename"/>
					</td>
				</tr>
			</table>
		</form>
	</xsl:template>

	<!--+
	    | Generate the delete item form.
	    +-->
	<xsl:template name="delete-item">
		<form method="get">
			<input type="hidden" name="AST-VIEW">
				<xsl:attribute name="value">
					<xsl:value-of select="//explorer/view/@ident"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="AST-PATH">
				<xsl:attribute name="value">
					<xsl:value-of select="//explorer/view/selected/@path"/>
				</xsl:attribute>
			</input>
			<input type="hidden" name="action"   value="delete-item"/>
			<input type="hidden" name="confirm"  value="true"/>
			<table class="info" border="0">
				<tr class="info">
					<td class="info">
						Name
					</td>
					<td class="info">
						<xsl:value-of select="//explorer/view/selected/@name"/>
					</td>
				</tr>
				<tr class="info">
					<td class="info">
						Path
					</td>
					<td class="info">
						<xsl:value-of select="//explorer/view/selected/@path"/>
					</td>
				</tr>
				<tr class="info">
					<td class="info">
					</td>
					<td class="info">
						<input type="submit" value="Delete"/>
					</td>
				</tr>
			</table>
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
