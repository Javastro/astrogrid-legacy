#!/usr/bin/env python

#!/usr/bin/env python

#tests for the siap component

import unittest

class Cone(unittest.TestCase):
    def setUp(self):
        self.sesame = ar.cds.sesame
        self.reg = ar.ivoa.registry
        self.siap = ar.ivoa.siap
    def testStraightQuery(self):
        #the Cone service to query (selected using voexplorer)
        service = "ivo://irsa.ipac/2MASS-PSC"
        #resolve an object name to a position
        pos = ar.cds.sesame.resolve('m54')
        cone = ar.ivoa.cone #take a reference to the AR Cone component
        #build a query
        query = cone.constructQuery(service,pos['ra'],pos['dec'],0.001)
        #print "QueryURL",query
        ##execute the query, to return a map that can be inspected programmatically
        #rows = cone.execute(query)
        ##inspect what we've got.
        #print "Rows Returned",len(rows)
        #print "Metadata Keys",rows[0].keys()
        #import pprint
        #pprint.pprint(rows[0])
        ## execute the query, to return a votable document
        #votable = cone.executeVotable(query)
        #print votable
        #
        ##execute the query, and convert to another format.    
        #csv = ar.util.tables.convertFromFile(query,"votable","csv")
        #
        ##execute the query, and save as another format.
        #from tempfile import mktemp
        #from urlparse import urlunsplit
        #from os.path import abspath
        ##create a temporary file
        #tmpFile = mktemp("html")
        ## find the file URL for the temporary file
        #tmpURL = urlunsplit(['file','',abspath(tmpFile),'','']) 
        ##convert votable result to html, saving to a local file
        #ar.util.tables.convertFiles(query,"votable",tmpURL,"html")
        ##display temporary html file in browser
        #ar.system.browser.openURL(tmpURL)
        #
        ##pass the query to a plastic application to perform and display
        plastic = ar.plastic.hub
        try:
            myId = plastic.registerNoCallBack("exampleScript")
            print myId
            # broadcast a message. - tells the plastic apps to go get this table themselves.
            plastic.requestAsynch(myId,'ivo://votech.org/votable/loadFromURL',[query,query])            
        finally:
            #clean up, by unregistering
            plastic.unregister(myId) 
        #
        

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Cone)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    

