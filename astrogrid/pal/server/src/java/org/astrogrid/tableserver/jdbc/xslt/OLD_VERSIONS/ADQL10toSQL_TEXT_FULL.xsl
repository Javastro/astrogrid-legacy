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
	 -->
	
    <xsl:output method="text" indent="no"/>
   		
    <xsl:param name="spaceCharacter" select="' '"/>      		
   		
	<!-- the root template -->
	

	<xsl:template match="/">
	       <xsl:apply-templates select="*"/>
	</xsl:template>
	
	
	<!-- the 'select' template -->
	
	<xsl:template match="ad:Select | ad:selection | ad:Selection">
		<xsl:text>Select </xsl:text>
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
				<xsl:text>,</xsl:text>
				<xsl:value-of select="$spaceCharacter"/>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
	    <xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'aliasSelectionItemType'] | *[@xsi:type = 'aliasSelectionItemType']">
		<xsl:apply-templates select="ad:Expression"/>
		<xsl:if test="@As">
			<xsl:text> as </xsl:text>
			<xsl:value-of select="@As"/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'allSelectionItemType'] | *[@xsi:type = 'allSelectionItemType']">
		<xsl:text>*</xsl:text>
	</xsl:template>
	
	<xsl:template match="ad:InTo">	    
		<xsl:text>Into</xsl:text>
		<xsl:value-of select="ad:TableName"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<xsl:template match="ad:From">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Table">
				<xsl:apply-templates select="."/>
				<xsl:text>, </xsl:text>
			</xsl:for-each>
		</xsl:variable>		
		<xsl:text>From </xsl:text>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'archiveTableType'] | *[@xsi:type = 'archiveTableType']">
	    <xsl:value-of select="@Archive"/>
		<xsl:text>:</xsl:text>
		<xsl:value-of select="@Name"/>
		<xsl:if test="@Alias">
			<xsl:text> as </xsl:text>
			<xsl:value-of select="@Alias"/>
		</xsl:if>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'tableType'] | *[@xsi:type = 'tableType']">
      <xsl:if test="@Archive">
         <xsl:value-of select="@Archive"/>
         <xsl:text>.</xsl:text>
      </xsl:if>
		<xsl:value-of select="@Name"/>
		<xsl:if test="@Alias">
			<xsl:text> as </xsl:text>
			<xsl:value-of select="@Alias"/>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="ad:Where">  
		<xsl:text>Where </xsl:text>
		<xsl:apply-templates select="ad:Condition"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<xsl:template match="ad:GroupBy">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Column">
				<xsl:apply-templates select="."/>
				<xsl:text>,</xsl:text>
				<xsl:value-of select="$spaceCharacter"/>
			</xsl:for-each>
		</xsl:variable>		
		<xsl:text>Group By </xsl:text>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<xsl:template match="ad:Having"> 	
		<xsl:text>Having </xsl:text>
		<xsl:apply-templates select="ad:Condition"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<xsl:template match="ad:OrderBy">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Item">
				<xsl:apply-templates select="ad:Expression"/>
				<xsl:apply-templates select="ad:Order"/>
				<xsl:text>,</xsl:text>
				<xsl:value-of select="$spaceCharacter"/>
			</xsl:for-each>
		</xsl:variable>
		<xsl:text>Order By </xsl:text>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="ad:Order">
		<xsl:text> </xsl:text>
		<xsl:value-of select="@Direction"/>
	</xsl:template>
	
	<!-- the 'searchType' templates -->
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'intersectionSearchType'] | *[@xsi:type = 'intersectionSearchType']">    
      <xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Condition[1]"/>
      <xsl:text>) </xsl:text>
		<xsl:text> And </xsl:text>
      <xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Condition[2]"/>
      <xsl:text>) </xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'unionSearchType'] | *[@xsi:type = 'unionSearchType']">
      <xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Condition[1]"/>
      <xsl:text>) </xsl:text>
		<xsl:text> Or </xsl:text>
      <xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Condition[2]"/>
      <xsl:text>) </xsl:text>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'likePredType'] | *[@xsi:type = 'likePredType']">
		<xsl:apply-templates select="ad:Arg"/>	
		<xsl:text> Like </xsl:text>
		<xsl:apply-templates select="ad:Pattern"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'notLikePredType'] | *[@xsi:type = 'notLikePredType']">
		<xsl:apply-templates select="ad:Arg"/>	
		<xsl:text> Not Like </xsl:text>
		<xsl:apply-templates select="ad:Pattern"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'inclusiveSearchType'] | *[@xsi:type = 'inclusiveSearchType']">
		<xsl:apply-templates select="ad:Expression"/>
		<xsl:text> In (</xsl:text>
		<xsl:apply-templates select="ad:Set"/>
		<xsl:text>)</xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'exclusiveSearchType'] | *[@xsi:type = 'exclusiveSearchType']">
		<xsl:apply-templates select="ad:Expression"/>
		<xsl:text> Not In (</xsl:text>
		<xsl:apply-templates select="ad:Set"/>
		<xsl:text>)</xsl:text>
		<xsl:value-of select="$spaceCharacter"/>		
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'subQuerySet'] | *[@xsi:type = 'subQuerySet']">
		<xsl:apply-templates select="ad:Selection"/>
		<xsl:apply-templates select="ad:selection"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'constantListSet'] | *[@xsi:type = 'constantListSet']">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Item">
				<xsl:apply-templates select="."/>
				<xsl:text>,</xsl:text>
				<xsl:value-of select="$spaceCharacter"/>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'ConstantListSet'] | *[@xsi:type = 'ConstantListSet']">
		<xsl:variable name="list">
			<xsl:for-each select="ad:Item">
				<xsl:apply-templates select="."/>
				<xsl:text>,</xsl:text>
				<xsl:value-of select="$spaceCharacter"/>
			</xsl:for-each>
		</xsl:variable>
		<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'closedSearchType'] | *[@xsi:type = 'closedSearchType']">
		<xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Condition"/>
      <xsl:text>) </xsl:text>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'comparisonPredType'] | *[@xsi:type = 'comparisonPredType']">
		<xsl:apply-templates select="ad:Arg[1]"/>
		<xsl:value-of select="@Comparison"/>
		<xsl:apply-templates select="ad:Arg[2]"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'betweenPredType'] | *[@xsi:type = 'betweenPredType']">   
		<xsl:apply-templates select="ad:Arg[1]"/>
		<xsl:text> Between </xsl:text>
		<xsl:apply-templates select="ad:Arg[2]"/>
		<xsl:text> And </xsl:text>
		<xsl:apply-templates select="ad:Arg[3]"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'notBetweenPredType'] | *[@xsi:type = 'notBetweenPredType']">
		<xsl:apply-templates select="ad:Arg[1]"/>
		<xsl:text> Not Between </xsl:text>
		<xsl:apply-templates select="ad:Arg[2]"/>
		<xsl:text> And </xsl:text>
		<xsl:apply-templates select="ad:Arg[3]"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'inverseSearchType'] | *[@xsi:type = 'inverseSearchType']">
		<xsl:text>Not </xsl:text>
		<xsl:apply-templates select="ad:Condition"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<!-- the 'expressionType' templates -->
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'closedExprType'] | *[@xsi:type = 'closedExprType']">
		<xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Arg"/>
		<xsl:text>) </xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
   <!-- KEA: 
       - Added brackets around binary expressions to ensure correct
          precendence
       - Put spaces around operator to avoid issues with e.g. subtracting
          a negated arg. (SQLServer doesn't like doubled - with no spaces)
   -->
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'binaryExprType'] | *[@xsi:type = 'binaryExprType']">
		<xsl:text> (</xsl:text>
		<xsl:apply-templates select="ad:Arg[1]"/>
      <xsl:value-of select="$spaceCharacter"/>
		<xsl:value-of select="@Oper"/>
      <xsl:value-of select="$spaceCharacter"/>
		<xsl:apply-templates select="ad:Arg[2]"/>
		<xsl:text>) </xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'unaryExprType'] | *[@xsi:type = 'unaryExprType']">
		<xsl:value-of select="@Oper"/>
		<xsl:apply-templates select="ad:Arg"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'columnReferenceType'] | *[@xsi:type = 'columnReferenceType']">
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
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="ad:Restrict">
		<xsl:text>Top </xsl:text>
		<xsl:value-of select="@Top"/>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<!-- the 'functionType' templates -->
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'trigonometricFunctionType'] | *[@xsi:type = 'trigonometricFunctionType']">
		<xsl:value-of select="@Name"/>
		<xsl:text>(</xsl:text>
		<xsl:apply-templates select="ad:Allow"/>
		<xsl:apply-templates select="ad:Arg"/>
		<xsl:text>)</xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'mathFunctionType'] | *[@xsi:type = 'mathFunctionType']">
		<xsl:value-of select="@Name"/>
		<xsl:text>(</xsl:text>
		<xsl:apply-templates select="ad:Allow"/>
		<xsl:apply-templates select="ad:Arg"/>
		<xsl:text>)</xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'aggregateFunctionType'] | *[@xsi:type = 'aggregateFunctionType']">
		<xsl:value-of select="@Name"/>
		<xsl:text>(</xsl:text>
		<xsl:apply-templates select="ad:Allow"/>
		<xsl:apply-templates select="ad:Arg"/>
		<xsl:text>)</xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'userDefinedFunctionType'] | *[@xsi:type = 'userDefinedFunctionType']">
	    <xsl:value-of select="$spaceCharacter"/>
		<xsl:value-of select="ad:Name"/>
		<xsl:text>(</xsl:text>
		<xsl:if test="ad:Params">
			<xsl:variable name="list">
				<xsl:for-each select="ad:Params">
					<xsl:apply-templates select="."/>
					<xsl:text>,</xsl:text>
					<xsl:value-of select="$spaceCharacter"/>
				</xsl:for-each>
			</xsl:variable>
			<xsl:value-of select="substring($list, 1, string-length($list)-2)"/>
		</xsl:if>
		<xsl:text>)</xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<!-- Jeff's additions start here -->
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'regionSearchType'] | *[@xsi:type = 'regionSearchType']"> 
		<xsl:text>Region( </xsl:text>
		<xsl:apply-templates select="ad:Region"/>
		<xsl:text> )</xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<xsl:template match="*[substring-after(@xsi:type, ':') = 'circleType'] | *[@xsi:type = 'circleType']">
	    <xsl:value-of select="$spaceCharacter"/>
		<xsl:text>'Circle  J2000  </xsl:text>
		<xsl:apply-templates select="stc:Center"/>
		<xsl:text>  </xsl:text>
		<xsl:apply-templates select="stc:Radius"/>
		<xsl:text>'</xsl:text>
		<xsl:value-of select="$spaceCharacter"/>
	</xsl:template>
	
	<xsl:template match="stc:Center">
		<xsl:value-of select="."/>
	</xsl:template>
	
	<xsl:template match="stc:Radius">
		<xsl:value-of select="."/>
	</xsl:template>
	
	<!-- Jeff's additions end here -->
	
	
	
	
	
	
	<xsl:template match="text()"/>
	
</xsl:stylesheet>

