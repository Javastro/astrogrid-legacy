#!/usr/bin/env python
#test for the vosi availability component.

import unittest
from xmlrpclib import DateTime
service = 'ivo://wfau.roe.ac.uk/ukidssDR1-dsa/wsa'

class Vosi(unittest.TestCase):
    def testServiceExists(self):
        try:
            ar.ivoa.registry.getResource(service)
        except Fault:
            self.fail(service + " not found - subsequent tests will fail")
    def testVosi(self):
        b = ar.ivoa.vosi.checkAvailability(service)
#        can't say much about the status of the service
#       just check that b is sensible.
        self.assertTrue(isinstance(b['available'],bool),"Expected 'available' to be a boolean")
        if b['available']:
            self.assertTrue( 'upSince' in b)
            self.assertTrue(isinstance(b['upSince'],DateTime),"Expected 'upsince' to be a date")
        else:
            #dunno what else I can test.
            pass
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Vosi)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    