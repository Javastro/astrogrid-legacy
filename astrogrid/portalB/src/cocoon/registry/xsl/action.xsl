<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"/>

   <xsl:include href="../../common/xsl/astrogrid.xsl"/>
   
   <xsl:template match="responseRecord">
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
     <tr>
       <xsl:choose> 
         <xsl:when test="@item = 'metadataType'"/>
         <xsl:when test="@item = 'ERROR:'">
           <p><strong>ERROR: </strong> <xsl:value-of select="@value" /></p>
         </xsl:when>
         <xsl:otherwise>
           <td> <em> <xsl:value-of select="@item" /> </em> </td>
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
             <xsl:when test="@item = 'servicelocation' or contains(@item,'URL') or contains(@item,'Url')">
               <xsl:text disable-output-escaping="yes">
               &lt;a href="</xsl:text>
               <xsl:value-of select="@value" />
               <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
               <xsl:value-of select="@value" />
               <xsl:text disable-output-escaping="yes">&lt;/a&gt;</xsl:text>
             </xsl:when>
             <xsl:when test="@item = 'identifier'">
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
       </xsl:otherwise>
     </xsl:choose>
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

   <xsl:template match="debug">
         <em>DEBUG:</em>
         <strong>
         <xsl:apply-templates/>
         </strong>
   </xsl:template>

   <xsl:template match="query">
         <p><em>Registry Query Selection: </em><br/>
         <strong> &lt;query&gt;
         <xsl:apply-templates/>
         &lt;/query&gt; </strong>
         </p>
   </xsl:template>

   <xsl:template match="selectionSequence">
         &lt;selectionSequence&gt;<br/>
         <xsl:apply-templates/>
         &lt;/selectionSequence&gt;
   </xsl:template>

   <xsl:template match="selection">
         &lt;selection<xsl:text> </xsl:text>
         item='<xsl:value-of select="@item"/>'<xsl:text> </xsl:text>
         itemOp='<xsl:value-of select="@itemOp"/>'<xsl:text> </xsl:text>
         value='<xsl:value-of select="@value"/>'&gt;<br/>
   </xsl:template>

   <xsl:template match="selectionOp">
         &lt;selectionOp<xsl:text> </xsl:text>
         op='<xsl:value-of select="@op"/><xsl:text> </xsl:text>'&gt;<br/>
   </xsl:template>

   <xsl:template/>

</xsl:stylesheet> 
