/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;

import junit.framework.TestCase;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.VFS;
import org.astrogrid.acr.util.Tables;

/** test cases to replicate error seen in bz 2116
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 20, 20071:10:51 PM
 */
public class Tables2116UnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        tables = new TablesImpl(VFS.getManager());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        tables = null;
    }
    
    Tables tables;
    public void testCsvInline() throws Exception  {

            StrBuilder sb = new StrBuilder();
            sb.appendln("name,ra,dec");
            sb.appendln("s1,14.0,50.0");
            sb.appendln("s2,14.0,51.0");
            tables.convert(sb.toString(),"csv","votable");
       
    }    
    public void testCsvFile() throws Exception  {
        File tmp = null;        
        PrintWriter bw = null;
        try {
            tmp = File.createTempFile("2116",".csv");
            tmp.deleteOnExit();
            bw = new PrintWriter(new FileWriter(tmp));
            bw.println("name,ra,dec");
            bw.println("s1,14.0,50.0");
            bw.println("s2,14.0,51.0");
            
        } catch (IOException x) {
            fail("unable to create input file");
        } finally {
            if (bw != null) {
                bw.close();
            }
        }
        URI csvURI = tmp.toURI();
            tables.convertFromFile(csvURI,"csv","votable");
       
    }

}
