#!/usr/bin/env groovy
/*
Transcription of JES Conesearch workflow into a commandline groovy script.

connects to a running Astro Runtime using the RMI method - the AR rmi jars must
be added to the groovy classpath.

For comparison only - the xmlrpc equivalent 'conesearch.groovy' is more fully developed.

*/

//CONNECTING TO AR
acr = new org.astrogrid.acr.Finder().find()

// import the services we require from the ACR
tables = acr.getService("util.tables")
myspace = acr.getService("astrogrid.myspace")
cone = acr.getService("ivoa.cone")
registry = acr.getService("ivoa.registry")

//DEFINITIONS

//TODO add commandline argument parsing for these - or display dialogue to prompt user if
// not supplied.
//user_votable = new URI( "${myspace.home}qso/veron_sample.vot")
user_votable = new File("veron_sample.vot").toURI()
URI output_dir = new URI("${myspace.home}test")
radius = 0.1

//list the registry IDs of the cone searches to query against.
//todo add option to display UI dialogue that lists available cones.
cones = [
         new URI("ivo://sdss.jhu/services/DR4CONE")
	 	, new URI("ivo://irsa.ipac/2MASS-PSC")
		, new URI("ivo://ned.ipac/Basic_Data_Near_Position")
        ]

// PARSE POSITIONS       
// a slightly idiomatic way of reading a table - but ensures the table gets standardized, cleaned up, etc.
// in future, could use this call to convert into a votable before reading.
votString = tables.convertFromFile(user_votable,"votable","votable");
// use groovy's xml & xpath here..
vot = new XmlParser(false,false).parseText(votString) // false, false tells it to ignore namespaces
cols = vot.RESOURCE.TABLE.FIELD
rows = vot.RESOURCE.TABLE.DATA.TABLEDATA.TR

// inspect the metadata for the table - find the indexes of the columns we want.
nameCol  = cols.'@name'.indexOf( 'Name')
raCol  = cols.'@name'.indexOf( '_RAJ2000')
decCol  = cols.'@name'.indexOf('_DEJ2000')

println "Total number of sources read from catalogue: $rows.size"

// LOAD REGISTRY METADATA
// Get some metadata for these services.
coneDescriptions = []
for (c in cones) {
	res = registry.getResource(c)
	coneDescriptions.add(res)
	println "Will query $res.title, results will be named '${res.shortName}.vot"
}

// PERFORM QUERIES
// iterate through each row in the table.
for (row in rows) {
	// parse that row.
	rowData = row.TD.value
	srcname = rowData[nameCol].replaceAll(" ","_").replaceAll("\\+","p")
	ra = rowData[raCol].toDouble()
	dec = rowData[decCol].toDouble()
	print "Querying for $srcname $ra $dec.. "
	
	// construct a query
	for (service in coneDescriptions) {
		print "${service.shortName}.."
		try {
			query = cone.constructQuery(service.id,ra,dec,radius)
			saveLocation = new URI("${output_dir}/${srcname}/${service.shortName}.vot")
			cone.executeAndSave(query,saveLocation)
			print "ok "
		} catch (Throwable e) {
			println "FAILED - $e.message"
		}
	}
	println "" // onto next service
}
println "COMPLETED"
// end script
System.exit(0)