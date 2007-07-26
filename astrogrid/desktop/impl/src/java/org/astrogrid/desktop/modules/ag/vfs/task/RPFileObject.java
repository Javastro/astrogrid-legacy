/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.task;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileContentInfo;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSelector;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.NameScope;
import org.apache.commons.vfs.RandomAccessContent;
import org.apache.commons.vfs.impl.DefaultFileContentInfo;
import org.apache.commons.vfs.operations.FileOperations;
import org.apache.commons.vfs.provider.AbstractFileObject;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.provider.DefaultURLStreamHandler;
import org.apache.commons.vfs.provider.MoreAbstractFileObject;
import org.apache.commons.vfs.provider.UriParser;
import org.apache.commons.vfs.util.RandomAccessMode;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.RPFileName;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.ResultFileName;

/** abstract file object for tasks.
 * 
 * found I can't use AbstractFileObject - as it's implementattion depends on
 * AbstractFileName - which I'm not using. Naughty thing.
 * 
 * Anyhow, as the process filesystem is quite feature limited, should't be too hard to stub it out.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 20076:49:19 PM
 */
public abstract class RPFileObject extends MoreAbstractFileObject implements   FileObject {
    /**
     * Logger for this class
     */
    protected static final Log logger = LogFactory.getLog(RPFileObject.class);

   
    /**
     * @param name
     * @param fs
     */
    protected RPFileObject(RPFileName name, RPFileSystem fs) {
        super(name,fs);
        this.processFileSystem = fs;
    }
    
    protected final RPFileSystem processFileSystem;

    protected boolean doIsWriteable() throws Exception
    {
        return false;
    }
    
    protected boolean doDeleteSelf(FileObject file) {
        return false; // never permitted
    }

    protected long doGetContentSize() throws Exception {
        return 0; // not available.
    }


    protected FileType doGetType() throws Exception {
        return getName().getType(); // inexpensive.
    }
    
    protected FileContent doCreateFileContent() throws FileSystemException {
        return new RPFileContent();
    }
    
    
// file content object.
    public class RPFileContent implements FileContent {

        public void close() throws FileSystemException {
            if (isOpen()) {
                try {
                    is.close();
                }catch (IOException e) {
                    // ignored
                }
                is = null;
            }
        }
        public InputStream getInputStream() throws FileSystemException {
            if (! (getFile() instanceof ResultFileObject)) {
                throw new FileSystemException("can't get input stream of a folder ");
            }            
            ResultFileObject rfo = (ResultFileObject)getFile();
            is = rfo.getInputStream();
            boolean indirect = false;
            return is;
        }
        private InputStream is;
        
        public boolean isOpen() {
            return is != null;
        }

        public FileContentInfo getContentInfo() throws FileSystemException {
            if (! (getFile() instanceof ResultFileObject)) {
                throw new FileSystemException("can't get content info of a folder ");
            }             
            return new DefaultFileContentInfo(((ResultFileObject)getFile()).getContentType(),"UTF-8");//@todo fill that in.
        }        
        
        
// stuff not needed       

        public Certificate[] getCertificates() throws FileSystemException {
            return new Certificate[0];
        }

        public FileObject getFile() {
            return RPFileObject.this;
        }


        public OutputStream getOutputStream() throws FileSystemException {
            throw new FileSystemException("Not supported");            
        }

        public OutputStream getOutputStream(boolean append)
                throws FileSystemException {
            throw new FileSystemException("Not supported");
        }

        public RandomAccessContent getRandomAccessContent(RandomAccessMode mode)
                throws FileSystemException {
            throw new FileSystemException("Not supported");
        }

        public long getSize() throws FileSystemException {
            return 0;
        }
        
// attributes
        public void setAttribute(String attrName, Object value)
                throws FileSystemException {
            throw new FileSystemException("Not supported");
        }
        public Object getAttribute(String attrName) throws FileSystemException {
            return getAttributes().get(attrName);
        }

        public String[] getAttributeNames() throws FileSystemException {
            return (String[])getAttributes().keySet().toArray(new String[0]);
        }

        public Map getAttributes() throws FileSystemException {
            if (! (getFile() instanceof ResultFileObject)) {
                return MapUtils.EMPTY_MAP;
            } else {
                return ((ResultFileObject)getFile()).getAttributes();
            }       
        }
        public void setLastModifiedTime(long modTime)
                throws FileSystemException {
            throw new FileSystemException("Not supported");
        }
        public long getLastModifiedTime() throws FileSystemException {
            return 0;
        }
    
    } // end of inner class.




}

