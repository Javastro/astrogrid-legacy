<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema">
    <xsl:template match="/">
        <html>
            <head />
            <body>
                <h1>
                    <img border="0">
                        <xsl:attribute name="src"><xsl:text disable-output-escaping="yes">aglogo.gif</xsl:text></xsl:attribute>
                    </img>
                    <br />Datasets Available</h1>
                <br />
                <br />
            </body>
        </html>
    </xsl:template>
    <xsl:template match="recordKeyPair">test</xsl:template>
</xsl:stylesheet>
