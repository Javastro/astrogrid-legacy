<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
	xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.4"
	xmlns:exist="http://exist.sourceforge.net/NS/exist"
	xmlns:util="http://apache.org/xsp/util/2.0">

  <xsl:template match="agVaribleDisplayFrame">
	<agVaribleDisplayFrame><xsl:apply-templates/></agVaribleDisplayFrame>
  </xsl:template>

  <xsl:template match="resultsFromRegistry">
<resultsFromRegistry>
	<xsl:apply-templates/>
</resultsFromRegistry>
  </xsl:template>

  <xsl:template match="vr:VODescription">
	<agVODescription><xsl:apply-templates/></agVODescription>
  </xsl:template>

  <xsl:template match="vr:Description">
	<agDescription><xsl:apply-templates/></agDescription>
  </xsl:template>

  <xsl:template match="vs:Description">
	<agDescription><xsl:apply-templates/></agDescription>
  </xsl:template>

  <xsl:template match="Description">
	<agDescription><xsl:apply-templates/></agDescription>
  </xsl:template>

  <xsl:template match="description">
	<agDescription><xsl:apply-templates/></agDescription>
  </xsl:template>

  <xsl:template match="vr:description">
	<agDescription><xsl:apply-templates/></agDescription>
  </xsl:template>

  <xsl:template match="vs:description">
	<agDescription><xsl:apply-templates/></agDescription>
  </xsl:template>

  <xsl:template match="vr:Resource">
	<agResource><xsl:apply-templates/></agResource>
  </xsl:template>

  <xsl:template match="vs:Resource">
	<agResource><xsl:apply-templates/></agResource>
  </xsl:template>

  <xsl:template match="Resource">
	<agResource><xsl:apply-templates/></agResource>
  </xsl:template>

  <xsl:template match="vr:Title">
	<agTitle><xsl:apply-templates/></agTitle>
  </xsl:template>

  <xsl:template match="vs:Title">
	<agTitle><xsl:apply-templates/></agTitle>
  </xsl:template>

  <xsl:template match="Title">
	<agTitle><xsl:apply-templates/></agTitle>
  </xsl:template>

  <xsl:template match="vr:Table">
	<agTable><xsl:apply-templates/></agTable>
  </xsl:template>

  <xsl:template match="vs:Table">
	<agTable><xsl:apply-templates/></agTable>
  </xsl:template>

  <xsl:template match="vs:Name">
	<agName><xsl:apply-templates/></agName>
  </xsl:template>

  <xsl:template match="vr:Name">
	<agName><xsl:apply-templates/></agName>
  </xsl:template>

  <xsl:template match="Name">
	<agName><xsl:apply-templates/></agName>
  </xsl:template>

  <xsl:template match="vr:name">
	<agName><xsl:apply-templates/></agName>
  </xsl:template>

  <xsl:template match="vs:name">
	<agName><xsl:apply-templates/></agName>
  </xsl:template>

  <xsl:template match="name">
	<agName><xsl:apply-templates/></agName>
  </xsl:template>

  <xsl:template match="vr:Column">
	<agColumn><xsl:apply-templates/></agColumn>
  </xsl:template>

  <xsl:template match="vs:Column">
	<agColumn><xsl:apply-templates/></agColumn>
  </xsl:template>

  <xsl:template match="vr:Unit">
	<agUnit><xsl:apply-templates/></agUnit>
  </xsl:template>

  <xsl:template match="vs:Unit">
	<agUnit><xsl:apply-templates/></agUnit>
  </xsl:template>

  <xsl:template match="vr:unit">
	<agUnit><xsl:apply-templates/></agUnit>
  </xsl:template>

  <xsl:template match="vs:unit">
	<agUnit><xsl:apply-templates/></agUnit>
  </xsl:template>

  <xsl:template match="vr:UCD">
	<agUCD><xsl:apply-templates/></agUCD>
  </xsl:template>

  <xsl:template match="vs:UCD">
	<agUCD><xsl:apply-templates/></agUCD>
  </xsl:template>

  <xsl:template match="vr:ucd">
	<agUCD><xsl:apply-templates/></agUCD>
  </xsl:template>

  <xsl:template match="ucd">
	<agUCD><xsl:apply-templates/></agUCD>
  </xsl:template>

  <xsl:template match="UCD">
	<agUCD><xsl:apply-templates/></agUCD>
  </xsl:template>

  <xsl:template match="UCDs">
	<agUCD><xsl:apply-templates/></agUCD>
  </xsl:template>

  <xsl:template match="Waveband">
	<agWaveband><xsl:apply-templates/></agWaveband>
  </xsl:template>

  <xsl:template match="vr:Waveband">
	<agWaveband><xsl:apply-templates/></agWaveband>
  </xsl:template>

  <xsl:template match="vs:Waveband">
	<agWaveband><xsl:apply-templates/></agWaveband>
  </xsl:template>


  <xsl:template match="Action">
  </xsl:template>

<!--	leave untouched -->
  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>


  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
