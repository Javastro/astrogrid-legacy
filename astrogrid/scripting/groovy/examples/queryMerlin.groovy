#!/bin/env groovy
import org.apache.axis.utils.XMLUtils
import org.astrogrid.datacenter.sql.SQLUtils

Astrogrid = org.astrogrid.scripting.Astrogrid.getInstance()

merlin = Astrogrid.datacenters.find{it.description ==~ ".*merlin.*"}
assert merlin != null
delegate = merlin.createDelegate()
print "connecting to ${merlin.endpoint}"
queryElement = SQLUtils.toQueryBody("select * from merlinCatalog where DataNo = 657037")
results = delegate.doQuery("Votable",queryElement)
assert results.isVotable()
print XMLUtils.ElementToString(results.getVotable())


