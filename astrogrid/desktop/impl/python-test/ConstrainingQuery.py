#!/usr/bin/env python

#simple test to verify that pre-canned xqueries can be composed
import unittest

class ConstrainingQuery(unittest.TestCase):
    def testCombineQuery(self):
        coneQuery = ar.ivoa.cone.getRegistryXQuery()
        abellConeQuery = "let $cq := " + coneQuery + """
        for $r in $cq
        where contains($r/content/subject,"dwarf")
        return $r
        """
        rs = ar.ivoa.registry.xquerySearch(abellConeQuery)
        self.assertTrue(len(rs) > 0)

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(ConstrainingQuery)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    