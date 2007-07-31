/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.task;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.Advanced;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.RPFileName;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.ResultFileName;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 20076:59:25 PM
 */
public class ResultFileObject extends RPFileObject {
    private final ProcessMonitor monitor;
    private boolean indirect = false;
    private String resultName;
    private ParameterBean description;
    private final Map attr = new HashMap();
    private String contentType = VoDataFlavour.PLAIN.getMimeType();
    /**
     * @param name
     * @param fs
     */
    protected ResultFileObject(RPFileName name,
            RPFileSystem fs) {
        super(name, fs);
        logger.debug("creating result file object from " + name + ", " + name.getClass().getName());
        this.monitor = fs.getRpmi().findMonitor(((ResultFileName)name).getExecId());
        resultName = StringUtils.substringBefore(((ResultFileName)getName()).getResultName(),".");
        logger.debug("searching for " + resultName);
        
        if (monitor instanceof ProcessMonitor.Advanced ) {
            final Advanced advanced = ((ProcessMonitor.Advanced)monitor);
            org.astrogrid.workflow.beans.v1.Tool t = advanced.getInvocationTool();
            ParameterBean[] descs = advanced.getApplicationDescription().getParameters();
            ParameterValue[] arr = t.getOutput().getParameter();
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].getIndirect()) {
                    if (StringUtils.substringBefore(
                            TaskFileObject.computeFileBaseName(arr[i])                            
                            ,".").equals(resultName)) {
                        // found a match - by indirection target name.
                        indirect = true;
                        resultName = arr[i].getName();
                        description = TaskFileObject.findDescriptionFor(arr[i],descs);
                        break;  
                    }
                } else {
                    if (arr[i].getName().equals(resultName)) {
                        // found a match, by result name.
                        indirect = false;
                        description = TaskFileObject.findDescriptionFor(arr[i],descs);                        
                        break;
                    }
                }
            }
            // see if we can work out some things.
            if (description != null) {
                this.contentType = computeContentType();
                if (StringUtils.containsIgnoreCase(description.getName(),"table")
                        || StringUtils.containsIgnoreCase(description.getDescription(),"table")) {
                    attr.put(VoDataFlavour.TABULAR_HINT,Boolean.TRUE);
                }
                if (StringUtils.containsIgnoreCase(description.getName(),"fits")
                        || StringUtils.containsIgnoreCase(description.getDescription(),"fits")) {
                    attr.put(VoDataFlavour.FITS_HINT,Boolean.TRUE);
                }                
            }
        } 
    }

    protected String[] doListChildren() throws Exception {
        return new String[]{}; // won't be called anyhow.
    }

    /**
     * @return the monitor
     */
    public final ProcessMonitor getMonitor() {
        return this.monitor;
    }
    
    public InputStream getInputStream() throws FileSystemException {

        // check for an indirect parameter if we've got the information..
        //@todo a hacky cludge - do this better later using a delegate file object.
        // so that all properties are delegated - not just hte input stream.
        try {
            String s = (String)monitor.getResults().get(resultName);
            if (indirect) {
                FileObject content = getFileSystem().getFileSystemManager().resolveFile(s);
                return content.getContent().getInputStream();
            } else {
                return new ByteArrayInputStream(s.getBytes());
            }
        } catch (Exception x) {
            throw new FileSystemException(x);
        }
    }

    public String getContentType() {
        return contentType;
    }
    /** return a suggestion of the content type - vague at best.
     * @return
     */
    protected String computeContentType() {
        if (description == null) {
            return  VoDataFlavour.PLAIN.getMimeType();
        }
        String type= description.getType();
        if (type.equalsIgnoreCase("fits")) {
            return VoDataFlavour.MIME_FITS_IMAGE;
        } else if (type.equalsIgnoreCase("binary")) {
            return VoDataFlavour.MIME_OCTET_STREAM;
        } else if (type.equalsIgnoreCase("anyxml")) {
            return VoDataFlavour.XML.getMimeType();
        } else if (type.equalsIgnoreCase("votable")) {
            return VoDataFlavour.MIME_VOTABLE;
        } else if(type.equalsIgnoreCase("adql")) {
            attr.put(VoDataFlavour.TABULAR_HINT,Boolean.TRUE);
            return VoDataFlavour.MIME_ADQL;
        } else {
            return VoDataFlavour.PLAIN.getMimeType();
        }        
    }
    
    /** return attributes - at the moment just used to hint what kind of thing I suspect this to be */
    public Map getAttributes() {
        return attr;
    }
    
}
