<?xml version="1.0"?>
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:agp="http://www.astrogrid.org/portal">
  <xsl:output omit-xml-declaration="yes"/>
  <xsl:strip-space elements="agp:menu agp:item"/>

  <!--
    * Main menu definition.
    *
    * The only menu definition allowed in a file is the main menu
    * definition.
  -->
  <xsl:template match="/agp:menu-def">
    /*
     * Main Menu.
     */
    domMenu_data.setItem(
      '<xsl:value-of select="@name"/>',
      new domMenu_Hash(
        <xsl:apply-templates select="/agp:menu-def/agp:menu | /agp:menu-def/agp:menu-def/agp:menu"/>));
      
    <xsl:apply-templates select="/agp:menu-def/agp:settings"/>
  </xsl:template>
  
  <!--
    * Main menu settings.
    *
    * The only menu settings allowed in a file are the main menu settings.
  -->
  <xsl:template match="agp:settings">
    /*
     * Main Menu Settings.
     */
    domMenu_settings.setItem(
      '<xsl:value-of select="../@name"/>',
      new domMenu_Hash(
      <xsl:apply-templates select="@*"/>));
  </xsl:template>

  <!--
    * If more menu definitions appear, simply process children.
  -->
  <xsl:template match="agp:menu-def">
    <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="agp:menu">
    /*
     * Menu.
     */
    <xsl:call-template name="common"/> , <xsl:apply-templates/>)
  </xsl:template>

  <xsl:template match="agp:item">
    /*
     * Menu Item.
     */
    <xsl:call-template name="common"/>)
  </xsl:template>

  <xsl:template name="common">
    <xsl:if test="position() != 1">,</xsl:if>
    <xsl:value-of select="position()"/> , new domMenu_Hash(
      <xsl:apply-templates select="@*"/>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:if test="position() != 1"> , </xsl:if>

    <xsl:if test="name() = 'contents'">
        'contents', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'contents-hover'">
        'contentsHover', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'icon'">
        'icon', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'uri'">
        'uri', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'target'">
        'target', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'status-text'">
        'statusText', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'id'">
        'id', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'class'">
        'className', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'style'">
        'style', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'open-event'">
        'domMenu_openEvent', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'close-event'">
        'domMenu_closeEvent', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'resolve-event'">
        'domMenu_resolveLink', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'menubar-class'">
        'menuBarClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'menu-element-class'">
        'menuElementClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'menu-element-hover-class'">
        'menuElementHoverClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'menu-element-active-class'">
        'menuElementActiveClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'submenu-bar-class'">
        'subMenuBarClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'submenu-element-class'">
        'subMenuElementClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'submenu-element-hover-class'">
        'subMenuElementHoverClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'submenu-element-active-class'">
        'subMenuElementActiveClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'submenu-element-heading-class'">
        'subMenuElementHeadingClass', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'menubar-width'">
        'menuBarWidth', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'submenu-min-width'">
        'subMenuMinWidth', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'distribute-space'">
        'distributeSpace', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'axis'">
        'axis', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'vertical-expand'">
        'verticalExpand', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'horizontal-expand'">
        'horizontalExpand', '<xsl:value-of select="."/>'
    </xsl:if>
    <xsl:if test="name() = 'submenu-width-correction'">
        'subMenuWidthCorrection', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'vertical-submenu-offset-x'">
        'verticalSubMenuOffsetX', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'vertical-submenu-offset-y'">
        'verticalSubMenuOffsetY', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'horizontal-submenu-offset-x'">
        'horizontalSubMenuOffsetX', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'horizontal-submenu-offset-y'">
        'horizontalSubMenuOffsetY', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'screen-padding'">
        'screenPadding', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'open-mouseover-delay'">
        'openMouseoverMenuDelay', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'open-mousedown-delay'">
        'openMousedownMenuDelay', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'close-mouseout-delay'">
        'closeMouseoutMenuDelay', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'close-click-menu-delay'">
        'closeClickMenuDelay', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'open-mouseover-submenu-delay'">
        'openMouseoverSubMenuDelay', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'open-click-submenu-delay'">
        'openClickSubMenuDelay', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'close-mouseout-submenu-delay'">
        'closeMouseoutSubMenuDelay', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'close-click-submenu-delay'">
        'closeClickSubMenuDelay', <xsl:value-of select="."/>
    </xsl:if>
    <xsl:if test="name() = 'z-index'">
        'baseZIndex', <xsl:value-of select="."/>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
