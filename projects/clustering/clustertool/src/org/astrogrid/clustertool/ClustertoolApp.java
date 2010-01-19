/*
 * ClustertoolApp.java
 */

package org.astrogrid.clustertool;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import no.uib.cipr.matrix.AGDenseMatrix;

import org.astrogrid.cluster.cluster.Clustering;
import org.astrogrid.cluster.cluster.CovarianceKind;
import org.astrogrid.cluster.cluster.Clustering.ClusteringResults;
import org.astrogrid.clustertool.stil.DenseMatrixStarTable;
import org.astrogrid.matrix.Matrix;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.JoinStarTable;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOTableBuilder;
import uk.ac.starlink.votable.VOTableWriter;

/**
 * The main class of the application. This can either run the clustering without further intervention when the -nogui commandline option is set, 
 * otherwise a GUI is presented via {@link ClustertoolView}.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 19 Jan 2010
 */
public class ClustertoolApp extends SingleFrameApplication {

    private File inputDataFile;
    
    private double _tol = 0.001;     
    private boolean _mix_var = false; 
    private boolean _err_dim = false;
    private boolean _outlier = false; 
    private boolean _mml =false;     
    private int _c_dim = 0 ;   
    private int _e_dim = 0 ;   
    private int _b_dim = 0 ;   
    private int _m_dim = 0 ;   
    private int _i_dim = 0   ; 
    private int _niters = 20;  
    private double _line_s;  
    private int _mml_min = 1; 
    private int  _mml_max = 4; 
    private  double  _mml_reg = 2; 
    private CovarianceKind _cv_type = CovarianceKind.diagonal;
    private AGDenseMatrix _indata;
    private StarTable _inTable;
    private String _outFilename = "results.vot";
    
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
             new LongOpt("tol", LongOpt.REQUIRED_ARGUMENT,null,'t'),
             new LongOpt("outlier", LongOpt.NO_ARGUMENT,null,'o'),
             new LongOpt("mml", LongOpt.NO_ARGUMENT,null,'m'),
             new LongOpt("contvar", LongOpt.REQUIRED_ARGUMENT,null,'c'),
             new LongOpt("errvar", LongOpt.REQUIRED_ARGUMENT,null,'e'),
             new LongOpt("binvar", LongOpt.REQUIRED_ARGUMENT,null,'b'),
             new LongOpt("modalvar", LongOpt.REQUIRED_ARGUMENT,null,'d'),
             new LongOpt("intvar", LongOpt.REQUIRED_ARGUMENT,null,'i'),
             new LongOpt("niters", LongOpt.REQUIRED_ARGUMENT,null,'n'),
             new LongOpt("classes", LongOpt.REQUIRED_ARGUMENT,null,'x'),
             new LongOpt("variance", LongOpt.REQUIRED_ARGUMENT,null,'v'),
             
        };
        Getopt g = new Getopt("clustertool", args, "homt:c:e:b:d:i:n:x:v:", lopts);
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
//                    logger.warn("getopt() returned " + c);
                    break;
              }
        }
        if(nogui ){
            ClustertoolApp app = new ClustertoolApp(args, lopts);
            app.runCliClustering();
            
        }else {
            launch(ClustertoolApp.class, args);
           
        }
        
    }
    
    private void runCliClustering() {
        try {
            FileInputStream in = new FileInputStream(inputDataFile);
            StarTable table = new StarTableFactory().makeStarTable( in, new VOTableBuilder(false));
            _indata = loadTable(table);
            ClusteringResults results = doClustering(true, _tol, _mix_var, _err_dim, _outlier, _mml, _c_dim, 
                    _e_dim, _b_dim, _m_dim, _i_dim, _niters, _line_s, _mml_min, _mml_max, _mml_reg, _cv_type, _indata);
            saveResults(results, new File(_outFilename));

        } catch (FileNotFoundException e) {
           logger.error("file not found",e);
           System.exit(1);
        } catch (TableFormatException e) {
            logger.error("cannot read data table",e);
            System.exit(1);
        } catch (IOException e) {
            logger.error("problem reading data files",e);
            System.exit(1);
        }
    }

    /**
     * Default constructor for use with GUI.
     */
    public ClustertoolApp(){
        
    }
    
    /**
     * Constructor for use from commandline.
     * @param args
     * @param lopts
     */
    public ClustertoolApp(String[] args, LongOpt lopts[] ){
 
        Getopt g = new Getopt("clustertool", args, "homt:c:e:b:d:i:n:x:v:", lopts);
        int c;
        boolean nogui = false;
        String arg;
        boolean opterr = false;
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
                
            case 't':
                arg = g.getOptarg();
                try {
                    _tol = Double.parseDouble(arg);
                } catch (NumberFormatException e) {
                    logger.error("invalid tol value - using default", e);
                }
                break;
            case 'o':
                _outlier = true;
                break;
            case 'm':
                _mml = true;
                break;
            case 'c':
                arg = g.getOptarg();
                try {
                    _c_dim = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    logger.fatal("invalid continuous variable dimension value - exiting", e);
                    System.exit(1);
                }
                break;
            case 'e':
                arg = g.getOptarg();
                try {
                    _e_dim = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    logger.fatal("invalid continuous variable with error dimension value - exiting", e);
                    System.exit(1);
                }
                break;
            case 'b':
                arg = g.getOptarg();
                try {
                    _b_dim = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    logger.fatal("invalid binary variable dimension value - exiting", e);
                    System.exit(1);
                }
                break;
            case 'd':
                arg = g.getOptarg();
                try {
                    _m_dim = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    logger.fatal("invalid categorical variable dimension value - exiting", e);
                    System.exit(1);
                }
               break;
            case 'i':
                arg = g.getOptarg();
                try {
                    _i_dim = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    logger.fatal("invalid integer variable dimension value - exiting", e);
                    System.exit(1);
                }
               break;
            case 'n':
                arg = g.getOptarg();
                try {
                    _niters = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    logger.error("invalid niter value - using default", e);
                }
               break;
            case 'x':
                arg = g.getOptarg();
                try {
                    _mml_max = Integer.parseInt(arg);
                } catch (NumberFormatException e) {
                    logger.error("invalid number of classes value - using default", e);
                }
               break;
            case 'v':
                arg = g.getOptarg();
                _cv_type = CovarianceKind.valueOf(arg);
                break;
                
            case ':':
                System.err.println("Doh! You need an argument for option " +
                                   (char)g.getOptopt());
                opterr = true;
                
                break;
                //
              case '?':
                System.err.println("The option '" + (char)g.getOptopt() + 
                                 "' is not valid");
                break;
    
                default:
                    logger.warn("getopt() returned " + c);
                    break;
              }
        }
         if(opterr){
            System.exit(1);
        }
        //options processed - now just pick up the input file name as the last argument.
        if(g.getOptind() < args.length){
        inputDataFile = new File(args[g.getOptind()]);
        }
        else {
            logger.error("missing input data file argument");
            System.exit(1);
        }

    }
    
    public AGDenseMatrix loadTable(StarTable table) throws IOException{
        _inTable = table;
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
    
    public void saveResults(Clustering.ClusteringResults results, File outfile) throws IOException{
        
        StarTable expectations = new DenseMatrixStarTable((AGDenseMatrix) results.R);
        
        
        for (int i = 0; i < expectations.getColumnCount(); i++) {
            ColumnInfo colinfo = expectations.getColumnInfo(i);
            colinfo.setName("P"+(i+1));
            colinfo.setDescription("probability of being in class");
            
        }
        StarTable output = new JoinStarTable(new StarTable[]{_inTable, expectations});
        VOTableWriter voWriter = new VOTableWriter( DataFormat.TABLEDATA, true );
        FileOutputStream out = new FileOutputStream(outfile);
        voWriter.writeStarTable(output, out);
    }

}
