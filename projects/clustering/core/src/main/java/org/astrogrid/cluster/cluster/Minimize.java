/*
 * $Id: Minimize.java,v 1.2 2010/01/11 21:22:46 pah Exp $
 * 
 * Created on 29 Dec 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.cluster.cluster;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;

import org.astrogrid.matrix.Matrix;
import static org.astrogrid.matrix.MatrixUtils.*;
import static org.astrogrid.matrix.Algorithms.*;
import static java.lang.Math.*;

public class Minimize {

    static Vector minimize(String f, int length, Object... args){
        // Minimize a continuous differentialble multivariate function. Starting point
        // is given by "X" (D by 1), and the function named in the string "f", must
        // return a function value and a vector of partial derivatives. The Polack-
        // Ribiere flavour of conjugate gradients is used to compute search directions,
        // and a line search using quadratic and cubic polynomial approximations and the
        // Wolfe-Powell stopping criteria is used together with the slope ratio method
        // for guessing initial step sizes. Additionally a bunch of checks are made to
        // make sure that exploration is taking place and that extrapolation will not
        // be unboundedly large. The "length" gives the length of the run: if it is
        // positive, it gives the maximum number of line searches, if negative its
        // absolute gives the maximum allowed number of function evaluations. You can
        // (optionally) give "length" a second component, which will indicate the
        // reduction in function value to be expected in the first line-search (defaults
        // to 1.0). The function returns when either its length is up, or if no further
        // progress can be made (ie, we are at a minimum, or so close that due to
        // numerical problems, we cannot get any closer). If the function terminates
        // within a few iterations, it could be an indication that the function value
        // and derivatives are not consistent (ie, there may be a bug in the
        // implementation of your "f" function). The function returns the found
        // solution "X", a vector of function values "fX" indicating the progress made
        // and "i" the number of iterations (line searches or function evaluations,
        // depending on the sign of "length") used.
        //
        // Usage: [X, fX, i] = minimize(X, f, length, P1, P2, P3, P4, P5)
        //
        // See also: checkgrad
        //
        // Copyright (C) 2001 and 2002 by Carl Edward Rasmussen. Date 2002-02-13

        final double RHO = 0.01;                            // a bunch of constants for line searches
        final double SIG = 0.5;       // RHO and SIG are the constants in the Wolfe-Powell conditions
        final double INT = 0.1;    // don't reevaluate within 0.1 of the limit of the current bracket
        final double EXT = 3.0;                    // extrapolate maximum 3 times the current bracket
        final int MAX = 20;                         // max 20 function evaluations per line search
        final double RATIO = 100;                                      // maximum allowed slope ratio

        Method func;
        Class clazz;
        Vector X = (Vector) args[0];
       
        
        try {
            clazz = Class.forName("org.astrogrid.cluster.cluster.Objective");
            func = clazz.getMethod(f, new Class[]{Object.class,Vector.class,Vector.class,Matrix.class,Matrix.class});
        } catch (Exception e) {
            throw new IllegalArgumentException("cannot instantiate the funtion to be minimized ",e);
        } 
        //
        //    if max(size(length)) == 2, red=length(2); length=length(1); else red=1; end
        //    if length>0, S=['Linesearch']; else S=['Function evaluation']; end
        int red=1; 
        int i = 0;                                            // zero the run length counter
        boolean ls_failed = false;                             // no previous line search has failed
        Vector fX;
        double f1;
        Vector df1;
        try {
            
            Objective.result result = (org.astrogrid.cluster.cluster.Objective.result) func.invoke(null, args);                      // get function value and gradient
            f1 = result.getF();
            df1 = result.getDf();

            i = i + ((length<0)?1:0);                                            // count epochs?!
            Vector s = result.getDf().scale(-1.0);                                        // search direction is steepest
            double d1 = -s.dot(s);                                                 // this is the slope
            double z1 = red/(1-d1);                                  // initial step is red/(|s|+1)

            while( i < abs(length)   ){                                   // while not finished
                i = i + ((length>0)?1:0);                                      // count iterations?!
                Vector X0 = new DenseVector(X); double f0 = f1; Vector df0 = new DenseVector(df1);                   // make a copy of current values
                X.add(z1,s);                                             // begin line search
                args[0]=X;
                result = (org.astrogrid.cluster.cluster.Objective.result) func.invoke(null, args);
                double f2 = result.getF(); Vector df2 = result.getDf();
                i = i + ((length<0)?1:0);                                          // count epochs?!
                double d2 = df2.dot(s);
                double f3 = f1; double d3 = d1; double z3 = -z1;             // initialize point 3 equal to point 1
                int M;
                if (length>0){
                    M = MAX;
                } else {
                    M = min(MAX, -length-i);
                }
                boolean success = false; double limit = -1;                     // initialize quanteties
                double A,B,z2;
                while (true){
                    while (((f2 > f1+z1*RHO*d1) || (d2 > -SIG*d1)) && (M > 0)){
                        limit = z1;                                         // tighten the bracket
                        if (f2 > f1){
                            z2 = z3 - (0.5*d3*z3*z3)/(d3*z3+f2-f3);                 // quadratic fit
                        }
                        else {
                            A = 6*(f2-f3)/z3+3*(d2+d3);                                 // cubic fit
                            B = 3*(f3-f2)-z3*(d3+2*d2);
                            z2 = (sqrt(B*B-A*d2*z3*z3)-B)/A;       // numerical error possible - ok!
                        }
                        if( Double.isNaN(z2) || z2 == Double.NEGATIVE_INFINITY || z2 == Double.POSITIVE_INFINITY){
                            z2 = z3/2;                  // if we had a numerical problem then bisect
                        }
                        z2 = max(min(z2, INT*z3),(1-INT)*z3);  // don't accept too close to limits
                        z1 = z1 + z2;                                           // update the step
                        X.add(z2,s);
                        args[0]=X;
                        result = (org.astrogrid.cluster.cluster.Objective.result) func.invoke(null, args);
                        f2 = result.getF();
                        df2 = result.getDf();
                        M = M - 1; i = i + ((length<0)?1:0);                           // count epochs?!
                        d2 = df2.dot(s);
                        z3 = z3-z2;                    // z3 is now relative to the location of z2
                    }
                    if (f2 > f1+z1*RHO*d1 || d2 > -SIG*d1){
                        //disp('failure, break')
                        break;  
                    }// this is a failure
                    else if (d2 > SIG*d1){
                        //disp('success, break')
                        success = true; break;  
                    }// success
                    else if (M == 0) {
                        //disp('M = 0, failure, break')
                        break;                                                          // failure
                    }
                    A = 6*(f2-f3)/z3+3*(d2+d3);                      // make cubic extrapolation
                    B = 3*(f3-f2)-z3*(d3+2*d2);
                    z2 = -d2*z3*z3/(B+sqrt(B*B-A*d2*z3*z3));        // num. error possible - ok!

                    if(  Double.isNaN(z2)  || z2 == Double.NEGATIVE_INFINITY || z2 == Double.POSITIVE_INFINITY || z2 < 0 ){  // num prob or wrong sign?
                        //disp('z2 is nan or inf or not real')
                        if (limit < -0.5)                               // if we have no upper limit
                            z2 = z1 * (EXT-1);                 // the extrapolate the maximum amount
                        else
                            z2 = (limit-z1)/2;                                   // otherwise bisect
                    }
                    else if ((limit > -0.5) && (z2+z1 > limit)) {          // extraplation beyond max?
                        z2 = (limit-z1)/2;                                               // bisect
                    }
                    else if ((limit < -0.5) && (z2+z1 > z1*EXT)) {      // extrapolation beyond limit
                        z2 = z1*(EXT-1.0);                           // set to extrapolation limit
                    }
                    else if( z2 < -z3*INT){
                        z2 = -z3*INT;
                    }
                    else if ((limit > -0.5) && (z2 < (limit-z1)*(1.0-INT)) ){  // too close to limit?
                        z2 = (limit-z1)*(1.0-INT);
                    }
                    f3 = f2; d3 = d2; z3 = -z2;                  // set point 3 equal to point 2
                    z1 = z1 + z2; X.add(z2,s);                      // update current estimates
                    args[0]=X;
                    result = (org.astrogrid.cluster.cluster.Objective.result) func.invoke(null,args);
                    f2 = result.getF();
                    df2 = result.getDf();
                    M = M - 1; i = i + ((length<0)?1:0);                             // count epochs?!
                    d2 = df2.dot(s);
                }                                                      // end of line search

                if (success){                                         // if line search succeeded
                    f1 = f2;
                    //         fprintf('//s //6i;  Value //4.6e\r', S, i, f1);
                    s = sub(s.scale((df2.dot(df2)-df1.dot(df2))/(df1.dot(df1))) , df2);      // Polack-Ribiere direction
                    Vector tmp = df1; df1 = df2; df2 = tmp;                         // swap derivatives
                    d2 = df1.dot(s);
                    if (d2 > 0){                                      // new slope must be negative
                        s = new DenseVector(df1).scale(-1.0);                              // otherwise use steepest direction
                        d2 = -s.dot(s);
                    }
                    z1 = z1 * min(RATIO, d1/(d2-Double.MIN_VALUE));          // slope ratio but max RATIO
                    d1 = d2;
                    ls_failed = false;                              // this line search did not fail
                }
                else {
                    X = X0; f1 = f0; df1 = df0;  // restore point from before failed line search
                    if (ls_failed || i > abs(length)){          // line search failed twice in a row
                        break;                             // or we ran out of time, so we give up
                    }
                    Vector tmp = df1; df1 = df2; df2 = tmp;                         // swap derivatives
                    s = new DenseVector(df1).scale(-1.0);;                                                    // try steepest
                    d1 = -s.dot(s);
                    z1 = 1/(1-d1);
                    ls_failed = true;                                    // this line search failed
                }
            }
            // fprintf('\n');
            return X;
        } catch (Exception e) {
            throw new RuntimeException("problem with invocation of method to be minimized", e);
        } 

    }

}


/*
 * $Log: Minimize.java,v $
 * Revision 1.2  2010/01/11 21:22:46  pah
 * reasonable numerical stability and fidelity to MATLAB results achieved
 *
 * Revision 1.1  2010/01/05 21:27:13  pah
 * basic clustering translation complete
 *
 */
