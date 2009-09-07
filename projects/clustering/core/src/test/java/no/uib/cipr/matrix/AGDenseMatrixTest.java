/*
 * $Id: AGDenseMatrixTest.java,v 1.1 2009/09/07 16:06:12 pah Exp $
 * 
 * Created on 11 Dec 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package no.uib.cipr.matrix;

import static org.junit.Assert.*;

import org.astrogrid.matrix.AbstractMatrixTest;
import org.astrogrid.matrix.Matrix;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AGDenseMatrixTest extends AbstractMatrixTest{

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAGDenseMatrixVectorIntInt() {
        Vector invec = new DenseVector(new double[]{1,2,3},true);
        Matrix out = new AGDenseMatrix(invec, 2, 3);
        System.out.println(out); 
        Matrix expected = new AGDenseMatrix(new double[][]{
                {1,1,1},
                {2,2,2},
                {3,3,3},
                {1,1,1},
                {2,2,2},
                {3,3,3}
        });
        assertResult(out, expected, "vector tiling constructor");
    }

    @Test
    public void testAGDenseMatrixMatrixIntInt() {
        Matrix inmat = new AGDenseMatrix(new double[][]{{1,2,3},{4,5,6}});
        Matrix out = new AGDenseMatrix(inmat, 2, 3);
        System.out.println(out);
        Matrix expected = new AGDenseMatrix( new double[][]{
                {1,2,3,1,2,3,1,2,3},
                {4,5,6,4,5,6,4,5,6},
                {1,2,3,1,2,3,1,2,3},
                {4,5,6,4,5,6,4,5,6}
        });
        assertResult(out, expected, "matrix tiling constructor");
   }
    
    @Test 
    public void testAGDenseMatrixInverse() {
        Matrix inmat = new AGDenseMatrix(new double[][]{{1,2},{3,4}});
        Matrix out = inmat.inv();
        Matrix expected = new AGDenseMatrix(new double[][]{{-2,1},{1.5, -0.5}});
        assertResult(out, expected, "matrix inverse");
    }

}


/*
 * $Log: AGDenseMatrixTest.java,v $
 * Revision 1.1  2009/09/07 16:06:12  pah
 * initial transcription of the core
 *
 */
