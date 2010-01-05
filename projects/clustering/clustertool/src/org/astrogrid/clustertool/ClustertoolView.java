/*
 * ClustertoolView.java
 */

package org.astrogrid.clustertool;

import no.uib.cipr.matrix.AGDenseMatrix;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.TaskService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import org.astrogrid.cluster.cluster.CovarianceKind;
import org.astrogrid.cluster.cluster.Clustering.ClusteringResults;
import org.jdesktop.swingx.combobox.EnumComboBoxModel;
import org.jdesktop.swingx.combobox.ListComboBoxModel;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.gui.TableLoadChooser;

import java.io.File;
import java.io.IOException;

/**
 * The application's main frame.
 */
public class ClustertoolView extends FrameView {

    private final EnumComboBoxModel<CovarianceKind> covEnumModel;
    private static final Logger logger = Logger.getLogger(ClustertoolView.class.getName());
    private boolean loadedOK;
    private AGDenseMatrix indata; 
    
    private AGMatrixTableModel muModel = new AGMatrixTableModel();
    private AGMatrixTableModel covModel = new AGMatrixTableModel();
    
    private MutableListModel likModel = new MutableListModel();
    private MutableListModel probModel = new MutableListModel();
   

    public boolean isLoadedOK() {
        return loadedOK;
    }
    protected String fileName;
    public static final String PROP_FILENAME = "fileName";

    /**
     * Get the value of fileName
     *
     * @return the value of fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Set the value of fileName
     *
     * @param fileName new value of fileName
     */
    public void setFileName(String fileName) {
        String oldFileName = this.fileName;
        this.fileName = fileName;
        firePropertyChange(PROP_FILENAME, oldFileName, fileName);
    }
    protected int ncontVar;
    public static final String PROP_NCONTVAR = "ncontVar";

    /**
     * Get the value of ncontVar
     *
     * @return the value of ncontVar
     */
    public int getNcontVar() {
        return ncontVar;
    }

    /**
     * Set the value of ncontVar
     *
     * @param ncontVar new value of ncontVar
     */
    public void setNcontVar(int ncontVar) {
        int oldNcontVar = this.ncontVar;
        this.ncontVar = ncontVar;
        firePropertyChange(PROP_NCONTVAR, oldNcontVar, ncontVar);
    }


    public void setLoadedOK(boolean loadedOK) {
        boolean oldValue = this.loadedOK;
        this.loadedOK = loadedOK;
        firePropertyChange("loadedOK", oldValue, loadedOK);
    }
    protected int nconterrVar;
    public static final String PROP_NCONTERRVAR = "nconterrVar";
    protected boolean mml;
    public static final String PROP_MML = "mml";

    /**
     * Get the value of mml
     *
     * @return the value of mml
     */
    public boolean isMml() {
        return mml;
    }

    /**
     * Set the value of mml
     *
     * @param mml new value of mml
     */
    public void setMml(boolean mml) {
        boolean oldMml = this.mml;
        this.mml = mml;
       firePropertyChange(PROP_MML, oldMml, mml);
    }
    protected boolean mix_ver;
    public static final String PROP_MIX_VER = "mix_ver";

    public boolean isMix_ver() {
        return mix_ver;
    }

    public void setMix_ver(boolean mix_ver) {
        boolean oldMix_ver = this.mix_ver;
        this.mix_ver = mix_ver;
       firePropertyChange(PROP_MIX_VER, oldMix_ver, mix_ver);
    }
    protected boolean outlier = true;
    public static final String PROP_OUTLIER = "outlier";

    public boolean isOutlier() {
        return outlier;
    }

    public void setOutlier(boolean outlier) {
        boolean oldOutlier = this.outlier;
        this.outlier = outlier;
       firePropertyChange(PROP_OUTLIER, oldOutlier, outlier);
    }
    protected double tol = 0.0001;
    public static final String PROP_TOL = "tol";

    public double getTol() {
        return tol;
    }

    public void setTol(double tol) {
        double oldTol = this.tol;
        this.tol = tol;
       firePropertyChange(PROP_TOL, oldTol, tol);
    }
    protected int niters = 20;
    public static final String PROP_NITERS = "niters";

    public int getNiters() {
        return niters;
    }

    public void setNiters(int niters) {
        int oldNiters = this.niters;
        this.niters = niters;
       firePropertyChange(PROP_NITERS, oldNiters, niters);
    }

    protected int mml_max = 2;
    public static final String PROP_MML_MAX = "mml_max";

    public int getMml_max() {
        return mml_max;
    }

    public void setMml_max(int mml_max) {
        int oldMml_max = this.mml_max;
        this.mml_max = mml_max;
        firePropertyChange(PROP_MML_MAX, oldMml_max, mml_max);
    }


    /**
     * Get the value of nconterrVar
     *
     * @return the value of nconterrVar
     */
    public int getNconterrVar() {
        return nconterrVar;
    }

    /**
     * Set the value of nconterrVar
     *
     * @param nconterrVar new value of nconterrVar
     */
    public void setNconterrVar(int nconterrVar) {
        int oldNconterrVar = this.nconterrVar;
        this.nconterrVar = nconterrVar;
        firePropertyChange(PROP_NCONTERRVAR, oldNconterrVar, nconterrVar);
        if(nconterrVar > 0)
            setErrors(true);
        else
            setErrors(false);
    }

    public int getNcols() {
        return ncols;
    }

    public void setNcols(int ncols) {
        int oldvalue = this.ncols;
        this.ncols = ncols;
        firePropertyChange("ncols",oldvalue, ncols);

    }

    public int getNrows() {

        return nrows;
    }

    public void setNrows(int nrows) {
        int oldvalue = this.nrows;
        this.nrows = nrows;
        firePropertyChange("nrows", oldvalue, nrows);
    }

    protected int nbinVar;
    public static final String PROP_NBINVAR = "nbinVar";

    /**
     * Get the value of nbinVar
     *
     * @return the value of nbinVar
     */
    public int getNbinVar() {
        return nbinVar;
    }

    /**
     * Set the value of nbinVar
     *
     * @param nbinVar new value of nbinVar
     */
    public void setNbinVar(int nbinVar) {
        int oldNbinVar = this.nbinVar;
        this.nbinVar = nbinVar;
        firePropertyChange(PROP_NBINVAR, oldNbinVar, nbinVar);
    }
    protected int ncatVar;
    public static final String PROP_NCATVAR = "ncatVar";

    /**
     * Get the value of ncatVar
     *
     * @return the value of ncatVar
     */
    public int getNcatVar() {
        return ncatVar;
    }

    /**
     * Set the value of ncatVar
     *
     * @param ncatVar new value of ncatVar
     */
    public void setNcatVar(int ncatVar) {
        int oldNcatVar = this.ncatVar;
        this.ncatVar = ncatVar;
        firePropertyChange(PROP_NCATVAR, oldNcatVar, ncatVar);
    }
    protected int nintVar;
    public static final String PROP_NINTVAR = "nintVar";

    protected boolean errors;
    public static final String PROP_ERRORS = "errors";

    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        boolean oldErrors = this.errors;
        this.errors = errors;
        firePropertyChange(PROP_ERRORS, oldErrors, errors);
    }


    /**
     * Get the value of nintVar
     *
     * @return the value of nintVar
     */
    public int getNintVar() {
        return nintVar;
    }

    /**
     * Set the value of nintVar
     *
     * @param nintVar new value of nintVar
     */
    public void setNintVar(int nintVar) {
        int oldNintVar = this.nintVar;
        this.nintVar = nintVar;
        firePropertyChange(PROP_NINTVAR, oldNintVar, nintVar);
    }

    protected CovarianceKind covarianceKind = CovarianceKind.common;
    public static final String PROP_COVARIANCEKIND = "covarianceKind";

    /**
     * Get the value of covarianceKind
     *
     * @return the value of covarianceKind
     */
    public CovarianceKind getCovarianceKind() {
        return covarianceKind;
    }

    /**
     * Set the value of covarianceKind
     *
     * @param covarianceKind new value of covarianceKind
     */
    public void setCovarianceKind(CovarianceKind covarianceKind) {
        CovarianceKind oldCovarianceKind = this.covarianceKind;
        this.covarianceKind = covarianceKind;
        firePropertyChange(PROP_COVARIANCEKIND, oldCovarianceKind, covarianceKind);
    }

    private int ncols;
    private int nrows;
    private SampCommunicator communicator_ = null;
    private Transmitter tableTransmitter;

    public ClustertoolView(ClustertoolApp app) {
        super(app);

        covEnumModel = new EnumComboBoxModel<CovarianceKind>(CovarianceKind.class);
        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        
        /* SAMP/PLASTIC interoperability. */
        try {
            communicator_ = new SampCommunicator( this );
            if(communicator_ != null){
                jInteropMenu.setEnabled(true);
                javax.swing.Action interopAct = communicator_.createWindowAction(this.getRootPane());
                jInteropMenu.add(interopAct);
                tableTransmitter = communicator_.getTableTransmitter();
                jInteropMenu.add(tableTransmitter.getBroadcastAction());
                jInteropMenu.add(tableTransmitter.createSendMenu());
            }
        } catch (IOException e1) {
            logger.log(Level.SEVERE, "cannot start up SAMP", e1);
        }

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        tableFactory = new StarTableFactory();
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = ClustertoolApp.getApplication().getMainFrame();
            aboutBox = new ClustertoolAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        ClustertoolApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        mainPanel = new javax.swing.JPanel();
        inputPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jMixVarCheckBox1 = new javax.swing.JCheckBox();
        jMeasErrorCheckBox1 = new javax.swing.JCheckBox();
        jOutliersCheckBox1 = new javax.swing.JCheckBox();
        jMMLCheckBox1 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jC_DIM_TextField1 = new javax.swing.JTextField();
        jE_DIM_TextField1 = new javax.swing.JTextField();
        jB_DIM_TextField1 = new javax.swing.JTextField();
        jM_DIM_TextField1 = new javax.swing.JTextField();
        jP_DIM_TextField1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jNClassesTextField1 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jCovarianceComboBox1 = new javax.swing.JComboBox();
        jLabel12 = new javax.swing.JLabel();
        jImputationComboBox2 = new javax.swing.JComboBox();
        jTolTextField1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jniterTextField1 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        fileNameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jNcolsField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jNrowsTextField = new javax.swing.JTextField();
        jLoadOKCheckBox1 = new javax.swing.JCheckBox();
        outputPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLogLikList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jcovTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jmuTable1 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jpropList1 = new javax.swing.JList();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jRunMenu = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jInteropMenu = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N

        inputPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        inputPanel.setName("Input Panel"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.astrogrid.clustertool.ClustertoolApp.class).getContext().getResourceMap(ClustertoolView.class);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jMixVarCheckBox1.setText(resourceMap.getString("jMixVarCheckBox1.text")); // NOI18N
        jMixVarCheckBox1.setName("jMixVarCheckBox1"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${mix_ver}"), jMixVarCheckBox1, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        jMeasErrorCheckBox1.setText(resourceMap.getString("jMeasErrorCheckBox1.text")); // NOI18N
        jMeasErrorCheckBox1.setName("jMeasErrorCheckBox1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${errors}"), jMeasErrorCheckBox1, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        jOutliersCheckBox1.setText(resourceMap.getString("jOutliersCheckBox1.text")); // NOI18N
        jOutliersCheckBox1.setEnabled(false);
        jOutliersCheckBox1.setName("jOutliersCheckBox1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${outlier}"), jOutliersCheckBox1, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        jMMLCheckBox1.setText(resourceMap.getString("jMMLCheckBox1.text")); // NOI18N
        jMMLCheckBox1.setFocusable(false);
        jMMLCheckBox1.setName("jMMLCheckBox1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${mml}"), jMMLCheckBox1, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jMixVarCheckBox1)
                    .add(jMeasErrorCheckBox1)
                    .add(jOutliersCheckBox1)
                    .add(jMMLCheckBox1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(26, 26, 26)
                .add(jMixVarCheckBox1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jMeasErrorCheckBox1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jOutliersCheckBox1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jMMLCheckBox1)
                .addContainerGap(96, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel3.border.title"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jC_DIM_TextField1.setColumns(3);
        jC_DIM_TextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jC_DIM_TextField1.setName("jC_DIM_TextField1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${ncontVar}"), jC_DIM_TextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("0");
        binding.setSourceUnreadableValue("0");
        bindingGroup.addBinding(binding);

        jE_DIM_TextField1.setColumns(3);
        jE_DIM_TextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jE_DIM_TextField1.setName("jE_DIM_TextField1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${nconterrVar}"), jE_DIM_TextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jB_DIM_TextField1.setColumns(3);
        jB_DIM_TextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jB_DIM_TextField1.setName("jB_DIM_TextField1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${nbinVar}"), jB_DIM_TextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jM_DIM_TextField1.setColumns(3);
        jM_DIM_TextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jM_DIM_TextField1.setName("jM_DIM_TextField1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${ncatVar}"), jM_DIM_TextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jP_DIM_TextField1.setColumns(3);
        jP_DIM_TextField1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jP_DIM_TextField1.setName("jP_DIM_TextField1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${nintVar}"), jP_DIM_TextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel5)
                    .add(jLabel6)
                    .add(jLabel7)
                    .add(jLabel8)
                    .add(jLabel9))
                .add(60, 60, 60)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jB_DIM_TextField1)
                    .add(jC_DIM_TextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jM_DIM_TextField1)
                    .add(jP_DIM_TextField1)
                    .add(jE_DIM_TextField1))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(jC_DIM_TextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jE_DIM_TextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jB_DIM_TextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7))
                .add(10, 10, 10)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jM_DIM_TextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel8))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jP_DIM_TextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel9))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel4.border.title"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jNClassesTextField1.setColumns(3);
        jNClassesTextField1.setToolTipText(resourceMap.getString("jNClassesTextField1.toolTipText")); // NOI18N
        jNClassesTextField1.setName("jNClassesTextField1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${mml_max}"), jNClassesTextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        jCovarianceComboBox1.setModel(covEnumModel);
        jCovarianceComboBox1.setName("jCovarianceComboBox1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${covarianceKind}"), jCovarianceComboBox1, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N

        jImputationComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jImputationComboBox2.setEnabled(false);
        jImputationComboBox2.setName("jImputationComboBox2"); // NOI18N

        jTolTextField1.setColumns(6);
        jTolTextField1.setName("jTolTextField1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${tol}"), jTolTextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N

        jniterTextField1.setName("jniterTextField1"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${niters}"), jniterTextField1, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel12)
                            .add(jLabel11)
                            .add(jLabel10)))
                    .add(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel13))
                    .add(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel14)))
                .add(21, 21, 21)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jImputationComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jCovarianceComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jNClassesTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 49, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTolTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jniterTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel10)
                    .add(jNClassesTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel11)
                    .add(jCovarianceComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel12)
                    .add(jImputationComboBox2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel13)
                    .add(jTolTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel14)
                    .add(jniterTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        jPanel5.setName("jPanel5"); // NOI18N

        fileNameField.setEditable(false);
        fileNameField.setName("fileNameField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${fileName}"), fileNameField, org.jdesktop.beansbinding.BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST"));
        bindingGroup.addBinding(binding);

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jNcolsField.setColumns(4);
        jNcolsField.setEditable(false);
        jNcolsField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNcolsField.setName("jNcolsField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${ncols}"), jNcolsField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jNrowsTextField.setColumns(4);
        jNrowsTextField.setEditable(false);
        jNrowsTextField.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jNrowsTextField.setName("jNrowsTextField"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${nrows}"), jNrowsTextField, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLoadOKCheckBox1.setText(resourceMap.getString("jLoadOKCheckBox1.text")); // NOI18N
        jLoadOKCheckBox1.setToolTipText(resourceMap.getString("jLoadOKCheckBox1.toolTipText")); // NOI18N
        jLoadOKCheckBox1.setFocusPainted(false);
        jLoadOKCheckBox1.setFocusable(false);
        jLoadOKCheckBox1.setName("jLoadOKCheckBox1"); // NOI18N
        jLoadOKCheckBox1.setRequestFocusEnabled(false);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${loadedOK}"), jLoadOKCheckBox1, org.jdesktop.beansbinding.BeanProperty.create("selected"));
        bindingGroup.addBinding(binding);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(fileNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 722, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jLabel3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jNcolsField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jNrowsTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 162, Short.MAX_VALUE)
                        .add(jLoadOKCheckBox1)
                        .add(282, 282, 282))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(fileNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jNcolsField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(jLabel3)
                        .add(jLabel4)
                        .add(jNrowsTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLoadOKCheckBox1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout inputPanelLayout = new org.jdesktop.layout.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inputPanelLayout.createSequentialGroup()
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(inputPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel1)
                    .add(inputPanelLayout.createSequentialGroup()
                        .add(24, 24, 24)
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inputPanelLayout.createSequentialGroup()
                .add(jLabel1)
                .add(4, 4, 4)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(8, 8, 8))
        );

        outputPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        outputPanel1.setName("outputPanel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jLogLikList1.setModel(likModel);
        jLogLikList1.setName("jLogLikList1"); // NOI18N
        jScrollPane1.setViewportView(jLogLikList1);

        jScrollPane2.setAutoscrolls(true);
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jcovTable1.setModel(covModel);
        jcovTable1.setName("jcovTable1"); // NOI18N
        jcovTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(jcovTable1);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jmuTable1.setModel(muModel);
        jmuTable1.setName("jmuTable1"); // NOI18N
        jmuTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(jmuTable1);

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jpropList1.setModel(probModel);
        jpropList1.setName("jpropList1"); // NOI18N
        jScrollPane4.setViewportView(jpropList1);

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N

        org.jdesktop.layout.GroupLayout outputPanel1Layout = new org.jdesktop.layout.GroupLayout(outputPanel1);
        outputPanel1.setLayout(outputPanel1Layout);
        outputPanel1Layout.setHorizontalGroup(
            outputPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputPanel1Layout.createSequentialGroup()
                .add(outputPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(outputPanel1Layout.createSequentialGroup()
                        .add(jLabel2)
                        .add(18, 18, 18)
                        .add(jLabel15)
                        .add(82, 82, 82)
                        .add(jLabel16)
                        .add(68, 68, 68)
                        .add(jLabel17))
                    .add(outputPanel1Layout.createSequentialGroup()
                        .add(35, 35, 35)
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 30, Short.MAX_VALUE)
                        .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 293, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel18)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 320, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(44, 44, 44))
        );
        outputPanel1Layout.setVerticalGroup(
            outputPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputPanel1Layout.createSequentialGroup()
                .add(outputPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jLabel15)
                    .add(jLabel16)
                    .add(jLabel17)
                    .add(jLabel18))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 200, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 167, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 187, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(outputPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.LEADING, mainPanelLayout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(inputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 916, Short.MAX_VALUE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(41, 41, 41)
                .add(inputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 386, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 23, Short.MAX_VALUE)
                .add(outputPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        menuBar.setName("menuBar"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.astrogrid.clustertool.ClustertoolApp.class).getContext().getActionMap(ClustertoolView.class, this);
        fileMenu.setAction(actionMap.get("loadFileTask")); // NOI18N
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setAction(actionMap.get("loadFileTask")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        fileMenu.add(jMenuItem1);

        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setEnabled(false);
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        fileMenu.add(jMenuItem3);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jRunMenu.setText(resourceMap.getString("jRunMenu.text")); // NOI18N
        jRunMenu.setName("jRunMenu"); // NOI18N

        jMenuItem4.setAction(actionMap.get("doClustering")); // NOI18N
        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jRunMenu.add(jMenuItem4);

        menuBar.add(jRunMenu);

        jInteropMenu.setText(resourceMap.getString("jInteropMenu.text")); // NOI18N
        jInteropMenu.setName("jInteropMenu"); // NOI18N
        menuBar.add(jInteropMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(965, 40));

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N
        statusPanelSeparator.setPreferredSize(new java.awt.Dimension(100, 12));

        statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text")); // NOI18N
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setText(resourceMap.getString("statusAnimationLabel.text")); // NOI18N
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 756, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .add(50, 50, 50))
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 964, Short.MAX_VALUE)
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                        .add(statusMessageLabel)
                        .add(statusAnimationLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public Task loadFileTask() {
        TableLoadChooser fc = new TableLoadChooser(tableFactory);
        StarTable table = fc.showTableDialog(getFrame());   
       
        Task task = null;
        if (table != null) {
            setFileName(table.getURL().toExternalForm() );
            fileNameField.setText(table.getURL().toExternalForm());
            task = new LoadFileTask(table);
        }
        return task;
    }
    
    /**
     * load a file - for use outside this Class. e.g. by the SAMP classes.
     * @param table
     */
    public void loadFile(StarTable table){
        TaskService taskService = getApplication().getContext().getTaskService();
        
        LoadFileTask task = new LoadFileTask(table );
        
        taskService.execute(task);

    }

    class LoadFileTask extends org.jdesktop.application.Task<AGDenseMatrix, Void> {
        private final StarTable file;
        LoadFileTask(StarTable table) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to LoadFileTaskTask fields, here.
            super(ClustertoolView.this.getApplication());
            this.file = table;
        }
        @Override protected AGDenseMatrix doInBackground() {
              try {
                 return ((ClustertoolApp) ClustertoolView.this.getApplication()).loadTable(file);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            } 
        }
        @Override protected void succeeded(AGDenseMatrix result) {
            setLoadedOK(true);
            indata = result;
            setNcols(indata.numColumns());
            setNrows(indata.numRows());
            
        }
    }
    private JFileChooser createFileChooser(String name) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(getResourceMap().getString(name + ".dialogTitle"));
        String textFilesDesc = getResourceMap().getString("txtFileExtensionDescription");
//        fc.setFileFilter(new TextFileFilter(textFilesDesc));
        return fc;
    }

    /** This is a substitute for FileNameExtensionFilter, which is
     * only available on Java SE 6.
     */
    private static class TextFileFilter extends FileFilter {

        private final String description;

        TextFileFilter(String description) {
            this.description = description;
        }

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String fileName = f.getName();
            int i = fileName.lastIndexOf('.');
            if ((i > 0) && (i < (fileName.length() - 1))) {
                String fileExt = fileName.substring(i + 1);
                if ("txt".equalsIgnoreCase(fileExt)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return description;
        }
    }

        @Action(enabledProperty = "loadedOK")
    public Task doClustering() {
        return new DoClusteringTask(getApplication());
    }
    private ClusteringResults clusterResult;
    private class DoClusteringTask extends org.jdesktop.application.Task<ClusteringResults, Void> {
        
        DoClusteringTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to DoClusteringTask fields, here.
            super(app);
        }
        @Override protected ClusteringResults doInBackground() {
        int mml_reg = 0; //TODO not connected yet
        int mml_min = 0; //TODO not connected yet
        ClusteringResults clust = ((ClustertoolApp)ClustertoolView.this.getApplication()).doClustering(
                   true, tol, mix_ver, errors, outlier, mml, ncontVar, nconterrVar, nbinVar, ncatVar,
                   nintVar, niters, 0.0, mml_min, mml_max, mml_reg,  covarianceKind, indata);
        return clust;
         }
        @Override protected void succeeded(ClusteringResults result) {
            clusterResult = result;
            
            muModel.setMatrix(clusterResult.bestmu);
            covModel.setMatrix(clusterResult.bestcov);
            probModel.setList(clusterResult.bestpp);
            likModel.setList(clusterResult.errlog);
            
        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField fileNameField;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField jB_DIM_TextField1;
    private javax.swing.JTextField jC_DIM_TextField1;
    private javax.swing.JComboBox jCovarianceComboBox1;
    private javax.swing.JTextField jE_DIM_TextField1;
    private javax.swing.JComboBox jImputationComboBox2;
    private javax.swing.JMenu jInteropMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JCheckBox jLoadOKCheckBox1;
    private javax.swing.JList jLogLikList1;
    private javax.swing.JCheckBox jMMLCheckBox1;
    private javax.swing.JTextField jM_DIM_TextField1;
    private javax.swing.JCheckBox jMeasErrorCheckBox1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JCheckBox jMixVarCheckBox1;
    private javax.swing.JTextField jNClassesTextField1;
    private javax.swing.JTextField jNcolsField;
    private javax.swing.JTextField jNrowsTextField;
    private javax.swing.JCheckBox jOutliersCheckBox1;
    private javax.swing.JTextField jP_DIM_TextField1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JMenu jRunMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTolTextField1;
    private javax.swing.JTable jcovTable1;
    private javax.swing.JTable jmuTable1;
    private javax.swing.JTextField jniterTextField1;
    private javax.swing.JList jpropList1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JPanel outputPanel1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
    private final StarTableFactory tableFactory;


    public StarTable getOutputTable() {
        AGDenseMatrix output = new AGDenseMatrix(indata);
        output.appendCol((AGDenseMatrix) clusterResult.R);
        return new DenseMatrixStarTable(output);
    }

    public StarTableFactory getTableFactory() {
        return tableFactory;
    }
}
