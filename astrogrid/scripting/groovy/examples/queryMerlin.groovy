#!/bin/env groovy

Astrogrid = org.astrogrid.scripting.Astrogrid.getInstance()

merlin = Astrogrid.datacenters.find{it.description ==~ ".*merlin.*"}
assert merlin != null
delegate = merlin.createDelegate()
print "connecting to ${merlin.endpoint}"
queryElement = Astrogrid.toQueryBody("select * from merlinCatalog where DataNo = 657037")
results = delegate.doQuery("Votable",queryElement)
assert results.isVotable()
print Astrogrid.ElementToString(results.getVotable())


