<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"/>

   <xsl:template match="/">
     <head><title>Registry Query Results</title>
     </head>
     <body>
     <h1>Registry Query Results</h1>
     <xsl:apply-templates/>
     </body>
   </xsl:template>

   <xsl:template match="queryResponse/responseRecord">
       <p> <table>
         <tr> <th>Item</th> <th>Value</th> </tr>
         <xsl:apply-templates/>
       </table>
       </p>
   </xsl:template>

   <xsl:template match="queryResponse">
      <xsl:choose>
        <xsl:when test="text() = 'java.lang.NullPointerException'">
           <strong>No Records selected</strong>
        </xsl:when>
        <xsl:otherwise>
          <xsl:apply-templates/>
        </xsl:otherwise>
      </xsl:choose>
   </xsl:template>

   <xsl:template match="recordKeyPair">
     <tr> <td> <em> <xsl:value-of select="@item" /> </em> </td>
          <td>
          <xsl:choose> 
            <xsl:when test="@item = 'email'">
              <xsl:text disable-output-escaping="yes">
              &lt;a href="mailto:</xsl:text>
              <xsl:value-of select="@value" />
              <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
              <xsl:value-of select="@value" />
              <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
            </xsl:when>
            <xsl:when test="@item = 'servicelocation'">
              <xsl:text disable-output-escaping="yes">
              &lt;a href="</xsl:text>
              <xsl:value-of select="@value" />
              <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
              <xsl:value-of select="@value" />
              <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
            </xsl:when>
            <xsl:when test="@item = 'master'">
              <xsl:text disable-output-escaping="yes">
              &lt;a href="</xsl:text>
              <xsl:value-of select="@value" />
              <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
              <xsl:value-of select="@value" />
              <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
            </xsl:when>
            <xsl:otherwise>
              <strong><xsl:value-of select="@value" /> </strong>
            </xsl:otherwise>
          </xsl:choose>
          </td>
     </tr>
   </xsl:template>

   <xsl:template match="queryException">
         <h2>Exception Raised</h2>
         <strong>
         <xsl:apply-templates/>
         </strong>
   </xsl:template>

   <xsl:template match="service">
         <em>Service:</em>
         <strong>
         <xsl:apply-templates/>
         </strong>
   </xsl:template>

   <xsl:template match="query">
         <p><em>Query Selection: </em><xsl:text> </xsl:text>
         <strong>
         <xsl:apply-templates/>
         </strong>
         </p>
   </xsl:template>

   <xsl:template match="selectionSequence">
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="selection">
         <xsl:value-of select="@item"/><xsl:text> </xsl:text>
         <xsl:value-of select="@itemOp"/><xsl:text> </xsl:text>
         '<xsl:value-of select="@value"/>'
   </xsl:template>
   <xsl:template match="selectionOp">
         <xsl:value-of select="@op"/><xsl:text> </xsl:text>
   </xsl:template>

</xsl:stylesheet> 
