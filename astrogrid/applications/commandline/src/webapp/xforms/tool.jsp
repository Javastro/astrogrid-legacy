<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html xmlns="http://www.w3.org/1999/xhtml"
xmlns:ev="http://www.w3.org/2001/xml-events"
		xmlns:xf="http://www.w3.org/2002/xforms"
		xmlns:xhtml="http://www.w3.org/1999/xhtml"
		xmlns:xsd="http://www.w3.org/2001/XMLSchema"
	    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns:ceat="http://www.ivoa.net/xml/CEA/types/v1.1"
		xmlns:cea="http://www.ivoa.net/xml/CEA/v1.0"
		xsi:schemaLocation="http://www.w3.org/2002/xforms http://www.w3.org/MarkUp/Forms/2002/XForms-Schema.xsd">
	<head>
		<title>Tool submission</title>
		<xf:model>
			<xf:submission id="debug" method="post"
					action="{$contextroot}/chiba-resources/jsp/debug-instance.jsp"/>
		    <xf:submission id="uws" method="post"
		            action="{$contextroot}/uws/jobs" />
		    <xf:submission id="uwsauto" method="post"
		            action="{$contextroot}/uws/jobs?AUTORUN" />
			<xf:instance id="TOOL" xmlns="" src="{$contextroot}/uws/reg/app/${param['application']}/tool?intf=${param['interface'] }">
			</xf:instance>
			<xf:instance id="CONTROL" xmlns="">
				<data id="silly">
					<inside id="daft" />
				</data>
			</xf:instance>
			<xf:instance id="APPDESC" src="{$contextroot}/uws/reg/app/${param['application']}/reschiba" xmlns=""/>
		<!-- bindings - these do not work with namespaces-->
			<xf:bind nodeset="instance('TOOL')/ceat:input/ceat:parameter" id="inpars">
				<xf:bind id="inparId" nodeset="@name" />
				<xf:bind nodeset="@indirect" type="xsd:boolean"  />
				
			</xf:bind>
			<xf:bind nodeset="instance('TOOL')/ceat:output/ceat:parameter" id="outpars" >
			  				<xf:bind nodeset="@indirect" type="xsd:boolean"  />
			  
			</xf:bind>

	
		</xf:model>
	</head>
	<body>
		<xf:group appearance="full">
			<xf:label>Tool Execution</xf:label>
		
			<xf:output ref="instance('TOOL')/@id">
				<xf:label>name:</xf:label>
			</xf:output>
			<br />
			<xf:output ref="instance('TOOL')/@interface">
				<xf:label>interface</xf:label>
			</xf:output>
			<br />
			<xf:group appearance="full">
			<!--  would prefer to link to bind but does not work for some reason -->
				<xf:label>Input Parameters</xf:label>
				<xf:repeat id="inlist" bind="inpars">
				
					<xf:output ref="./@id">
						<xf:label> name: </xf:label>
					</xf:output>
					<xf:output
							value="instance('APPDESC')/applicationDefinition/parameters/parameterDefinition[@id=current()/@id]/description">
						<xf:label>Description</xf:label>
					</xf:output>
					<xf:input ref="ceat:value">
						<xf:label>value</xf:label>
					</xf:input>
					<xf:input ref="@indirect">
						<xf:label>indirect?</xf:label>
					</xf:input>
					

			
				</xf:repeat>
			</xf:group>
			<xf:group appearance="full">
				<xf:label>Output Parameters</xf:label>
				<xf:repeat bind="outpars">
					<xf:output ref="./@id">
						<xf:label> name: </xf:label>
					</xf:output>
					<xf:output
							value="instance('APPDESC')/applicationDefinition/parameters/parameterDefinition[@id=current()/@id]/description">
						<xf:label>Description</xf:label>
					</xf:output>
					<xf:input ref="ceat:value">
						<xf:label>value</xf:label>
					</xf:input>
					<xf:input ref="@indirect">
						<xf:label>indirect?</xf:label>
					</xf:input>
			
				</xf:repeat>
			</xf:group>
			<xf:submit submission="uws">
				<xf:label>Run as UWS-PA</xf:label>
			</xf:submit>
			<xf:submit submission="uwsauto">
				<xf:label>Auto-Run as UWS-PA</xf:label>
			</xf:submit>
			<xf:submit submission="debug">
				<xf:label>Debug</xf:label>
			</xf:submit>

	
		</xf:group>
	</body>
</html>
