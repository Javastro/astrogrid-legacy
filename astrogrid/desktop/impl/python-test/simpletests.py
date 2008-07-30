#!/usr/bin/env python
#simple tests for the ar - refers to a global variable called ar.
#subsequent tests can use the same module template.

import unittest

class SimpleTest(unittest.TestCase):
    def testWebserverPort(self):
        self.assertEqual(ar.system.webserver.getPort(),8001)
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(SimpleTest)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    