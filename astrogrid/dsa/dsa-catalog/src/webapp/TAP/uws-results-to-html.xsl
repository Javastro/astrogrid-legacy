<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
    <!-- A stylesheet to convert a UWS results-list into HTML. 
         Because TAP has a fixed set of results - just one, named item - 
         this stylesheet ignores the content of the XML document and
         hard-coded the output. -->
    
    <xsl:output method="html"/>
    
    <xsl:template match="/">
        <html>
            <head>
              <title>Results of TAP query via UWS</title>
            </head>
            <body>
                <h1>Results of TAP query via UWS</h1>
                <p><a href="results/result">Table of results</a>.</p>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
