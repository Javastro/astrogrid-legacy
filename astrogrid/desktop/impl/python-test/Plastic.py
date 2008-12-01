#!/usr/bin/env python
#exercise the plastic hub.

import unittest

class Plastic(unittest.TestCase):
    def setUp(self):
        self.hub = ar.plastic.hub
        self.id = self.hub.getHubId()
        l = self.hub.getRegisteredIds()
        l.remove(self.id)
        self.vdId = None
        for id in l:
            n = self.hub.getName(id)
            if n == 'VO Desktop':
                self.vdId = id
        self.assertTrue(self.vdId != None,"Vodesktop not registered with plastic?")
        self.ECHO = 'ivo://votech.org/test/echo'
        self.ownName = 'testScript' 
        
    def testHubRegistered(self):
        self.assertTrue(self.id in self.hub.getRegisteredIds())
    
    def testHubName(self):
        self.assertEquals("hub",self.hub.getName(self.id))
    
    def testHubUnderstoodMessages(self):
        l = self.hub.getUnderstoodMessages(self.id)
        self.assertTrue(len(l) > 3)
        self.assertTrue(self.ECHO in l)
        
    def testAppUnderstoodMessages(self):
        l = self.hub.getUnderstoodMessages(self.vdId)
        self.assertTrue(len(l) > 6)
        self.assertTrue(self.ECHO in l)
        
    def testMessageRegisteredIds(self):
        ids = self.hub.getMessageRegisteredIds(self.ECHO)
        self.assertTrue(self.id in ids)
        self.assertTrue(self.vdId in ids)
        self.assertEquals(0,len(self.hub.getMessageRegisteredIds('ivo://unknown')))
        
    def testRegister(self):
        prevSize = len(self.hub.getRegisteredIds())
        myId = self.hub.registerNoCallBack(self.ownName )
        ids = self.hub.getRegisteredIds()
        self.assertEquals(prevSize,len(ids) - 1)
        self.assertTrue(myId in ids)
        self.hub.unregister(myId)
        ids = self.hub.getRegisteredIds()
        self.assertEquals(prevSize,len(ids))
        self.assertFalse(myId in ids)
        
    def testRequest(self):
        myId = self.hub.registerNoCallBack(self.ownName)
        r = self.hub.request(myId,self.ECHO,['msg'])
        self.hub.unregister(myId)
        self.assertTrue(self.id in r)
        self.assertTrue(self.vdId in r)
        self.assertEquals('msg',r[self.id])
        self.assertEquals('msg',r[self.vdId])


    def testRequestToSubset(self):
        myId = self.hub.registerNoCallBack(self.ownName)
        r = self.hub.requestToSubset(myId,self.ECHO,['msg'],[self.id,self.vdId])
        self.hub.unregister(myId)
        self.assertTrue(self.vdId in r,msg='VODesktop did not respond')
        self.assertTrue(self.id in r,msg='hub did not respond')
        self.assertEquals('msg',r[self.id])
        self.assertEquals('msg',r[self.vdId])        
        self.assertEquals(2,len(r))

    def testRegisterAsMockService(self):        
        myId = self.hub.registerXMLRPC(self.ownName,[self.ECHO],'http://localhost:7090')
        self.assertTrue(myId in self.hub.getRegisteredIds())
        self.assertTrue(myId in self.hub.getMessageRegisteredIds(self.ECHO))
        self.hub.unregister(myId)
        self.assertTrue(myId not in self.hub.getRegisteredIds())
        self.assertTrue(myId not in self.hub.getMessageRegisteredIds(self.ECHO))
        
    def testRegisterAsService(self):
        from SimpleXMLRPCServer import SimpleXMLRPCServer
        server = SimpleXMLRPCServer(('localhost',7090))
        server.register_introspection_functions()
        def fn(sender,message,args):
            if message == self.ECHO:
                return args[0]
        server.register_function(fn,"perform")
        import thread
        thread.start_new_thread(server.serve_forever,())
        
        servId = self.hub.registerXMLRPC('testService',[self.ECHO],'http://localhost:7090')
        myId = self.hub.registerNoCallBack(self.ownName)
        
        r = self.hub.request(myId,self.ECHO,['msg'])
        self.assertTrue(self.id in r)
        self.assertTrue(self.vdId in r)
        self.assertTrue(servId in r)
        self.assertEquals('msg',r[self.id])
        self.assertEquals('msg',r[self.vdId])
        self.assertEquals('msg',r[servId])        

        r = self.hub.requestToSubset(myId,self.ECHO,['msg'],[servId])
        self.assertTrue(servId in r,msg='service did not respond to subset message')
        self.assertEquals('msg',r[servId])  
        self.assertEquals(1,len(r))
        
        self.hub.unregister(myId)
        self.hub.unregister(servId)

#sadly it's too hard to clean up all the stuff that this one creates.
    def noTestRegisterAsAsynchService(self):
        from SimpleXMLRPCServer import SimpleXMLRPCServer
        server = SimpleXMLRPCServer(('localhost',7091))
        server.register_introspection_functions()
        def fn(sender,message,args):
            print sender, message, args
            if message == self.ECHO:
                self.seen = True
                return args[0]
        fn.seen = False
        server.register_function(fn,"perform")
        import thread
        thread.start_new_thread(server.serve_forever,())
        
        servId = self.hub.registerXMLRPC('testService',[self.ECHO],'http://localhost:7091')
        myId = self.hub.registerNoCallBack(self.ownName)
        
        import time
        self.hub.requestAsynch(myId,self.ECHO,['msg'])
        self.assertFalse(fn.seen)
        time.sleep(5)
        self.assertTrue(fn.seen)
        fn.seen = False
        self.hub.requestToSubsetAsynch(myId,self.ECHO,['msg'],[servId])
        time.sleep(5)
        self.assertTrue(fn.seen)
        fn.seen = False
        
        self.hub.unregister(myId)
        self.hub.unregister(servId)
        
def suite():
    return unittest.TestLoader().loadTestsFromTestCase(Plastic)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=1).run(suite())
    
