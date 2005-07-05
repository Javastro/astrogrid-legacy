<?xml version="1.0"?>
<!-- This script will convert a wsdl definition into a CEA definition for use in AG

This is currently rather limited in scope in that it will really only convert wrapped document literal (i.e the .Net rpc emulation) 
into a sensible CEA defintion

A better implementation would probably be to do this in java and read the wsdl in conjunction with the registry reference that was used to "discover" the WSDL

also this is being a bit loose with namespaces...
-->
<xsl:stylesheet version="1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"	
        xmlns:mime="http://schemas.xmlsoap.org/wsdl/mime/"
        xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/"
        xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
        xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9" 
	xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.1"
	xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1" 
	xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"
	>

<xsl:output method="xml" indent="yes" />
<xsl:template match="/">
<vr:VOResource>	
<xsl:apply-templates select="//wsdl:portType"/>
</vr:VOResource>
</xsl:template>
<!-- ports are to be equivalent to "applictions" -->
<xsl:template match="wsdl:portType">
<vr:Resource xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" status="active" xsi:type="cea:CeaApplicationType">
<vr:Identifier>
<vr:AuthorityID>org.astrogrid</vr:AuthorityID>
<vr:ResourceKey><xsl:value-of select="@name"/></vr:ResourceKey>
</vr:Identifier>
<vr:Title><xsl:value-of select="@name"/></vr:Title>
<vr:ShortName><xsl:value-of select="@name"/></vr:ShortName>
<vr:Summary>
<vr:Description><xsl:value-of select="wsdl:documentation"/></vr:Description>

<vr:ReferenceURL>TBD - not sure what is best here</vr:ReferenceURL>
</vr:Summary>
<vr:Type>Other</vr:Type>
<xsl:comment>
this curation information needs to be standardized 
</xsl:comment>
<vr:Curation>
<vr:Publisher>
<vr:Title>Astrogrid</vr:Title>
<vr:Description>blah blab</vr:Description>
<vr:ReferenceURL>http://www.astrogrid.org/</vr:ReferenceURL>
</vr:Publisher>
<vr:Contact>
<vr:Name>Paul Harrison</vr:Name>
<vr:Email>pah@jb.man.ac.uk</vr:Email>
</vr:Contact>
<vr:Date role="representative">2004-03-26</vr:Date>
<vr:Creator>
<vr:Name>Astrogrid</vr:Name>
<vr:Logo>??</vr:Logo>
</vr:Creator>
<vr:Version>Iteration 5</vr:Version>
</vr:Curation>
<vr:Subject>???</vr:Subject>
<cea:ApplicationDefinition>
<cea:Parameters>

</cea:Parameters>
<cea:Interfaces>
<xsl:apply-templates select="wsdl:operation"></xsl:apply-templates>
</cea:Interfaces>
</cea:ApplicationDefinition>
</vr:Resource>
<!--
<cea:ApplicationDefinition>

<cea:Parameters>
<cea:ParameterDefinition name="SPECTRA" type="text">
<ceapd:UI_Name>Spectra at Ages</ceapd:UI_Name>
<ceapd:UI_Description> Spectra (lambda&amp;amp; fluxes at chosen ages).</ceapd:UI_Description>
<ceapd:UCD/>
<ceapd:DefaultValue/>
<ceapd:Units/>
</cea:ParameterDefinition>
</cea:Parameters>
<cea:Interfaces>
<ceab:Interface name="simple">
<ceab:input>
<ceab:pref ref="CONFIGFILE" minoccurs="1" maxoccurs="1"/>
</ceab:input>
<ceab:output>
<ceab:pref ref="SPECTRA" minoccurs="1" maxoccurs="1"/>
<ceab:pref ref="OTHERMAGS" minoccurs="1" maxoccurs="1"/>
<ceab:pref ref="MAGZF1" minoccurs="1" maxoccurs="1"/>
<ceab:pref ref="ABMAG" minoccurs="1" maxoccurs="1"/>
<ceab:pref ref="VEGAMAGS" minoccurs="1" maxoccurs="1"/>
</ceab:output>

</ceab:Interface>
</cea:Interfaces>

</cea:ApplicationDefinition>
</vr:Resource>
</vr:VOResource>
-->
</xsl:template>
<xsl:template match="wsdl:operation">

<xsl:element name="ceab:Interface">
<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
</xsl:element>
<xsl:apply-templates></xsl:apply-templates>
</xsl:template>


<xsl:template match="wsdl:input">
<ceab:input>
<message>
<xsl:value-of select="substring-after(./@message, ':')"/>
<xsl:apply-templates select="preceding::wsdl:message[substring-after(current()/@message, ':') = @name]"/>
</message>
</ceab:input>
</xsl:template>
<xsl:template match="wsdl:output">
<ceab:output>
</ceab:output>
</xsl:template>

<xsl:template match="wsdl:message">
<xsl:apply-templates></xsl:apply-templates>
</xsl:template>
<xsl:template match="wsdl:part">
<xsl:for-each select="preceding::wsdl:types/xsd:schema/xsd:element[substring-after(current()/@element, ':') = @name]/xsd:complexType/xsd:sequence/xsd:element">
<xsl:element name="pref"><xsl:attribute name="ref"><xsl:value-of select="@name"/></xsl:attribute></xsl:element>
</xsl:for-each>

</xsl:template>

<!-- copy-all template - matches everything else 
<xsl:template match="node()|@*" >
        <xsl:copy> 
                <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>node()
</xsl:template>
-->

</xsl:stylesheet>
