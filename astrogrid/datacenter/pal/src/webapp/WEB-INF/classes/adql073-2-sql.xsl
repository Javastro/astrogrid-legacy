<xsl:stylesheet xmlns="http://adql.ivoa.net/v0.73" xmlns:ad="http://adql.ivoa.net/v0.73" xmlns:q1="urn:nvo-region" xmlns:q2="urn:nvo-coords" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0">
   <!--
   - Stylesheet to convert ADQL version 0.7.3 to an SQL String
   - Version 1.0 - Initial Revision
   - Ramon Williamson, National Center for SuperComputing Applications
   - March 5, 2004
   - Based on the schema: http://www.ivoa.net/internal/IVOA/IvoaVOQL/ADQL-0.7.3.xsd
   - mods Martin Hill, ROE
   -->
   
   <!-- Define order of output -->
   <xsl:template match="ad:Select" >
      <xsl:text>  SELECT </xsl:text>
      <xsl:apply-templates select="ad:Allow"/>
      <xsl:apply-templates select="ad:Restrict"/>
      <xsl:apply-templates select="ad:SelectionList"/>
      <xsl:text> FROM </xsl:text>
      <xsl:apply-templates select="ad:From"/>
      <xsl:apply-templates select="ad:Where"/>
      <xsl:apply-templates select="ad:GroupBy"/>
      <xsl:apply-templates select="ad:Having"/>
      <xsl:apply-templates select="ad:OrderBy"/>
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
     -->
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
   <!--
     -  SelectionList Template
     -->
   <xsl:template match="ad:SelectionList">
      <xsl:variable name="string">
         <xsl:for-each select="*">
            <xsl:apply-templates select="."/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring($string, 1, string-length($string) - 2)"/>
   </xsl:template>
   <!--
     -  From Template
     -->
   <xsl:template match="ad:From">
      <xsl:variable name="string">
         <xsl:for-each select="ad:Table">
            <xsl:value-of select="@Name"/>
            <xsl:text> </xsl:text>
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
   <!--
     -  Union: a OR b
     -->
   <xsl:template match="ad:Or">
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> OR </xsl:text>
      <xsl:apply-templates select="*[2]"/>
   </xsl:template>
   <!--
     -  Circle (a)
     -->
   <xsl:template match="ad:Circle">
      <xsl:text>REGION('Circle </xsl:text>
      <xsl:choose>
         <xsl:when test="q1:Center/q2:Pos2Vector">
            <xsl:text>J2000 </xsl:text>
            <xsl:apply-templates select="q1:Center/q2:Pos2Vector/q2:CoordValue"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="q1:Radius"/>
         </xsl:when>
         <xsl:when test="q1:Center/q2:Pos3Vector">
            <xsl:text>Cartesian </xsl:text>
            <xsl:apply-templates select="q1:Center/q2:Pos3Vector/q2:CoordValue"/>
            <xsl:text> </xsl:text>
            <xsl:value-of select="q1:Radius"/>
         </xsl:when>
      </xsl:choose>
      <xsl:text>') </xsl:text>
   </xsl:template>
   <!--
     -  XMatch
     -->
   <xsl:template match="ad:Xmatch">
      <xsl:text>XMATCH(</xsl:text>
      <xsl:variable name="string">
         <xsl:for-each select="ad:Include">
            <xsl:value-of select="@Name"/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
         <xsl:for-each select="ad:Drop">
            <xsl:text>!</xsl:text>
            <xsl:value-of select="@Name"/>
            <xsl:text>, </xsl:text>
         </xsl:for-each>
      </xsl:variable>
      <xsl:value-of select="substring($string, 1, string-length($string) - 2)"/>
      <xsl:text>)</xsl:text>
      <xsl:text> </xsl:text>
      <xsl:value-of select="ad:Nature"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="ad:Sigma"/>
   </xsl:template>
   <!--
     -  Simple binary operator comparison:  a op b
     -->
   <xsl:template match="ad:Compare">
      <xsl:variable name="comp">
         <xsl:value-of select="@Comparison"/>
      </xsl:variable>
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="$comp"/>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="*[2]"/>
   </xsl:template>
   <!--
     -  Intersection:  a AND b
     -->
   <xsl:template match="ad:And">
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> AND </xsl:text>
      <xsl:apply-templates select="*[2]"/>
   </xsl:template>
   <!--
     -  Negates comparisons below:  a NOT comp b
     -->
   <xsl:template match="ad:Not">
      <xsl:text>NOT </xsl:text>
      <xsl:apply-templates select="*"/>
   </xsl:template>
   <!--
     -  Like comparison:  a LIKE b
     -->
   <xsl:template match="ad:Like">
      <xsl:apply-templates select="*"/>
      <xsl:text>LIKE </xsl:text>
      <xsl:apply-templates select="*"/>
   </xsl:template>
   <!--
     -  NotLike comparison:  a NOT LIKE b
     -->
   <xsl:template match="ad:NotLike">
      <xsl:apply-templates select="*"/>
      <xsl:text>NOT LIKE </xsl:text>
      <xsl:apply-templates select="*"/>
   </xsl:template>
   <!--
     -  Between comparison:
      a BETWEEN b AND c,
     -->
   <xsl:template match="ad:Between">
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> BETWEEN </xsl:text>
      <xsl:call-template name="*[2]"/>
      <xsl:text> AND </xsl:text>
      <xsl:call-template name="*[3]"/>
   </xsl:template>
   <!--
     -  NotBetween comparison:
        a NOT BETWEEN b AND c,
     -->
   <xsl:template match="ad:NotBetween">
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> NOT BETWEEN </xsl:text>
      <xsl:call-template name="*[2]"/>
      <xsl:text> AND </xsl:text>
      <xsl:call-template name="*[3]"/>
   </xsl:template>
   <!-- Where Template -->
   <xsl:template match="ad:Where">
      <xsl:text> WHERE </xsl:text>
      <xsl:apply-templates select="*"/>
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
   <!-- Expressions -->
   <!--
     -  All Table Columns
     -->
   <xsl:template match="ad:All">
      <xsl:text>* </xsl:text>
   </xsl:template>
   <!--
     -  Table Columns
     -->
   <xsl:template match="ad:Column">
      <xsl:value-of select="@Table"/>
      <xsl:text>.</xsl:text>
      <xsl:value-of select="@Name"/>
   </xsl:template>
   <!--
     -  Archive Table Columns
     -->
   <xsl:template match="ad:Column">
      <xsl:value-of select="@Table"/>
      <xsl:text>.</xsl:text>
      <xsl:value-of select="@Name"/>
   </xsl:template>
   <!--
   - Alias
   -->
   <xsl:template match="ad:Alias">
      <xsl:variable name="alias" select="@As"/>
      <xsl:apply-templates select="*"/>
      <xsl:text> AS </xsl:text>
      <xsl:value-of select="$alias"/>
   </xsl:template>
   <!--
     -  Unary Operation
     -->
   <xsl:template match="ad:UnaryOperation">
      <xsl:apply-templates select="*"/>
   </xsl:template>
   <!--
     -  Binary Operation
     -->
   <xsl:template match="ad:BinaryOperation">
      <xsl:apply-templates select="*[1]"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="@Oper"/>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="*[2]"/>
   </xsl:template>
   <!--
     -  Atom Expression
     -->
   <xsl:template match="ad:Atom">
      <xsl:value-of select="*"/>
   </xsl:template>
   <!--
     -  Closed (a)
     -->
   <xsl:template match="ad:Closed">
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
