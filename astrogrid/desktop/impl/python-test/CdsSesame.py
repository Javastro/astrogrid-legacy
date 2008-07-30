#system test for the coordinate component of CDS module.
import unittest
import xmlrpclib

class CdsSesame(unittest.TestCase):
    def setUp(self):
        self.s = ar.cds.sesame
    def testResolve(self):
        b =  self.s.resolve("crab")
        self.assertEquals("crab",b['target'])
    def testResolveM32(self):
        b =  self.s.resolve("m32")
        self.assertEquals("m32",b['target'])
        self.assertTrue(len(b['aliases']) > 0)
    def testResolveWithSpace(self):
        b =  self.s.resolve("NGC 4321")
        self.assertEquals("NGC 4321",b['target'])
        self.assertTrue(len(b['aliases']) > 0)
    def testResolveUnknown(self):
        try:
            self.s.resolve("unknown object")
            self.fail("expected to fail")
        except xmlrpclib.Fault, x:
            pass
def suite():
    return unittest.TestLoader().loadTestsFromTestCase(CdsSesame)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    



