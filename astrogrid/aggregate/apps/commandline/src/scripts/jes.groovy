#!/usr/bin/env groovy
/** commandline interface to the jes server


*/
import org.astrogrid.workflow.beans.v1.execution.JobURN
import org.astrogrid.workflow.beans.v1.Workflow
import org.apache.axis.utils.XMLUtils
import org.exolab.castor.xml.Marshaller
import org.apache.commons.cli.*


import org.astrogrid.community.resolver.CommunityPasswordResolver


class Jes {

JobURN mkJobURN(string) {
  urn = new JobURN()
  urn.setContent(string)
  return urn;
}

// setup commandline options

Options o = (new Options())

Jes() {
 o.addOption("l","list",false,"List jobs")
 .addOption("L","fullList",false,"List full details of jobs")
 .addOption("j","job",true,"Query jes for details of specified job")
 .addOption("J","getJob",true,"Retreive workflow transcript for specified job")
 .addOption("C","cancel",true,"Cancel specified job")
 .addOption("d","delete",true,"Delete specified job")
 .addOption("s","submit",true,"Submit a job")
 .addOption("h","help",false,"Display this help")

 .addOption("u","user",true,"username (optional)")
 .addOption("p","password",true,"password (optional)")
 .addOption("c","community",true,"community (optional)")

og = new OptionGroup()

['l','L','j','J','C','d','s','h'].each{og.addOption(o.getOption(it))}
o.addOptionGroup(og)
}


astrogrid = new org.astrogrid.scripting.Toolbox()
jm = astrogrid.workflowManager.jobExecutionService

parser = new PosixParser()

static void main(String[] args) {
    (new Jes()).run(args)
}

void run(args){
try {// catch all exceptio handler.

line = parser.parse(o,args)

u=null
if (line.hasOption("u")) {
        u = line.getOptionValue("user")
} else {
        u = astrogrid.systemConfig.getProperty("username")
}

p=null
if (line.hasOption("p")) {
        p = line.getOptionValue("password")
} else {
        p = astrogrid.systemConfig.getProperty("password")
}
comm=null
if (line.hasOption("c")) {
        comm = line.getOptionValue("community")
} else {
        comm = astrogrid.systemConfig.getProperty("org.astrogrid.community.ident")
}
login(u,comm,p)
acc = astrogrid.objectBuilder.newAccount(u,comm)

if (line.hasOption("l")) {
        jm.listJobs(acc).each{
                println(it.jobId.content)
        }
}

else if (line.hasOption("L")) {//inefficient work-around, as full summary list isn't working.
        for (j in jm.listJobs(acc)) { // for some reason, internal iterators break here..
                wf = jm.readJob(j.jobId)
                er = wf.jobExecutionRecord
                println(wf.name + "\t" + er.status + "\t" + er.startTime + "\t" + er.finishTime + "\t" + j.jobId.content )
        }
}

else if (line.hasOption("j")) {
        // inefficient work-around. later will extract this info from summary list.
        wf = jm.readJob(mkJobURN(line.getOptionValue("j")))
                er = wf.jobExecutionRecord
                println(wf.name + "\t" + er.status + "\t" + er.startTime + "\t" + er.finishTime + "\t" + er.jobId.content )
}


else if (line.hasOption("J")) {
        wf = jm.readJob(mkJobURN(line.getOptionValue("J")))
        doc = XMLUtils.newDocument()
        Marshaller.marshal(wf,doc)
        XMLUtils.PrettyDocumentToStream(doc,System.out)
}

else if (line.hasOption("C")) {
        jm.cancelJob(mkJobURN(line.getOptionValue("C")))
}

else if (line.hasOption("d")) {
        jm.deleteJob(mkJobURN(line.getOptionValue("d")))
}

else if (line.hasOption("s")) {
        input = line.getOptionValue("s") == "-" ? System.in : new java.io.File(line.getOptionValue("s"))
        wf = Workflow.unmarshalWorkflow(input.newReader())
        result = jm.submitWorkflow(wf)
        println(result.content)
}

else {
        displayHelp()
}

} catch (Throwable e) { // catch all, for better reporting.
        println("An Error occurred :" + e.getClass().getName())
        println(e.getMessage())
        displayHelp()
        System.exit(-1)
}
}

void displayHelp() {
        (new HelpFormatter()).printHelp(
<<<DOC
jes <options>
manage jobs in a job server.
DOC
,o)
}

void login(u,comm,p) {

  security = new CommunityPasswordResolver();
  token = security.checkPassword("ivo://" + comm + "/" + u,p)
  // not that we do anything with the token afterwards at the moment..
}
}