#!/usr/bin/env python
#tests for the applications component.

import unittest
from xmlrpclib import Fault
from VOTable import VOTable
from tempfile import mktemp
from urlparse import urlunsplit
from os.path import abspath
from tempfile import mkdtemp
from os import listdir, remove, rmdir
import time


#cea application
testApp = 'ivo://wfau.roe.ac.uk/ukidssWorld-dsa/wsa/ceaApplication'

class Cea(unittest.TestCase):

    def createTool(self):
        """ helper method - reads test query from capabilities, asserts, and constructs query url from that"""
        tool = self.apps.createTemplateStruct(testApp,'ConeSearch')
        pos = self.sesame.resolve('m32')
        ip = tool['input']
        op = tool['output']
        ip['CatTable']['value']='wsa.lasSource'
        ip['RA']['value']=pos['ra']
        ip['DEC']['value']=pos['dec']
        ip['Radius']['value']=0.1
        ip['Format']['value']='VOTABLE'
        
        return tool

    def setUp(self):
        self.sesame = ar.cds.sesame
        self.reg = ar.ivoa.registry
        self.apps = ar.astrogrid.applications
        self.vosi = ar.ivoa.vosi
        self.rpm = ar.astrogrid.processManager
        
    def testApplicationExists(self):
        try:
            self.reg.getResource(testApp)
        except Fault:
            self.fail(service + " not found")
            
    def testApplicationIsUp(self):
        services = self.apps.listServersProviding(testApp)
        self.assertEqual(1,len(services),"Expected a single app server provider")
        avail = self.vosi.checkAvailability(services[0]['id'])
        self.assertTrue(avail['available'],"The app server is unavailable, other tests will fail: " + str(avail))
 
    def testExecuteDirect(self):
        tool = self.createTool()
        doc = self.apps.convertStructToDocument(tool)
        execId = self.rpm.submit(doc)
        
        time.sleep(5)
        #check on the progress of the query.
        progress = self.rpm.getExecutionInformation(execId)
        self.assertTrue('status' in progress)
        # loop round until the query completes.  
        while progress['status'] not in ['ERROR','COMPLETED','UNKNOWN'] :
            time.sleep(5)
            progress = self.rpm.getExecutionInformation(execId)
          #query finished
        if progress['status'] == "ERROR":
            self.fail("Query ended in error")
        results = self.apps.getResults(execId)
        # check we can parse resuls as a votable.
        self.assertTrue('Result' in results)
        vot = VOTable()
        vot.parseText(results['Result'])
        rowCount = len(vot.getDataRows())
        self.assertEquals(0,rowCount,'expected to find no rows')
        colCount = len(vot.getFields())
        self.assertTrue(colCount > 0,'no cols found')
    
    def notestExecuteIndirect(self):
        """a test of result inderection to myspace is done in bug2460.py"""
        pass
    
       


def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Cea)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

