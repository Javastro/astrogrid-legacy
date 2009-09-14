/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.astrogrid.clustertool;

import org.astrogrid.cluster.cluster.CovarianceKind;

/**
 *
 * @author pharriso
 */
public class ClusterControl {

    protected String fileName;

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
        this.fileName = fileName;
    }
    protected int ncont_var;

    /**
     * Get the value of ncont_var
     *
     * @return the value of ncont_var
     */
    public int getNcont_var() {
        return ncont_var;
    }

    /**
     * Set the value of ncont_var
     *
     * @param ncont_var new value of ncont_var
     */
    public void setNcont_var(int ncont_var) {
        this.ncont_var = ncont_var;
    }
    protected int nconterr_var;

    /**
     * Get the value of nconterr_var
     *
     * @return the value of nconterr_var
     */
    public int getNconterr_var() {
        return nconterr_var;
    }

    /**
     * Set the value of nconterr_var
     *
     * @param nconterr_var new value of nconterr_var
     */
    public void setNconterr_var(int nconterr_var) {
        this.nconterr_var = nconterr_var;
    }
    protected int nbin_var;

    /**
     * Get the value of nbin_var
     *
     * @return the value of nbin_var
     */
    public int getNbin_var() {
        return nbin_var;
    }

    /**
     * Set the value of nbin_var
     *
     * @param nbin_var new value of nbin_var
     */
    public void setNbin_var(int nbin_var) {
        this.nbin_var = nbin_var;
    }
    protected int ncat_var;

    /**
     * Get the value of ncat_var
     *
     * @return the value of ncat_var
     */
    public int getNcat_var() {
        return ncat_var;
    }

    /**
     * Set the value of ncat_var
     *
     * @param ncat_var new value of ncat_var
     */
    public void setNcat_var(int ncat_var) {
        this.ncat_var = ncat_var;
    }
    protected int nint_var;

    /**
     * Get the value of nint_var
     *
     * @return the value of nint_var
     */
    public int getNint_var() {
        return nint_var;
    }

    /**
     * Set the value of nint_var
     *
     * @param nint_var new value of nint_var
     */
    public void setNint_var(int nint_var) {
        this.nint_var = nint_var;
    }

    protected int nclasses;

    /**
     * Get the value of nclasses
     *
     * @return the value of nclasses
     */
    public int getNclasses() {
        return nclasses;
    }

    /**
     * Set the value of nclasses
     *
     * @param nclasses new value of nclasses
     */
    public void setNclasses(int nclasses) {
        this.nclasses = nclasses;
    }
    protected CovarianceKind covarianceKind;

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
        this.covarianceKind = covarianceKind;
    }


}
