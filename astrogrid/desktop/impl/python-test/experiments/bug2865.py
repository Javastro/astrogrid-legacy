#!/usr/bin/env python
#tests for bug 2865
# repeated queries to multi-cone capability service from astroscope gives different results each time.
# this script simulates the behaviour of Astroscope - see if we can replicate the error
# and so demonstrate it's a server-side problem.

import unittest
import os.path
from VOTable import VOTable
import threading
import xmlrpclib

#the service in question.
service = 'ivo://wfau.roe.ac.uk/ukidssDR2-dsa/wsa'



class bug2865(unittest.TestCase):

    def setUp(self):
        """ sets up the unit test """
        self.reg = ar.ivoa.registry
        self.cone = ar.ivoa.cone
        self.vosi = ar.ivoa.vosi
        
    def testApplicationExists(self):
        """verify UKIDSS is registered"""
        try:
            self.reg.getResource(service)
        except xmlrpclib.Fault:
            self.fail(service + " not found")
            
    def testApplicationIsUp(self):
        """ use vosi to verify UKIDSS is up """
        avail = self.vosi.checkAvailability(service)
        self.assertTrue(avail['available'],
                        "The service is unavailable, other tests will fail: " + str(avail))
       
    def constructQueries(self,ra,dec,sz):
        """ constructs a map of capabilityName /tableName to Query URL  """
        queries = {}
        serv = self.reg.getResource(service)
        coneCap = None
        self.assertTrue('capabilities' in serv, 'no capabilities')
        for cap in serv['capabilities']:
            if cap['standardID'] == 'ivo://ivoa.net/std/ConeSearch':
                #it's a cone, build a query
                tableName = cap['description']
                accessURL = cap['interfaces'][0]['accessUrls'][0]['value']
                #use AR to construct a query URL for the position given by Eduardo
                query = self.cone.constructQuery(accessURL,ra,dec,sz) 
                queries[tableName] = query
        self.assertEqual(10,len(queries),"unexpected nummber of cone capabilities: " + str(len(queries)))
        return queries
 
    def testConcurrentQuery(self):  
        "simulate astroscope"          
        count = {} # dictionary mapping from table name to result count
        threads = self.runQueries(self.constructQueries(180.0,0.0,0.2))  
        for t in threads:
            count[t.tableName] = t.rowCount
     
        count1 = {}
        threads = self.runQueries(self.constructQueries(180.0,30.0,0.2))  
        for t in threads:
            count1[t.tableName] = t.rowCount
        
        count2 = {}
        threads = self.runQueries(self.constructQueries(180.0,30.0,0.4))  
        for t in threads:
            count2[t.tableName] = t.rowCount        
        
        import pprint
        pprint.pprint(count)
        pprint.pprint(count1)
        pprint.pprint(count2)
        self.assertEquals(count,count1,"According to bug report, these should return the same")
        self.assertEquals(count1,count2,"According to bug report, these should return the same")
      

    def runQueries(self, qs):
        """given a map of tableName->query url, runs them all concurrently"""
        threads = [] # set of threads
        #create a worker for each thread, start them running concurrently
        for tableName, url in qs.iteritems():
            t = ServiceQuery(tableName, url)
            threads.append(t)
            t.start()
            #wait for all threads to complete
        
        map(lambda x: x.join(), threads)
        return threads

 

class ServiceQuery(threading.Thread):
    """ worker thread that parses a url into a votable, 
    and records the number of rows """
    def __init__(self,tableName,url):
        threading.Thread.__init__(self,name=tableName)
        self.tableName = tableName
        self.url = url
        self.rowCount = None
        
    def run(self):
        vot = VOTable()
        vot.parse(self.url) #Python VOTable library performs the query. no AR involved.
        self.rowCount= len(vot.getDataRows())
        print self.tableName, self.rowCount
     

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(bug2865)


def setupAR():
    """connect to the ar"""
    #connect to AR
    fname = os.path.expanduser("~/.astrogrid-desktop")
    assert os.path.exists(fname),  'No AR running: Please start your AR and rerun this script'
    prefix = file(fname).next().rstrip()
    #store AR in a new global variable 
    global ar 
    ar = xmlrpclib.Server(prefix + "xmlrpc")

if __name__ == '__main__':
    #setup the fixture - connection to AR.
    setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

