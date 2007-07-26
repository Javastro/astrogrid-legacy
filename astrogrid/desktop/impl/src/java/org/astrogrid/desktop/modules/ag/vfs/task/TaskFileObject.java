/**
 * 
 */
package org.astrogrid.desktop.modules.ag.vfs.task;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.Map;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.provider.DelegateFileObject;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.Advanced;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.RPFileName;
import org.astrogrid.desktop.modules.ag.vfs.task.RPParser.TaskFileName;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 23, 20076:58:36 PM
 */
public class TaskFileObject extends RPFileObject {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(TaskFileObject.class);

     private final ProcessMonitor monitor;

    /**
     * @return the monitor
     */
    public final ProcessMonitor getMonitor() {
        return this.monitor;
    }

    /**
     * @param name
     * @param fs
     */
    protected TaskFileObject(RPFileName name,
            RPFileSystem fs) {
        super(name, fs);
        this.monitor = fs.getRpmi().findMonitor(((TaskFileName)name).getExecId());
    }
    
    protected String[] doListChildren() throws Exception {
       
        if (monitor instanceof ProcessMonitor.Advanced) {
            final Advanced advanced = ((ProcessMonitor.Advanced)monitor);
            org.astrogrid.workflow.beans.v1.Tool t = advanced.getInvocationTool();
            ParameterBean[] descs = advanced.getApplicationDescription().getParameters();
            ParameterValue[] arr = t.getOutput().getParameter();
            
            // populate an array of filenames.
            String[] children = new String[arr.length];
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].getIndirect()) { // 
                    // name the file after the indirect file - then need to add code to check in the result file object for this.
                    children[i] = StringUtils.substringAfterLast(arr[i].getValue(),"/");
                } else {
                    children[i] = arr[i].getName() + suggestExtension(findDescriptionFor(arr[i],descs));
                }
            }
            return children;
        } else {
            // no metadata - so need to get the results themselves..
            Map m = monitor.getResults(); // for the default monitors this is an inexpensive operation.
            logger.debug(m.keySet());
            
            return (String[])m.keySet().toArray(new String[m.size()]);
        }
    }

    private String suggestExtension(ParameterBean pb) {
        if (pb == null) {
            return "";
        }
        String type = pb.getType();
        if (type.equalsIgnoreCase("fits")) {
            return ".fits";
        } else if (type.equalsIgnoreCase("binary")) {
            return ".bin";
        } else if (type.equalsIgnoreCase("anyxml")) {
            return ".xml";
        } else if (type.equalsIgnoreCase("votable")) {
            return ".vot";
        } else if(type.equalsIgnoreCase("adql")) {
            return ".adql";
        } else {
            return ".txt";
        }
    }
    
    static ParameterBean findDescriptionFor(ParameterValue pv,ParameterBean[] descs) {
        for (int i = 0; i < descs.length; i++) {
            if (pv.getName().equals(descs[i].getName())) {
                return descs[i];
            }            
        }
        return null;
        
    }
}
