<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"/>
<xsl:strip-space elements="testNode//*"/>

   <xsl:include href="../../common/xsl/agtemplate.xsl"/>

   <xsl:template match="edit">
       <form method="POST" action="edit.xsp">
       <input type='SUBMIT' name='Edit' value='Edit This Resource'/>
       <input type='RESET' name='Reset' value='Reset'/>
       <p/>
       <table>
        <xsl:apply-templates/>
       </table>
       </form>
   </xsl:template>

   <xsl:template match="delete">
       <form method="POST" action="delete.xsp">
       <input type='SUBMIT' name='Delete' value='Delete This Resource'/>
       <input type='RESET' name='Reset' value='Reset'/>
       <p/>
       <table>
          <xsl:apply-templates/>
       </table>
       </form>
   </xsl:template>

   <xsl:template match="add">
       <form method="POST" action="add.xsp">
       <input type='SUBMIT' name='Add' value='Add This Resource'/>
       <input type='RESET' name='Reset' value='Reset'/>
       <p/>
       <table>
          <xsl:apply-templates/>
       </table>
       </form>
   </xsl:template>

   <xsl:template match="service//*">
      <xsl:if test="not(*)">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td>
            <input size="40" type="text">
               <xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
               <xsl:attribute name="value"><xsl:value-of select="current()"/></xsl:attribute>
            </input>
         </td></tr>
      </xsl:if>
      <xsl:if test="*">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td>
            <input type="hidden">
               <xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
               <xsl:attribute name="value"></xsl:attribute>
            </input>
         </td></tr>
         <xsl:apply-templates/>
      </xsl:if>
   </xsl:template>

   <xsl:template match="delete//*/service//*">
      <xsl:if test="not(*)">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td>
            <input size="40" type="hidden">
               <xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
               <xsl:attribute name="value"><xsl:value-of select="current()"/></xsl:attribute>
            </input>
            <xsl:value-of select="current()"/>
         </td></tr>
      </xsl:if>
      <xsl:if test="*">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td>
            <input type="hidden">
               <xsl:attribute name="name"><xsl:value-of select="name()"/></xsl:attribute>
               <xsl:attribute name="value"></xsl:attribute>
            </input>
         </td></tr>
         <xsl:apply-templates/>
      </xsl:if>
   </xsl:template>

   <xsl:template match="header">
     <table>
       <tr>
       <td>
         <input size="20" type="hidden">
            <xsl:attribute name="name">username</xsl:attribute>
            <xsl:attribute name="value"><xsl:value-of select="username"/></xsl:attribute>
         </input>
       </td></tr>
       <tr>
       <td>
         <input size="20" type="hidden">
            <xsl:attribute name="name">certificate</xsl:attribute>
            <xsl:attribute name="value"><xsl:value-of select="certificate"/></xsl:attribute>
         </input>
       </td></tr>
       <tr>
       <td>
         <input size="20" type="hidden">
            <xsl:attribute name="name">webserver</xsl:attribute>
            <xsl:attribute name="value"><xsl:value-of select="webserver"/></xsl:attribute>
         </input>
       </td></tr>
     </table>
   </xsl:template>

   <xsl:template match="queryException">
         <h2>Exception Raised</h2>
         <strong>
         <xsl:apply-templates/>
         </strong>
   </xsl:template>

   <xsl:template match="add//*/service">
         <tr><td colspan="2" bgcolor="#DDDDDD">
         <table>
         <tr>
         <td valign="bottom"><strong><u>SERVICE</u></strong>
         <input type="hidden">
            <xsl:attribute name="name">service</xsl:attribute>
            <xsl:attribute name="value"></xsl:attribute>
         </input></td>
         </tr></table>
         </td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="edit//*/service">
         <tr><td colspan="2" bgcolor="#DDDDDD">
         <table>
         <tr>
         <td valign="top" align="left"><input type="radio">
            <xsl:attribute name="name">chooseService</xsl:attribute>
            <xsl:attribute name="value"><xsl:value-of select="//*/shortName"/></xsl:attribute>
         </input></td>
         <td valign="bottom"><strong><u>SERVICE</u>: 
         <font color="blue"><xsl:value-of select="//*/shortName"/></font></strong>
         <input type="hidden">
            <xsl:attribute name="name">service</xsl:attribute>
            <xsl:attribute name="value"></xsl:attribute>
         </input></td>
         </tr></table>
         </td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="delete//*/service">
         <tr><td colspan="2" bgcolor="#DDDDDD">
         <table>
         <tr>
         <td valign="top" align="left"><input type="radio">
            <xsl:attribute name="name">chooseService</xsl:attribute>
            <xsl:attribute name="value"><xsl:value-of select="//*/shortName"/></xsl:attribute>
         </input></td>
         <td valign="bottom"><strong><u>SERVICE</u>: 
         <font color="blue"><xsl:value-of select="//*/shortName"/></font></strong>
         <input type="hidden">
            <xsl:attribute name="name">service</xsl:attribute>
            <xsl:attribute name="value"></xsl:attribute>
         </input></td>
         </tr></table>
         </td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="//*/identity">
         <tr><td><strong><u>Identity</u></strong></td>
         <td><input type="hidden">
            <xsl:attribute name="name">curation</xsl:attribute>
            <xsl:attribute name="value"></xsl:attribute>
         </input></td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="//*/curation">
         <tr><td><strong><u>Curation</u></strong></td>
         <td><input type="hidden">
            <xsl:attribute name="name">curation</xsl:attribute>
            <xsl:attribute name="value"></xsl:attribute>
         </input></td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="//*/content">
         <tr><td><strong><u>Content</u></strong></td>
         <td><input type="hidden">
            <xsl:attribute name="name">curation</xsl:attribute>
            <xsl:attribute name="value"></xsl:attribute>
         </input></td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="//*/serviceMetadataConcept">
         <tr><td><strong><u>Service Metadata Concepts</u></strong></td>
         <td><input type="hidden">
            <xsl:attribute name="name">serviceMetadataConcept</xsl:attribute>
            <xsl:attribute name="value"></xsl:attribute>
         </input></td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="debug">
         <em>DEBUG:</em>
         <strong>
         <xsl:apply-templates/>
         </strong>
   </xsl:template>

</xsl:stylesheet> 
