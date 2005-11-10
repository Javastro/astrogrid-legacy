<?xml version="1.0"?>
<xsl:stylesheet xmlns="http://www.ivoa.net/xml/ADQL/v0.7.4" xmlns:ad="http://www.ivoa.net/xml/ADQL/v0.7.4" xmlns:q1="urn:nvo-region" xmlns:q2="urn:nvo-coords" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0">
   <!--
   - Stylesheet to convert ADQL version 0.7.4 to an SQL String
   - Version 1.0 - Initial Revision
   - Ramon Williamson, National Center for SuperComputing Applications
   - April 1, 2004
   - Based on the schema: http://www.ivoa.net/internal/IVOA/IvoaVOQL/ADQL-0.7.4.xsd
   - Mods by MCH, ROE in order to get SELECT, FROM and AS to appear; not sure why they didn't already...
   -->
   
   
   <xsl:param name="resource_elem"/>
   <xsl:param name="declare_elems"/>

   
   <!-- Define order of output -->
   <xsl:template match="ad:Select" >
   
    <!--
      <xsl:text>
         declare namespace vr = "http://www.ivoa.net/xml/VOResource/v0.9";
         declare namespace vc = "http://www.ivoa.net/xml/VOCommunity/v0.2";
         declare namespace vg = "http://www.ivoa.net/xml/VORegistry/v0.2";
         declare namespace vs = "http://www.ivoa.net/xml/VODataService/v0.4";
         declare namespace vt = "http://www.ivoa.net/xml/VOTable/v0.1";
         declare namespace cs = "http://www.ivoa.net/xml/ConeSearch/v0.2";
         declare namespace sia = "http://www.ivoa.net/xml/SIA/v0.6";
         declare namespace cea="http://www.ivoa.net/xml/CEAService/v0.1"; 
         declare namespace ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1";

       for $x in //vr:Resource
      </xsl:text>
   
      <xsl:text>  SELECT </xsl:text>
      <xsl:apply-templates select="ad:Allow"/>
      <xsl:apply-templates select="ad:Restrict"/>
      <xsl:apply-templates select="ad:SelectionList"/>
      <xsl:text> FROM </xsl:text>
      <xsl:apply-templates select="ad:From"/>
      -->
   
      <xsl:apply-templates select="ad:Where"/>
      
      <!--
      <xsl:apply-templates select="ad:GroupBy"/>
      <xsl:apply-templates select="ad:Having"/>
      <xsl:apply-templates select="ad:OrderBy"/>
      -->
   
   </xsl:template>
   
   <!--
     -  Allow Template
     -->
   <xsl:template match="ad:Allow">
      <xsl:value-of select="@Option"/>
      <xsl:text> </xsl:text>
   </xsl:template>
   <!--
     -  Restrict Template
     -->
   <xsl:template match="ad:Restrict">
      <xsl:text>TOP </xsl:text>
      <xsl:value-of select="@Top"/>
      <xsl:text> </xsl:text>
   </xsl:template>
   <!--
     -  OrderBy Template

   <xsl:template match="ad:OrderBy">
      <xsl:text> ORDER BY </xsl:text>
      <xsl:variable name="string">
         <xsl:for-each select="ad:Item">
            <xsl:apply-templates select="*[1]"/>
            <xsl:choose>
               <xsl:when test="*[2] = ad:Order">
                  <xsl:text> </xsl:text>
                  <xsl:value-of select="*[2]/@Direction"/>
                  <xsl:text>, </xsl:text>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:text>, </xsl:text>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring($string, 1, string-length($string) - 2)"/>
   </xsl:template>
     -->   
   <!--
     -  SelectionList Template

   <xsl:template match="ad:SelectionList">
      <xsl:variable name="string">
         <xsl:for-each select="ad:Item">
            <xsl:choose>
               <xsl:when test="@xsi:type='allSelectionItemType'">
                  <xsl:text> * </xsl:text>
                  <xsl:text>, </xsl:text>
               </xsl:when>
               <xsl:when test="@xsi:type='aliasSelectionItemType'">
                  <xsl:apply-templates select="*"/>
                  <xsl:text> AS </xsl:text>
                  <xsl:value-of select="@As"/>
                  <xsl:text>, </xsl:text>
               </xsl:when>
               <xsl:otherwise>
                  <xsl:apply-templates select="."/>
                  <xsl:text>, </xsl:text>
               </xsl:otherwise>
            </xsl:choose>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring($string, 1, string-length($string) - 2)"/>
   </xsl:template>
     -->   
   <!--
     -  From Template
   <xsl:template match="ad:From">
      <xsl:variable name="string">
         <xsl:for-each select="ad:Table">
            <xsl:value-of select="@Name"/>
            <xsl:text> AS </xsl:text>
            <xsl:value-of select="@Alias"/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
         <xsl:for-each select="ad:ArchiveTable">
            <xsl:value-of select="@Name"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="@Alias"/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring($string, 1, string-length($string) - 2)"/>
   </xsl:template>
     -->   
     
   <!-- Search Types -->
   <!--
     -  Intersection Search:  a AND b
     -->
   <xsl:template match="*[@xsi:type='intersectionSearchType']">
    <!--
   	<xsl:if test="../local-name() = 'Where'">
     -->
   	<xsl:if test="local-name(..) = 'Where'">
	  <xsl:text>(</xsl:text>
    </xsl:if>
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> and </xsl:text>
      <xsl:apply-templates select="*[2]"/>
	<xsl:if test="local-name(..) = 'Where'">
 	  <xsl:text>)</xsl:text>
    </xsl:if>
   </xsl:template>

   <!--
     -  Union: a OR b
     -->
   <xsl:template match="*[@xsi:type='unionSearchType']">
     <xsl:if test="local-name(..) = 'Where'">
	 	<xsl:text>(</xsl:text>
     </xsl:if>
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> or </xsl:text>
      <xsl:apply-templates select="*[2]"/>
     <xsl:if test="local-name(..) = 'Where'">
		 <xsl:text>)</xsl:text>
	 </xsl:if>
   </xsl:template>

   <!--
     -  Circle (a)

   <xsl:template match="*[@xsi:type='regionSearchType']">
      <xsl:text>REGION('Circle </xsl:text>
      <xsl:choose>
         <xsl:when test="ad:Region/q1:Center/q2:Pos2Vector">
            <xsl:text>J2000 </xsl:text>
            <xsl:apply-templates select="ad:Region/q1:Center/q2:Pos2Vector/q2:CoordValue"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="ad:Region/q1:Radius"/>
         </xsl:when>
         <xsl:when test="ad:Region/q1:Center/q2:Pos3Vector">
            <xsl:text>Cartesian </xsl:text>
            <xsl:apply-templates select="ad:Region/q1:Center/q2:Pos3Vector/q2:CoordValue"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="ad:Region/q1:Radius"/>
         </xsl:when>
      </xsl:choose>
      <xsl:text>') </xsl:text>
   </xsl:template>
     -->   
   <!--
     -  XMatch

   <xsl:template match="*[@xsi:type='xMatchType']">
      <xsl:text>XMATCH(</xsl:text>
      <xsl:variable name="string">
         <xsl:for-each select="ad:Table">
            <xsl:if test="@xsi:type='includeTableType'">
                <xsl:value-of select="@Name"/>
                <xsl:text>, </xsl:text>
            </xsl:if>
            <xsl:if test="@xsi:type='dropTableType'">
                <xsl:text>!</xsl:text>
                <xsl:value-of select="@Name"/>
                <xsl:text>, </xsl:text>
            </xsl:if>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring($string, 1, string-length($string) - 2)"/>
      <xsl:text>)</xsl:text>
      <xsl:text> </xsl:text>
      <xsl:value-of select="ad:Nature"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="ad:Sigma/@Value"/>
   </xsl:template>
     -->   
   <!--
     -  Simple binary operator comparison:  a op b
     -->
   <xsl:template match="*[@xsi:type='comparisonPredType']">
      <xsl:variable name="comp">
         <xsl:value-of select="@Comparison"/>
      </xsl:variable>
      <xsl:text>(</xsl:text>      
      <xsl:apply-templates select="ad:Arg[1]"/>
      <xsl:text> </xsl:text>      
      <xsl:choose>
         <xsl:when test="$comp = 'LIKE'">
           <xsl:text> &amp;= </xsl:text>            
		   <!--
           <xsl:text> |= </xsl:text>
			-->
         </xsl:when>
         <xsl:otherwise>
            <xsl:value-of select="$comp"/>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="ad:Arg[2]"/>
      <xsl:text>) </xsl:text>
   </xsl:template>
   <!--
     -  Negates comparisons below:  a NOT comp b
     -->
   <xsl:template match="*[@xsi:type='inverseSearchType']">
      <xsl:text>NOT </xsl:text>
      <xsl:apply-templates select="*"/>
   </xsl:template>
   <!--
     -  Like comparison:  a LIKE b
     -->
   <xsl:template match="*[@xsi:type='likePredType']">
      <xsl:apply-templates select="ad:Arg"/>
           <xsl:text> &amp;= </xsl:text>            
		   <!--
           <xsl:text> |= </xsl:text>
			-->
      <xsl:apply-templates select="ad:Pattern/ad:Literal"/>
   </xsl:template>
   <!--
     -  NotLike comparison:  a NOT LIKE b
     -->
   <xsl:template match="*[@xsi:type='notLikePredType']">
      <xsl:apply-templates select="ad:Arg"/>
      <xsl:text> NOT LIKE </xsl:text>
      <xsl:apply-templates select="ad:Pattern/ad:Literal"/>
   </xsl:template>
   <!--
     -  Between comparison:
      a BETWEEN b AND c,
     -->
   <xsl:template match="*[@xsi:type='betweenPredType']">
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> BETWEEN </xsl:text>
      <xsl:apply-templates select="*[2]"/>
      <xsl:text> AND </xsl:text>
      <xsl:apply-templates select="*[3]"/>
   </xsl:template>
   <!--
     -  NotBetween comparison:
        a NOT BETWEEN b AND c,
     -->
   <xsl:template match="*[@xsi:type='notBetweenPredType']">
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> NOT BETWEEN </xsl:text>
      <xsl:apply-templates select="*[2]"/>
      <xsl:text> AND </xsl:text>
      <xsl:apply-templates select="*[3]"/>
   </xsl:template>
   <!--
     -  Closed (a)
     -->
   <xsl:template match="*[@xsi:type='closedSearchType']">
      <xsl:text>(</xsl:text>
      <xsl:apply-templates select="*"/>
      <xsl:text>)</xsl:text>
   </xsl:template>

   <!-- Where Template -->
   <xsl:template match="ad:Where">
      <!--
      <xsl:text> WHERE </xsl:text>
      <xsl:apply-templates select="ad:Condition"/>
      for $x in //vr:Resource where
      -->
      
   <xsl:value-of select="$declare_elems"/>
   <xsl:text>
      for $x in //</xsl:text><xsl:value-of select="$resource_elem"/>
      <xsl:text>
       where 
      </xsl:text>      

<!--
      <xsl:text>
       where 
       $x/@status='active' and 
      </xsl:text>      
-->

      <xsl:apply-templates select="ad:Condition"/>
      <xsl:text> 
         return $x
     </xsl:text>
   </xsl:template>
   
   <!-- GroupBy Template -->
   <xsl:template match="ad:GroupBy">
      <xsl:text> GROUP BY </xsl:text>
      <xsl:variable name="string">
         <xsl:for-each select="*">
            <xsl:apply-templates select="."/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring($string, 1, string-length($string) - 2)"/>
   </xsl:template>
   <!-- Having Template -->
   <xsl:template match="ad:Having">
      <xsl:text> HAVING </xsl:text>
      <xsl:apply-templates select="*"/>
   </xsl:template>
   <!-- scalarExpressionTypes -->
   <!--
     -  Table Columns (columnReferenceType)
     -->
   <xsl:template match="*[@xsi:type='columnReferenceType']">
      <!--
      <xsl:value-of select="@Table"/>
      <xsl:text>.</xsl:text>
      <xsl:value-of select="@Name"/>      
      -->
      <xsl:variable name="colName" select="@Name" />
      <!--
      <xsl:choose>
      
         <xsl:when test="starts-with($colName,'Resource')">
            <xsl:value-of select="substring(@Name, 10, string-length(@Name))"/>
         </xsl:when>
         <xsl:when test="starts-with($colName,'vr:Resource')">
            <xsl:value-of select="substring(@Name, 12, string-length(@Name))"/>
         </xsl:when>
      
         <xsl:otherwise>
            <xsl:value-of select="$colName" />
         </xsl:otherwise>
      </xsl:choose>
      -->
      <xsl:text>$x/</xsl:text><xsl:value-of select="$colName" />
   </xsl:template>
   <!--
     -  Unary Operation
     -->
   <xsl:template match="*[@xsi:type='unaryExprType']">
      <xsl:apply-templates select="ad:Arg"/>
            <xsl:text> </xsl:text>
      <xsl:value-of select="@Oper"/>
      <xsl:text> </xsl:text>
   </xsl:template>
   <!--
     -  Binary Operation
     -->
   <xsl:template match="*[@xsi:type='binaryExprType']">
      <xsl:apply-templates select="ad:Arg[1]"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="@Oper"/>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="ad:Arg[2]"/>
   </xsl:template>
   <!--
     -  Atom Expression
     -->
   <xsl:template match="*[@xsi:type='atomType']">
      <xsl:apply-templates select="*"/>
   </xsl:template>
   <!--
     -  Closed (a)
     -->
   <xsl:template match="*[@xsi:type='closedExprType']">
      <xsl:text>(</xsl:text>
      <xsl:apply-templates select="*"/>
      <xsl:text>)</xsl:text>
   </xsl:template>
   <!--
     -  Function Expression
     -->
   <xsl:template match="ad:Function">
      <xsl:value-of select="*[1]"/>
      <xsl:text>(</xsl:text>
      <xsl:choose>
         <xsl:when test="local-name(*[2]) = 'Allow'">
            <xsl:apply-templates select="*[2]/@Option"/>
            <xsl:text> </xsl:text>
            <xsl:apply-templates select="*[3]"/>
         </xsl:when>
         <xsl:otherwise>
            <xsl:apply-templates select="*[2]"/>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:text>)</xsl:text>
   </xsl:template>
      <!--
     -  Trigonometric Function Expression
     -->
   <xsl:template match="*[@xsi:type = 'trigonometricFunctionType']">
      <xsl:value-of select="@Name"/>
      <xsl:text>(</xsl:text>
       <xsl:apply-templates select="*"/>
      <xsl:text>)</xsl:text>
   </xsl:template>
   <!--
     -  Math Function Expression
     -->
   <xsl:template match="*[@xsi:type = 'mathFunctionType']">
      <xsl:value-of select="@Name"/>
      <xsl:text>(</xsl:text>
       <xsl:apply-templates select="*"/>
      <xsl:text>)</xsl:text>
   </xsl:template>
      <!--
     - Aggregate Function Expression
     -->
   <xsl:template match="*[@xsi:type = 'aggregateFunctionType']">
      <xsl:value-of select="@Name"/>
      <xsl:text>(</xsl:text>
       <xsl:apply-templates select="*"/>
      <xsl:text>)</xsl:text>
   </xsl:template>
   <!--
   -    Literal Values
   -->
      <xsl:template match="ad:Literal">
         <xsl:choose>
            <xsl:when test="@xsi:type='integerType'">
                  <xsl:value-of select="@Value"/>
            </xsl:when>
            <xsl:when test="@xsi:type='realType'">
                  <xsl:value-of select="@Value"/>
            </xsl:when>
            <xsl:when test="@xsi:type='stringType'">
                  <xsl:text>'</xsl:text>
                  <xsl:choose>
                     <xsl:when test="contains(@Value,'%')">
                        <xsl:value-of select="translate(@Value, '%', '*')" />
                     </xsl:when>
                     <xsl:otherwise>
                        <xsl:value-of select="@Value"/>
                     </xsl:otherwise>
                  </xsl:choose>
                  <xsl:text>'</xsl:text>
            </xsl:when>
         </xsl:choose>
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
      <xsl:value-of select="substring($string, 1, string-length($string) - 1)"/>
   </xsl:template>
</xsl:stylesheet>


