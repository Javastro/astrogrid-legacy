#!/usr/bin/env python
# test for bug 2460
#When I submit an application (in particular I am submitting a DSA query) using python and XMLRPC, and ask the result to be saved in MySpace, the result contains the URL of the saved file. I expect the IVORN:
#
#r.results()
#['http://loki.roe.ac.uk/astrogrid-filestore/filestore/.435d564f.116cdc48144.7fff']
#
#Is this the expected behavior or is it something to correct?

import unittest
from xmlrpclib import Fault
import time

testApp = 'ivo://wfau.roe.ac.uk/ukidssWorld-dsa/wsa/ceaApplication'


class Report(unittest.TestCase):
    def setUp(self):
        self.apps = ar.astrogrid.applications
        self.ms = ar.astrogrid.myspace
    def testApplicationExists(self):
        try:
            ar.ivoa.registry.getResource(testApp)
        except Fault:
            self.fail(testApp + " not found")
        
    def testReport(self):
        dict = self.apps.createTemplateStruct(testApp,'ADQL')
        dict['input']['Query']['value']='Select a.filterID From Filter as a' #simple query
        #setup an output location
        root = self.ms.getHome() # will force login.
        output = root + "/bug2460.vot"
        if self.ms.exists(output) :
            self.ms.delete(output)
        dict['output']['Result']['indirect']=True
        dict['output']['Result']['value'] = output
        doc = self.apps.convertStructToDocument(dict)
        execId = self.apps.submit(doc)
        # take a nap while things kick off. if we don't, we may not find it's there yet.
        time.sleep(10)

        #check on the progress of the query.
        progress = self.apps.getExecutionInformation(execId)
        # loop round until the query completes.  
        while progress['status'] not in ['ERROR','COMPLETED','UNKNOWN'] :
            time.sleep(10)
            progress = self.apps.getExecutionInformation(execId)

        #query finished
        if progress['status'] == "ERROR":
            self.fail("Query ended in error")
        results = self.apps.getResults(execId)
        #print results
        resultLoc = results['Result']
        self.assertNotEquals(output,resultLoc,msg="results now returns ivo:// location")
        altResultLoc = self.ms.getReadContentURL(output)
        # read content url isn't necessarily the same as the op location.
        #self.assertEquals(msg="results is not the myspace location",self.ms.getReadContentURL(output),resultLoc)
        #however, both URLs should point to the same content
        import urllib
        content = urllib.urlopen(resultLoc).read()
        content1 = urllib.urlopen(altResultLoc).read()  
        self.assertEquals(content,content1,msg="Results content differs")
        
def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Report)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    
