#!/bin/env js

Astrogrid = Packages.org.astrogrid.scripting.Astrogrid.getInstance()

merlin = null
for (i = Astrogrid.datacenters.iterator(); i.hasNext(); ) {
   dc = i.next()
   if (dc.description.search(/merlin/) != -1) {
   	merlin = dc
	break
   }
}

if (merlin == null) {
  print("Failed to find merlin service")
  quit()
  }
delegate = merlin.createDelegate()
print("connecting to " + merlin.endpoint)
queryElement = Astrogrid.toQueryBody("select * from merlinCatalog where DataNo = 657037")
results = delegate.doQuery("Votable",queryElement)
print(Astrogrid.ElementToString(results.getVotable()))


