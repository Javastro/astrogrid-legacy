#!/usr/bin/env groovy
/** commandline interface to query the registry

*/
import org.apache.axis.utils.XMLUtils
import org.apache.commons.cli.*

// setup commandline options
Options o = new Options()
o.addOption("r","resolve",true,"Resolve identifier to endpoint")
o.addOption("l","list",false,"List identifiers of records in registry")
o.addOption("i","identfier",true,"Ask registry about an identifier")
o.addOption("x","registry",false,"Ask about the registry itself")
o.addOption("q","query",true,"Run a quey against the registry")

// lots more that could be added here, but dunno what it all means really.

og = new OptionGroup()
['r','l','i','x',"q"].each{og.addOption(o.getOption it)}
o.addOptionGroup og

try {
parser = new PosixParser()
line = parser.parse(o,this.args)

astrogrid = new org.astrogrid.scripting.Toolbox()
r = astrogrid.createRegistryClient()



if (line.hasOption "r") {
        idname = line.getOptionValue "r"
        println(r.getEndPointByIdentifier(idname))
}

else if (line.hasOption "l") {
        doc = r.listIdentifiers()
        XMLUtils.PrettyDocumentToStream(doc,System.out)
}

else if (line.hasOption "i") {
        doc = r.getResourceByIdentifier(line.getOptionValue("i"))
        XMLUtils.PrettyDocumentToStream(doc,System.out)
}

else if (line.hasOption "x") {
        doc = r.getRegistries()
        XMLUtils.PrettyDocumentToStream(doc,System.out)
}


else if (line.hasOption "q") {
        input = line.getOptionValue("s") == "-" ? System.in : new java.io.File(line.getOptionValue("q"))
        doc = r.search(r.text)
        XMLUtils.PrettyDocumentToStream(doc,System.out)
}
else {
        displayHelp()
}
} catch (Exception e) {
        println("An Error occurred :" + e.class.name)
        println e.message
        displayHelp()
        System.exit(-1)
}

def displayHelp() {
        (new HelpFormatter()).printHelp(
<<<DOC
reg <options>
query the registry
DOC,
o)
}
