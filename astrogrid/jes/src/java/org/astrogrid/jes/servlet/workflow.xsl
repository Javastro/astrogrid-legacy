<?xml version="1.0" ?>
<xsl:stylesheet  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:wf="http://www.astrogrid.org/schema/AGWorkflow/v1"
  xmlns:er="http://www.astrogrid.org/schema/ExecutionRecord/v1"
  xmlns:creds="http://www.astrogrid.org/schema/Credentials/v1"
  xmlns:pd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
  xmlns:cea="http://www.astrogrid.org/schema/CEATypes/v1"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

<xsl:template match="/">
  <html>
   <head>
   <style type="text/css">
    div.header{
    }
    div.body{
            border-top-width:5px;
        border-top-color:lightblue;
        border-top-style:solid;
        margin-top:10px;
        padding:5px;
    }

    div.description{
        padding:5px;
        background-color:#FFFFCC;
    }

    div.block, div.step{
            margin-top:5px;
        padding-left:5px;
        padding-top:5px;
        padding-bottom:5px;
    }
    div.step {
            margin-left:20px;
        border-left-width:1px; border-left-style:solid;
            border-top-width:1px; border-top-style:solid;
        border-bottom-width:1px; border-bottom-style:solid;
    }
    div.sequence-children{
            border-left-width:1px; border-left-style:dashed;
            margin-left:20px;

    }
    div.flow-children {
            border-left-width:1px; border-left-style:dotted;
        margin-left:20px;
    }
    div.tool{
            margin-left:20px;
    }
    div.params{
            margin-left:20px;
    }
    div.exec-record{
            border-top-width:1px; border-top-style:solid;
        padding-top:5px;
        margin-top:5px;
        margin-bottom:5px;
    }
    div.info, div.warn, div.error{
            margin-left:20px;
        font-size:smaller;
    }
    div.warn {
            boder-width:1px; border-color:orange; border-style:solid;
    }
    div.error {
            border-width:2px; border-color:red; border-style:solid;
    }
    span.RUNNING, span.INITIALIZING{color:green;}
    span.ERROR{color:red;}
    span.PENDING, span.COMPLETED{color:blue;}
    span.UNKNOWN{color:orange;}
   </style>
   <title><xsl:value-of select="wf:workflow/@name" /></title>
   </head>
   <body>
   <div class="header" >
   <xsl:apply-templates select="wf:workflow" />
   </div>
   <div class="body">
   <xsl:apply-templates select="wf:workflow/wf:sequence" />
   </div>
   </body>
  </html>
</xsl:template>



<xsl:template match="wf:workflow" >
 Name: <i><xsl:value-of select="@name" /></i><br />
 <xsl:apply-templates select="wf:Credentials" />
 <xsl:apply-templates select="wf:description" />
 <xsl:apply-templates select="er:job-execution-record" />
</xsl:template>


<xsl:template match="er:job-execution-record">
         JobURN: <i><xsl:value-of select="er:jobId" /></i><br />
        <xsl:call-template name="exec-record" />
</xsl:template>

<xsl:template match="wf:Credentials">
  User:<i><xsl:value-of select="normalize-space(creds:Account/creds:Name)"/>@<xsl:value-of select="normalize-space(creds:Account/creds:Community)"/></i>,
  Group: <i><xsl:value-of select="normalize-space(creds:Group/creds:Name)"/>@<xsl:value-of select="normalize-space(creds:Group/creds:Community)"/></i>
</xsl:template>

<xsl:template match="wf:sequence" name="sequence">
        <div class="block"><b>Sequence</b>
                <div class="sequence-children">
                <xsl:apply-templates />
                </div>
        </div>
</xsl:template>


<xsl:template match="wf:flow" name="flow">
        <div class="block"><b>Flow</b>
                <div class="flow-children">
                <xsl:apply-templates />
                </div>
        </div>
</xsl:template>

<xsl:template match="wf:step" name="step">
        <div class="step">
        <b>Step: </b> <i><xsl:value-of select="@name"/></i>,
        Join Condition: <i><xsl:value-of select="@joinCondition" /></i><br />
        <xsl:apply-templates select="wf:description" />
        <xsl:apply-templates select="wf:tool" />
        <xsl:apply-templates select="er:step-execution-record" />
        </div>
</xsl:template>

<xsl:template match="wf:description">
        <div class="description"><xsl:value-of select="."/></div>
</xsl:template>

<xsl:template match="wf:tool">
        <div class="tool">
        <b>Tool: </b> <i><xsl:value-of select="@name" /></i>,
        Interface: <xsl:value-of select="@interface" /><br />
                <div class="params">
                <i>Inputs</i><br />
                <xsl:apply-templates select="wf:input/wf:parameter" />
                <br /><i>Outputs</i><br />
                <xsl:apply-templates select="wf:output/wf:parameter" />
                </div>
        </div>
</xsl:template>


<xsl:template match="wf:parameter">
        <b><xsl:value-of select="@name"/></b> : <xsl:value-of select="@type" /> := <i><xsl:value-of select="pd:value" /></i><br />
</xsl:template>


<xsl:template match="er:step-execution-record" name="exec-record">
 <div class="exec-record">
         <b>Execution: </b>
 <span>
         <xsl:attribute name="class"><xsl:value-of select="@status"/></xsl:attribute>
         <xsl:value-of select="@status" />
 </span>
 <xsl:if test="@status != 'PENDING'">
         Start: <xsl:value-of select="@startTime" />
</xsl:if>
<xsl:if test="not (@status = 'PENDING' or @status ='INITIALIZING' or @status = 'RUNNING')">
         Finish: <xsl:value-of select="@finishTime" />
 </xsl:if>
 <xsl:apply-templates select="cea:message">
         <xsl:sort select="timestamp" />
 </xsl:apply-templates>
 </div>
</xsl:template>

<xsl:template match="cea:message">
        <div>
                <xsl:attribute name="class"><xsl:value-of select="normalize-space(cea:level)"/></xsl:attribute>
        <xsl:value-of select="cea:timestamp" />
        : <span>
                <xsl:attribute name="class"><xsl:value-of select="normalize-space(cea:phase)" /></xsl:attribute>
                <xsl:value-of select="cea:phase" />
        </span>
        : <xsl:value-of select="cea:source" />
        : <xsl:value-of select="cea:level" />
                <div class="description">
                        <xsl:value-of select="cea:content" />
                </div>
        </div>
</xsl:template>



<!-- work-arouond for xsi-types -->
<xsl:template match="wf:Activity">
        <xsl:choose>
                <xsl:when test="@xsi:type = 'step'">
                        <xsl:call-template name="step" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'flow'">
                        <xsl:call-template name="flow" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'sequence'">
                        <xsl:call-template name="sequence" />
                </xsl:when>
        </xsl:choose>
</xsl:template>

</xsl:stylesheet>