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
   public static char NUL    = '\u0000';     //byte 0
   public static char LF     = '\u0009' +1;  //for some reason doesn't like 000a
   public static char CR     = '\u000c' +1;  //doesn't like 000d either
   public static byte CR8    = 13;
   public static char EOF    = '\u001b';     //end of file

   public static char SPACE  = '\u0020';
   public static byte SPACE8 = 32;

   public static char PIP    = new String("'").charAt(0); //inverted comma/single quote

   public static char QUOTE  = '"';          //double quote

   public static byte[] NL   = "\n".getBytes();
   public static byte NL8    = CR8;          //new line (or byte[] NL8 = { CR8, LF8} )
}
