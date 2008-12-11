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
            
    def testApplicationServerIsUp(self):
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
    
    def testCanUseServerId(self):
        #when a server only provides a single app, tolerate server ID
        # to be used in it's place - undocumented, helps the user.
        services = self.apps.listServersProviding(testApp)
        self.assertEqual(1,len(services),"Expected a single app server provider")
        ceaCap = None
        for cap in services[0]['capabilities']:
            if cap['standardID'] == 'ivo://org.astrogrid/std/CEA/v1.0':
                ceaCap = cap
        self.assertTrue(ceaCap != None,'no cea server capability')
        #verify this server only provides a single app.
        self.assertEquals(1,len(ceaCap['managedApplications']),'server manages more than one application')
        
        #find the server ID, verify we get the same behaviour using this
        #as using the testApp ID.
        servId = services[0]['id']    
        #documentation.
        self.assertEquals(self.apps.getDocumentation(testApp)
                          ,self.apps.getDocumentation(servId)
                          ,'expected documentation to be equal')        
        #template document
        self.assertEquals(self.apps.createTemplateDocument(testApp,"default")
                          ,self.apps.createTemplateDocument(servId,"default")
                          ,'template doc not equal')
        #template struct
        self.assertEquals(self.apps.createTemplateStruct(testApp,"default")
                          ,self.apps.createTemplateStruct(servId,"default")
                          ,'template struct not equal')        
        #list servers providinng
        services1 = self.apps.listServersProviding(servId)

        self.assertEqual(len(services),len(services1)
                         ,'list of providers not equal')
        self.assertEqual(services[0]['id'],services1[0]['id']
                         ,'list of providers not equal')        
                
    def testCannotUseServerId(self):
        #test that correct failures happens when using the id of a server that 
        #provides more than one app
        multiAppServerId = 'ivo://mssl.ucl.ac.uk/MSSLToolCEAAccess'
        service = self.reg.getResource(multiAppServerId)
        ceaCap = None
        for cap in service['capabilities']:
            if cap['standardID'] == 'ivo://org.astrogrid/std/CEA/v1.0':
                ceaCap = cap
        self.assertTrue(ceaCap != None,'no cea server capability')
        #verify this server only provides a single app.
        self.assertTrue(len(ceaCap['managedApplications']) > 1
                        ,'server expected to manage more than one application')
        
        #all these should fail.
        try:
            self.apps.getDocumentation(multiAppServerId)
            self.fail("expected to throw")
        except Fault:
            pass
        try:
            self.apps.createTemplateDocument(multiAppServerId,"default")
            self.fail("expected to throw")
        except Fault:
            pass
        try:
            self.apps.createTemplateStruct(multiAppServerId,"default")
            self.fail("expected to throw")
        except Fault:
            pass
        try:
            self.apps.listServersProviding(multiAppServerId)
            self.fail("expected to throw")
        except Fault:
            pass                        
       


def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Cea)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

