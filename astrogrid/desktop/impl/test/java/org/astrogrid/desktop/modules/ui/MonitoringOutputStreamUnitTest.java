/**
 * 
 */
package org.astrogrid.desktop.modules.ui;

import static org.astrogrid.Fixture.createTempDir;
import static org.easymock.EasyMock.and;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.gt;
import static org.easymock.EasyMock.leq;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20086:36:46 PM
 */
public class MonitoringOutputStreamUnitTest extends TestCase {

    private File dir;
    private File input;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.dir = createTempDir(MonitoringOutputStreamUnitTest.class);
        URL u = this.getClass().getResource("test-resources/img.jpg");
        assertNotNull("Failed to find input resource",u);
        input = FileUtils.toFile(u);
        assertNotNull("input resource url could not be converted to file ",input);
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        dir = null;
        input = null;
    }

    public void testCopyBitwise() throws Exception {
        WorkerProgressReporter reporter = createMock(WorkerProgressReporter.class);
        reporter.setProgress(0,36); // size is 36 k
        reporter.setProgress(and(gt(0),leq(36)),eq(36)); 
        expectLastCall().times(30,40);
        
        replay(reporter);
        InputStream is = new FileInputStream(input);
        File output = new File(dir,"out.jpg");
        OutputStream os = new FileOutputStream(output);
        MonitoringOutputStream mos = new MonitoringOutputStream(
                reporter, os,input.length(),MonitoringOutputStream.ONE_KB);
        for (int i = is.read(); i > -1; i = is.read()) {
            mos.write(i);
        }
        is.close();
        mos.close();
        // check the copy was successful.
        assertTrue(IOUtils.contentEquals(new FileInputStream(input)
            ,new FileInputStream(output)));
        verify(reporter); // check that progress was reported.
                
    }

    /** copy using a buffer - so progress is reported less often */
    public void testCopyBulk() throws Exception {
        WorkerProgressReporter reporter = createMock(WorkerProgressReporter.class);
        reporter.setProgress(0,36); // size is 36 k
        reporter.setProgress(and(gt(0),leq(36)),eq(36)); 
        expectLastCall().times(5,40); // bigger chunks.
        
        replay(reporter);
        InputStream is = new FileInputStream(input);
        File output = new File(dir,"out.jpg");
        OutputStream os = new FileOutputStream(output);
        MonitoringOutputStream mos = new MonitoringOutputStream(
                reporter, os,input.length(),MonitoringOutputStream.ONE_KB);
        IOUtils.copy(is,mos);
        is.close();
        mos.close();
        // check the copy was successful.
        assertTrue(IOUtils.contentEquals(new FileInputStream(input)
            ,new FileInputStream(output)));
        verify(reporter); // check that progress was reported.
                
    }
    
}

