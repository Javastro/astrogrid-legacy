<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id: analyzesiap.xsl,v 1.1 2008/06/08 14:14:22 pah Exp $
Analyse the results of the siap tests
Paul Harrison pah@jb.man.ac.uk
 -->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:xs="http://www.w3.org/2001/XMLSchema" 
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
                xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0" 
                xmlns:vs="http://www.ivoa.net/xml/VODataService/v1.0" 
                xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0" 
                xmlns:ceab="http://www.ivoa.net/xml/CEA/base/v1.1"
                xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0"               
                xmlns:ceaimpl="http://www.astrogrid.org/schema/CEAImplementation/v2.0"               
                exclude-result-prefixes="ceaimpl">
   <xsl:output method="html" encoding="UTF-8" indent="yes" />
   <xsl:strip-space elements="*"/>
   
   <xsl:template match="/" >
    <html>
    <head>
      <style type="text/css">
        .fail { color:red }
        .rec  { color:orange}
 table {
	border: none;
}
td {
	vertical-align: top;
}
th {
	background-color: #CCCCCC;
}
     </style>
    </head>
    <body>
       <h1>SIAP Compliance Analysis</h1>
       <p>This is the result of running all the siap services found in the Astrogrid Registry through the <a href="http://nvo.ncsa.uiuc.edu/dalvalidate/siavalidate.html">NVO SIAP Validator</a> on 05/06/2008</p>
       <h2>Overview</h2>
       <p>total services=<xsl:value-of select="count(/tests/resource/SIAValidation)"/></p>

       <p>conditionally compliant services=<xsl:value-of select="count(/tests/resource/SIAValidation[count(./testQuery/test[@status='fail'])=0])"/></p>
       <p>fully compliant services (with no recommendations)=<xsl:value-of select="count(/tests/resource/SIAValidation[count(./testQuery/test)=0 ])"/></p>
       <p>services that failed compliance=<xsl:value-of select="count(/tests/resource/SIAValidation[count(./testQuery/test[@status='fail'])>0  ])"/></p>
      <p>services with warnings=<xsl:value-of select="count(/tests/resource/SIAValidation[count(./testQuery/test[@status='warn'])>0  ])"/></p>
       <p>complete failures (probably not SIAP services, or the service is down) = <xsl:value-of select="count(//failure)"/></p>
       <p>services returning data=<xsl:value-of select="count(/tests/resource/SIAValidation/testQuery[@name='user' and @recordCount != '0'])"/></p>
       <h3> votable versions </h3>
       <table>
       <tr><th>votable version</th><th>freqency</th></tr>
       <xsl:for-each-group select="//testQuery[@name='metadata']" group-by="@votable-version">
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td></tr>
       </xsl:for-each-group>
       </table>
 

       <h2>Common errors</h2>
       <p>number of incorrectly registered endpoints (not ending in &amp; or ?) = <xsl:value-of select="count(/tests/resource[@url != @urlorig])"/></p>
       <p>FORMAT in baseURL = <xsl:value-of select="count(/tests/resource[contains(upper-case(@url),'FORMAT=')])"/></p>
       <p>OPTION with text content = <xsl:value-of select="count(/tests/resource[metadata//OPTION[string-length(text())>0]])"/></p>
       <h2>Parameters</h2>
       <p>the frequencies of parameters over all the services</p>
       <table>
             <tr><th>Param</th><th>occurrence</th><th>description</th></tr>
 
       <xsl:for-each-group select="//PARAM" group-by="@name">
         <xsl:sort select="current-grouping-key()" />
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td><td><xsl:value-of select="current-group()[1]/DESCRIPTION"/></td></tr>
       </xsl:for-each-group>
       </table>
       <h2>Columns</h2>
        <p>the frequencies of column types over all the services(grouped by UCD)</p>
       <table>
       <tr><th>UCD</th><th>occurrence</th><th>description</th></tr>
       <xsl:for-each-group select="//TABLE/FIELD[@ucd]" group-by="@ucd">
         <xsl:sort select="current-grouping-key()" />
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td><td><xsl:value-of select="current-group()[1]/DESCRIPTION"/></td></tr>
       </xsl:for-each-group>
       </table>
       
       
       <p>the frequencies of column types over all the services where UCD not specified(grouped by name)</p>
 

         
       <table>
       <tr><th>name</th><th>occurrence</th><th>ID</th><th>description</th></tr>
       <xsl:for-each-group select="//TABLE/FIELD[not(@ucd)]" group-by="@name">
         <xsl:sort select="current-grouping-key()" />
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td><td><xsl:value-of select="current-group()[1]/@ID"/></td><td><xsl:value-of select="current-group()[1]/DESCRIPTION"/></td></tr>
       </xsl:for-each-group>
       </table>
      
         <p>the frequencies of column types over all the services where UCD and (illegally) name not specified(grouped by ID)</p>
 

         
       <table>
             <tr><th>ID</th><th>occurrence</th><th>description</th></tr>
 
       <xsl:for-each-group select="//TABLE/FIELD[not(@ucd) and not (@name)]" group-by="@ID">
         <xsl:sort select="current-grouping-key()" />
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td><td><xsl:value-of select="current-group()[1]/DESCRIPTION"/></td></tr>
       </xsl:for-each-group>
       </table>
     
        
        <h2>Failures</h2>
        <table>
        <tr><th>std para</th><th>occurrences</th><th>detailed messages</th></tr>
       <xsl:for-each-group select="//testQuery/test[@status='fail']" group-by="@item">
         <xsl:sort select="count(current-group())" order="descending"/>
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td><td><xsl:for-each-group select="current-group()" group-by="text()"><xsl:value-of select="current-grouping-key()"/></xsl:for-each-group></td></tr>
       </xsl:for-each-group>
        </table>
        <h2>Warnings</h2>
        <table>
        <tr><th>std para</th><th>occurrences</th><th>detailed messages</th></tr>
       <xsl:for-each-group select="//testQuery/test[@status='warn']" group-by="@item">
         <xsl:sort select="count(current-group())" order="descending"/>
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td><td><xsl:for-each-group select="current-group()" group-by="text()"><xsl:value-of select="current-grouping-key()"/></xsl:for-each-group></td></tr>
       </xsl:for-each-group>
        </table>
                <h2>Recommendations</h2>
        <table>
        <tr><th>std para</th><th>occurrences</th><th>example messages</th></tr>
       <xsl:for-each-group select="//testQuery/test[@status='rec']" group-by="@item">
         <xsl:sort select="count(current-group())" order="descending"/>
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td><td><xsl:value-of select="current-group()[1]"/></td></tr>
       </xsl:for-each-group>
        </table>
        
        <h2>Service details</h2>
        <xsl:apply-templates select="/tests/resource"/>
 
    </body>
    </html>
   </xsl:template>
   
   <xsl:template match="resource">
   <h3>
   <xsl:attribute name="class">
    <xsl:choose>
      <xsl:when test="count(./SIAValidation/testQuery/test[@status='fail'])>0">
         <xsl:value-of select="'fail'"/>
      </xsl:when>
      <xsl:when test="count(./SIAValidation/testQuery/test[@status=('rec','warn')])>0">
         <xsl:value-of select="'rec'"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="'pass'"/>
      </xsl:otherwise>
    </xsl:choose>
   </xsl:attribute>
   <xsl:value-of select="@id"/></h3>
   <p><xsl:value-of select="@title"/> url=<a>
   <xsl:attribute name='href'>
      <xsl:value-of select="concat(@url,'POS=83.633208,22.014472&amp;SIZE=1.0,1.0&amp;FORMAT=ALL')" />
   </xsl:attribute>
   <xsl:value-of select="@urlorig"/></a></p> 
   <p><a>
   <xsl:attribute name='href'>
      <xsl:value-of select="concat('http://nvo.ncsa.uiuc.edu/dalvalidate/SIAValidater?runid=vsb54&amp;RA=83.633208&amp;DEC=22.014472&amp;RASIZE=1.0&amp;DECSIZE=1.0&amp;FORMAT=ALL&amp;format=text&amp;show=fail&amp;show=warn&amp;show=rec&amp;op=Validate&amp;endpoint=',encode-for-uri(@url))" />
   </xsl:attribute>
   Rerun test
   </a></p>
   <table>
       <xsl:for-each-group select="./SIAValidation/testQuery/test" group-by="@status">
         <tr><td><xsl:value-of select="current-grouping-key()"/></td><td> <xsl:value-of select="count(current-group())"/></td></tr>
       </xsl:for-each-group>
    </table>
 
   
   </xsl:template>
   
   <xsl:template match="SIAValidation">
   <p><xsl:value-of select="@baseURL"></xsl:value-of></p>
   <ul>
     <xsl:for-each select="testQuery">
         <li><xsl:value-of select="@showStatus"/></li>
     </xsl:for-each>
   </ul>
   </xsl:template>
   
    
 
   <!-- copy-all attributes template  -->
   <xsl:template match="@*|node()" >
      <xsl:copy> 
         <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
   </xsl:template>
   
</xsl:stylesheet>
