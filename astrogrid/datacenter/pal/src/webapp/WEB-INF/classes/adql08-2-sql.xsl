<?xml version="1.0"?>
<xsl:stylesheet xmlns="http://voql.ivoa.net/" 
                xmlns:ad="http://voql.ivoa.net/" 
                xmlns:q1="urn:nvo-region" 
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
      <xsl:apply-templates select="ad:OptionalAllOrDistinct"/>
      <xsl:apply-templates select="ad:OptionalTop"/>
      <xsl:apply-templates select="ad:Selection"/>
      <xsl:text> FROM </xsl:text>
      <xsl:apply-templates select="ad:TableClause/ad:FromClause"/>
      <xsl:apply-templates select="ad:TableClause/ad:WhereClause"/>
       <xsl:apply-templates select="ad:TableClause/ad:GroupByClause"/>
      <xsl:apply-templates select="ad:TableClause/ad:HavingClause"/>
      <xsl:apply-templates select="ad:OrderBy"/>
   </xsl:template>

   <!-- 
     -  OptionalAllOrDistinct Template 
     -->
   <xsl:template match="ad:OptionalAllOrDistinct">
      <xsl:value-of select="ad:Option"/>
      <xsl:text> </xsl:text>
   </xsl:template>

   <!-- 
     -  OptionalTop Template 
     -->
   <xsl:template match="ad:OptionalTop">
      <xsl:text>TOP </xsl:text>
      <xsl:value-of select="ad:Count"/>
      <xsl:text> </xsl:text>
   </xsl:template>

   <!-- 
     -  OrderBy Template 
     -->
   <xsl:template match="ad:OrderBy">
      <xsl:text> ORDER BY </xsl:text>
      <xsl:variable name="string">
         <xsl:for-each select="ad:OrderList/ad:Order">
            <xsl:apply-templates select="ad:Expr"/>
            <xsl:if test="Option/Direction">
               <xsl:text> </xsl:text>
               <xsl:value-of select="ad:Option/ad:Direction"/>
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
   <xsl:template match="ad:Selection">
      <xsl:variable name="string">
         <xsl:for-each select="ad:Items/ad:SelectionItem">
            <xsl:choose>
               <xsl:when test="@xsi:type='AllSelectionItem'">
                  <xsl:text> * </xsl:text>
                  <xsl:text>, </xsl:text>
               </xsl:when>
               <xsl:when test="@xsi:type='AliasSelectionItem'">
                  <xsl:apply-templates select="ad:Expr"/>
                  <xsl:text> AS </xsl:text>
                  <xsl:value-of select="ad:AliasName"/>
                  <xsl:text>, </xsl:text>
               </xsl:when>
               <xsl:when test="@xsi:type='ExprSelectionItem'">
                  <xsl:apply-templates select="ad:Expr"/>
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
   <xsl:template match="ad:FromClause">
      <xsl:variable name="string">
         <xsl:for-each select="ad:TableReference/ad:Table">
            <xsl:value-of select="ad:Name"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="ad:AliasName"/>
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
      <xsl:apply-templates select="ad:FirstCondition"/>
      <xsl:text> AND </xsl:text>
      <xsl:apply-templates select="ad:SecondCondition"/>
   </xsl:template>

   <!-- 
     -  Union: a OR b
     -->
   <xsl:template match="*[@xsi:type='UnionSearch']">
      <xsl:apply-templates select="ad:FirstCondition"/>
      <xsl:text> OR </xsl:text>
      <xsl:apply-templates select="ad:SecondCondition"/>
   </xsl:template>

   <!-- 
     -  Inverse: NOT a
     -->
   <xsl:template match="*[@xsi:type='InverseSearch']">
      <xsl:text>NOT </xsl:text>
      <xsl:apply-templates select="ad:Condition"/>
   </xsl:template>

   <!--
     -  Closed:  (a)
     -->
   <xsl:template match="*[@xsi:type='ClosedSearch']">
      <xsl:text>(</xsl:text>
      <xsl:apply-templates select="ad:Condition"/>
      <xsl:text>)</xsl:text>
   </xsl:template>

   <!--
     -  Region Region(a)
     -->
   <xsl:template match="*[@xsi:type='RegionSearch']">
      <xsl:text>REGION('Circle </xsl:text>
      <xsl:choose>
         <xsl:when test="ad:Region/q1:Center/q2:Pos2Vector">
            <xsl:text>J2000 </xsl:text>
            <xsl:apply-templates 
                 select="ad:Region/q1:Center/q2:Pos2Vector/q2:CoordValue"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="ad:Region/q1:Radius"/>
         </xsl:when>
         <xsl:when test="ad:Region/q1:Center/q2:Pos3Vector">
            <xsl:text>Cartesian </xsl:text>
            <xsl:apply-templates 
                 select="ad:Region/q1:Center/q2:Pos3Vector/q2:CoordValue"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="ad:Region/q1:Radius"/>
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
         <xsl:for-each select="ad:Args/ad:Alias">
            <xsl:if test="ad:Negate">
               <xsl:text>!</xsl:text>
            </xsl:if>
            <xsl:value-of select="ad:Name"/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of 
           select="substring($string, 1, string-length($string) - 2)"/>
      <xsl:text>)</xsl:text>
      <xsl:text> </xsl:text>
      <xsl:value-of select="ad:Compare"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="ad:Distance"/>
   </xsl:template>

   <!--
     -  Predicate: a comparison search of some type
     -->
   <xsl:template match="*[@xsi:type='PredicateSearch']">
      <xsl:apply-templates select="ad:Pred"/>
   </xsl:template>

   <!--
     -  Simple binary operator comparison:  a op b
     -->
   <xsl:template match="*[@xsi:type='ComparisonPred']">
      <xsl:apply-templates select="ad:FirstExpr"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="ad:Compare"/>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="ad:SecondExpr"/>
   </xsl:template>

   <!--
     -  Like comparison:  a LIKE b, a NOT LIKE b
     -->
   <xsl:template match="*[@xsi:type='LikePred']">
      <xsl:apply-templates select="ad:Expr"/>
      <xsl:if test="ad:Negate">
         <xsl:text>NOT </xsl:text>
      </xsl:if>
      <xsl:text>LIKE </xsl:text>
      <xsl:apply-templates select="ad:Value/ad:Value"/>
   </xsl:template>

   <!--
     -  Between comparison:  a BETWEEN b AND c, a NOT BETWEEN b AND c, 
     -->
   <xsl:template match="*[@xsi:type='BetweenPred']">
      <xsl:apply-templates select="ad:Expr"/>
      <xsl:if test="ad:Negate">
         <xsl:text> NOT</xsl:text>
      </xsl:if>
      <xsl:text> BETWEEN </xsl:text>
      <xsl:apply-templates select="ad:FirstExpr"/>
      <xsl:text> AND </xsl:text>
      <xsl:apply-templates select="ad:SecondExpr"/>
   </xsl:template>
   <!-- Where Template -->
   <xsl:template match="ad:WhereClause">
      <xsl:text> WHERE </xsl:text>
      <xsl:apply-templates select="ad:Condition"/>
   </xsl:template>

   <!-- GroupBy Template -->
   <xsl:template match="ad:GroupByClause">
      <xsl:text> GROUP BY </xsl:text>
      <xsl:apply-templates select="ad:Column/ad:ColumnReference"/>
   </xsl:template>

   <!-- Having Template -->
   <xsl:template match="ad:HavingClause">
       <xsl:text> HAVING </xsl:text>
      <xsl:apply-templates select="ad:Condition"/>
   </xsl:template>


   <!-- Expressions -->
   <!--
     -  Column Expression
     -->
   <xsl:template match="*[@xsi:type='ColumnExpr']">
      <xsl:apply-templates select="ad:Column"/>
   </xsl:template>

   <!-- 
     -  Unary Expression
     -->
   <xsl:template match="*[@xsi:type='UnaryExpr']">
      <xsl:value-of select="ad:Operator"/>
      <xsl:apply-templates select="ad:Expr"/>
   </xsl:template>

   <!--
     -  Binary Expression
     -->
   <xsl:template match="*[@xsi:type='BinaryExpr']">
      <xsl:apply-templates select="ad:FirstExpr"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="ad:Operator"/>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="ad:SecondExpr"/>
   </xsl:template>

   <!--
     -  Atom Expression
     -->
   <xsl:template match="*[@xsi:type='AtomExpr']">
      <xsl:apply-templates select="ad:Value/ad:Value"/>
   </xsl:template>

   <!--
     -  Closed Expression
     -->
   <xsl:template match="*[@xsi:type='ClosedExpr']">
      <xsl:text>(</xsl:text>
      <xsl:apply-templates select="ad:Expr"/>
      <xsl:text>)</xsl:text>
   </xsl:template>

   <!--
     -  Function Expression
     -->
   <xsl:template match="*[@xsi:type='FunctionExpr']">
           <xsl:apply-templates select="ad:FunctionReference"/>
   </xsl:template>

   <!--
     -  Aggregate function
     -->
   <xsl:template match="ad:FunctionReference[ad:AggregateFunction]">
      <xsl:call-template name="formatFunction"/>
   </xsl:template>

   <!--
     -  Math function
     -->
   <xsl:template match="ad:FunctionReference[ad:MathFunction]">
      <xsl:call-template name="formatFunction"/>
   </xsl:template>

   <!--
     -  Trig function
     -->
   <xsl:template match="ad:FunctionReference[ad:TrigonometricFunction]">
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
      <xsl:apply-templates select="ad:Expr" />
   </xsl:template>

   <!--
     -  ALL function argument
     -->
   <xsl:template match="*[@xsi:type='AllExpressionsFunction']" 
                 mode="functionArg">
      <xsl:text>ALL </xsl:text>
      <xsl:apply-templates select="ad:Expr" />
   </xsl:template>

   <!--
     -  DISTINCT function argument
     -->
   <xsl:template match="*[@xsi:type='DistinctColumnFunction']" 
                 mode="functionArg">
      <xsl:text>DISTINCT </xsl:text>
      <xsl:apply-templates select="ad:Column" />
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
      <xsl:value-of select="ad:TableName"/>
      <!-- <xsl:text> </xsl:text> -->
   </xsl:template>

   <xsl:template match="*[@xsi:type='SingleColumnReference']">
      <xsl:value-of select="ad:TableName"/>
      <xsl:text>.</xsl:text>
      <xsl:value-of select="ad:Name"/>
   </xsl:template>

   <!--
     -  Numerical Values
     -->
   <xsl:template match="ad:Value">
      <xsl:if test="@xsi:type='NumberLiteral'">
         <xsl:value-of select="ad:Num/ad:Value"/>
      </xsl:if>
      <xsl:if test="@xsi:type='StringLiteral'">
         <xsl:variable name="string">
            <xsl:for-each select="ad:Value/ad:string">
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

