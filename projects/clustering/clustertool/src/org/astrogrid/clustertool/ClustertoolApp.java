/*
 * ClustertoolApp.java
 */

package org.astrogrid.clustertool;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.uib.cipr.matrix.AGDenseMatrix;

import org.astrogrid.cluster.cluster.Clustering;
import org.astrogrid.cluster.cluster.CovarianceKind;
import org.astrogrid.matrix.Matrix;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;

/**
 * The main class of the application.
 */
public class ClustertoolApp extends SingleFrameApplication {

    private File inputDataFile;
    private AGDenseMatrix indata;
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(ClustertoolApp.class);
    

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new ClustertoolView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ClustertoolApp
     */
    public static ClustertoolApp getApplication() {
        return Application.getInstance(ClustertoolApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {

        LongOpt lopts[] = new LongOpt[]{
             new LongOpt("nogui", LongOpt.NO_ARGUMENT   , null, 1),
             new LongOpt("help", LongOpt.NO_ARGUMENT   , null, 'h'),
             
        };
        Getopt g = new Getopt("clustertool", args, "h", lopts);
        int c;
        boolean nogui = false;
        while ((c = g.getopt()) != -1){
            switch (c)
              {
            
            case 1:
                 nogui = true;
                break;
            case 'h':
                System.out.println("clustertool -h --nogui \n" +
                		"" +
                		"--nogui do not run the gui, but process a file directly as specified by other arguments\n" +
                		"");
                break;
                default:
                    logger.warn("getopt() returned " + c);
                    break;
              }
        }
        if(nogui ){
            
        }else {
            launch(ClustertoolApp.class, args);
           
        }
        
    }
    
    public AGDenseMatrix loadTable(StarTable table) throws IOException{
        RowSequence rseq = table.getRowSequence();
        int ncols = table.getColumnCount();
        List<double[]> rows = new ArrayList<double[]>();
        while (rseq.next()) {
            double array[] = new double[ncols];
            for (int i = 0; i < ncols; i++) {
                 Object ob = rseq.getCell(i);
                 if (ob instanceof Double) {
                    Double d = (Double) ob;
                    array[i] = d.doubleValue();


                } else if ( ob instanceof Float){
                    Float f = (Float) ob;
                    array[i] = f.floatValue();
                }

                 else {
                    throw new IOException("can only handle doubles or floats at the moment");
                }
                
            }
           rows.add(array);
        }
        return new AGDenseMatrix((double[][])rows.toArray(new double[][]{{}}));
        
    }
    
    public Clustering.ClusteringResults doClustering(boolean display, 
            double tol,     
            boolean mix_var, 
            boolean err_dim,
            boolean outlier, 
            boolean mml,     
            int c_dim,   
            int e_dim,   
            int b_dim,   
            int m_dim,   
            int i_dim,   
            int niters,  
            double line_s,  
            int mml_min, 
            int  mml_max, 
            double  mml_reg, 
            CovarianceKind cv_type , Matrix data){
        return new Clustering().clustering(display, tol, mix_var, err_dim, outlier, mml, c_dim, e_dim, b_dim, m_dim,
                i_dim, niters, line_s, mml_min, mml_max, mml_reg,  cv_type, data);
    }

}
