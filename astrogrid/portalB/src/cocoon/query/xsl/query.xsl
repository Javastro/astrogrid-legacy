<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<!--+
	    | The current action, set from the Cocoon action.
	    +-->
	<xsl:param name="QueryString" />
	<xsl:param name="Script" />
	<xsl:param name="QueryStringSent" />	
	<xsl:param name="ErrorMessage" />
	<xsl:param name="LastWebCall" />
	
	<!--+
	    | The page names, set from the Cocoon sitemap.
	    +-->
	<xsl:param name="explorer-page">explorer</xsl:param>
	<xsl:param name="votable-page">votable</xsl:param>

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the explorer element.
		+-->
	<xsl:template match="query">
		<page>
			<!-- Add our page content -->
			<content>
				<xsl:call-template name="query_form"/>
			</content>
		</page>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="query_form">
		<xsl:if test="$ErrorMessage != ''">	
			<font color="red">
				<xsl:value-of select="$ErrorMessage" />
			</font>	
		</xsl:if>
		<xsl:if test="$Script != ''">	
		<SCRIPT language="javascript">
				<xsl:comment>
				<xsl:value-of select="$Script" />
				</xsl:comment>
		</SCRIPT>
		</xsl:if>
		<form method="get" name="DataQueryForm">
			<strong>DataSetAgent Server</strong>
			<select name="DataSetAgent">
				<xsl:for-each select="//query/options/dsagents/dsagent">
					<xsl:element name="option">
						<xsl:attribute name="value">
							<xsl:value-of select="@val"/>
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
			<br />
			
			<strong>Information you want returned:</strong>
			<select name="DataSetName" onChange="updateCols(document.DataQueryForm.DataSetName.selectedIndex,document.DataQueryForm.ReturnColumn)">
				<xsl:for-each select="//query/options/datasets/datasetname">
					<xsl:element name="option">
						<xsl:attribute name="value">
							<xsl:value-of select="@val"/>
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
			<select name="ReturnColumn">
				<xsl:for-each select="//query/options/subofdatasets/returncolumn">
					<xsl:element name="option">
						<xsl:attribute name="value">
							<xsl:value-of select="@val"/>
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
			<br />
			<input type="submit" name="AddSelection" value="Add Selection" />
			<input type="submit" name="RemoveSelection" value="Remove Selection" />
			<hr />
			<h1>Query Filter</h1>
			<xsl:if test="//query/options/joins">
				<br />
				<strong>Choose the join operation for your next criteria:</strong>
				<select name="JoinType">
					<xsl:for-each select="//query/options/joins/jointype">
						<xsl:element name="option">
							<xsl:attribute name="value">
								<xsl:value-of select="@val"/>
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
			</xsl:if>
			<br />
			<strong>Choose a Data set name that matches with your Selection:</strong>
			<select name="DataSetNameCriteria" onChange="updateCols(document.DataQueryForm.DataSetNameCriteria.selectedIndex,document.DataQueryForm.FilterColumn)">
				<xsl:for-each select="//query/options/datasets/datasetname">
					<xsl:element name="option">
						<xsl:attribute name="value">
							<xsl:value-of select="@val"/>
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
			<br />
			<strong>Choose a Column, UCD, Function to filter on:</strong>
			<select name="FilterColumn">
				<xsl:for-each select="//query/options/subofdatasets/returncolumn">
					<xsl:element name="option">
						<xsl:attribute name="value">
							<xsl:value-of select="@val"/>
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
			<br />
			<strong>Pick a Operator you desire to filter against the value:</strong>
			<select name="Operator">
				<xsl:for-each select="//query/options/operators/operator">
					<xsl:element name="option">
						<xsl:attribute name="value">
							<xsl:value-of select="@val"/>
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
			<i>* A NONE is provided in case certain functions do not require an operator</i>
			<br />
			Only to be used for Functions, this is a comma seperated values list:
			<input type="text" name="FunctionValues" />
			<i>* ex:  44,56.98,22.33</i>
			<br />
			Please input the value to filter against:
			<input type="text" name="Value" />
			<br />
			<input type="submit" name="AddCriteria" value="Add Criteria" />
			<input type="submit" name="RemoveCriteria" value="Remove Criteria" />
			<xsl:if test="$QueryString != ''">
				<hr />
				<h1>The Query</h1>
				<xsl:value-of select="$QueryString" />
				<br />
				<input type="submit" name="ClearQuery" value="Clear Query" />
				<input type="submit" name="SubmitQuery" value="Send Query" />
			</xsl:if>
		</form>
		<xsl:if test="$QueryStringSent != ''">
			<br />
			<xsl:value-of select="$QueryStringSent" />
		</xsl:if>
		<xsl:if test="$LastWebCall != ''">	
			<br />
			<br />
			<i>Here is a look at the last XML string sent to the webservice.</i>
			<br />
			<xsl:value-of select="$LastWebCall" />
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
