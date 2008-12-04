#!/usr/bin/env python
#tests for bug 2864
# repeated queries to multi-cone capability service from astroscope gives different results each time.
# this script simulates the behaviour of Astroscope - see if we can replicate the error
# and so demonstrate it's a server-side problem.

import unittest
import os.path
from VOTable import VOTable
import threading
import xmlrpclib

#the service in question.
service = 'ivo://uk.ac.cam.ast/iphas-dsa-catalog/IDR'



class bug2864(unittest.TestCase):

    def setUp(self):
        """ sets up the unit test """
        self.reg = ar.ivoa.registry
        self.cone = ar.ivoa.cone
        self.vosi = ar.ivoa.vosi
        
    def testApplicationExists(self):
        """verify IDR is registered"""
        try:
            self.reg.getResource(service)
        except xmlrpclib.Fault:
            self.fail(service + " not found")
            
    def testApplicationIsUp(self):
        """ use vosi to verify IDR is up """
        avail = self.vosi.checkAvailability(service)
        self.assertTrue(avail['available'],
                        "The service is unavailable, other tests will fail: " + str(avail))
       
    def constructQueries(self):
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
                query = self.cone.constructQuery(accessURL,90,22.5,0.1) #was 0.2
                queries[tableName] = query
        self.assertEqual(5,len(queries),"unexpected nummber of cone capabilities")
        return queries
 
    def testConcurrentQuery(self):  
        "simulate astroscope"  
        qs = self.constructQueries()
        count = {} # dictionary mapping from table name to result count
        
        for i in range(10): # repeat the concurrent fetch 10 times.
            print "loop " + str(i)
            threads = [] # set of threads
            #create a worker for each thread, start them running concurrently
            for tableName,url in qs.iteritems():
                t = ServiceQuery(tableName,url)
                threads.append(t)
                t.start()
            #wait for all threads to complete
            map(lambda x : x.join(), threads)  
            #check result of each thread
            for t in threads:
                if i == 0: # first time round the loop, just record the row count
                    count[t.tableName] = t.rowCount
                else: # compare to previously seen count
                    expected = count[t.tableName]
                    actual = t.rowCount
                    self.assertEquals(expected, actual
                                      , "Expected %s rows but got %s for %s" % (expected,actual,t.tableName))
        #everything ok!
        print 'Ran 10 times, returned identical results each time'
        print count
 

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
        try:
            self.rowCount= len(vot.getDataRows())
            print self.tableName, self.rowCount
        except AttributeError:
            #see what kind of error reporting we've got instead.
            print vot.printAllNodes()

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(bug2864)


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
    

