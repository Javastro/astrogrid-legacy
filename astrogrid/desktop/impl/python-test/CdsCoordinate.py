#!/usr/bin/env python
#system test for the coordinate component of CDS module.
import unittest

class CdsCoordinate(unittest.TestCase):
    def setUp(self):
        self.c = ar.cds.coordinate
    def testConvert(self):
        self.assertEquals("03 45 14.3838 +47 58 07.990 (J2000.0)"
                          ,self.c.convert(10.0,15.0,20.0,8))
        
    def testConvertL(self):
        self.assertEquals("00 48 00.0 +45 00 0. (J2000.0)"
                          ,self.c.convertL(12.0,45.0,5))
    def testConvertE(self):
        self.assertEquals("150.4806267 -05.3873952 (Gal)"
                          ,self.c.convertE(1,2,10.0,15.0,20.0,8,2000.0,1900.0))
    def testConvertLE(self):
        self.assertEquals("122.2936260 -17.8674235 (Gal)"
                          ,self.c.convertLE(1,2,12.0,45.0,8,2000.0,1900.0))
       

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(CdsCoordinate)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

