#!/usr/bin/env python
#tests that no problems occur when performing very large registry queries.

import unittest

class LargeRegistryQuery(unittest.TestCase):
      
    def testReport(self):
        #a huge query. takes ages to complete.
        #want to check - that the 'prevent overlarge queries' preference doesn't affect this
        #and that the query does get back, eventually, without barfing.
        
        #ok. now barfes to to memory exhaustion. result! (of a kind)
        q = ar.ivoa.cone.getRegistryXQuery()
        rs = ar.ivoa.registry.xquerySearch(q)
        print len(rs)
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(LargeRegistryQuery)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    