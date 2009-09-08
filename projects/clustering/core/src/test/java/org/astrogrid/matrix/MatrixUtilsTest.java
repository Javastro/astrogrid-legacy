/*
 * $Id: MatrixUtilsTest.java,v 1.2 2009/09/08 19:22:21 pah Exp $
 * 
 * Created on 5 Dec 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.matrix;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.io.MatrixVectorReader;

import org.astrogrid.cluster.cluster.CovarianceKind;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MatrixUtilsTest extends AbstractMatrixTest{
    
    protected Matrix input;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        input = new AGDenseMatrix(new MatrixVectorReader(new InputStreamReader(MatrixUtilsTest.class.getResourceAsStream("inputmatrix.txt"))));
    }

    @Test
    public void testCov() throws IOException {
        Matrix result = MatrixUtils.cov(input);
        Matrix expected = new AGDenseMatrix(new MatrixVectorReader(new InputStreamReader(MatrixUtilsTest.class.getResourceAsStream("matrixcov.txt"))));
        assertResult(result, expected, "covariance function");
    }

    @Test
    public void testSum() throws IOException {
        Vector result = MatrixUtils.sum(input, 1);
        Vector expected = new DenseVector(new MatrixVectorReader(new InputStreamReader(MatrixUtilsTest.class.getResourceAsStream("matrixsum.txt"))));
        expected.add(-1.0, result);  
        double norm = expected.norm(Vector.Norm.Infinity);
        if(Math.abs(norm)> 1e-8){
            fail("sum function does not return correct value");
        }
    }
    
    @Test
    public void testSimple(){
        double[][] a={{1, 1, 1},{1, 2, 3},{1, 3, 6}};
        Matrix start = new AGDenseMatrix(a);
        Matrix result = MatrixUtils.cov(start);
    }
    
    @Test
    public void testDeterminant(){
        Matrix mat = new AGDenseMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,10}});
        Double result = mat.det();
        System.out.println("error in determinant=" + Math.abs(-3 - result));
        assertTrue("determinant error too much ", Math.abs(-3 - result) < 1e-9);
        
    }
    @Test
    public void testDeterminant2(){
        Matrix mat = new AGDenseMatrix(new double[][]{   {0.5472 ,  -0.2917 ,  -0.1369},
                {-0.1772 ,   0.4326 ,  -0.1369},
                { -0.1772,   -0.2917 ,   0.5874}
});
        Double result = mat.det();
        System.out.println("error in determinant=" + Math.abs(0.06218776 - result));
        assertTrue("determinant error too much ", Math.abs(0.06218776 - result) < 1e-7);
        
    }

}


/*
 * $Log: MatrixUtilsTest.java,v $
 * Revision 1.2  2009/09/08 19:22:21  pah
 * added new determinant test to check against sign changes
 *
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
