#!/usr/bin/env python
#test error transport on malformed xquery - want to make sure debug info from registry gets back to client.
#correct line numbers, in  full query, too.
import unittest
from xmlrpclib import Fault
from pprint import pprint

class XQueryError(unittest.TestCase):
    def setUp(self):
        #deliberate error - bin instead of in
        self.xq = """
        for $r bin //vor:Resource where $r/@xsi:type  &=  '*DataCollection' 
        return $r
        """
        self.reg = ar.ivoa.registry
        
    def testErrorTransport(self):
        try:
            self.reg.xquerySearch(self.xq)
            self.fail("expected to throw")
        except Fault, f:
            self.assertTrue(f.faultString.find("Parse error") != -1)
            
def suite():
    return unittest.TestLoader().loadTestsFromTestCase(XQueryError)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    