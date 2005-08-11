<?xml version="1.0" ?>
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:wf="http://www.astrogrid.org/schema/AGWorkflow/v1"
  xmlns:er="http://www.astrogrid.org/schema/ExecutionRecord/v1"
  xmlns:creds="http://www.astrogrid.org/schema/Credentials/v1"
  xmlns:pd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
  xmlns:cea="http://www.astrogrid.org/schema/CEATypes/v1"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 >

<xsl:template match="/">
  <html>
   <head>
   <style type="text/css">
    div.header{
        font-size:larger;
        background-color:lightblue;
    }
    div.body{
            border-top-width:5px;
        border-top-color:lightblue;
        border-top-style:solid;
        margin-top:10px;
        padding:5px;
    }

    div.description{
        font-style:italic;
        padding:5px;
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
            margin:2px; background-color:lightblue;
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
    span.KEY{text-decoration:underline; padding-left:5px;padding-right:5px;}
    span.VALUE{font-family:monospace;}
    span.ACTIVITY{font-weight:bold;}
   </style>
   <title>Transcript of <xsl:value-of select="normalize-space(wf:workflow/@name)" /></title>
   </head>
   <body>
   <h1>Workflow Transcript</h1>
   <h2>Summary</h2>
   <xsl:apply-templates select="wf:workflow" />
   <div class="body">
   <h2>Activity Details</h2>
   <xsl:apply-templates select="wf:workflow/wf:sequence" />
   </div>
   </body>
  </html>
</xsl:template>



<xsl:template match="wf:workflow" >

   <div class="header" >

 <nobr><span class="KEY">Name</span> <span class="VALUE"><xsl:value-of select="@name" /></span></nobr><br />
 <xsl:apply-templates select="wf:Credentials" />
 <xsl:apply-templates select="wf:description" />

 <xsl:apply-templates select="er:job-execution-record" />
  </div>
</xsl:template>


<xsl:template match="er:job-execution-record">
         <nobr><span class="KEY">JobURN</span> <span class="VALUE"><xsl:value-of select="normalize-space(er:jobId)" /></span></nobr><br />
        <xsl:call-template name="exec-record" />
</xsl:template>

<xsl:template match="wf:Credentials">
  <nobr>
  <span class='KEY'>User</span> <span class='VALUE'><xsl:value-of select="normalize-space(creds:Account/creds:Name)"/>@<xsl:value-of select="normalize-space(creds:Account/creds:Community)"/></span>
  <span class='KEY'>Group</span> <span class='VALUE'><xsl:value-of select="normalize-space(creds:Group/creds:Name)"/>@<xsl:value-of select="normalize-space(creds:Group/creds:Community)"/></span>
  </nobr>
</xsl:template>

<xsl:template match="wf:sequence" name="sequence">
        <div class="block"><span class='ACTIVITY'>Sequence</span>
                <div class="sequence-children">
                <xsl:apply-templates />
                </div>
        </div>
</xsl:template>


<xsl:template match="wf:flow" name="flow">
        <div class="block"><span class='ACTIVITY'>Flow</span>
                <div class="flow-children">
                <xsl:apply-templates />
                </div>
        </div>
</xsl:template>

<xsl:template match="wf:step" name="step">
        <div class="step">
        <nobr>
        <span class='ACTIVITY'>Step:</span> <span class='KEY'>Name</span> <span class='VALUE'><xsl:value-of select="@name"/></span>,
                <span class='KEY'>Result Var</span> <span class='VALUE'><xsl:value-of select="@result-var"/></span>
        </nobr><br />
                <xsl:apply-templates select="wf:description" />
                <xsl:apply-templates select="wf:tool" />
                <xsl:apply-templates select="er:step-execution-record"/>
        </div>
</xsl:template>

<xsl:template match="wf:script" name="script">
        <div class="step">
                <span class='ACTIVITY'>Script</span><br/>
                <xsl:apply-templates select="wf:description" />
                <pre>
                <xsl:value-of select="wf:body" />
                </pre><br />
                <xsl:apply-templates select="er:step-execution-record"/>
        </div>
</xsl:template>

<xsl:template match="wf:if" name="if">
        <div class="block">
                <nobr><span class='ACTIVITY'>If</span> <span class='VALUE'><xsl:value-of select="@test" /></span></nobr><br />
                <span class='ACTIVITY'>>Then</span>
                <div class="sequence-children">
                        <xsl:apply-templates select="./wf:then/*"/>
                </div>
                <span class='ACTIVITY'>Else</span>
                <div class="sequence-children">
                        <xsl:apply-templates select="./wf:else/*"/>
                </div>
        </div>
</xsl:template>

<xsl:template match="wf:for" name="for">
        <div class="block">
        <span class='ACTIVITY'>For</span>
                <nobr><span class='VALUE'><xsl:value-of select="normalize-space(@var)" /></span> in <span class='VALUE'><xsl:value-of select="@items" /></span></nobr>
                <div class="sequence-children">
                        <xsl:apply-templates />
                </div>
        </div>
</xsl:template>

<xsl:template match="wf:while" name="while">
        <div class="block">
                <span class='ACTIVITY'>While</span> <span class='VALUE'><xsl:value-of select="@test" /></span>
                <div class="sequence-children">
                        <xsl:apply-templates />
                </div>
        </div>
</xsl:template>

<xsl:template match="wf:parfor" name="parfor">
                <div class="block">
                        <span class='ACTIVITY'>ParFor</span>
                <nobr><span class='VALUE'><xsl:value-of select="normalize-space(@var)" /></span> in <span class='VALUE'><xsl:value-of select="@values" /></span></nobr>
                <div class="sequence-children">
                        <xsl:apply-templates />
                </div>
        </div>
</xsl:template>

<xsl:template match="wf:set" name="set">
        <div class="step">
                <span class='ACTIVITY'>Set</span>
                <nobr><span class='VALUE'><xsl:value-of select="normalize-space(@var)"/></span> := <span class='VALUE'><xsl:value-of select="@value" /></span></nobr>
        </div>
</xsl:template>

<xsl:template match="wf:unset" name="unset">
        <div class="step">
                <nobr><span class='ACTIVITY'>Unset</span> <span class='VALUE'><xsl:value-of select="normalize-space(@var)" /></span></nobr>
        </div>
</xsl:template>

<xsl:template match="wf:scope" name="scope">
        <div class="block">
        <span class='ACTIVITY'>New Scope</span>
                <div>
                <xsl:apply-templates />
                </div>
        </div>
</xsl:template>


<xsl:template match="wf:description">
        <div class="description"><xsl:value-of select="."/></div>
</xsl:template>

<xsl:template match="wf:tool">
        <div class="tool">
        <nobr><span class='KEY'>Tool</span> <span class='VALUE'><xsl:value-of select="@name" /></span>
        <span class='KEY'>Interface</span> <span class='VALUE'><xsl:value-of select="@interface" /></span></nobr><br />
                <div class="params">
                <i>Inputs</i><br />
                <xsl:apply-templates select="wf:input/wf:parameter" />
                <i>Outputs</i><br />
                <xsl:apply-templates select="wf:output/wf:parameter" />
                </div>
        </div>
</xsl:template>


<xsl:template match="wf:parameter">
        <nobr>
        <span class='KEY'><xsl:value-of select="normalize-space(@name)"/></span> := <xsl:if test="@indirect = 'true'"><b>Remote Reference</b></xsl:if> <span class='VALUE'><xsl:value-of select="pd:value" /></span></nobr><br />
</xsl:template>


<xsl:template match="er:step-execution-record" name="exec-record">
 <div class="exec-record">
 <nobr>
         <span class='KEY'>Execution</span>
 <span>
         <xsl:attribute name="class"><xsl:value-of select="@status"/></xsl:attribute>
         <xsl:value-of select="@status" />
 </span>
 <xsl:if test="@status != 'PENDING'">
         <span class='KEY'>Start</span> <span class='VALUE'><xsl:value-of select="@startTime" /></span>
</xsl:if>
<xsl:if test="not (@status = 'PENDING' or @status ='INITIALIZING' or @status = 'RUNNING')">
         <span class='KEY'>Finish</span> <span class='VALUE'><xsl:value-of select="@finishTime" /></span>
 </xsl:if>
 </nobr>
 <!-- could tabulate these .. -->
 <xsl:apply-templates select="cea:message">
         <xsl:sort select="timestamp" />
 </xsl:apply-templates>
 </div>
</xsl:template>

<xsl:template match="cea:message">
        <div>
                <xsl:attribute name="class"><xsl:value-of select="normalize-space(cea:level)"/></xsl:attribute>

        <span class='ACTIVITY'>Message</span><br />
         <nobr>
         <span style='font-stretch:condensed;'>
         <span class='KEY'>Time</span> <span class='VALUE'><xsl:value-of select="normalize-space(cea:timestamp)" /></span>

        <span class='KEY'>Phase</span>
        <span>
                <xsl:attribute name="class"><xsl:value-of select="normalize-space(cea:phase)" /></xsl:attribute>
                <xsl:value-of select="normalize-space(cea:phase)" />
        </span>

        <span class='KEY'>Source</span> <span class='VALUE'><xsl:value-of select="normalize-space(cea:source)" /></span>
        </span>
        </nobr>
                <div class="message-content">
                                        <pre>
                        <xsl:value-of select="cea:content" />
                                        </pre>
                                        <br />
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
                <xsl:when test="@xsi:type = 'for'">
                        <xsl:call-template name="for" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'parfor'">
                        <xsl:call-template name="parfor" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'while'">
                        <xsl:call-template name="while" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'if'">
                        <xsl:call-template name="if" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'script'">
                        <xsl:call-template name="script" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'set'">
                        <xsl:call-template name="set" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'unset'">
                        <xsl:call-template name="unset" />
                </xsl:when>
                <xsl:when test="@xsi:type = 'scope'">
                        <xsl:call-template name="scope" />
                </xsl:when>
        </xsl:choose>
</xsl:template>

</xsl:stylesheet>