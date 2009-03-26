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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 25, 20088:12:13 PM
 */
public class MonitoringInputStreamUnitTest extends TestCase {
    private File input;
    private File dir;
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
        MonitoringInputStream mis = new MonitoringInputStream(
                reporter, is,input.length(),MonitoringInputStream.ONE_KB);
        for (int i = mis.read(); i > -1; i = mis.read()) {
            os.write(i);
        }
        os.close();
        mis.close();
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
        MonitoringInputStream mis = new MonitoringInputStream(
                reporter, is,input.length(),MonitoringInputStream.ONE_KB);        
        IOUtils.copy(mis,os);
        os.close();
        mis.close();
        // check the copy was successful.
        assertTrue(IOUtils.contentEquals(new FileInputStream(input)
            ,new FileInputStream(output)));
        verify(reporter); // check that progress was reported.
                
    }
    
    
    
}
