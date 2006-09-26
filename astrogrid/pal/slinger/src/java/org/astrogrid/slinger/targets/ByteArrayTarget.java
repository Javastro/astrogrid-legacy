/*
 * $Id: ByteArrayTarget.java,v 1.2 2006/09/26 15:34:42 clq2 Exp $
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

