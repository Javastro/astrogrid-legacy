/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.NameScope;
import org.apache.commons.vfs.provider.AbstractFileName;
import org.apache.commons.vfs.provider.AbstractFileNameParser;
import org.apache.commons.vfs.provider.FileNameParser;
import org.apache.commons.vfs.provider.VfsComponentContext;
import org.astrogrid.desktop.modules.ag.RemoteProcessManagerInternal;


/** parser for process names.
 * 
 * cargo cult programming - need to check this.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 20075:03:31 PM
 */
public class RPParser extends AbstractFileNameParser implements
FileNameParser {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(RPParser.class);

    private final RemoteProcessManagerInternal rpmi;
    private final RootFileName root;

    public FileName parseUri(VfsComponentContext context, FileName base,
            String filename) throws FileSystemException {
        try {
        // first drop the scheme... dunnow why I'm getting three forms forms...
        if (filename.startsWith("task:///")) {
            filename = StringUtils.substringAfter(filename,"task:///");
        } else if (filename.startsWith("task://")) {
            filename = StringUtils.substringAfter(filename,"task://");
        } else if (filename.startsWith("task:/")) {
            filename = StringUtils.substringAfter(filename,"task:/");
        }
        filename = URLDecoder.decode(filename);
        logger.debug(filename);
        if (filename == null || filename.length() == 0 ) {
            return root;
        }
        
        // try it as a task id.
        
        URI u;
            u = new URI(filename);
        if (rpmi.findMonitor(u) != null) {
            return new TaskFileName(u);
        }
        
        // must be a result of a task then..
        //  drop last '/' I guess.
        String tid = StringUtils.substringBeforeLast(filename,"/");
        u = new URI(tid);
        if (rpmi.findMonitor(u) == null) {
            throw new FileSystemException("Unknown task: " + u);
        }
         return new ResultFileName(u,StringUtils.substringAfterLast(filename,"/"));  
        } catch (URISyntaxException x) {
            throw new FileSystemException(x);
        } 
    }


    /** abstract class for all remote process filenames
     * 
     *  implemented from scrathc instead of using AbstractFileName, as I can't
     *  work out what I need to implement for that class - and also the
     *  filenames for this protocol are very simple.
     *  */
    public abstract class RPFileName implements FileName{

        public RPFileName(String absPath,
                FileType type) {
            this.path = absPath;
            this.type = type;
        }
        private final FileType type;
        private final String path;

        /** method to ceeate a file object appropriate to this filename type */
        public abstract RPFileObject createFileObject(RPFileSystem sys);

        public String getExtension() {
            return "";
        }
        public String getFriendlyURI() {
            // nothing sensitive - so just serialize it.
            return toString();
        }  
        public FileName getRoot() {
            return root;
        }
        public String getRootURI() {
            return root.getURI();
        }
        public FileType getType() {
            return type;
        }
        public String getScheme() {
            return "task";
        }        
        public String getPath() {
            return path;
        }
        public int compareTo(Object arg0) {
            RPFileName other = (RPFileName) arg0;
            return this.path.compareTo(other.path);
        }
        public String getURI() {
            return  getScheme() + ":/" + path;
        }
        
        public String toString() {
            return getURI();
        }
        public String getPathDecoded() throws FileSystemException {
            return URLDecoder.decode(path); // that'lll do for not. @todo check this in future.
        }        
        public boolean isDescendent(FileName descendent) {
            return isDescendent(descendent,NameScope.DESCENDENT);
        }        
    }

    /** filename subclass for the root filename */
    public class RootFileName extends RPFileName {

        public RootFileName() {
            super("/", FileType.FOLDER);
        }

        public String getBaseName() {
            return FileName.ROOT_PATH;
        }

        public int getDepth() {
            return 0;
        }

        public FileName getParent() {
            return null;
        }

        public String getRelativeName(FileName name) throws FileSystemException {
            if (!(name instanceof RPFileName)) {
                throw new FileSystemException("not a descendant");
            }
            if (name == root) {
                return ".";
            }
            RPFileName fn = (RPFileName)name;
            
            return fn.path.substring(1); // drops leading '/'
        }

        public boolean isAncestor(FileName ancestor) {
            return false;
        }

        public boolean isDescendent(FileName descendent, NameScope nameScope) {
            if (! (descendent instanceof RPFileName)) {
                return false;
            }
            if (nameScope.equals(NameScope.CHILD)) {
                return descendent instanceof TaskFileName;
            } else if(nameScope.equals(NameScope.DESCENDENT)){
                return descendent != root;
            } else {
                return true;
            }
        }


        public RPFileObject createFileObject(
                RPFileSystem sys) {
            return new RootFileObject(this,sys);
        }

    }

    /** filename subclass for a 'task' folder */
    public class TaskFileName extends RPFileName {

        public TaskFileName(URI execId) {
            super("/" + URLEncoder.encode(execId.toString()), FileType.FOLDER);
            this.execId = execId;
        }
        private final URI execId;
        
        public URI getExecId() {
            return execId;
        }
        
        public String getBaseName() {
            return execId.toString();
        }
        public int getDepth() {
            return 1;
        }
        public FileName getParent() {
            return root;
        }
        public String getRelativeName(FileName name) throws FileSystemException {
            if (!(name instanceof RPFileName)) {
                throw new FileSystemException("not a descendant");
            }
            if (name == root) {
                return "..";
            }
            if (name instanceof TaskFileName) {
                TaskFileName td = (TaskFileName)name;
                if (td.execId.equals(execId)) {
                    return ".";
                } else {
                    return "../" + td.execId + "/";
                }
            }
            // must be a result then.
            ResultFileName fn = (ResultFileName)name;
            if (fn.execId.equals(execId)) {
                return fn.resultName;
            } else {
                return "../" + fn.execId + "/" + fn.resultName;
            }
        }
        public boolean isAncestor(FileName ancestor) {
            return ancestor == root;
        }
        public boolean isDescendent(FileName descendent, NameScope nameScope) {
            if (! (descendent instanceof RPFileName)) {
                return false;
            }
            // special case.
            if (nameScope.equals(nameScope.DESCENDENT_OR_SELF) 
                    && descendent instanceof TaskFileName
                    && ((TaskFileName)descendent).execId.equals(execId)) {
                return true;
            }
            return descendent instanceof ResultFileName // already checked for 'self' case
                    && ((ResultFileName)descendent).execId.equals(execId);
     
        }
        public RPFileObject createFileObject(
                RPFileSystem sys) {
            return new TaskFileObject(this,sys);
        }
    }

    /** filename subclass for a 'result' file */
    public class ResultFileName extends RPFileName {

        private final URI execId;
        private final String resultName;

        public ResultFileName(URI execId, String resultName) {
            super("/" + URLEncoder.encode(execId.toString()) + "/" + resultName, FileType.FILE);
            this.execId = execId;
            this.resultName = resultName;  
        }

        public String getBaseName() {
            return resultName;
        }
        public URI getExecId() {
            return execId;
        }        

        public int getDepth() {
            return 2;
        }

        public FileName getParent() {
            return new TaskFileName(execId);
        }

        //@todo Q - does relativity work from the directory, or from the file??
        // assume it's relative to the containing folder.
        public String getRelativeName(FileName name) throws FileSystemException {
            if (!(name instanceof RPFileName)) {
                throw new FileSystemException("not a descendant");
            }
            if (name == root) {
                return "..";
            }
            if (name instanceof TaskFileName) {
                TaskFileName td = (TaskFileName)name;
                if (td.execId.equals(execId)) {
                    return ".";
                } else {
                    return "../" + td.execId + "/";
                }
            }
            // must be a result then.
            ResultFileName fn = (ResultFileName)name;
            if (fn.execId.equals(execId)) {
                return fn.resultName;
            } else {
                return "../" + fn.execId + "/" + fn.resultName;
            }
        }

        public boolean isAncestor(FileName ancestor) {
            return ancestor == root
                || ancestor instanceof TaskFileName 
                    && ((TaskFileName)ancestor).execId.equals(execId);
        }

        public boolean isDescendent(FileName descendent, NameScope nameScope) {
            return nameScope.equals(NameScope.DESCENDENT_OR_SELF)
                && descendent instanceof ResultFileName
                && ((ResultFileName)descendent).execId.equals(execId);
        }

        public RPFileObject createFileObject(
                RPFileSystem sys) {
            return new ResultFileObject(this,sys);
        }

        /**
         * @return the resultName
         */
        public final String getResultName() {
            return this.resultName;
        }

    }

    public RPParser(RemoteProcessManagerInternal rpmi) {
        super();
        this.rpmi = rpmi;
        root = new RootFileName();
    }

}
