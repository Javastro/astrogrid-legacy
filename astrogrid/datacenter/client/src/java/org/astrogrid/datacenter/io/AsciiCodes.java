/*
 * $Id: AsciiCodes.java,v 1.1 2003/11/14 00:36:40 mch Exp $
 */

package org.astrogrid.datacenter.io;

/**
 * Description: ASCII Codes.  An interface that can be implemented by
 * any class that wants short-cut access to various standard ASCII
 * codes, such as Line Feed (LF), End of File (EOF), etc
 *
 * @see Subsystem    : JUICE
 *
 * @version          :
 * @ADD Chapter      :
 * @Created          : 16/01/2000
 * @Last Update      :
 * @CMS Library      :
 *
 * @author           : M Hill
 *
 *
 **/

public interface AsciiCodes
{
   /** NUL - char 0 */
   public static char NUL    = '\u0000';     //byte 0
   /** Line Feed - char 10 */
   public static char LF     = '\u0009' +1;  //for some reason doesn't like 000a
   /** Carriage Return - char 13 */
   public static char CR     = '\u000c' +1;  //doesn't like 000d either
   /** Carriage Return - byte 13 (8 bit) */
   public static byte CR8    = 13;
   /** End of File - char 27  */
   public static char EOF    = '\u001b';     //end of file

   /** Space - char 32 */
   public static char SPACE  = '\u0020';
   /** Space - byte 32 (8 bit) */
   public static byte SPACE8 = 32;

   /** single quote/inverted comma */
   public static char PIP    = new String("'").charAt(0); //inverted comma/single quote
   /** double quote */
   public static char QUOTE  = '"';          //double quote

   /** New line - created from '\n' */
   public static byte[] NL   = "\n".getBytes();
   /** New line - 8 bit, NB this is probably platform dependent... */
   public static byte NL8    = CR8;          //new line (or byte[] NL8 = { CR8, LF8} )
}

/*
$Log: AsciiCodes.java,v $
Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.3  2003/10/02 12:53:49  mch
It03-Close

 */
