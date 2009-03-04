#!/usr/bin/env python
#tests for the siap component

import unittest
from xmlrpclib import Fault
from VOTable import VOTable
from tempfile import mktemp
from urlparse import urlunsplit
from os.path import abspath
from tempfile import mkdtemp
from os import listdir, remove, rmdir


#siap service, with a test query.
service = 'ivo://nasa.heasarc/skyview/rass'

class Siap(unittest.TestCase):

    def constructQuery(self):
        """ helper method - reads test query from capabilities, asserts, and constructs query url from that"""
        serv = self.reg.getResource(service)
        siapCap = None
        self.assertTrue('capabilities' in serv, 'no capabilities')
        for cap in serv['capabilities']:
            if cap['standardID'] == 'ivo://ivoa.net/std/SIA':
                siapCap = cap
        self.assertTrue(siapCap != None, 'no sia capability')
        self.assertTrue('testQuery' in siapCap, 'no test query in sia capability')
        tq = siapCap['testQuery']
        pos = tq['pos']
        size = tq['size']
        #try running a query.
        query = self.siap.constructQuery(service, pos['long'], pos['lat'], size['long'])
        return query

    def setUp(self):
        self.sesame = ar.cds.sesame
        self.reg = ar.ivoa.registry
        self.siap = ar.ivoa.siap
        self.vosi = ar.ivoa.vosi
        self.info = ar.file.info
        self.manager = ar.file.manager
        
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
        result = self.siap.executeVotable(query)
        #check I can parse this as votable.
        vot = VOTable()
        vot.parseText(result)
        rowCount = len(vot.getDataRows())
        self.assertTrue(rowCount > 0,'no rows found')
        colCount = len(vot.getFields())
        self.assertTrue(colCount > 0,'no cols found')
        #now re-run to fetch as a datastructure.
        result = self.siap.execute(query)
        #check equivalence.
        self.assertEquals(rowCount,len(result), "not all rows in votable returned")
        line = result[0]
        self.assertEquals(colCount,len(line),'not all columns in votable returned')
        #check for access reference.
        for l in result:
            self.assertTrue('AccessReference' in l,'row lacks an access reference')
        
        
    def testExecuteAndSave(self):
        query = self.constructQuery()
        tmp = mktemp("vot", "ivoaSiapSystemTest")
        #convert this to a url
        tmpURL= urlunsplit(['file','',abspath(tmp),'',''])
        result = self.siap.executeAndSave(query,tmpURL)
        #parse from disk, check what we've got.
        vot = VOTable()
        vot.parse(tmp)
        # and now create a control document
        control = VOTable()
        control.parseText(self.siap.executeVotable(query))
        #should have the same document in both cases
        self.assertEquals(len(control.getDataRows()),len(vot.getDataRows()),'number of rows differ')
        self.assertEquals(len(control.getFields()),len(vot.getFields()),'number of columns differ')
        remove(tmp)
        
    def testSaveDatasets(self):
        query = self.constructQuery()
        tmpDir = mkdtemp("dir","ivoaSiapDatasets")
        tmpURL = urlunsplit(['file','',abspath(tmpDir),'',''])        
        count = self.siap.saveDatasets(query,tmpURL)
        # check how many rows we should have got.
        result = self.siap.execute(query)
        self.assertEquals(count,len(result),"didn't download all files")
        self.assertEquals(count,len(listdir(tmpDir)),"not all files downloaded")
        #clean up
        import os
        for root, dirs, files in os.walk(tmpDir, topdown=False):
            for name in files:
                os.remove(os.path.join(root, name))
        os.rmdir(tmpDir)
        
    def testSaveDatasetsSubset(self):
        query = self.constructQuery()
        tmpDir = mkdtemp("dir","ivoaSiapDatasets")
        tmpURL = urlunsplit(['file','',abspath(tmpDir),'',''])        
        count = self.siap.saveDatasetsSubset(query,tmpURL,[1])
        # check how many rows we should have got.
        self.assertEquals(1,count,"didn't download all instructed files")
        self.assertEquals(1,len(listdir(tmpDir)),"not all files downloaded")
        #clean up
        import os
        for root, dirs, files in os.walk(tmpDir, topdown=False):
            for name in files:
                os.remove(os.path.join(root, name))
        os.rmdir(tmpDir)
        
    def testSaveDatasetsSubsetMyspace(self):
        query = self.constructQuery()
        tmpURL = 'workspace://python/ivoasiap/datasets'
        if self.info.exists(tmpURL):
            self.manager.delete(tmpURL)     
        count = self.siap.saveDatasetsSubset(query,tmpURL,[1])
        # check how many rows we should have got.
        self.assertEquals(1,count,"didn't download all instructed files")
        self.assertTrue(self.info.isFolder(tmpURL))
        children = self.manager.listChildUris(tmpURL)
        self.assertEquals(1,len(children),'not all files downloaded')
        #clean up
        for c in children:
            self.manager.delete(c)      
        self.manager.delete(tmpURL)
      

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Siap)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

