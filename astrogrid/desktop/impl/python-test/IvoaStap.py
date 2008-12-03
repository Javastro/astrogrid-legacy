#!/usr/bin/env python
#tests for the stap component

import unittest
from xmlrpclib import Fault
from VOTable import VOTable
from tempfile import mktemp
from urlparse import urlunsplit
from os.path import abspath
from tempfile import mkdtemp
from os import listdir, remove, rmdir
from xmlrpclib import DateTime

#stap service
service = 'ivo://mssl.ucl.ac.uk/stap/cdaw/cluster'

class Stap(unittest.TestCase):

    def constructQuery(self):
        """ helper method - checks for stap capability, then builds query url from that"""
        serv = self.reg.getResource(service)
        stapCap = None
        self.assertTrue('capabilities' in serv, 'no capabilities')
        for cap in serv['capabilities']:
            if cap['standardID'] == 'ivo://org.astrogrid/std/STAP/v1.0':
                stapCap = cap
        self.assertTrue(stapCap != None, 'no stap capability')
        start = DateTime('20000101T00:00:00')
        end = DateTime('20000102T00:00:00')
        query = self.stap.constructQuery(service, start,end)
        return query

    def setUp(self):
        self.sesame = ar.cds.sesame
        self.reg = ar.ivoa.registry
        self.stap = ar.astrogrid.stap
        self.vosi = ar.ivoa.vosi
    def testApplicationExists(self):
        try:
            self.reg.getResource(service)
        except Fault:
            self.fail(service + " not found")
    def dontTestApplicationIsUp(self):
        """ this service doesn't have a vosi capability, so can't do this test"""
        avail = self.vosi.checkAvailability(service)
        self.assertTrue(avail['available'],"The service is unavailable, other tests will fail: " + str(avail))

    def testExecute(self):
        query = self.constructQuery()
        #run query to fetch raw votable
        result = self.stap.executeVotable(query)
        #check I can parse this as votable.
        vot = VOTable()
        vot.parseText(result)
        rowCount = len(vot.getDataRows())
        self.assertTrue(rowCount > 0,'no rows found')
        self.assertEquals(1,rowCount,'expected 1 rows')
        colCount = len(vot.getFields())
        self.assertTrue(colCount > 0,'no cols found')    
        #now re-run to fetch as a datastructure.
        result = self.stap.execute(query)        
        #check equivalence.
        self.assertEquals(rowCount,len(result), "not all rows in votable returned")
        line = result[0]
        # can't test this - this service returns two columns with same UCD - non unique.
        self.assertEquals(colCount,len(line),'not all columns in votable returned')
        #check for access reference.
        for l in result:
            self.assertTrue('VOX:AccessReference' in l,'row lacks an access reference')
        
        
    def testExecuteAndSave(self):
        query = self.constructQuery()
        tmp = mktemp("vot", "ivoaStapSystemTest")
        #convert this to a url
        tmpURL= urlunsplit(['file','',abspath(tmp),'',''])        
        result = self.stap.executeAndSave(query,tmpURL)        
        #parse from disk, check what we've got.
        vot = VOTable()
        vot.parse(tmp)        
        # and now create a control document
        control = VOTable()
        control.parseText(self.stap.executeVotable(query))        
        #should have the same document in both cases
        self.assertEquals(len(control.getDataRows()),len(vot.getDataRows()),'number of rows differ')
        self.assertEquals(len(control.getFields()),len(vot.getFields()),'number of columns differ')
        remove(tmp)
        
    def testSaveDatasets(self):
        query = self.constructQuery()
        tmpDir = mkdtemp("dir","ivoaStapDatasets")
        tmpURL = urlunsplit(['file','',abspath(tmpDir),'',''])        
        count = self.stap.saveDatasets(query,tmpURL)
        # check how many rows we should have got.
        result = self.stap.execute(query)
        self.assertEquals(count,len(result),"failed to download all required files: expected " +str(len(result)) + "but got " +  str(count) )
        self.assertEquals(count,len(listdir(tmpDir)),"not all files downloaded")
        #clean up
        import os
        for root, dirs, files in os.walk(tmpDir, topdown=False):
            for name in files:
                os.remove(os.path.join(root, name))
        os.rmdir(tmpDir)
        
    def testSaveDatasetsSubset(self):
        query = self.constructQuery()
        tmpDir = mkdtemp("dir","ivoaStapDatasets")
        tmpURL = urlunsplit(['file','',abspath(tmpDir),'',''])        
        count = self.stap.saveDatasetsSubset(query,tmpURL,[0])
        # check how many rows we should have got.
        self.assertEquals(1,count,"failed to download all required files")
        self.assertEquals(1,len(listdir(tmpDir)),"not all files downloaded")
        #clean up
        import os
        for root, dirs, files in os.walk(tmpDir, topdown=False):
            for name in files:
                os.remove(os.path.join(root, name))
        os.rmdir(tmpDir)
               
      

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Stap)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

