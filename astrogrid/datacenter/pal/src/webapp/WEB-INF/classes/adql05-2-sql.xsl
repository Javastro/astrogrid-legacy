<?xml version="1.0"?>
<xsl:stylesheet xmlns:q1="urn:nvo-region" 
                xmlns:q2="urn:nvo-coords" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                version="1.0">

   <xsl:output method="text"/>

   <xsl:template match="/">
      <xsl:text>SELECT </xsl:text>
      <xsl:apply-templates select="/*"/>
   </xsl:template>

   <xsl:template match="/*">
      <xsl:apply-templates select="OptionalAllOrDistinct"/>
      <xsl:apply-templates select="OptionalTop"/>
      <xsl:apply-templates select="Selection"/>
      <xsl:text> FROM </xsl:text>
      <xsl:apply-templates select="TableClause/FromClause"/>
      <xsl:apply-templates select="TableClause/WhereClause"/>
       <xsl:apply-templates select="TableClause/GroupByClause"/>
      <xsl:apply-templates select="TableClause/HavingClause"/>
      <xsl:apply-templates select="OrderBy"/>
   </xsl:template>

   <!-- 
     -  OptionalAllOrDistinct Template 
     -->
   <xsl:template match="OptionalAllOrDistinct">
      <xsl:value-of select="Option"/>
      <xsl:text> </xsl:text>
   </xsl:template>

   <!-- 
     -  OptionalTop Template 
     -->
   <xsl:template match="OptionalTop">
      <xsl:text>TOP </xsl:text>
      <xsl:value-of select="Count"/>
      <xsl:text> </xsl:text>
   </xsl:template>

   <!-- 
     -  OrderBy Template 
     -->
   <xsl:template match="OrderBy">
      <xsl:text> ORDER BY </xsl:text>
      <xsl:variable name="string">
         <xsl:for-each select="OrderList/Order">
            <xsl:apply-templates select="Expr"/>
            <xsl:if test="Option/Direction">
               <xsl:text> </xsl:text>
               <xsl:value-of select="Option/Direction"/>
            </xsl:if>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of 
           select="substring($string, 1, string-length($string) - 2)"/>
   </xsl:template>

   <!-- 
     -  Selection Template 
     -->
   <xsl:template match="Selection">
      <xsl:variable name="string">
         <xsl:for-each select="Items/SelectionItem">
            <xsl:choose>
               <xsl:when test="@xsi:type='AllSelectionItem'">
                  <xsl:text> * </xsl:text>
                  <xsl:text>, </xsl:text>
               </xsl:when>
               <xsl:when test="@xsi:type='AliasSelectionItem'">
                  <xsl:apply-templates select="Expr"/>
                  <xsl:text> AS </xsl:text>
                  <xsl:value-of select="AliasName"/>
                  <xsl:text>, </xsl:text>
               </xsl:when>
               <xsl:when test="@xsi:type='ExprSelectionItem'">
                  <xsl:apply-templates select="Expr"/>
                  <xsl:text>, </xsl:text>
               </xsl:when>
            </xsl:choose>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of 
           select="substring($string, 1, string-length($string) - 2)"/>
   </xsl:template>

   <!-- 
     -  From Template 
     -->
   <xsl:template match="FromClause">
      <xsl:variable name="string">
         <xsl:for-each select="TableReference/Table">
            <xsl:value-of select="Name"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="AliasName"/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of 
           select="substring($string, 1, string-length($string) - 2)"/>
   </xsl:template>

   <!-- 
     -  Intersection: a AND b
     -->
   <xsl:template match="*[@xsi:type='IntersectionSearch']">
      <xsl:apply-templates select="FirstCondition"/>
      <xsl:text> AND </xsl:text>
      <xsl:apply-templates select="SecondCondition"/>
   </xsl:template>

   <!-- 
     -  Union: a OR b
     -->
   <xsl:template match="*[@xsi:type='UnionSearch']">
      <xsl:apply-templates select="FirstCondition"/>
      <xsl:text> OR </xsl:text>
      <xsl:apply-templates select="SecondCondition"/>
   </xsl:template>

   <!-- 
     -  Inverse: NOT a
     -->
   <xsl:template match="*[@xsi:type='InverseSearch']">
      <xsl:text>NOT </xsl:text>
      <xsl:apply-templates select="Condition"/>
   </xsl:template>

   <!--
     -  Closed:  (a)
     -->
   <xsl:template match="*[@xsi:type='ClosedSearch']">
      <xsl:text>(</xsl:text>
      <xsl:apply-templates select="Condition"/>
      <xsl:text>)</xsl:text>
   </xsl:template>

   <!--
     -  Region Region(a)
     -->
   <xsl:template match="*[@xsi:type='RegionSearch']">
      <xsl:text>REGION('Circle </xsl:text>
      <xsl:choose>
         <xsl:when test="Region/q1:Center/q2:Pos2Vector">
            <xsl:text>J2000 </xsl:text>
            <xsl:apply-templates 
                 select="Region/q1:Center/q2:Pos2Vector/q2:CoordValue"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="Region/q1:Radius"/>
         </xsl:when>
         <xsl:when test="Region/q1:Center/q2:Pos3Vector">
            <xsl:text>Cartesian </xsl:text>
            <xsl:apply-templates 
                 select="Region/q1:Center/q2:Pos3Vector/q2:CoordValue"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="Region/q1:Radius"/>
         </xsl:when>
      </xsl:choose>
      <xsl:text>') </xsl:text>
   </xsl:template>

   <!--
     -  XMatch
     -->
   <xsl:template match="*[@xsi:type='XMatch']">
      <xsl:text>XMATCH(</xsl:text>
      <xsl:variable name="string">
         <xsl:for-each select="Args/Alias">
            <xsl:if test="Negate">
               <xsl:text>!</xsl:text>
            </xsl:if>
            <xsl:value-of select="Name"/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of 
           select="substring($string, 1, string-length($string) - 2)"/>
      <xsl:text>)</xsl:text>
      <xsl:text> </xsl:text>
      <xsl:value-of select="Compare"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="Distance"/>
   </xsl:template>

   <!--
     -  Predicate: a comparison search of some type
     -->
   <xsl:template match="*[@xsi:type='PredicateSearch']">
      <xsl:apply-templates select="Pred"/>
   </xsl:template>

   <!--
     -  Simple binary operator comparison:  a op b
     -->
   <xsl:template match="*[@xsi:type='ComparisonPred']">
      <xsl:apply-templates select="FirstExpr"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="Compare"/>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="SecondExpr"/>
   </xsl:template>

   <!--
     -  Like comparison:  a LIKE b, a NOT LIKE b
     -->
   <xsl:template match="*[@xsi:type='LikePred']">
      <xsl:apply-templates select="Expr"/>
      <xsl:if test="Negate">
         <xsl:text>NOT </xsl:text>
      </xsl:if>
      <xsl:text>LIKE </xsl:text>
      <xsl:apply-templates select="Value/Value"/>
   </xsl:template>

   <!--
     -  Between comparison:  a BETWEEN b AND c, a NOT BETWEEN b AND c, 
     -->
   <xsl:template match="*[@xsi:type='BetweenPred']">
      <xsl:apply-templates select="Expr"/>
      <xsl:if test="Negate">
         <xsl:text> NOT</xsl:text>
      </xsl:if>
      <xsl:text> BETWEEN </xsl:text>
      <xsl:apply-templates select="FirstExpr"/>
      <xsl:text> AND </xsl:text>
      <xsl:apply-templates select="SecondExpr"/>
   </xsl:template>
   <!-- Where Template -->
   <xsl:template match="WhereClause">
      <xsl:text> WHERE </xsl:text>
      <xsl:apply-templates select="Condition"/>
   </xsl:template>

   <!-- GroupBy Template -->
   <xsl:template match="GroupByClause">
      <xsl:text> GROUP BY </xsl:text>
      <xsl:apply-templates select="Column/ColumnReference"/>
   </xsl:template>

   <!-- Having Template -->
   <xsl:template match="HavingClause">
       <xsl:text> HAVING </xsl:text>
      <xsl:apply-templates select="Condition"/>
   </xsl:template>


   <!-- Expressions -->
   <!--
     -  Column Expression
     -->
   <xsl:template match="*[@xsi:type='ColumnExpr']">
      <xsl:apply-templates select="Column"/>
   </xsl:template>

   <!-- 
     -  Unary Expression
     -->
   <xsl:template match="*[@xsi:type='UnaryExpr']">
      <xsl:value-of select="Operator"/>
      <xsl:apply-templates select="Expr"/>
   </xsl:template>

   <!--
     -  Binary Expression
     -->
   <xsl:template match="*[@xsi:type='BinaryExpr']">
      <xsl:apply-templates select="FirstExpr"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="Operator"/>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="SecondExpr"/>
   </xsl:template>

   <!--
     -  Atom Expression
     -->
   <xsl:template match="*[@xsi:type='AtomExpr']">
      <xsl:apply-templates select="Value/Value"/>
   </xsl:template>

   <!--
     -  Closed Expression
     -->
   <xsl:template match="*[@xsi:type='ClosedExpr']">
      <xsl:text>(</xsl:text>
      <xsl:apply-templates select="Expr"/>
      <xsl:text>)</xsl:text>
   </xsl:template>

   <!--
     -  Function Expression
     -->
   <xsl:template match="*[@xsi:type='FunctionExpr']">
           <xsl:apply-templates select="FunctionReference"/>
   </xsl:template>

   <!--
     -  Aggregate function
     -->
   <xsl:template match="FunctionReference[AggregateFunction]">
      <xsl:call-template name="formatFunction"/>
   </xsl:template>

   <!--
     -  Math function
     -->
   <xsl:template match="FunctionReference[MathFunction]">
      <xsl:call-template name="formatFunction"/>
   </xsl:template>

   <!--
     -  Trig function
     -->
   <xsl:template match="FunctionReference[TrigonometricFunction]">
      <xsl:call-template name="formatFunction"/>
   </xsl:template>

   <!--
     -  a generic function template.  This template assumes that
     -  the context node is of a type "Function" (or one of its 
     -  derivatives).
     -->
   <xsl:template name="formatFunction">
      <xsl:value-of select="*[1]"/>
      <xsl:text>(</xsl:text>
      <xsl:apply-templates select="." mode="functionArg"/>
      <xsl:text>)</xsl:text>
   </xsl:template>

   <!--
     -  normal expression function argument
     -->
   <xsl:template match="*[@xsi:type='ExpressionFunction']" 
                 mode="functionArg">
      <xsl:apply-templates select="Expr" />
   </xsl:template>

   <!--
     -  ALL function argument
     -->
   <xsl:template match="*[@xsi:type='AllExpressionsFunction']" 
                 mode="functionArg">
      <xsl:text>ALL </xsl:text>
      <xsl:apply-templates select="Expr" />
   </xsl:template>

   <!--
     -  DISTINCT function argument
     -->
   <xsl:template match="*[@xsi:type='DistinctColumnFunction']" 
                 mode="functionArg">
      <xsl:text>DISTINCT </xsl:text>
      <xsl:apply-templates select="Column" />
   </xsl:template>

   <!--
     -  Multiple Columns function argument
     -  Note:  needs consultation against standard!
     -->
   <xsl:template match="*[@xsi:type='DistinctColumnFunction']" 
                 mode="functionArg">
      <xsl:text></xsl:text>
   </xsl:template>

   <!--
     -  Table Columns
     -->
   <xsl:template match="*[@xsi:type='AllColumnReference']">
      <xsl:value-of select="TableName"/>
      <!-- <xsl:text> </xsl:text> -->
   </xsl:template>

   <xsl:template match="*[@xsi:type='SingleColumnReference']">
      <xsl:value-of select="TableName"/>
      <xsl:text>.</xsl:text>
      <xsl:value-of select="Name"/>
   </xsl:template>

   <!--
     -  Numerical Values
     -->
   <xsl:template match="Value">
      <xsl:if test="@xsi:type='NumberLiteral'">
         <xsl:value-of select="Num/Value"/>
      </xsl:if>
      <xsl:if test="@xsi:type='StringLiteral'">
         <xsl:variable name="string">
            <xsl:for-each select="Value/string">
               <xsl:value-of select="."/>
               <xsl:text>, </xsl:text>
            </xsl:for-each>
         </xsl:variable>
         <xsl:value-of 
              select="substring($string, 1, string-length($string) - 2)"/>
      </xsl:if>
   </xsl:template>

   <!--
     -  Coordinate Values
     -->
   <xsl:template match="q2:CoordValue">
      <xsl:variable name="string">
         <xsl:for-each select="q2:Value/q2:double">
            <xsl:value-of select="."/>
            <xsl:text> </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of 
           select="substring($string, 1, string-length($string) - 1)"/>
   </xsl:template>
</xsl:stylesheet>
