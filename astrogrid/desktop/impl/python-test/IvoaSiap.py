#!/usr/bin/env python
#tests for the siap component

import unittest

class Siap(unittest.TestCase):
    def setUp(self):
        self.sesame = ar.cds.sesame
        self.reg = ar.ivoa.registry
        self.siap = ar.ivoa.siap
        self.SERVICE = "ivo://irsa.ipac/MAST-Scrapbook"
    def testQuery(self):
        r = self.reg.getResource(self.SERVICE)
        pos = self.sesame.resolve('crab')
        u = self.siap.constructQuery(r['id'],pos['ra'],pos['dec'],0.001)
        rows = self.siap.execute(u)
        self.assertTrue(len(rows) > 0)
        for r in rows :
            self.assertTrue('AccessReference' in r)
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Siap)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

