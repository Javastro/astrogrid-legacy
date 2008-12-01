#!/usr/bin/env python
#test equivalence of different forms of xquery
import unittest

class XQueryTrials(unittest.TestCase):
    def setUp(self):
        xq = """
        for $r in //vor:Resource[not (@status='inactive' or @status='deleted')] 
where $r/@xsi:type  &=  '*DataCollection' 
return $r
"""
        rs = ar.ivoa.registry.xquerySearch(xq)
        self.assertTrue(len(rs) > 0)
        self.expectedSize = len(rs)

    def testShorterForm(self):
        xq = """
        for $r in //vor:Resource where $r/@xsi:type  &=  '*DataCollection' 
        return $r
        """
        rs = ar.ivoa.registry.xquerySearch(xq)
        self.assertEqual(self.expectedSize,len(rs))
        
    def testShortestForm(self):
        xq = "//vor:Resource[@xsi:type  &=  '*DataCollection']"
        rs = ar.ivoa.registry.xquerySearch(xq)
        self.assertEqual(self.expectedSize,len(rs))

    def testCustomXQuery(self):
        xq = """
	<ssap-wavebands> 
          {
          (:find all spectral services :)
           let $ssap := //vor:Resource[capability/@standardID="ivo://ivoa.net/std/SSA"]          
           (: find the distinct set of wavebands these services cover  (no duplicates) :)
           for $waveband in distinct-values($ssap/coverage/waveband)
           order by $waveband
           (: print each waveband in turn :)
           return <band name="{data($waveband)}">
                  {
                  (: list IDs of all services that cover this band :)
                    for $r in $ssap[coverage/waveband=$waveband]
                    return $r/identifier
                  }
                  </band> 
          }
       </ssap-wavebands>
        """
        doc = ar.ivoa.registry.xquerySearchXML(xq)
        #if we've got this far without error, it's a pass.

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(XQueryTrials)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    
