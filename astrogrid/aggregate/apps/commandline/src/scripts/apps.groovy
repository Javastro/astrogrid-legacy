#!/usr/bin/env groovy
/** commandline interface to the application list.

*/
import org.apache.axis.utils.XMLUtils
import org.apache.commons.cli.*

// setup commandline options
Options o = new Options()
o.addOption("l","list",false,"List names of registered applications")
 .addOption("L","fullList",false,"List info of each registered application")
 .addOption("Q","reg",true,"Return full registry entry for an application")
 .addOption("q","query",true,"Return information for an application")
 .addOption("h","help",false,"Display this help.")

og = new OptionGroup()
['l','L','q','Q','h'].each{og.addOption(o.getOption it)}
o.addOptionGroup og

try {
parser = new PosixParser()
line = parser.parse(o,this.args)

astrogrid = new org.astrogrid.scripting.Toolbox()
reg = astrogrid.workflowManager.toolRegistry



if (line.hasOption "l") {
        reg.listApplications().each { println(it) }
}

else if (line.hasOption "L") {
        reg.listUIApplications().each{
             println(it.name +"\t" + it.getUIName() +  "\t" + it.interfaceNames.toList()) }
}

else if (line.hasOption "Q") {
        descr = reg.getDescriptionFor(line.getOptionValue "Q")
        XMLUtils.PrettyElementToStream(descr.originalVODescription,System.out)
}

else if (line.hasOption "q") {
        descr= reg.getDescriptionFor(line.getOptionValue "q")
/*
        resource = new XmlParser(false,true).parseText(
                XMLUtils.ElementToString(descr.originalVODescription))
        println(resource["${vr}:VODescription"])
*/

        vr = "http://www.ivoa.net/xml/VOResource/v0.9"
        println("Application: ${descr.name}")
        println(descr.originalVODescription.getElementsByTagNameNS(vr,"Description").item(0).firstChild.nodeValue)
        descr.parameters.parameter.each { param |
           println("Parameter: ${param.UI_Name}")
           println(param.UI_Description.content)
           ["name","type","subType","units","acceptEncodings","defaultValue","UCD","optionList"].each{ prop |
                if (param[prop] != null) {
                println("\t ${prop} : ${param[prop]}")
                }
           }
       }
       descr.interfaces.get_interface().each { iface |
        println("Interface: ${iface.name}")
        println("Inputs")
        iface.input.pref.each { p |
          println("\t ${p.ref} max: ${p.maxoccurs}, min: ${p.minoccurs}")
        }
        println("Outputs")
        iface.output.pref.each { p |
          println("\t ${p.ref} max: ${p.maxoccurs}, min: ${p.minoccurs}")
        }
       }
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
apps <options>
query list of registered applications
DOC,
o)
}



