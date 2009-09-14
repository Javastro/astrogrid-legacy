/*
 * $Id: RobustClusterErrTest.java,v 1.2 2009/09/14 19:08:43 pah Exp $
 * 
 * Created on 4 Sep 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.cluster.cluster;

import static org.junit.Assert.*;

import java.io.InputStreamReader;
import java.io.Reader;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.io.MatrixVectorReader;

import org.astrogrid.cluster.cluster.RobustClusterErr.RobustClusterErrResult;
import org.astrogrid.matrix.Matrix;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RobustClusterErrTest {

    private Matrix data;
    private Matrix datatype;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
    }

    @Before
    public void setUp() throws Exception {
        Reader in = new InputStreamReader(RobustClusterErrTest.class.getResourceAsStream("/testdata.mm"));
        MatrixVectorReader vr = new MatrixVectorReader(in);
        data = new AGDenseMatrix(vr);
        double dataTypeArray[][] = {
                { 1  ,   4},
                { 2  ,   3},
                { 3  ,   0},
                { 4  ,   0},
                { 5  ,   0},
                { 6  ,   3}

        };
        datatype = new AGDenseMatrix(dataTypeArray);
   }

    @Test
    public void testRobust_cluster_err() {
        RobustClusterErrResult result = RobustClusterErr.robust_cluster_err(data, datatype, 2, 40, 0.0001, CovarianceKind.free);
        assertNotNull(result);
    }

}


/*
 * $Log: RobustClusterErrTest.java,v $
 * Revision 1.2  2009/09/14 19:08:43  pah
 * code runs clustering, but not giving same results as matlab exactly
 *
 * Revision 1.1  2009/09/07 16:06:12  pah
 * initial transcription of the core
 *
 */
