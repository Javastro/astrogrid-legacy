#!/usr/bin/env python
#simple tests for the ar - refers to a global variable called ar.
#subsequent tests can use the same module template.

import unittest

class CdsVizier(unittest.TestCase):
    def setUp(self):
        self.viz = ar.cds.vizier
    def testCataloguesMetaData(self):
        self.viz.cataloguesMetaData("m32",0.1,"arcsec","")
        
    def testCataloguesMetaDataWavelength(self):
        self.viz.cataloguesMetaDataWavelength("m32",0.1,"arcsec","","optical")        

    def testCataloguesData(self):
        self.viz.cataloguesData("m32",0.1,"arcsec","")
        
    def testCataloguesDataWavelength(self):
        self.viz.cataloguesDataWavelength("m32",0.1,"arcsec","","optical")   

    def testMetaAll(self):
        self.viz.metaAll()

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(CdsVizier)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

