/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.file.Manager;
import org.astrogrid.acr.file.Name;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** test for name impl.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 18, 20094:17:27 PM
 */
public class NameImplIntegrationTest extends InARTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        m = (Manager)assertServiceExists(Manager.class,"file.manager");
        n = (Name)assertServiceExists(Name.class,"file.name");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
         m = null;
         n = null;
    }
    
    private Manager m;
    private Name n;

    public static Test suite() {
        return new ARTestSetup(new TestSuite(NameImplIntegrationTest.class));
    }    
    

    URI root = URI.create("http://www.foo.bar/");
    URI rootWithoutSlash = URI.create("http://www.foo.bar");
    URI file = URI.create("http://www.foo.bar/coo/boo/file.txt");
    URI folder = URI.create("http://www.foo.bar/coo/boo");
    

    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#getScheme(java.net.URI)}.
     */
    public void testGetScheme() throws Exception {
       assertEquals("http",n.getScheme(root));
       assertEquals("http",n.getScheme(file));       
       try {
           n.getScheme(null);
           fail("expected to fail");
       } catch (final InvalidArgumentException e) {
           // ok.
       }
       
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#getRoot(java.net.URI)}.
     */
    public void testGetRoot() throws Exception {
        assertEquals(root, n.getRoot(file));
        assertEquals(root, n.getRoot(root));
        assertEquals(root,n.getRoot(rootWithoutSlash));
        try {
            n.getRoot(null);
            fail("expected to fail");
        } catch (final InvalidArgumentException e) {
            // ok.
        }        
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#getName(java.net.URI)}.
     */
    public void testGetName()throws Exception  {
        assertEquals("file.txt",n.getName(file));
        assertEquals("",n.getName(root));
        assertEquals("",n.getName(rootWithoutSlash));
        assertEquals("boo",n.getName(folder));
        try {
            n.getName(null);
            fail("expected to fail");
        } catch (final InvalidArgumentException e) {
            // ok.
        }  
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#getExtension(java.net.URI)}.
     */
    public void testGetExtension() throws Exception {
        assertEquals("txt",n.getExtension(file));
        assertEquals("",n.getExtension(root));
        assertEquals("",n.getExtension(rootWithoutSlash));
        assertEquals("",n.getExtension(folder));
        try {
            n.getExtension(null);
            fail("expected to fail");
        } catch (final InvalidArgumentException e) {
            // ok.
        }  
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#getPath(java.net.URI)}.
     */
    public void testGetPath() throws Exception {
        assertEquals("/coo/boo/file.txt",n.getPath(file));
        assertEquals("/",n.getPath(root));
        assertEquals("/",n.getPath(rootWithoutSlash));
        assertEquals("/coo/boo",n.getPath(folder));
        try {
            n.getPath(null);
            fail("expected to fail");
        } catch (final InvalidArgumentException e) {
            // ok.
        }  
        
        
        // now ., ..
        assertEquals("/foo/file.txt"
                ,n.getPath(new URI("http://foo.bar.com/foo//bar/.././file.txt"))
                );
        
        // trailing /
        assertEquals("/coo/boo",n.getPath(new URI(folder.toString() + "/")));
    }


    
    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#getParent(java.net.URI)}.
     */
    public void testGetParent() throws Exception {
        
        assertEquals(folder,n.getParent(file));
        assertEquals(new URI("http://www.foo.bar/coo"),n.getParent(folder));
        try {
            n.getParent(root);
            fail("expected to fail");
        } catch (final InvalidArgumentException e) {
        }
        try {
            n.getParent(rootWithoutSlash);
            fail("expected to fail");
        } catch (final InvalidArgumentException e) {
        }        
        try {
            n.getParent(null);
            fail("expected to fail");
        } catch (final InvalidArgumentException e) {
            // ok.
        } 
    }



    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#isAncestor(java.net.URI, java.net.URI)}.
     */
    public void testIsAncestor() throws Exception {
        // nothing is an ancestor of itself
        assertFalse(n.isAncestor(file,file));
        assertFalse(n.isAncestor(root,root));
        assertFalse(n.isAncestor(rootWithoutSlash,rootWithoutSlash));
        assertFalse(n.isAncestor(root,rootWithoutSlash));
        assertFalse(n.isAncestor(rootWithoutSlash,root));
        
        
        assertTrue(n.isAncestor(file,root));
        assertTrue(n.isAncestor(file,rootWithoutSlash));
        assertTrue(n.isAncestor(file,folder));
        assertTrue(n.isAncestor(folder,root));
        
        assertFalse(n.isAncestor(folder,file));
        assertFalse(n.isAncestor(root,folder));
        assertFalse(n.isAncestor(root,file));
        
        // different domain altogether
        final URI other = new URI("http://www.slashdot.org/");
        assertFalse(n.isAncestor(file,other));
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#relativize(java.net.URI, java.net.URI)}.
     */
    public void testRelativize()throws Exception  {
       assertEquals("file.txt",n.relativize(folder,file));
       assertEquals(n.getPath(file),"/" + n.relativize(root,file));
       assertEquals(n.getPath(file),"/" + n.relativize(rootWithoutSlash,file));  
//       urg! server name is ignored altogether.       
//       final URI other = new URI("http://www.slashdot.org/articles");
//       final String s= n.relativize(file,other);
//       System.err.println(s);
//       assertEquals(other.toString()
//               ,s
//               );
       
       
       assertEquals("..",n.relativize(file,folder));
       assertEquals("../../..",n.relativize(file,root));
       assertEquals(".",n.relativize(file,file));
       assertEquals(".",n.relativize(root,rootWithoutSlash));
    }

    /**
     * Test method for {@link org.astrogrid.desktop.modules.file.NameImpl#resolve(java.net.URI, java.lang.String)}.
     */
    public void testResolve() throws Exception {
        final String path = n.relativize(root,file);
        roundTrip(root,file);
        roundTrip(rootWithoutSlash,file);
        roundTrip(folder, file);
        roundTrip(file,folder); //!! works with .. too
        
        // leading slash acts as 'root'
        assertEquals(
                n.resolve(root,"file.txt")
                ,n.resolve(folder,"/file.txt"));
        // verify that spaces get escaped
        assertEquals(
                n.resolve(root,"foo%20bar/file.txt")
                ,n.resolve(root,"foo bar/file.txt")
                );
        
    }
        private void roundTrip(final URI root,final URI u) throws ACRException{
            final String path = n.relativize(root,u);
            assertEquals(u,
                    n.resolve(root,path));            
        }
}
