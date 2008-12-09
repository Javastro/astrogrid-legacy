#!/usr/bin/env python
#tests for the sessioning system.
# takes time to run - so excluded from the 

import unittest
from time import sleep
import xmlrpclib

class Sessions(unittest.TestCase):
    def setUp(self):
        self.sess = ar.builtin.sessionManager
    def testCreateExistsDispose(self):
        id = self.sess.createNewSession(1)
        self.assertTrue(self.sess.exists(id))
        self.sess.dispose(id)
        self.assertFalse(self.sess.exists(id))
        
    def testExpiration(self):
        id = self.sess.createNewSession(1)
        self.assertTrue(self.sess.exists(id))
        sleep(30)
        self.assertTrue(self.sess.exists(id))
        sleep(35)
        self.assertFalse(self.sess.exists(id))        
     
    def testExpirationReset(self):
        id = self.sess.createNewSession(1)
        self.assertTrue(self.sess.exists(id))
        #access the new session endpoint
        endpoint = self.sess.findXmlRpcSession(id)
        sessAR = xmlrpclib.Server(endpoint)
        #wait till within 15s of expiry.
        sleep(45)
        self.assertTrue(self.sess.exists(id))
        #call a function, to prolong the session.
        sessAR.system.configuration.listKeys()
        #wait a little longer
        sleep(30)#15s over first expiry
        self.assertTrue(self.sess.exists(id))      
        # now wait just long enough for session to expire.
        sleep(35) #5 over second expiry
        self.assertFalse(self.sess.exists(id))          

def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Sessions)



if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    