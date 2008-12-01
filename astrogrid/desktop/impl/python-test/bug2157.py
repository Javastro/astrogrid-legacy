#!/usr/bin/env python
#Test that myspace files can be overwritten.

import unittest

#NB - if SDSS is broken, all these tests will fail - no fault of ours.
serviceQuery = 'http://casjobs.sdss.org/vo/DR4SIAP/SIAP.asmx/getSiapInfo?BANDPASS=ugriz&POS=180.0%2C2.0&SIZE=1.0&FORMAT=ALL'

class Bug2157(unittest.TestCase):
    def setUp(self):
        self.ms = ar.astrogrid.myspace
        self.siap = ar.ivoa.siap
        self.home = self.ms.getHome()
    def testService(self):
        self.siap.executeVotable(serviceQuery)
    def testNonExistentFileInHomeDir(self):
        output = self.home + "/2157.vot"
        if self.ms.exists(output):
            self.ms.delete(output)
        self.failIf(self.ms.exists(output))
        self.siap.executeAndSave(serviceQuery, output)
        self.assertTrue(self.ms.exists(output))
    def testExistingFileInHomeDir(self):
        output = self.home + "/2157.vot"
        if self.ms.exists(output):
            self.ms.delete(output)
        self.failIf(self.ms.exists(output))
        #create a file with known content.
        txt = "this is some text"
        self.ms.write(output,txt)
        #verify this
        self.assertTrue(self.ms.exists(output))
        self.assertEquals(txt,self.ms.read(output))
        #do the query
        self.siap.executeAndSave(serviceQuery, output)
        #verify content has been overwritten        
        self.assertTrue(self.ms.exists(output))
        vot = self.ms.read(output)
        self.assertNotEquals(txt,vot)
        self.assertTrue(str(vot).startswith("<?xml"))



def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Bug2157)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())


