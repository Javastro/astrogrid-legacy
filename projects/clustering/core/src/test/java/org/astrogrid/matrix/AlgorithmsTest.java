/*
 * $Id: AlgorithmsTest.java,v 1.1 2009/09/07 16:06:11 pah Exp $
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

}


/*
 * $Log: AlgorithmsTest.java,v $
 * Revision 1.1  2009/09/07 16:06:11  pah
 * initial transcription of the core
 *
 */
