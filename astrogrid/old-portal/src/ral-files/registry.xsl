<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

   <xsl:template match="/">
    <html>
     <head>
      <title>Registry WebService invoked via Cocoon</title>
     </head>

     <body>
      <center><h1>Registry WebService from Cocoon</h1></center>
         <h2>Registry Details</h2>
      <xsl:apply-templates/>
     </body>
    </html>
   </xsl:template>
   
   <xsl:template match="basicMetadata">
      <h3> Service: <xsl:value-of select="title"/> (<xsl:value-of select="id"/>) </h3>
   </xsl:template>

   <xsl:template match="curationMetadata">
      <table>
      <xsl:apply-templates/>
      </table>
   </xsl:template>

   <xsl:template match="metadataFormat">
      <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="registrySvc">
      <table border="on"> <caption>Registry Service:</caption> 
          <xsl:apply-templates/>
      </table>
   </xsl:template>

   <xsl:template match="location">
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="mirror">
      <tr><td>Mirror:</td> <td><xsl:apply-templates/>  </td></tr>
   </xsl:template>
   <xsl:template match="variant">
      <tr><td>Variant:</td> <td><xsl:apply-templates/>  </td></tr>
   </xsl:template>

   <xsl:template match="master">
     <tr><td> Master:</td> <td> <xsl:apply-templates/>  </td></tr>
   </xsl:template>
   <xsl:template match="servicelocation">
      <tr><td>Service Location:</td> <td>  <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="community">
     <tr><td> Community:</td> <td> <xsl:apply-templates/> </td></tr>
   </xsl:template>

   <xsl:template match="publisher">
      <tr><td>Publisher:</td> <td>  <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="creator">
      <tr><td>Creator:</td> <td>     <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="description">
      <tr><td>Description:</td> <td> <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="contributor">
      <tr><td>Contributor:</td> <td> <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="date">
      <tr><td>Date:</td> <td>        <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="version">
      <tr><td>Version:</td> <td>     <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="serviceURL">
      <tr><td>URL: </td> <td>        <xsl:apply-templates/> </td></tr>
   </xsl:template>

   <xsl:template match="contact">
      <tr><td>Contact:</td> <td> <xsl:value-of select="name"/> </td>
          <td> <xsl:text disable-output-escaping="yes"> &lt;a href=&quot; </xsl:text>
               mailto:<xsl:value-of select="email"/> 
               <xsl:text disable-output-escaping="yes"> &quot;&gt; </xsl:text>
               <xsl:value-of select="email"/>
               <xsl:text disable-output-escaping="yes"> &lt;/a&gt;</xsl:text></td>
      </tr>
   </xsl:template>

   <xsl:template match="subjectList">
         <tr><td>Subjects:</td>
         <xsl:apply-templates select="subject"/> </tr>
   </xsl:template>
   <xsl:template match="subject">
      <td> <xsl:apply-templates/> </td>
   </xsl:template>

   <xsl:template match="dataSvc">
      <table border="on"> <caption>Data Service:</caption> 
          <xsl:apply-templates/>
      </table>
   </xsl:template>

   <xsl:template match="dataSvc/type">
       <tr><td>Type: </td> <td> <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="dataSvc/format">
       <tr><td>Format:</td> <td>  <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="dataSvc/coverage">
       <tr><td> Coverage:</td> <td>
       <table border="on">
           <xsl:apply-templates/> 
       </table>
       </td></tr>
   </xsl:template>
   <xsl:template match="coverage/region">
       <tr><td></td> <td>Region:</td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/coordinates">
       <tr><td></td> <td>Coordinates </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/wavelengthrange">
       <tr><td></td> <td>Wavelength Range </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/temporal">
       <tr><td></td> <td>Temporal </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/wavelengthshort">
       <tr><td></td> <td>Wavelength - short </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/wavelengthlong">
       <tr><td></td> <td>Wavelength - long </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/ramin">
       <tr><td></td> <td>RA min </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/ramax">
       <tr><td></td> <td>RA max </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/decmin">
       <tr><td></td> <td>Dec min </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/decmax">
       <tr><td></td> <td>Dec max </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="coverage/sensitvity">
       <tr><td></td> <td>Sensitivity </td> <td><xsl:apply-templates/> </td></tr>
   </xsl:template>

   <xsl:template match="spatialresolution">
       <tr><td>Spacial Resolution:</td><td> <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="facility">
       <tr><td>Facility:</td><td> <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="instrument">
       <tr><td>Instrumment:</td><td> <xsl:apply-templates/> </td></tr>
   </xsl:template>
   <xsl:template match="briefsummary">
       <tr><td>Summary:</td><td> <xsl:apply-templates/> </td></tr>
   </xsl:template>

</xsl:stylesheet> 
