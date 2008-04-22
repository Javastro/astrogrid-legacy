<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
		xmlns="http://www.ivoa.net/xml/ADQL/v1.0" 
		xmlns:ad="http://www.ivoa.net/xml/ADQL/v1.0"
		xmlns:stc="http://www.ivoa.net/xml/STC/STCregion/v1.10"
		xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
	
	<!-- 
		Stylesheet to convert ADQL/x v1.0 to an SQL string
		Version 1.0 - first release - July 8, 2005
		Aurelien STEBE - ESAC - ESA
		Aurelien.Stebe@sciops.esa.int
		
		Special version amended by Jeff Lusted as a tool used
		within the Astrogrid Query Builder. June, 2006.
		jl99@star.le.ac.uk
	 -->
	
    <xsl:output method="text" indent="no"/>
    
    <xsl:param name="keywordColor" select="'#E9967A'"/>
    <xsl:param name="defaultColor" select="'Black'"/>   
    <xsl:param name="opsColor" select="'Black'" />
    <xsl:param name="literalColor" select="'Blue'" />
    <xsl:param name="commentColor" select="'Green'" />
   		
	<!-- the root template -->
	<xsl:template match="/">
	       <xsl:call-template name="startHtml" />
	       <xsl:apply-templates select="*"/>
	       <xsl:call-template name="endHtml" />
	</xsl:template>
	
	
	<!-- the 'select' template -->
	
	<xsl:template match="ad:Select | ad:selection | ad:Selection">
	    <xsl:call-template name="startKeyword" />
		<xsl:text>Select </xsl:text>
		<xsl:call-template name="endKeyword" />
		
		<xsl:apply-templates select="ad:Allow"/>
		<xsl:apply-templates select="ad:Restrict"/>
		<xsl:apply-templates select="ad:SelectionList"/>
		<xsl:apply-templates select="ad:InTo"/>
		<xsl:apply-templates select="ad:From"/>
		<xsl:apply-templates select="ad:Where"/>
		<xsl:apply-templates select="ad:GroupBy"/>
		<xsl:apply-templates select="ad:Having"/>
		<xsl:apply-templates select="ad:OrderBy"/>
	</xsl:template>
	
	<!-- the "main" elements -->
	
	<xsl:template match="ad:SelectionList">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Item">
				<xsl:apply-templates select="."/>
				<xsl:text>,&#160;</xsl:text>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
	    <xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'aliasSelectionItemType'] | *[@xsi:type = 'aliasSelectionItemType']">
		<xsl:apply-templates select="ad:Expression"/>
	    <xsl:call-template name="startKeyword" />
		<xsl:if test="@As">
			<xsl:text> as </xsl:text>
			<xsl:value-of select="@As"/>
		</xsl:if>
		<xsl:call-template name="endKeyword" />
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'allSelectionItemType'] | *[@xsi:type = 'allSelectionItemType']">
		<xsl:text>*</xsl:text>
	</xsl:template>
	
	<xsl:template match="ad:InTo">
		<xsl:call-template name="startKeyword" />	    
		<xsl:text>Into</xsl:text>
		<xsl:call-template name="endKeyword" />
		<xsl:value-of select="ad:TableName"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="ad:From">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Table">
				<xsl:apply-templates select="."/>
				<xsl:text>, </xsl:text>
			</xsl:for-each>
		</xsl:variable>	
		<xsl:call-template name="startKeyword" />	
		<xsl:text>From </xsl:text>
		<xsl:call-template name="endKeyword" />
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'archiveTableType'] | *[@xsi:type = 'archiveTableType']">
	    <xsl:value-of select="@Archive"/>
		<xsl:text>:</xsl:text>
		<xsl:value-of select="@Name"/>
		<xsl:if test="@Alias">
		    <xsl:call-template name="startKeyword" />
			<xsl:text> as </xsl:text>
			<xsl:call-template name="endKeyword" />
			<xsl:value-of select="@Alias"/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'tableType'] | *[@xsi:type = 'tableType']">
	    <!-- xsl:call-template name="startLiteral" / -->
		<xsl:value-of select="@Name"/>
		<!-- xsl:call-template name="endLiteral" / -->
		<xsl:if test="@Alias">
		    <!-- xsl:call-template name="startKeyword" / -->
			<xsl:text> as </xsl:text>
			<!-- xsl:call-template name="endKeyword" / -->
			<!-- xsl:call-template name="startLiteral" / -->
			<xsl:value-of select="@Alias"/>
			<!-- xsl:call-template name="endLiteral" / -->
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'joinTableType'] | *[@xsi:type = 'joinTableType']">
	    <xsl:variable name="joinType" select="ad:Qualifier" />
	    <xsl:apply-templates select="ad:Tables/ad:fromTableType[1]"/>
	    <xsl:call-template name="startKeyword" />
        <xsl:choose>
           <xsl:when test="$joinType='LEFT_OUTER'" ><xsl:text> LEFT OUTER JOIN </xsl:text></xsl:when>
           <xsl:when test="$joinType='RIGHT_OUTER'" ><xsl:text> RIGHT OUTER JOIN </xsl:text></xsl:when>           
           <xsl:when test="$joinType='FULL_OUTER'" ><xsl:text> FULL OUTER JOIN </xsl:text></xsl:when>           
           <xsl:when test="$joinType='INNER'" ><xsl:text> INNER JOIN </xsl:text></xsl:when>    
           <xsl:when test="$joinType='CROSS'" ><xsl:text> CROSS JOIN </xsl:text></xsl:when> 
           <xsl:otherwise><xsl:text> INNER JOIN </xsl:text></xsl:otherwise>  
        </xsl:choose> 
        <xsl:call-template name="endKeyword" />
        <xsl:apply-templates select="ad:Tables/ad:fromTableType[2]"/>   
        <xsl:choose>
           <xsl:when test="$joinType!='CROSS'" >
              <xsl:call-template name="startKeyword" />
              <xsl:text> ON </xsl:text> 
              <xsl:call-template name="endKeyword" /> 
              <xsl:apply-templates select="ad:Condition"/> 
           </xsl:when>
        </xsl:choose>              
	</xsl:template>
	
	<xsl:template match="ad:Tables">
	    <xsl:variable name="list">
			<xsl:for-each select="ad:fromTableType">
				<xsl:apply-templates select="."/>
				<xsl:text>, </xsl:text>
			</xsl:for-each>
		</xsl:variable>	
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="ad:Where">  
	    <xsl:call-template name="startKeyword" />
		<xsl:text>Where </xsl:text>
		<xsl:call-template name="endKeyword" />
		<xsl:apply-templates select="ad:Condition"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="ad:GroupBy">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Column">
				<xsl:apply-templates select="."/>
				<xsl:text>,&#160;</xsl:text>
			</xsl:for-each>
		</xsl:variable>	
		<xsl:call-template name="startKeyword" />	
		<xsl:text>Group By </xsl:text>
		<xsl:call-template name="endKeyword" />
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="ad:Having"> 
	    <xsl:call-template name="startKeyword" />	
		<xsl:text>Having </xsl:text>
		<xsl:call-template name="endKeyword" />
		<xsl:apply-templates select="ad:Condition"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="ad:OrderBy">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Item">
				<xsl:apply-templates select="ad:Expression"/>
				<xsl:apply-templates select="ad:Order"/>
				<xsl:text>,&#160;</xsl:text>
			</xsl:for-each>
		</xsl:variable>
		<xsl:call-template name="startKeyword" />
		<xsl:text>Order By </xsl:text>
		<xsl:call-template name="endKeyword" />
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="ad:Order">
		<xsl:text> </xsl:text>
		<xsl:value-of select="@Direction"/>
	</xsl:template>
	
	<!-- the 'searchType' templates -->
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'intersectionSearchType'] | *[@xsi:type = 'intersectionSearchType']">    
		<xsl:apply-templates select="ad:Condition[1]"/>
		<xsl:text> And </xsl:text>
		<xsl:apply-templates select="ad:Condition[2]"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'unionSearchType'] | *[@xsi:type = 'unionSearchType']">
		<xsl:apply-templates select="ad:Condition[1]"/>
		<xsl:text> Or </xsl:text>
		<xsl:apply-templates select="ad:Condition[2]"/>
        <xsl:text>&#160;</xsl:text>		
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'likePredType'] | *[@xsi:type = 'likePredType']">
		<xsl:apply-templates select="ad:Arg"/>
		<xsl:text> Like </xsl:text>
		<xsl:apply-templates select="ad:Pattern"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'notLikePredType'] | *[@xsi:type = 'notLikePredType']">
		<xsl:apply-templates select="ad:Arg"/>
		<xsl:text> Not Like </xsl:text>
		<xsl:apply-templates select="ad:Pattern"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'inclusiveSearchType'] | *[@xsi:type = 'inclusiveSearchType']">
		<xsl:apply-templates select="ad:Expression"/>
		<xsl:text> In (</xsl:text>
		<xsl:apply-templates select="ad:Set"/>
		<xsl:text>)</xsl:text>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'exclusiveSearchType'] | *[@xsi:type = 'exclusiveSearchType']">
		<xsl:apply-templates select="ad:Expression"/>
		<xsl:text> Not In (</xsl:text>
		<xsl:apply-templates select="ad:Set"/>
		<xsl:text>)</xsl:text>
		<xsl:text>&#160;</xsl:text>		
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'subQuerySet'] | *[@xsi:type = 'subQuerySet']">
		<xsl:apply-templates select="ad:Selection"/>
		<xsl:apply-templates select="ad:selection"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'constantListSet'] | *[@xsi:type = 'constantListSet']">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Item">
				<xsl:apply-templates select="."/>
				<xsl:text>,&#160;</xsl:text>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'ConstantListSet'] | *[@xsi:type = 'ConstantListSet']">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Item">
				<xsl:apply-templates select="."/>
				<xsl:text>,&#160;</xsl:text>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'closedSearchType'] | *[@xsi:type = 'closedSearchType']">
		<xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Condition"/>
		<xsl:text>)&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'comparisonPredType'] | *[@xsi:type = 'comparisonPredType'] | *[@Comparison]">
		<xsl:apply-templates select="ad:Arg[1]"/>
		<!-- xsl:value-of select="@Comparison"/ -->
		<xsl:choose>
			<xsl:when test="@Comparison = '='"><xsl:text>=</xsl:text></xsl:when> 
			<xsl:when test="@Comparison = '&lt;&gt;'"><xsl:text>&#038;lt;&gt;</xsl:text></xsl:when> 
			<xsl:when test="@Comparison = '&lt;>'"><xsl:text>&#038;lt;&gt;</xsl:text></xsl:when> 
			<xsl:when test="@Comparison = '&gt;'"><xsl:text>&gt;</xsl:text></xsl:when>
			<xsl:when test="@Comparison = '&gt;='"><xsl:text>&gt;=</xsl:text></xsl:when>
			<xsl:when test="@Comparison = '&lt;'"><xsl:text>&#038;lt;</xsl:text></xsl:when>
			<xsl:when test="@Comparison = '&lt;='"><xsl:text>&#038;lt;=</xsl:text></xsl:when>
			<xsl:otherwise><xsl:value-of select="@Comparison"/></xsl:otherwise>
		</xsl:choose>
		<xsl:apply-templates select="ad:Arg[2]"/>	  
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'betweenPredType'] | *[@xsi:type = 'betweenPredType']">   
		<xsl:apply-templates select="ad:Arg[1]"/>
		<xsl:text> Between </xsl:text>
		<xsl:apply-templates select="ad:Arg[2]"/>
		<xsl:text> And </xsl:text>
		<xsl:apply-templates select="ad:Arg[3]"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'notBetweenPredType'] | *[@xsi:type = 'notBetweenPredType']">
		<xsl:apply-templates select="ad:Arg[1]"/>
		<xsl:text> Not Between </xsl:text>
		<xsl:apply-templates select="ad:Arg[2]"/>
		<xsl:text> And </xsl:text>
		<xsl:apply-templates select="ad:Arg[3]"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'inverseSearchType'] | *[@xsi:type = 'inverseSearchType']">
		<xsl:text>Not </xsl:text>
		<xsl:apply-templates select="ad:Condition"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<!-- the 'expressionType' templates -->
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'closedExprType'] | *[@xsi:type = 'closedExprType']">
		<xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Arg"/>
		<xsl:text>)&#160;</xsl:text>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'binaryExprType'] | *[@xsi:type = 'binaryExprType']">
		<xsl:apply-templates select="ad:Arg[1]"/>
		<xsl:value-of select="@Oper"/>
		<xsl:apply-templates select="ad:Arg[2]"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'unaryExprType'] | *[@xsi:type = 'unaryExprType']">
		<xsl:value-of select="@Oper"/>
		<xsl:apply-templates select="ad:Arg"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'columnReferenceType'] | *[@xsi:type = 'columnReferenceType'] | ad:Column">
		<xsl:value-of select="@Table"/>
		<xsl:text>.</xsl:text>
		<xsl:value-of select="@Name"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'atomType'] | *[@xsi:type = 'atomType']">
		<xsl:apply-templates select="ad:Literal"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'realType'] | *[@xsi:type = 'realType']">
		<xsl:value-of select="@Value"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'integerType'] | *[@xsi:type = 'integerType']">
		<xsl:value-of select="@Value"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'stringType'] | *[@xsi:type = 'stringType']">
		<xsl:text>'</xsl:text>
		<xsl:value-of select="@Value"/>
		<xsl:text>'</xsl:text>
	</xsl:template>
	
	<xsl:template match="ad:Allow">
		<xsl:value-of select="@Option"/>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="ad:Restrict">
	    <xsl:call-template name="startKeyword" />
		<xsl:text>Top </xsl:text>
		<xsl:call-template name="endKeyword" />
		<xsl:call-template name="startLiteral" />
		<xsl:value-of select="@Top"/>
		<xsl:call-template name="endLiteral" />
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<!-- the 'functionType' templates -->
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'trigonometricFunctionType'] | *[@xsi:type = 'trigonometricFunctionType']">
		<xsl:value-of select="@Name"/>
		<xsl:text>(</xsl:text>
        <!-- Jeff's change to accommodate function argument cardinalities -->
			<xsl:variable name="list">
				<xsl:for-each select="ad:Arg">
					<xsl:apply-templates select="."/>
					<xsl:text>,</xsl:text>
					<xsl:text> </xsl:text>
				</xsl:for-each>
			</xsl:variable>
			<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
	    <!-- end of Jeff's change to accommodate function argument cardinalities -->			
		<xsl:text>)</xsl:text>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'mathFunctionType'] | *[@xsi:type = 'mathFunctionType']">
		<xsl:value-of select="@Name"/>
		<xsl:text>(</xsl:text>
		<!-- Jeff's change to accommodate function argument cardinalities -->
			<xsl:variable name="list">
				<xsl:for-each select="ad:Arg">
					<xsl:apply-templates select="."/>
					<xsl:text>,</xsl:text>
					<xsl:text> </xsl:text>
				</xsl:for-each>
			</xsl:variable>
			<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
	    <!-- end of Jeff's change to accommodate function argument cardinalities -->	
		<xsl:text>)</xsl:text>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'aggregateFunctionType'] | *[@xsi:type = 'aggregateFunctionType']">    
		<xsl:value-of select="@Name"/>
		<xsl:text>(</xsl:text>
		<xsl:apply-templates select="ad:Allow"/>
		<xsl:apply-templates select="ad:Arg"/>		
		<xsl:text>)</xsl:text>
		<xsl:text>&#160;</xsl:text>
	</xsl:template>
	
	<!-- Jeff's additions start here -->
	
	<xsl:template name="startDefault" >
	   <xsl:text>&lt;font color="</xsl:text>
	   <xsl:value-of select="$defaultColor"/>
	   <xsl:text>"&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template name="endDefault" >
	   <xsl:call-template name="endFont" />
	</xsl:template>
	
	<xsl:template name="startKeyword" >
	   <xsl:text>&lt;font color="</xsl:text>
	   <xsl:value-of select="$keywordColor"/>
	   <xsl:text>"&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template name="endKeyword" >
	   <xsl:call-template name="endFont" />
	</xsl:template>
	
	<xsl:template name="startOps" >
	   <xsl:text>&lt;font color="</xsl:text>
	   <xsl:value-of select="$opsColor"/>
	   <xsl:text>"&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template name="endOps" >
	   <xsl:call-template name="endFont" />
	</xsl:template>
	
	<xsl:template name="startLiteral" >
	   <xsl:text>&lt;font color="</xsl:text>
	   <xsl:value-of select="$literalColor"/>
	   <xsl:text>"&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template name="endLiteral" >
	   <xsl:call-template name="endFont" />
	</xsl:template>
	
	<xsl:template name="startComment" >
	   <xsl:text>&lt;font color="</xsl:text>
	   <xsl:value-of select="$literalColor"/>
	   <xsl:text>"&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template name="endComment" >
	   <xsl:call-template name="endFont" />
	</xsl:template>
	
	<xsl:template name="endFont" >
	   <xsl:text>&lt;/font&gt;</xsl:text>
	</xsl:template>
	
	<xsl:template name="startHtml" >
	   <xsl:text>&lt;html&gt;</xsl:text>
	   <xsl:call-template name="startDefault" />
	</xsl:template>
	
	<xsl:template name="endHtml" >	   
	   <xsl:call-template name="endDefault" />
	   <xsl:text>&lt;/html&gt;</xsl:text>
	</xsl:template>
	
	
	<!-- Jeff's additions end here -->
	
	<xsl:template match="text()"/>
	
</xsl:stylesheet>

