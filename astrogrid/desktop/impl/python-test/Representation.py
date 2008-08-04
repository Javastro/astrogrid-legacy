#!/usr/bin/env python
#Tests of code to produce __name__ and __repr__ impleentations.
# most important is that neither of these methods throws an exception.
#use this when checking for formatting of api help.
import unittest

class Representation(unittest.TestCase):
    def testStrModule(self):
        print ar.plastic.hub
    def testStrFunction(self):
        print ar.system.webserver.getRoot
    def testReprModule(self):
        print repr(ar.plastic.hub)
    def testReprFunction(self):
        print repr(ar.system.webserver.getRoot)

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Representation)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    
