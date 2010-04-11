<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="/">
        <html>
            <head>
                <title>DB schema</title>
            </head>
            <body>
                <h1>DB schema</h1>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="text()|@*"/>

    <xsl:template match="table">
        <h2><xsl:text>Table </xsl:text><xsl:value-of select="name"/></h2>
        <table>
            <tr>
                <th>Name</th>
                <th>Type</th>
                <th>Unit</th>
                <th>UCD</th>
                <th>Description</th>
            </tr>
            <xsl:apply-templates/>
        </table>
    </xsl:template>

    <xsl:template match="column">
        <tr>
            <td><xsl:value-of select="name"/></td>
            <td><xsl:value-of select="dataType"/></td>
            <td><xsl:value-of select="unit"/></td>
            <td><xsl:value-of select="ucd"/></td>
            <td><xsl:value-of select="description"/></td>
        </tr>
    </xsl:template>

</xsl:stylesheet>
