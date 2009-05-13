/*
 * $Id: ByteArrayTarget.java,v 1.1 2009/05/13 13:20:41 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.ByteArrayOutputStream;

/**
 * For holding in-memory targets, eg for tests
 *
 */

public class ByteArrayTarget extends StreamTarget implements TargetIdentifier {
   
   public ByteArrayTarget() {
      super(new ByteArrayOutputStream());
   }

   /** return string contents */
   public String getString() {
      return out.toString();
   }

   /** return contents as array of bytes */
   public byte[] getBytes() {
      return ((ByteArrayOutputStream) out).toByteArray();
   }


   
}

