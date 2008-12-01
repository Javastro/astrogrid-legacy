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
        service = "ivo://archive.eso.org/ESO-SAF-SSAP"
        #resolve an object name to a position
        pos = ar.cds.sesame.resolve('m1')
        ssap = ar.ivoa.ssap #take a reference to the AR SSAP component
        #build a query
        query = ssap.constructQuery(service,pos['ra'],pos['dec'],0.005)
        print "QueryURL",query
        #execute the query
        rows = ssap.execute(query)
        #inspect what we've got.
        print "Rows Returned",len(rows)
        print "Metadata Keys",rows[0].keys()
        print "Image URLs"
        #for r in rows :
        #    print r['AccessReference']
        #download first three datasets into current directory
        #compute url for current directory
        from urlparse import urlunsplit
        from os import getcwd
        currentDirURL = urlunsplit(['file','',getcwd(),'',''])
        print "Downloading images to",currentDirURL
        ssap.saveDatasetsSubset(query,currentDirURL,[0,1,2])
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Siap)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

