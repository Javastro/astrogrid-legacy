#!/bin/env groovy

Astrogrid = org.astrogrid.scripting.Astrogrid.getInstance()
app = Astrogrid.applications[0].createDelegate()
list =  app.listApplications()
println list
xmlDescr = app.getApplicationDescription(list[0]).xmlDescriptor
//println xmlDescr
parser = new XmlParser()
xmlNodes = parser.parse(new java.io.StringReader(xmlDescr))
//demonstrate extracting things from the xml
println "Information about " + list[0]
println "Executable: " + xmlNodes.ExecutionPath[0].value()
println "Parameters:"
xmlNodes.Parameters.Parameter.each{println (it.attributes().name + " : " + it.UI_Description[0].value())}
