<?xml version="1.0" encoding="UTF-8"?>
<!--Designed and generated by Altova StyleVision Enterprise Edition 2008 rel. 2 sp1 - see http://www.altova.com/stylevision for more information.-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:n1="http://www.ivoa.net/xml/STC/stc-v1.30.xsd" xmlns:xdt="http://www.w3.org/2005/xpath-datatypes" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:altova="http://www.altova.com">
	<xsl:output version="4.0" method="html" indent="no" encoding="UTF-8" doctype-public="-//W3C//DTD HTML 4.0 Transitional//EN" doctype-system="http://www.w3.org/TR/html4/loose.dtd"/>
	<xsl:param name="SV_OutputFormat" select="'HTML'"/>
	<xsl:variable name="XML" select="/"/>
	<xsl:import-schema schema-location="http://hea-www.harvard.edu/~arots/nvometa/v1.30/stc-v1.30.xsd" namespace="http://www.ivoa.net/xml/STC/stc-v1.30.xsd"/>
	<xsl:template match="/">
    <!--
		<html>
			<head>
				<title/>
			</head>
			<body>
      -->
				<xsl:for-each select="$XML">
                <!-- 
					<span>
						<xsl:text>STC-S string(s):</xsl:text>
					</span>
					<br/>
                 -->
					<xsl:for-each select="n1:STCResourceProfile">
						<xsl:for-each select="n1:AstroCoordArea">
							<xsl:for-each select="n1:Position2VecInterval">
								<span>
									<xsl:text>PositionInterval </xsl:text>
								</span>
								<xsl:for-each select="@fill_factor">
									<span>
										<xsl:text>fillfactor </xsl:text>
									</span>
									<span>
										<xsl:value-of select="string(.)"/>
									</span>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="$XML">
									<xsl:for-each select="n1:STCResourceProfile">
										<xsl:for-each select="n1:AstroCoordSystem">
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:ICRS">
													<span>
														<xsl:text>ICRS </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK5">
													<span>
														<xsl:text>FK5 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK4">
													<span>
														<xsl:text>FK4 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_II">
													<span>
														<xsl:text>GALACTIC </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:ECLIPTIC">
													<span>
														<xsl:text>ECLIPTIC </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:GEO_C">
													<span>
														<xsl:text>GEO_C </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEO_D">
													<span>
														<xsl:text>GEO_D</xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEOCENTER">
													<span>
														<xsl:text>GEOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:BARYCENTER">
													<span>
														<xsl:text>BARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:HELIOCENTER">
													<span>
														<xsl:text>HELIOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_CENTER">
													<span>
														<xsl:text>GALACTIC_CENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:EMBARYCENTER">
													<span>
														<xsl:text>EMBARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:TOPOCENTER">
													<span>
														<xsl:text>TOPOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:SPHERICAL">
													<span>
														<xsl:text>SPHER2 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:UNITSPHERE">
													<span>
														<xsl:text>UNITSPHER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:CARTESIAN">
													<span>
														<xsl:text>CART2 </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="@xlink:href">
												<span>
													<xsl:value-of select="substring-before(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>&#160;</xsl:text>
												</span>
												<span>
													<xsl:value-of select="substring-after(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>CENTER </xsl:text>
												</span>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:LoLimit2Vec">
									<xsl:for-each select="n1:C1">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
									<span>
									</span>
									<xsl:for-each select="n1:C2">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:HiLimit2Vec">
									<xsl:for-each select="n1:C1">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
									<xsl:for-each select="n1:C2">
										<xsl:apply-templates/>
									</xsl:for-each>
								</xsl:for-each>
								<br/>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="n1:STCResourceProfile">
						<xsl:for-each select="n1:AstroCoordArea">
							<xsl:for-each select="n1:Circle">
								<span>
									<xsl:text>Circle </xsl:text>
								</span>
								<xsl:for-each select="@fill_factor">
									<span>
										<xsl:text>fillfactor </xsl:text>
									</span>
									<span>
										<xsl:value-of select="string(.)"/>
									</span>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="$XML">
									<xsl:for-each select="n1:STCResourceProfile">
										<xsl:for-each select="n1:AstroCoordSystem">
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:ICRS">
													<span>
														<xsl:text>ICRS </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK5">
													<span>
														<xsl:text>FK5 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK4">
													<span>
														<xsl:text>FK4 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_II">
													<span>
														<xsl:text>GALACTIC </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:ECLIPTIC">
													<span>
														<xsl:text>ECLIPTIC </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:GEO_C">
													<span>
														<xsl:text>GEO_C </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEO_D">
													<span>
														<xsl:text>GEO_D</xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEOCENTER">
													<span>
														<xsl:text>GEOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:BARYCENTER">
													<span>
														<xsl:text>BARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:HELIOCENTER">
													<span>
														<xsl:text>HELIOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_CENTER">
													<span>
														<xsl:text>GALACTIC_CENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:EMBARYCENTER">
													<span>
														<xsl:text>EMBARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:TOPOCENTER">
													<span>
														<xsl:text>TOPOCENTER </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="@xlink:href">
												<span>
													<xsl:value-of select="substring-before(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>&#160;</xsl:text>
												</span>
												<span>
													<xsl:value-of select="substring-after(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>CENTER </xsl:text>
												</span>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:Center">
									<xsl:for-each select="n1:C1">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
									<xsl:for-each select="n1:C2">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:Radius">
									<xsl:apply-templates/>
								</xsl:for-each>
								<br/>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="n1:STCResourceProfile">
						<xsl:for-each select="n1:AstroCoordArea">
							<xsl:for-each select="n1:Polygon">
								<span>
									<xsl:text>Polygon </xsl:text>
								</span>
								<xsl:for-each select="@fill_factor">
									<span>
										<xsl:text>fillfactor </xsl:text>
									</span>
									<span>
										<xsl:value-of select="string(.)"/>
									</span>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="$XML">
									<xsl:for-each select="n1:STCResourceProfile">
										<xsl:for-each select="n1:AstroCoordSystem">
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:ICRS">
													<span>
														<xsl:text>ICRS </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK5">
													<span>
														<xsl:text>FK5 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK4">
													<span>
														<xsl:text>FK4 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_II">
													<span>
														<xsl:text>GALACTIC </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:ECLIPTIC">
													<span>
														<xsl:text>ECLIPTIC </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:GEO_C">
													<span>
														<xsl:text>GEO_C </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEO_D">
													<span>
														<xsl:text>GEO_D</xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEOCENTER">
													<span>
														<xsl:text>GEOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:BARYCENTER">
													<span>
														<xsl:text>BARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:HELIOCENTER">
													<span>
														<xsl:text>HELIOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_CENTER">
													<span>
														<xsl:text>GALACTIC_CENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:EMBARYCENTER">
													<span>
														<xsl:text>EMBARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:TOPOCENTER">
													<span>
														<xsl:text>TOPOCENTER </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="@xlink:href">
												<span>
													<xsl:value-of select="substring-before(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>&#160;</xsl:text>
												</span>
												<span>
													<xsl:value-of select="substring-after(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>CENTER </xsl:text>
												</span>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:Vertex">
									<xsl:for-each select="n1:Position">
										<xsl:for-each select="n1:C1">
											<xsl:apply-templates/>
											<span>
												<xsl:text>&#160;</xsl:text>
											</span>
										</xsl:for-each>
										<xsl:for-each select="n1:C2">
											<xsl:apply-templates/>
											<span>
												<xsl:text>&#160;</xsl:text>
											</span>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
								<br/>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="n1:STCResourceProfile">
						<xsl:for-each select="n1:AstroCoordArea">
							<xsl:for-each select="n1:AllSky">
								<span>
									<xsl:text>AllSky </xsl:text>
								</span>
								<xsl:for-each select="@fill_factor">
									<span>
										<xsl:text>fillfactor </xsl:text>
									</span>
									<span>
										<xsl:value-of select="string(.)"/>
									</span>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="$XML">
									<xsl:for-each select="n1:STCResourceProfile">
										<xsl:for-each select="n1:AstroCoordSystem">
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:ICRS">
													<span>
														<xsl:text>ICRS </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK5">
													<span>
														<xsl:text>FK5 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK4">
													<span>
														<xsl:text>FK4 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_II">
													<span>
														<xsl:text>GALACTIC </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:ECLIPTIC">
													<span>
														<xsl:text>ECLIPTIC </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:GEO_C">
													<span>
														<xsl:text>GEO_C </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEO_D">
													<span>
														<xsl:text>GEO_D</xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEOCENTER">
													<span>
														<xsl:text>GEOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:BARYCENTER">
													<span>
														<xsl:text>BARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:HELIOCENTER">
													<span>
														<xsl:text>HELIOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_CENTER">
													<span>
														<xsl:text>GALACTIC_CENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:EMBARYCENTER">
													<span>
														<xsl:text>EMBARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:TOPOCENTER">
													<span>
														<xsl:text>TOPOCENTER </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="@xlink:href">
												<span>
													<xsl:value-of select="substring-before(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>&#160;</xsl:text>
												</span>
												<span>
													<xsl:value-of select="substring-after(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>CENTER </xsl:text>
												</span>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
								<br/>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="n1:STCResourceProfile">
						<xsl:for-each select="n1:AstroCoordArea">
							<xsl:for-each select="n1:Box">
								<span>
									<xsl:text>Box </xsl:text>
								</span>
								<xsl:for-each select="@fill_factor">
									<span>
										<xsl:text>fillfactor </xsl:text>
									</span>
									<span>
										<xsl:value-of select="string(.)"/>
									</span>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="$XML">
									<xsl:for-each select="n1:STCResourceProfile">
										<xsl:for-each select="n1:AstroCoordSystem">
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:ICRS">
													<span>
														<xsl:text>ICRS </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK5">
													<span>
														<xsl:text>FK5 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK4">
													<span>
														<xsl:text>FK4 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_II">
													<span>
														<xsl:text>GALACTIC </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:ECLIPTIC">
													<span>
														<xsl:text>ECLIPTIC </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:GEO_C">
													<span>
														<xsl:text>GEO_C </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEO_D">
													<span>
														<xsl:text>GEO_D</xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEOCENTER">
													<span>
														<xsl:text>GEOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:BARYCENTER">
													<span>
														<xsl:text>BARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:HELIOCENTER">
													<span>
														<xsl:text>HELIOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_CENTER">
													<span>
														<xsl:text>GALACTIC_CENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:EMBARYCENTER">
													<span>
														<xsl:text>EMBARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:TOPOCENTER">
													<span>
														<xsl:text>TOPOCENTER </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="@xlink:href">
												<span>
													<xsl:value-of select="substring-before(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>&#160;</xsl:text>
												</span>
												<span>
													<xsl:value-of select="substring-after(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>CENTER </xsl:text>
												</span>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:Center">
									<xsl:for-each select="n1:C1">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
									<xsl:for-each select="n1:C2">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:Size">
									<xsl:for-each select="n1:C1">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
									<xsl:for-each select="n1:C2">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
								</xsl:for-each>
								<br/>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="n1:STCResourceProfile">
						<xsl:for-each select="n1:AstroCoordArea">
							<xsl:for-each select="n1:Convex">
								<span>
									<xsl:text>Convex </xsl:text>
								</span>
								<xsl:for-each select="@fill_factor">
									<span>
										<xsl:text>fillfactor </xsl:text>
									</span>
									<span>
										<xsl:value-of select="string(.)"/>
									</span>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="$XML">
									<xsl:for-each select="n1:STCResourceProfile">
										<xsl:for-each select="n1:AstroCoordSystem">
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:ICRS">
													<span>
														<xsl:text>ICRS </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK5">
													<span>
														<xsl:text>FK5 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK4">
													<span>
														<xsl:text>FK4 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_II">
													<span>
														<xsl:text>GALACTIC </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:ECLIPTIC">
													<span>
														<xsl:text>ECLIPTIC </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:GEO_C">
													<span>
														<xsl:text>GEO_C </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEO_D">
													<span>
														<xsl:text>GEO_D</xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEOCENTER">
													<span>
														<xsl:text>GEOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:BARYCENTER">
													<span>
														<xsl:text>BARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:HELIOCENTER">
													<span>
														<xsl:text>HELIOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_CENTER">
													<span>
														<xsl:text>GALACTIC_CENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:EMBARYCENTER">
													<span>
														<xsl:text>EMBARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:TOPOCENTER">
													<span>
														<xsl:text>TOPOCENTER </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="@xlink:href">
												<span>
													<xsl:value-of select="substring-before(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>&#160;</xsl:text>
												</span>
												<span>
													<xsl:value-of select="substring-after(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>CENTER </xsl:text>
												</span>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:Halfspace">
									<xsl:for-each select="n1:Vector">
										<xsl:for-each select="n1:C1">
											<xsl:apply-templates/>
											<span>
												<xsl:text>&#160;</xsl:text>
											</span>
										</xsl:for-each>
										<xsl:for-each select="n1:C2">
											<xsl:apply-templates/>
											<span>
												<xsl:text>&#160;</xsl:text>
											</span>
										</xsl:for-each>
										<xsl:for-each select="n1:C3">
											<xsl:apply-templates/>
											<span>
												<xsl:text>&#160;</xsl:text>
											</span>
										</xsl:for-each>
									</xsl:for-each>
									<xsl:for-each select="n1:Offset">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
								</xsl:for-each>
								<br/>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
					<xsl:for-each select="n1:STCResourceProfile">
						<xsl:for-each select="n1:AstroCoordArea">
							<xsl:for-each select="n1:Ellipse">
								<span>
									<xsl:text>Ellipse </xsl:text>
								</span>
								<xsl:for-each select="@fill_factor">
									<span>
										<xsl:text>fillfactor </xsl:text>
									</span>
									<span>
										<xsl:value-of select="string(.)"/>
									</span>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="$XML">
									<xsl:for-each select="n1:STCResourceProfile">
										<xsl:for-each select="n1:AstroCoordSystem">
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:ICRS">
													<span>
														<xsl:text>ICRS </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK5">
													<span>
														<xsl:text>FK5 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:FK4">
													<span>
														<xsl:text>FK4 </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_II">
													<span>
														<xsl:text>GALACTIC </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:ECLIPTIC">
													<span>
														<xsl:text>ECLIPTIC </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="n1:SpaceFrame">
												<xsl:for-each select="n1:GEO_C">
													<span>
														<xsl:text>GEO_C </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEO_D">
													<span>
														<xsl:text>GEO_D</xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GEOCENTER">
													<span>
														<xsl:text>GEOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:BARYCENTER">
													<span>
														<xsl:text>BARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:HELIOCENTER">
													<span>
														<xsl:text>HELIOCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:GALACTIC_CENTER">
													<span>
														<xsl:text>GALACTIC_CENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:EMBARYCENTER">
													<span>
														<xsl:text>EMBARYCENTER </xsl:text>
													</span>
												</xsl:for-each>
												<xsl:for-each select="n1:TOPOCENTER">
													<span>
														<xsl:text>TOPOCENTER </xsl:text>
													</span>
												</xsl:for-each>
											</xsl:for-each>
											<xsl:for-each select="@xlink:href">
												<span>
													<xsl:value-of select="substring-before(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>&#160;</xsl:text>
												</span>
												<span>
													<xsl:value-of select="substring-after(  substring-after( . , &quot;-&quot; )  , &quot;-&quot; )"/>
												</span>
												<span>
													<xsl:text>CENTER </xsl:text>
												</span>
											</xsl:for-each>
										</xsl:for-each>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:Center">
									<xsl:for-each select="n1:C1">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
									<xsl:for-each select="n1:C2">
										<xsl:apply-templates/>
										<span>
											<xsl:text>&#160;</xsl:text>
										</span>
									</xsl:for-each>
								</xsl:for-each>
								<xsl:for-each select="n1:SemiMajorAxis">
									<xsl:apply-templates/>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="n1:SemiMinorAxis">
									<xsl:apply-templates/>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								</xsl:for-each>
								<xsl:for-each select="n1:PosAngle">
									<xsl:apply-templates/>
									<span>
										<xsl:text>&#160;</xsl:text>
									</span>
								<br/>
								</xsl:for-each>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</xsl:for-each>
                <!-- 
			</body>
		</html>
                 -->
	</xsl:template>
</xsl:stylesheet>