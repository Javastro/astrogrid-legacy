#!/usr/bin/env python
#tests for the Dal Cone component


import unittest
from xmlrpclib import Fault
from VOTable import VOTable

#cone service, with vosi capability.
service = 'ivo://mssl.ucl.ac.uk/xmmsuss_dsa/XMMSUSS'

class Cone(unittest.TestCase):

    def constructQuery(self):
        """ helper method - reads test query from capabilities, asserts, and constructs query url from that"""
        serv = self.reg.getResource(service)
        coneCap = None
        self.assertTrue('capabilities' in serv, 'no capabilities')
        for cap in serv['capabilities']:
            if cap['standardID'] == 'ivo://ivoa.net/std/ConeSearch':
                coneCap = cap
        self.assertTrue(coneCap != None, 'no cone capability')
        self.assertTrue('testQuery' in coneCap, 'no test query in cone capability')
        tq = coneCap['testQuery']
        #try running a query.
        query = self.cone.constructQuery(service, tq['ra'], tq['dec'], tq['sr'])
        return query

    def setUp(self):
        self.sesame = ar.cds.sesame
        self.reg = ar.ivoa.registry
        self.cone = ar.ivoa.cone
        self.vosi = ar.ivoa.vosi
    def testApplicationExists(self):
        try:
            self.reg.getResource(service)
        except Fault:
            self.fail(service + " not found")
    def testApplicationIsUp(self):
        avail = self.vosi.checkAvailability(service)
        self.assertTrue(avail['available'],"The service is unavailable, other tests will fail: " + str(avail))

    def testExecute(self):
        query = self.constructQuery()
        #run query to fetch raw votable
        result = self.cone.executeVotable(query)
        #check I can parse this as votable.
        vot = VOTable()
        vot.parseText(result)
        rowCount = len(vot.getDataRows())
        self.assertTrue(rowCount > 0,'no rows found')
        colCount = len(vot.getFields())
        self.assertTrue(colCount > 0,'no cols found')    
        #now re-run to fetch as a datastructure.
        result = self.cone.execute(query)        
        #check equivalence.
        self.assertEquals(rowCount,len(result), "not all rows in votable returned")
        line = result[0]
        self.assertEquals(colCount,len(line),'not all columns in votable returned')
 
        
    def testExecuteAndSave(self):
        query = self.constructQuery()
        import tempfile
        tmp = tempfile.mktemp("vot", "ivoaConeSystemTest")
        #convert this to a url
        from urlparse import urlunsplit
        from os.path import abspath
        tmpURL= urlunsplit(['file','',abspath(tmp),'',''])        
        result = self.cone.executeAndSave(query,tmpURL)        
        #parse from disk, check what we've got.
        vot = VOTable()
        vot.parse(tmp)        
        # and now create a control document
        control = VOTable()
        control.parseText(self.cone.executeVotable(query))        
        #should have the same document in both cases
        self.assertEquals(len(control.getDataRows()),len(vot.getDataRows()),'number of rows differ')
        self.assertEquals(len(control.getFields()),len(vot.getFields()),'number of columns differ')
        #cleanup
        import os
        os.remove(tmp)
        
    def testSaveDatasets(self):
        query = self.constructQuery()
        tmpURL = 'file:///' #unused, so can be any old file reference
        try:
            self.cone.saveDatasets(query,tmpURL)
            self.fail("Expected to fail")
        except Fault:
            pass
    
    def testSaveDatasetsSubset(self):
        query = self.constructQuery()
        tmpURL = 'file:///' #unused, so can be any old file reference
        try:
            self.cone.saveDatasetsSubset(query,tmpURL,[0,1,2])
            self.fail("Expected to fail")
        except Fault:
            pass        
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Cone)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

