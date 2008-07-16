#!/usr/bin/env python
#$ xq=acr.ivoa.cone.getRegistryXQuery()
#
#$ print xq
#//vor:Resource[((capability/@xsi:type &= '*ConeSearch')  or (capability/@standardID = 'ivo://ivoa.net/std/ConeSearch')) and ( not ( @status = 'inactive' or @status='deleted'))]
#
#$ acr.ivoa.registry.xquerySearch(xq)
#Fault: <Fault 0: 'org.apache.xmlrpc.XmlRpcException: Invalid character data corresponding to XML entity &#8217;'>

import unittest

class Report(unittest.TestCase):
    def testSizeProblem(self):
        #verifies that the query itself isn't at fault.
        q = ar.ivoa.cone.getRegistryXQuery()
        print q
        sizingQuery = "let $sizeResults := ( " + q + ") return <size>{count($sizeResults)}</size>"
        print sizingQuery
        print ar.ivoa.registry.xquerySearchXML(sizingQuery)
    def testReport(self):
        #the actual report. I suspect it's something in the xml return that's the problem.
        q = ar.ivoa.cone.getRegistryXQuery()
        print q
        print ar.ivoa.registry.xquerySearch(q)
        

def suite():
    #suite = unittest.TestSuite()
    #return suite
    return unittest.TestLoader().loadTestsFromTestCase(Report)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    
