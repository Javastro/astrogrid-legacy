#!/usr/bin/env python
#Tests for new file  components.
# unit and integration tests have already tested with tmp:/ 
# here test with remove filesystems.
#will require login to myspace.
import unittest

class File(unittest.TestCase):
    def setUp(self):
        self.m = ar.file.manager
        self.i = ar.file.info
        self.n = ar.file.name

    def testWorkspace(self):
        #verify that workspace:/ maps to myspace.
        ws = 'workspace:/'
        self.assertTrue(self.i.exists(ws))
        self.assertTrue(self.i.isFolder(ws))
        self.assertFalse(self.i.isFile(ws))

    def testWorkspaceManip(self):
        #find a unique directory name.
        ws = 'workspace:/'
        dir = ''
        children = self.m.listChildren(ws)
        for ix in range(1,1000):
            candidate = 'systemtest' + str(ix)
            if candidate not in children:
                dir = ws + candidate
                break
        self.m.createFolder(dir)
        try: 
            self.assertTrue(self.i.exists(dir))
            self.assertTrue(self.i.isFolder(dir))
            # not possible to get attribs from a folder.
            #attr = self.i.getAttributes(dir)            
            #  print self.i.getContentType(dir)
            #create another file, write some data into it, check we cah retriev it
            f = dir + "/foo/bar/file.txt"
            txt = "here is some text"
            self.m.write(f,txt)
            self.assertTrue(self.i.exists(f))
            self.assertEquals(txt,self.m.read(f))
            self.assertTrue(self.i.isReadable(f))
            self.assertTrue(self.i.isWritable(f))
            self.m.refresh(f)
            #check attribs has read content url
            m = self.i.getAttributes(f)
            self.assertTrue('ContentURL' in m)
            self.assertTrue('ContentMethod' in m)
            
            self.assertTrue(self.i.getSize(f) > 0)
            self.assertEquals("text/plain",self.i.getContentType(f))
            
            # check we can ls it.
            bar = self.n.getParent(f)
            self.assertTrue(self.n.isAncestor(f,bar))
            self.assertTrue(self.i.exists(bar))
            self.assertTrue(self.i.isFolder(bar))
            self.assertTrue('file.txt' in self.m.listChildren(bar))
            #try a download
            tmp = "tmp://foo/file.txt"
            self.m.copy(f,tmp)
            self.assertTrue(self.i.exists(tmp))
            self.assertEqual(txt,self.m.read(tmp))
            
            #copy elsewhere in myspace.
            elsewhere = self.n.getParent(bar) + "/blink/fink.txt"
            self.m.copy(f,elsewhere)
            self.assertTrue(self.i.exists(elsewhere))
            self.assertEqual(txt,self.m.read(elsewhere))
            
            #now try a move in myspace.
            fink = bar + "/fink.txt"
            self.m.move(f,fink)
            self.assertTrue(self.i.exists(fink))
            self.assertFalse(self.i.exists(f))
            self.assertEqual(txt,self.m.read(fink))
            
            #check a deletion            
            self.m.delete(elsewhere)
            self.assertFalse(self.i.exists(elsewhere))
        finally:
            self.m.delete(dir)
        
def suite():
    return unittest.TestLoader().loadTestsFromTestCase(File)


if __name__ == '__main__':
    import alltests
    alltests.setupAR()
    #run the tests.
    unittest.TextTestRunner(verbosity=2).run(suite())
    