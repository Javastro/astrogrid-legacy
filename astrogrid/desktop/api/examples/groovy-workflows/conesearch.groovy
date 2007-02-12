#!/usr/bin/env groovy
/*
Transcription of JES Conesearch workflow into a commandline groovy script.
connects to a running Astro Runtime using the XMLRPC method - advantage of this is
that no additional jars need to be installed in groovy.
*/
//CONNECTING TO AR
f = new File(System.getProperty('user.home'),".astrogrid-desktop")
assert f.exists(), 'No AR running: Please start your AR and rerun this script'
acr = new groovy.net.xmlrpc.XMLRPCServerProxy("${f.text.trim()}xmlrpc")

// SETUP VARIABLES
user_votable = ""
output_dir = new File("").toURI().toString() // current working directory
//output_dir = "${acr.astrogrid.myspace.getHome()}cones" // alternative default value
radius = 0.1
cones = [
      "ivo://sdss.jhu/services/DR4CONE"
	 	, "ivo://irsa.ipac/2MASS-PSC"
		, "ivo://ned.ipac/Basic_Data_Near_Position"
      ]

//    PARSE COMMANDLINE OPTIONS
cli = new CliBuilder()
cli.usage ='conesearch.groovy [options]'
// define options
cli.h(longOpt: 'help', "usage information")
cli.r(longOpt: 'radius', args:1 ,"search radius \n(default: $radius)")
cli.f(longOpt:'sourcefile', args:1, "URI/file of sourcelist votable\n (default: prompt using dialogue)")
cli.o(longOpt:'outputdir', args:1," URI/file output location \n(default: current direcotory)")
cli.s(longOpt:'service',args:20,"URL/registryID of a service to query - repeat for multiple services")
cli.d(longOpt:'defaultServices', "add default services to list of services to query \n(defaults: ${cones.join(',\n')})")
// todo - an option to read service list from a file; an option to 'add' a service to existing list; an option
//  to display registry dialogue that allows user to select from list of available cones.

// process options.
options = cli.parse(args)
if (!options) return // parse failed
if (options.h) 	{cli.usage(); return}// print usage and exit.
if (options.r) radius = options.r.toDouble()
if (options.s) { // custom list of services specified
	if (!options.d) cones = [] // defaultServices not asked for - so remove them.
	cones.addAll(options.ss) // ss gives the list of values.
}
if (options.f) {
	user_votable=mkURI(options.f)
} else {
	user_votable = acr.dialogs.resourceChooser.chooseResource("Select a votable of sources",true)
}
if (options.o) {
	output_dir = mkURI(options.o)	
} /*else { // not possible to prompt at the moment- need a direcotyr chooser dialogue.
	output_dir = acr.dialogs.resourceChooser.chooseResource("Select an output directory",true)
}*/

// check we've got all we need.
assert user_votable && user_votable != 'NULL', "No sourcelist provided"
assert output_dir && output_dir != 'NULL', "No output directory provided" 
assert cones, "No services specified" // will fail if empty list

// PARSE POSITIONS       
println "Reading sources from $user_votable"
// a slightly idiomatic way of reading a table - but ensures the table gets standardized, cleaned up, etc.
votString = acr.util.tables.convertFromFile(user_votable,"votable","votable");
// use groovy's xml & xpath here..
vot = new XmlParser(false,false).parseText(votString) // false, false tells it to ignore namespaces
cols = vot.RESOURCE.TABLE.FIELD
rows = vot.RESOURCE.TABLE.DATA.TABLEDATA.TR

// inspect the metadata for the table.
// TODO - how general are these column names? would using UCDs be more versatile here?
nameCol  = cols.'@name'.indexOf( 'Name')
raCol  = cols.'@name'.indexOf( '_RAJ2000')
decCol  = cols.'@name'.indexOf('_DEJ2000')
// verify we've found something
assert nameCol > -1, "Could not locate name column"
assert raCol > -1, "Could not locate RA column"
assert decCol > -1, "Could not locate DEC column"

println "Number of sources read: $rows.size"

// LOAD REGISTRY METADATA
// Get some metadata for these services.
coneDescriptions = []
for (c in cones) {
	res = acr.ivoa.registry.getResource(c)
	coneDescriptions.add(res)
	println "Will query $res.title, results will be named '${res.shortName}.vot"
}
println "Results will be written to $output_dir"
// PERFORM QUERIES
// iterate through each row in the table.
cone = acr.ivoa.cone
for (row in rows) {
	// parse that row.
	rowData = row.TD.value
	srcname = rowData[nameCol].replaceAll(" ","_").replaceAll("\\+","p")
	ra = rowData[raCol].toDouble()
	dec = rowData[decCol].toDouble()
	print "Querying for $srcname $ra $dec.. "
	saveDir = "${output_dir}${srcname}"
	if (saveDir.startsWith("file:/")) { //work-around for AR bug - doesn't create directories on local filesystem.
		d = new File(new URI(saveDir))
		d.mkdirs()
	}	
	// construct a query
	for (service in coneDescriptions) {
		print "${service.shortName}.."
		try {
			query = cone.constructQuery(service.id,ra,dec,radius)
			saveLocation = "${saveDir}/${service.shortName}.vot"

			cone.executeAndSave(query,saveLocation)
			print "ok "
		} catch (Throwable e) {
			println "FAILED - $e.message"
		}
	}
	println "" // onto next service
}
println "COMPLETED"
// end main script.

//SUPPORTING FUNCTIONS

/* converts a file parameter (which might be relative), url, or myspace refereces to an absolute URI */
String mkURI(input) {
	if (input.startsWith('#')) { // a short-cut notation for user's myspace - make absolute
		return acr.astrogrid.myspace.getHome() + input.substring(1) + "/"
	}
	base = new File('').toURI() // current working direcotory
	u = new URI(input)
	if (!u.absolute) { // assume non-absolute uris are files relative to the current directory.
		u = base.resolve(u)
	}
	return u.toString() + "/"
}
// end script