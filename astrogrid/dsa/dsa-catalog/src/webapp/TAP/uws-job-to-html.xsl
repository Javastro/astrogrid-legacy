<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:uws="http://www.ivoa.net/xml/UWS/v1.0rc3"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    
    <!-- A stylesheet to convert a UWS job-summary into HTML.  -->
    
    <xsl:output method="html"/>

    <xsl:template match="text()|@*">
      <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="/">
        <html>
            <head>
              <title>UWS job</title>
            </head>
            <body>
                <h1>Summary of UWS job</h1>
                <xsl:apply-templates/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="uws:job">
      <xsl:variable name="jobId"><xsl:value-of select="uws:jobId"/></xsl:variable>
      <table>
        <tr>
          <td><xsl:text>Job identifier:</xsl:text></td>
          <td><xsl:value-of select="uws:jobId"/></td>
        </tr>
        <tr>
          <td><xsl:text>Phase:</xsl:text></td>
          <td><xsl:value-of select="uws:phase"/></td>
      </tr>
      <tr>
        <td><xsl:text>Start time</xsl:text></td>
        <td><xsl:value-of select="uws:startTime"/></td>
      </tr>
      <tr>
        <td><xsl:text>End time:</xsl:text></td>
        <td><xsl:value-of select="uws:endTime"/></td>
      </tr>
      <tr>
        <td><xsl:text>Maximum duration:</xsl:text></td>
        <td><xsl:value-of select="uws:duration"/></td>
      </tr>
      <tr>
        <td><xsl:text>Destruction time:</xsl:text></td>
        <td><xsl:value-of select="uws:destruction"/></td>
      </tr>
      <tr>
        <td><xsl:text>Query results:</xsl:text></td>
        <td><a href="{$jobId}/results/result">result</a></td>
      </tr>
      </table>
      <p>
          <form action="{$jobId}/phase" method="post">
              <input type="hidden" name="PHASE" value="RUN"/>
              <input type="submit" value="Execute query"/>
          </form>
      </p>
        <p>
            <form action="{$jobId}/phase" method="post">
                <input type="hidden" name="PHASE" value="ABORT"/>
                <input type="submit" value="Abort query"/>
            </form>
        </p>
        <p>
            <form action="{$jobId}" method="post">
                <input type="hidden" name="ACTION" value="DELETE"/>
                <input type="submit" value="Delete job"/>
            </form>
        </p>
        <p>
            <form action="{$jobId}/destruction" method="post">
              <input type="submit" value="Change destruction time to"/>
              <xsl:element name="input">
                <xsl:attribute name="type">text</xsl:attribute>
                <xsl:attribute name="name">DESTRUCTION</xsl:attribute>
                <xsl:attribute name="value"><xsl:value-of select="uws:destruction"/></xsl:attribute>
                <xsl:attribute name="size">23</xsl:attribute>
              </xsl:element>
            </form>
        </p>
        <p>
          <a href="../async">List of known jobs</a>
        </p>
    </xsl:template>

</xsl:stylesheet>
