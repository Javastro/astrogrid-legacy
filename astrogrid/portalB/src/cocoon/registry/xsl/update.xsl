<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"/>
<xsl:strip-space elements="testNode//*"/>

   <xsl:include href="../../common/xsl/registrytemplate.xsl"/>
 
   <xsl:template match="registry">
        <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="edit">
       <hr/>
       <form method="POST" action="adminaction.xsp">
       <input type='SUBMIT' name='Edit' value='Edit This Resource'/>
       <input type='RESET' name='Reset' value='Reset'/>
       <p/>
       <table>
        <xsl:apply-templates/>
       </table>
       </form>
   </xsl:template>

   <xsl:template match="delete">
       <hr/>
       <form method="POST" action="adminaction.xsp">
       <input type='SUBMIT' name='Delete' value='Delete This Resource'/>
       <input type='RESET' name='Reset' value='Reset'/>
       <p/>
       <table>
          <xsl:apply-templates/>
       </table>
       </form>
   </xsl:template>

   <xsl:template match="add">
       <hr/>
       <form method="POST" action="adminaction.xsp">
       <input type='SUBMIT' name='Add' value='Add This Resource'/>
       <input type='RESET' name='Reset' value='Reset'/>
       <p/>
       <table>
         <xsl:apply-templates />
       </table>
       </form>
   </xsl:template>



   <xsl:template name="service" match="service//*">
      <xsl:if test="not(*)">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td> 
            <input size="40" type="text">
               <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="0"/>name_<xsl:value-of select="name()"/></xsl:attribute>
               <xsl:attribute name="value"><xsl:value-of select="current()"/></xsl:attribute>
            </input>
         </td></tr>
      </xsl:if>
      <xsl:if test="*">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td>
            <input type="hidden">
               <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_<xsl:value-of select="name()"/></xsl:attribute>
               <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
            </input>
         </td></tr>
         <xsl:apply-templates/>
      </xsl:if>
   </xsl:template>

   <xsl:template match="webserver">
         <tr><td></td>
         <td>
            <input type="hidden">
               <xsl:attribute name="name">webserver</xsl:attribute>
               <xsl:attribute name="value"><xsl:value-of select="current()"/></xsl:attribute>
            </input>
         </td></tr>
   </xsl:template>

   <xsl:template match="delete/service//*">
      <xsl:if test="not(*)">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td>
            <input size="40" type="hidden">
               <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="0"/>name_<xsl:value-of select="name()"/></xsl:attribute>
               <xsl:attribute name="value"><xsl:value-of select="current()"/></xsl:attribute>
            </input>
            <xsl:value-of select="current()"/>
         </td></tr>
      </xsl:if>
      <xsl:if test="*">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td>
            <input type="hidden">
               <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_<xsl:value-of select="name()"/></xsl:attribute>
               <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
            </input>
         </td></tr>
         <xsl:apply-templates/>
      </xsl:if>
   </xsl:template>

   <xsl:template match="queryException">
         <h2>Exception Raised</h2>
         <strong>
         <xsl:apply-templates/>
         </strong>
   </xsl:template>

   <xsl:template match="add/service">
         <tr><td colspan="2" bgcolor="#DDDDDD">
         <table>
         <tr>
         <td valign="bottom"><strong><u>SERVICE</u></strong>
         <input type="hidden">
            <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_service</xsl:attribute>
            <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
         </input></td>
         </tr></table>
         </td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="edit/service">
         <tr><td colspan="2" bgcolor="#DDDDDD">
         <table>
         <tr>
         <td valign="bottom" colspan="2"><strong><u>SERVICE</u>: 
         <font color="blue"><xsl:value-of select="identity/shortName"/></font></strong>
         <input type="hidden">
            <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_service</xsl:attribute>
            <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
        </input></td>
         </tr></table>
         </td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="delete/service">
         <tr><td colspan="2" bgcolor="#DDDDDD">
         <table>
         <tr>
         <td valign="bottom" colspan="2"><strong><u>SERVICE</u>: 
         <font color="blue"><xsl:value-of select="identity/shortName"/></font></strong>
         <input type="hidden">
            <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_service</xsl:attribute>
            <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
         </input></td>
         </tr></table>
         </td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="edit/service/identity/shortName">
         <tr><td><xsl:value-of select="name()"/>: </td>
         <td>
            <input type="hidden">
               <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"$
               <xsl:attribute name="value"><xsl:value-of select="current()"/></xsl:attribute>
            </input>
            <xsl:value-of select="current()"/>
         </td>
         </tr>
   </xsl:template>


   <xsl:template match="//*/identity">
         <tr><td><strong><u>Identity</u></strong></td>
         <td><input type="hidden">
            <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_identity</xsl:attribute>
            <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
         </input></td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="//*/curation">
         <tr><td><strong><u>Curation</u></strong></td>
         <td><input type="hidden">
            <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_curation</xsl:attribute>
            <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
         </input></td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="//*/content">
         <tr><td><strong><u>Content</u></strong></td>
         <td><input type="hidden">
            <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_content</xsl:attribute>
            <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
         </input></td></tr>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="//*/serviceMetadataConcept">
         <tr><td><strong><u>Service Metadata Concepts</u></strong></td>
         <td><input type="hidden">
            <xsl:attribute name="name">key_<xsl:value-of select="generate-id(.)"/>children_<xsl:value-of select="count(*)"/>name_serviceMetadataConcept</xsl:attribute>
            <xsl:attribute name="value">NO_TEXT_NODE</xsl:attribute>
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
