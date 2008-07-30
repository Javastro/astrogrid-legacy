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
        #verifies that the query itself isn't at fault.(without retrieving all the data)
        q = ar.ivoa.cone.getRegistryXQuery()
        sizingQuery = "let $sizeResults := ( " + q + ") return <size>{count($sizeResults)}</size>"
        result =  ar.ivoa.registry.xquerySearchXML(sizingQuery)
        
    def notestReport(self):
        #the actual report. takes ages to complete, so not run as a test
        #
        q = ar.ivoa.cone.getRegistryXQuery()
        print q
        ar.ivoa.registry.xquerySearch(q)
    def testRetrieveResourceWithOffendingharacter(self):
        #a smaller query, that returns the same kind of offending resource.
        #therre's only one of these in the registry at the moment.
        ar.ivoa.registry.getResource('ivo://KeckObs/TKRS')
   
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Report)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    
