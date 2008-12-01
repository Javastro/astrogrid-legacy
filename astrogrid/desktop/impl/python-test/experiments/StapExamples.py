#!/usr/bin/env python

#tests for the siap component

import unittest

class Siap(unittest.TestCase):
    def setUp(self):
        self.sesame = ar.cds.sesame
        self.reg = ar.ivoa.registry
        self.siap = ar.ivoa.siap
    def testStraightQuery(self):
        #the cone service to query (selected using voexplorer)
        service = "ivo://mssl.ucl.ac.uk/stap-lasco"
        from xmlrpclib import DateTime
        stap = ar.astrogrid.stap #take a reference to the AR STAP component
        #build a query
        start = DateTime('20000101T00:00:00')
        end = DateTime('20000102T00:00:00')
        query = stap.constructQuery(service,start,end)
        print "QueryURL",query
        #execute the query
        rows = stap.execute(query)
        #inspect what we've got.
        print "Rows Returned",len(rows)
        import pprint
        pprint.pprint(rows[0])
        #print "Image URLs"
        #for r in rows :
        #    print r['AccessReference']
        #download first datasets into current directory
        #compute url for current directory
        from urlparse import urlunsplit
        from os import getcwd
        currentDirURL = urlunsplit(['file','',getcwd(),'',''])
        print "Downloading data to",currentDirURL
        stap.saveDatasetsSubset(query,currentDirURL,[0])
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Siap)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

