/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.Selectors;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.file.Manager;

/** Implementation of the file manager component.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20083:07:23 PM
 */
public class ManagerImpl extends AbstractFileComponent implements Manager{
    
    /**
     * @param vfs
     */
    public ManagerImpl(final FileSystemManager vfs) {
        super(vfs);
    }

    
    public void createFile(final URI arg0) throws ACRException {
        try {
            fo(arg0).createFile();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public void createFolder(final URI arg0) throws ACRException {
        try {
            fo(arg0).createFolder();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public boolean delete(final URI arg0) throws ACRException {
        try {
            final int count =  fo(arg0).delete(Selectors.SELECT_ALL);
            return count > 0;
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }        
    }
    


    public void copy(final URI src, final URI dest) throws ACRException {
        nullCheck(src);
        nullCheck(dest);
        try {
            final FileObject srcO = fo(src);
            final FileObject destO = fo(dest);
            destO.copyFrom(srcO,Selectors.SELECT_ALL);
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }          
    }

    /** move doesn't work across filesystems, and also seems to 
     * fail in some other cases, when you'd expect it to work.
     * (non-existend parent folder of destination, for example).
     * - so implement using copy & delete, unless in same folder.
     */
    public void move(final URI srcU, final URI destU) throws ACRException {
        nullCheck(srcU);
        nullCheck(destU);
        try {
            final FileObject src = fo(srcU);
            final FileObject dest = fo(destU);
            final FileObject srcParent = src.getParent();
            if(srcParent != null 
                    && srcParent.equals(dest.getParent())
                    && src.canRenameTo(dest)) {
                src.moveTo(dest);
            } else {
                dest.copyFrom(src,Selectors.SELECT_ALL);
                src.delete(Selectors.SELECT_ALL);
            }
           
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }           
    }
//
//    public void copyTo(final Map<URI,String> srcs, final URI dest) throws ACRException {
//        nullCheck(dest);
//        // marshalling: convert map to a list of commands.
//        final List<CopyCommand> cmds = new ArrayList<CopyCommand>();
//        for (final Entry<URI,String> e : srcs.entrySet()) {
//            CopyCommand cp;
//            if (StringUtils.isBlank(e.getValue())) {
//                cp = new CopyCommand(e.getKey());
//            } else {
//                cp = new CopyAsCommand(e.getKey(),e.getValue());
//            }
//            cmds.add(cp);
//            final ARBulkCopyWorker worker = new ARBulkCopyWorker(dest,cmds.toArray(new CopyCommand[cmds.size()]));
//            worker.start();
//            // how do we get our result back though?
//        }
//    }
//    
//    /** extension of the bulk copy worker
//     * suitable for running in AR (i.e. not launched from EDT)
//     * 
//     * doing this, because it's nice to have some UI reporting of this
//     * long-running task (progress dialogue), but 
//     * @author Noel.Winstanley@manchester.ac.uk
//     * @since Feb 20, 200912:40:33 PM
//     */
//    private class ARBulkCopyWorker extends BulkCopyWorker {
//
//        public ARBulkCopyWorker(final URI saveLoc, final CopyCommand[] l) {
//            super(vfs, parent, saveLoc, l);
//        }
//    }
//    



    public URI[] listChildUris(final URI arg0) throws ACRException {
        final FileObject fo = fo(arg0);
        try {
            final FileObject[] children = fo.getChildren();
            final URI[] results = new URI[children.length];
            for (int i = 0; i < children.length; i++) {
                final FileObject f = children[i];
                //System.err.println(f.getURL());
                final String uri = f.getName().getURI();
                results[i] = new URI(StringUtils.replace(uri.trim()," ","%20"));
            }
            return results;
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        } catch (final URISyntaxException x) {
            throw new ACRException(x.getMessage());
        }
    }

    public String[] listChildren(final URI arg0) throws ACRException {
        final FileObject fo = fo(arg0);
        try {
            final FileObject[] children = fo.getChildren();
            final String[] results = new String[children.length];
            for (int i = 0; i < children.length; i++) {
                final FileObject f = children[i];
                results[i] = f.getName().getBaseName();
            }
            return results;
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        } 
    }

    public String read(final URI arg0) throws ACRException {
        final FileObject f = fo(arg0);
        InputStream is = null;
        try {
            is = f.getContent().getInputStream();
            return IOUtils.toString(is);
        } catch (final IOException x) {
            throw new ACRException(x.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public byte[] readBinary(final URI arg0) throws ACRException {
        final FileObject f = fo(arg0);
        InputStream is = null;
        try {
            is = f.getContent().getInputStream();
            return IOUtils.toByteArray(is);
        } catch (final IOException x) {
            throw new ACRException(x.getMessage());
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    public void refresh(final URI arg0) throws ACRException {
        try {
            fo(arg0).refresh();
        } catch (final FileSystemException x) {
            throw new ACRException(x.getMessage());
        }        
    }

    public void write(final URI arg0, final String arg1) throws ACRException {
        final FileObject f = fo(arg0);
        OutputStream os = null;
        try {
            os = f.getContent().getOutputStream();
            IOUtils.write(arg1,os);
        } catch (final IOException x) {
            throw new ACRException(x.getMessage());
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    public void writeBinary(final URI arg0, final byte[] arg1) throws ACRException {
        final FileObject f = fo(arg0);
        OutputStream os = null;
        try {
            os = f.getContent().getOutputStream();
            IOUtils.write(arg1,os);
        } catch (final IOException x) {
            throw new ACRException(x.getMessage());
        } finally {
            IOUtils.closeQuietly(os);
        }
    }


    public void append(final URI arg0, final String arg1) throws ACRException {
        final FileObject f = fo(arg0);
        OutputStream os = null;
        try {
            os = f.getContent().getOutputStream(true);
            IOUtils.write(arg1,os);
        } catch (final IOException x) {
            throw new ACRException(x.getMessage());
        } finally {
            IOUtils.closeQuietly(os);
        }        
    }


    public void appendBinary(final URI arg0, final byte[] arg1) throws ACRException {
        final FileObject f = fo(arg0);
        OutputStream os = null;
        try {
            os = f.getContent().getOutputStream(true);
            IOUtils.write(arg1,os);
        } catch (final IOException x) {
            throw new ACRException(x.getMessage());
        } finally {
            IOUtils.closeQuietly(os);
        }        
    }
    
}

