/*
 * $Id: AlgorithmsTest.java,v 1.2 2010/01/11 21:22:46 pah Exp $
 * 
 * Created on 9 Dec 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.matrix;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;
import no.uib.cipr.matrix.io.MatrixVectorReader;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlgorithmsTest extends AbstractMatrixTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testKmeans() throws IOException {
        
        Matrix input = new AGDenseMatrix(new MatrixVectorReader(new InputStreamReader(MatrixUtilsTest.class.getResourceAsStream("inputmatrix.txt"))));
        input=input.sliceCol(0, 5);
        Matrix result = Algorithms.centre_kmeans(input, 2, 5);
        Matrix expected = new AGDenseMatrix(new MatrixVectorReader(new InputStreamReader(MatrixUtilsTest.class.getResourceAsStream("matrixkmeans.txt"))));
        assertResult(result, expected, "kmeans");
    }

    @Test
    public void testRandperm() {
        int[] a = Algorithms.randperm(20);
        assertEquals("array length ",20, a.length);
    }
    
    @Test
    public void testDist2() {
        Matrix a = new AGDenseMatrix(new double[][]{{1, 1, 1},{1, 2, 3},{1, 3, 6}});
        Matrix b = new AGDenseMatrix(new double[][]{{2, 1, 3},{4, 4, 6}});
        
        Matrix result = Algorithms.dist2(a,b);
        Matrix expected =  new AGDenseMatrix(new double[][]{{5,43},{2,22},{14,10}});
        assertResult(result, expected, "dist2");
       
    }
    
    @Test
    public void testMultinorm() {
        Matrix x = new AGDenseMatrix(new double[][]{{1, 1, 1, 4},{1, 2, 3, 4},{1, 3, 6, 4}});
        Vector mu = new DenseVector(new double[]{1,2,3});
        Matrix cov = new AGDenseMatrix(new double[][]{{1,.4,.2},{0,1,.3},{.1,0.2,1.9}});
        
        Vector result = Algorithms.multinorm(x, mu, cov);
        Vector expected = new DenseVector(new double[]{121.1596e-4,  469.1022e-4,   35.0938e-4 ,   2.3030e-4});
        expected.add(-1.0, result);  
        double norm = expected.norm(Vector.Norm.Infinity);
        if(Math.abs(norm)> 1e-8){
            fail("multinorm function does not return correct value");
        }
     }
    @Test
    public void testtMultinorm() {
        Matrix x = new AGDenseMatrix(new double[][]{{1, 1, 1, 4},{1, 2, 3, 4},{1, 3, 6, 4}});
        Vector mu = new DenseVector(new double[]{1,2,3});
        Matrix cov = new AGDenseMatrix(new double[][]{{1,.4,.2},{0,1,.3},{.1,0.2,1.9}});
        
        Vector result = Algorithms.t_multinorm(x, mu, cov, 1.0);
        Vector expected = new DenseVector(new double[]{54.4619e-4,  748.5788e-4,   19.5648e-4,    5.5315e-4,});
        expected.add(-1.0, result);  
        double norm = expected.norm(Vector.Norm.Infinity);
        if(Math.abs(norm)> 1e-8){
            fail("t_multinorm function does not return correct value");
        }
    }
    
    @Test
    public void testDist3(){
        Matrix x = new AGDenseMatrix(new double[][]{{1,2,3},{4,5,6},{7,8,9},{10,11,12},{1,1,1}});//
        Matrix centres = new AGDenseMatrix(new double[][]{{1,1,1},{2,2,2}});
        Matrix covars[] = new AGDenseMatrix[2];
        covars[0] = new AGDenseMatrix(new double[][]{  {  0.2000 ,   0.3000 ,  -0.200},
            {0.3000,    1.5000,    0.6000},
            {-0.2000,    0.6000,    1.0000}});
        covars[1] = new AGDenseMatrix(new double[][]{ {1.2000 ,   0.3000,    0.2000},
            {0.3000 ,   0.5000,   -0.1000},
            {0.2000 ,  -0.1000,    0.4000}});
        
        Matrix expectedFull = new AGDenseMatrix(new double[][]{    {46.6666666667,     6},
            {2531.666666667,       84},
            {8826.666666667 ,      270},
            {18931.66666667  ,    564},
            {0    ,   6}

});
        
        Matrix results = Algorithms.dist3_free(x, centres, covars);
        assertResult(results, expectedFull, "dist3_full");
       //TODO test diag and spherical;
        
    }

   
    }


/*
 * $Log: AlgorithmsTest.java,v $
 * Revision 1.2  2010/01/11 21:22:46  pah
 * reasonable numerical stability and fidelity to MATLAB results achieved
 *
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
