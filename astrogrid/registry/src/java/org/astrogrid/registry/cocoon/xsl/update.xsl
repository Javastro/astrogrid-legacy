<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"/>

   <xsl:include href="../../common/xsl/agtemplate.xsl"/>

   <xsl:template match="edit">
       <form method="POST" action="agregistryhelp.html">
       <input type='SUBMIT' name='Edit' value='Edit This Resource'/>
       <input type='RESET' name='Reset' value='Reset'/>
       <table>
         <xsl:apply-templates mode="Edit"/>
       </table>
       </form>
   </xsl:template>

   <xsl:template match="delete">
       <form method="POST" action="agregistryhelp.html">
       <input type='SUBMIT' name='Delete' value='Delete This Resource'/>
       <input type='RESET' name='Reset' value='Reset'/>
       <table>
          <xsl:apply-templates mode="Delete"/>
       </table>
       </form>
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

   <xsl:template match="recordKeyPair" mode="Edit">
     <tr>
       <xsl:choose> 
         <xsl:when test="@item = 'metadataType'"/>
         <xsl:when test="@item = 'ERROR:'">
           <td>
           <strong>ERROR: </strong> <xsl:value-of select="@value" />
           </td>
         </xsl:when>
         <xsl:otherwise>
           <td> <em> <xsl:value-of select="@item" /> </em> </td>
           <td>
             <input size="80">
   <xsl:attribute name="name"><xsl:value-of select="@item"/></xsl:attribute>
   <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
             </input>
           </td>
         </xsl:otherwise>
       </xsl:choose>
     </tr>
   </xsl:template>

   <xsl:template match="recordKeyPair" mode="Delete">
       <xsl:variable name="type">Delete</xsl:variable>
     <tr>
       <xsl:choose> 
         <xsl:when test="@item = 'metadataType'"/>
         <xsl:when test="@item = 'ERROR:'">
           <td>
           <strong>ERROR: </strong> <xsl:value-of select="@value" />
           </td>
         </xsl:when>
         <xsl:otherwise>
           <td> <em> <xsl:value-of select="@item" /> </em> </td>
           <td> <strong><xsl:value-of select="@value"/></strong> </td>
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
   
</xsl:stylesheet> 
