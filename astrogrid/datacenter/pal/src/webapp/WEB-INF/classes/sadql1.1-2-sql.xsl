<!-- Transformation from SADQL straight to SQL.  Use this to build varients for particular
     databases -->
<xsl:transform version = '1.0'  xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns="http://astrogrid.org/sadql/v1.1">

<!-- Strips out all new lines, etc
<xsl:strip-space elements="*"/>
-->

<!-- to do - union/intersection select='*' seems to be screwing up the layer below
 possible bug - if there are two tables in the database, one with the name WIBBLE and the other with the name
 WIBBLEtable then the generated table aliases might get mixed up with the table names.
 Not sure how to get around this without having the user
 specify the *column* aliases...
 -->

<!-- Define order of output -->
<xsl:template match="ADQ" >
   <xsl:text>  SELECT </xsl:text>
   <xsl:apply-templates select="Return/First"/>
   <xsl:apply-templates select="Return/Set"/>
   <xsl:apply-templates select="Return/Into"/>
   <xsl:apply-templates select="From"/>
   <xsl:apply-templates select="Where"/>
   <xsl:apply-templates select="Return/OrderedBy"/>
   <xsl:apply-templates select="Return/GroupedBy"/>
   <xsl:apply-templates select="Having"/>
</xsl:template>

<!-- define references to Alias Table & Columns when given an Alias -->
<xsl:key name="AliasTable" match="//ADQ/From/Column/Table" use="../Alias"/>
<xsl:key name="AliasColumn" match="//ADQ/From/Column/Column" use="../Alias"/>

<!-- Create a table/column pair for this alias element -->
<xsl:template match="Alias">
   <xsl:call-template name="Alias"/>
</xsl:template>

<!-- Replace the alias with the relevent table alias and column -->
<xsl:template name="Alias">
   <xsl:if test="not(key('AliasTable', text()))">
      <xsl:message terminate="yes">
         <xsl:text>Alias Not Found</xsl:text>
        
      </xsl:message>
   </xsl:if>

   <xsl:value-of select="key('AliasTable', text())"/>
   <xsl:text>table.</xsl:text>
   <xsl:value-of select="key('AliasColumn', text())"/>
</xsl:template>

<!-- List the tables and make up suitable aliases for them -->
<xsl:template match="From">
   <xsl:text>  FROM </xsl:text>
   <!-- lump up all tables so we only get one of each -->
   <xsl:for-each select="//ADQ/From/Column[not(Table = preceding-sibling::Column/Table)]">
       <!-- the database name of the table -->
       <xsl:value-of select="Table"/>
       <xsl:text> </xsl:text>
       <!-- the sql-alias for the table - just add lower case 'table' to the end -->
       <xsl:value-of select="Table"/><xsl:text>table</xsl:text>
       <xsl:if test="not (position()=last())">
           <xsl:text>, </xsl:text>
        </xsl:if>
   </xsl:for-each>
</xsl:template>

<!-- The list of columns to be returned - the SQL SELECT column list  -->
<xsl:template match="Return/Set">
    <xsl:if test="All">
        <xsl:text>* </xsl:text>
    </xsl:if>
    <xsl:for-each select="Alias">
        <xsl:call-template name="Alias"/>
        <xsl:if test="not (position()=last())">
            <xsl:text>, </xsl:text>
        </xsl:if>
    </xsl:for-each>
</xsl:template>

<!-- Defines where the results will be sent - the SQL INTO xxx IN yyyy statement -->
<xsl:template match="Return/Into">
    <xsl:if test="Form/Table">
        <xsl:text>  INTO </xsl:text>
        <xsl:value-of select="Form/Table"/>
        <xsl:if test="Store">
            <xsl:text> IN </xsl:text>
            <xsl:value-of select="Store"/>
        </xsl:if>
    </xsl:if>
</xsl:template>

<!-- Defines the max number of results to be returned - the SQL TOP statement -->
<xsl:template match="Return/First">
   <xsl:text> TOP </xsl:text>
   <xsl:value-of select="text()"/>
   <xsl:text>  </xsl:text>
</xsl:template>

<!-- The sort order of the results - the SQL ORDER BY statement -->
<xsl:template match="Return/OrderedBy">
    <xsl:text>  ORDER BY </xsl:text>
    <xsl:for-each select="Key">
         <xsl:apply-templates/>
              <xsl:if test="not (position()=last())">
                 <xsl:text>, </xsl:text>
              </xsl:if>
    </xsl:for-each>
</xsl:template>

<!-- How the results are grouped - the SQL GROUP BY statement -->
<xsl:template match="Return/GroupedBy">
     <xsl:text>  GROUP BY </xsl:text>
     <xsl:for-each select="Key">
         <xsl:apply-templates/>
              <xsl:if test="not (position()=last())">
                 <xsl:text>, </xsl:text>
              </xsl:if>
     </xsl:for-each>
</xsl:template>


<xsl:template match="Where">
    <xsl:text>  WHERE </xsl:text>
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="Having">
    <xsl:text>  HAVING </xsl:text>
   <xsl:apply-templates/>
</xsl:template>

<xsl:template match="Union">
    <xsl:text> (or </xsl:text>
    <xsl:for-each select="*">
         <xsl:apply-templates/>
         <!--xsl:call-template name="BooleanElement"/ -->
         <xsl:if test="not (position()=last())">
             <xsl:text> OR </xsl:text>
          </xsl:if>
    </xsl:for-each>
    <xsl:text> ) </xsl:text>
</xsl:template>

<xsl:template name="parent">
   <xsl:for-each select="./../name()">
      <xsl:apply-templates/>
   </xsl:for-each>
</xsl:template>

<xsl:template match="Intersection">
    <xsl:text> (and </xsl:text>
    <xsl:for-each select="*">
     <xsl:apply-templates/>
         <xsl:if test="not (position()=last())">
             <xsl:text> AND </xsl:text>
          </xsl:if>
    </xsl:for-each>
    <xsl:text> ) </xsl:text>
</xsl:template>

<xsl:template match="Not">
    <xsl:text> (NOT </xsl:text>
    <xsl:apply-templates/>
    <xsl:text> ) </xsl:text>
</xsl:template>

<xsl:template match="Compare">
    <xsl:text> (c </xsl:text>
    <xsl:apply-templates/>
    <xsl:text> ) </xsl:text>
</xsl:template>

<xsl:template match="Compare/GREATERTHAN">
    <xsl:text disable-output-escaping="yes"> &gt; </xsl:text>
</xsl:template>
<xsl:template match="Compare/LESSTHAN">
    <xsl:text disable-output-escaping="yes"> &lt; </xsl:text>
</xsl:template>
<xsl:template match="Compare/EQUALS">
    <xsl:text disable-output-escaping="yes"> = </xsl:text>
</xsl:template>
<xsl:template match="Compare/GREATERTHANOREQUALS">
    <xsl:text disable-output-escaping="yes"> &gt;= </xsl:text>
</xsl:template>
<xsl:template match="Compare/LESSTHANOREQUALS">
    <xsl:text disable-output-escaping="yes"> &lt;= </xsl:text>
</xsl:template>
<xsl:template match="Compare/LIKE">
    <xsl:text disable-output-escaping="yes"> LIKE </xsl:text>
</xsl:template>

<xsl:template match="Math">
    <xsl:text> (m </xsl:text>
    <xsl:apply-templates/>
    <xsl:text> ) </xsl:text>
</xsl:template>
   
      <xsl:template match="Math/ADDTO">
    <xsl:text disable-output-escaping="yes"> + </xsl:text>
</xsl:template>
<xsl:template match="Math/SUBTRACT">
    <xsl:text disable-output-escaping="yes"> - </xsl:text>
</xsl:template>
<xsl:template match="Math/MULTIPLYBY">
    <xsl:text disable-output-escaping="yes"> * </xsl:text>
</xsl:template>
<xsl:template match="Math/DIVIDEBY">
    <xsl:text disable-output-escaping="yes"> / </xsl:text>
</xsl:template>


<!-- Region search -->
<!-- this will almost certainly change according to the database.  This is noddy back-to-ADQL/SQL
     transformation -->
   <xsl:template match="Region">
      <xsl:text>REGION('Circle </xsl:text>
      <xsl:if test="Coords">
          <xsl:value-of select="Coords"/>
      </xsl:if>
      <xsl:if test="not(Coords)">
         <xsl:text>J2000 </xsl:text>
      </xsl:if>
      <xsl:value-of select="RA"/>
      <xsl:text>, </xsl:text>
      <xsl:value-of select="DEC"/>
      <xsl:text>, </xsl:text>
      <xsl:value-of select="d"/>
      <xsl:text>, </xsl:text>
      <xsl:text>') </xsl:text>
   </xsl:template>

<!-- Cross match -->
<!-- this will almost certainly change according to the database.  This is noddy back-to-ADQL/SQL
     transformation -->
   <xsl:template match="XMatch">
      <xsl:text>XMATCH(</xsl:text>
      <xsl:for-each select="Source">
            <xsl:value-of select="."/>
              <xsl:text>, </xsl:text>
      </xsl:for-each>
      <xsl:for-each select="NotMatch">
              <xsl:text>!</xsl:text>
            <xsl:value-of select="."/>
           <xsl:text>, </xsl:text>
     </xsl:for-each>
     <xsl:text> &lt; </xsl:text>
     <xsl:value-of select="Sigma"/>
     <xsl:text>) </xsl:text>
   </xsl:template>

<!-- Single argument functions -->
<xsl:template match="SIN"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="COS"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="TAN"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="COT"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="ASIN"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="ACOS"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="ATAN"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="ABS"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="EXP"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="SQRT"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="CEILING"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="FLOOR"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="DEGREES"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="RADIANS"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>
<xsl:template match="SQUARE"><xsl:text> SIN(</xsl:text><xsl:apply-templates/><xsl:text>) </xsl:text></xsl:template>

<xsl:template match="PI"><xsl:text> PI </xsl:text></xsl:template>

<!-- Power function, assuming it exists as a POW(mantiass, exponent) in the database -->
<xsl:template match="POWER">
   <xsl:text> POW(</xsl:text>
   <xsl:for-each select="Mantissa">
       <xsl:apply-templates/>
   </xsl:for-each>
   <xsl:for-each select="Exponent">
       <xsl:apply-templates/>
   </xsl:for-each>
   <xsl:text>) </xsl:text>
</xsl:template>

<!-- Alternative power function assuming it doesn't exist in the database -->
<!-- nb assumes exponent is an integer number.. if it is a more complicated expression, or one that needs resolving, we cannot
    handle it -->

<!--xsl:template name="LoopAppend">
   <xsl:param name="N"/>
   <xsl:param name="Text"/>
   <xsl:text></xsl:text>
</xsl:template -->
       
    
<!-- double argument functions -->
<xsl:template match="LOG">
   <xsl:text> LOG(</xsl:text>
   <xsl:for-each select="Base">
       <xsl:apply-templates/>
   </xsl:for-each>
   <xsl:for-each select="Argument">
       <xsl:apply-templates/>
   </xsl:for-each>
   <xsl:text>) </xsl:text>
</xsl:template>

</xsl:transform>

