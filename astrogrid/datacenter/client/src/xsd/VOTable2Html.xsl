<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/">
    <html>
      <head/>
      <body>
        <xsl:for-each select="VOTABLE">
          <xsl:for-each select="RESOURCE">

            <h2>Parameters</h2>
            <table border="1">
              <tr>
                <td><b>Name</b></td>
                <td><b>Value</b></td>
              </tr>
              <xsl:for-each select="PARAM">
                <tr>
                  <xsl:for-each select="@ID">
                    <td> <xsl:value-of select="."/> </td>
                  </xsl:for-each>
                  <xsl:for-each select="@value">
                    <td> <xsl:value-of select="."/> </td>
                  </xsl:for-each>
                </tr>
              </xsl:for-each>
            </table>

			<xsl:for-each select="TABLE">

			  <h2>Fields</h2>
			  <table border="1"> <tr>
				<td><b>name</b></td>
				<td><b>ID</b></td>
				<td><b>unit</b></td>
				<td><b>ucd</b></td>
				<td><b>datatype</b></td>
				<td><b>arraysize</b></td>
				<td><b>precision</b></td>
				<td><b>width</b></td>
				<td><b>ref</b></td>
				<td><b>type</b></td> </tr>

			  <xsl:for-each select="FIELD"> 
					<tr>
				  <td> <xsl:value-of select="@name"/> </td>
				  <td> <xsl:value-of select="@ID"/> </td>
				  <td> <xsl:value-of select="@unit"/> </td>
				  <td> <xsl:value-of select="@ucd"/> </td>
				  <td> <xsl:value-of select="@datatype"/> </td>
				  <td> <xsl:value-of select="@arraysize"/> </td>
				  <td> <xsl:value-of select="@precision"/> </td>
				  <td> <xsl:value-of select="@width"/> </td>
				  <td> <xsl:value-of select="@ref"/> </td>
				  <td> <xsl:value-of select="@type"/> </td>
					</tr>
			  </xsl:for-each> 
				</table>

				<h2>Data</h2>
				<table border="1">

				<xsl:for-each select="FIELD"> 
					<td> <b><xsl:value-of select="@name"/></b> </td>
				</xsl:for-each> 

			  <xsl:for-each select="DATA">
				<xsl:for-each select="TABLEDATA">

				  <xsl:for-each select="TR">
					<tr>
					  <xsl:for-each select="TD">
						<td width="100"> <xsl:value-of select="."/> </td>
					  </xsl:for-each>
					</tr>
				  </xsl:for-each>

				</xsl:for-each>
			  </xsl:for-each>
				</table>

			</xsl:for-each>

		  </xsl:for-each>
		</xsl:for-each>
      </body>
    </html>
  </xsl:template>
</xsl:stylesheet>
