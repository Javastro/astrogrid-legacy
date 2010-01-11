/*
 * $Id: ObjectiveTest.java,v 1.1 2010/01/11 21:22:46 pah Exp $
 * 
 * Created on 11 Jan 2010 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2010 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.cluster.cluster;

import static org.junit.Assert.*;

import no.uib.cipr.matrix.AGDenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;

import org.astrogrid.cluster.cluster.Objective.result;
import org.astrogrid.matrix.Matrix;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ObjectiveTest {

    private Vector q;
    private Vector cvk;
    private Vector mu;
    private Matrix data;
    private Matrix s;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        
        
    }

    @Before
    public void setUp() throws Exception {
        q = new DenseVector(new double[]{1,2,3,4});
        cvk = new DenseVector(new double[]{1,2,3});
        mu = new DenseVector(new double[]{1,2,3});
        data = new AGDenseMatrix(new double[][]{
                {1,2,3},
                {4,5,6},
                {7,8,9},
                {10,11,12}
        });
        s = new AGDenseMatrix(new double[][]{
                {.1,.2,.3},
                {.4,.5,.6},
                {.7,.8,.9},
                {.10,.11,.12}
        });
         
    }

    @Test
    public void testDiag(){
        result rs = Objective.objectiveDiag(cvk, q, mu, data, s);
        assertTrue("", Math.abs(rs.getF()-282.2959)<1e-3);
        Vector expected = new DenseVector(new double[]{-306.5838,
                -44.9524,
                -12.4055});
        
    }
    @Test
    public void testSpheric(){
        result rs = Objective.objectiveSpherical(new DenseVector(new double[]{3.0}), q, mu, data, s);
        assertTrue("", Math.abs(rs.getF()-106.3314)<1e-3);
        System.out.println(rs.getDf());
       
    }
    
    @Test
    public void testMinimize(){
        Vector result = Minimize.minimize("objectiveDiag",10, cvk, q, mu, data, s );
        Vector expected = new DenseVector(new double[]{6.7078,
                6.7106,
                6.7079});
        expected.add(-1.0, result);  
        double norm = expected.norm(Vector.Norm.Infinity);
        if(Math.abs(norm)> 1e-4){
            fail("minimize function does not return correct value");
        }
       
    }
    @Test
    public void testMinimize2(){
        Vector result = Minimize.minimize("objectiveSpherical",10, new DenseVector(new double[]{3}), q, mu, data, s );
        Vector expected = new DenseVector(new double[]{6.6952});
        expected.add(-1.0, result);  
        double norm = expected.norm(Vector.Norm.Infinity);
        if(Math.abs(norm)> 1e-4){
            fail("minimize function does not return correct value");
        }
   }
    
}



/*
 * $Log: ObjectiveTest.java,v $
 * Revision 1.1  2010/01/11 21:22:46  pah
 * reasonable numerical stability and fidelity to MATLAB results achieved
 *
 */
