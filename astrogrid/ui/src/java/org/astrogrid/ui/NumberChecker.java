// NumberChecker v1.0
// Alan Maxwell
//
// Version History
//
// 1.0:  14 Nov 2002
//       Initial version.
//

package org.astrogrid.ui;

public class NumberChecker
{
  /**
   * Tests to see if string testInt *COULD* parse as an integer number.
   * 
   * 'Could' is used above because this function does not assume the 
   * number passed is completed yet, so it checks if the passed string is
   * either an integer number, or a valid START to an integer number.
   *
   * This is used in some UI components when the user is required to enter
   * an integer in a text field...
   */
  public static boolean isPotentialInt(String testInt)
  {
    if (testInt.length() < 1)
    {
      return true;
    };

    char testChar = testInt.charAt(0);
       
    if ( !Character.isDigit(testChar) && !(testChar == '-') )
    {
      return false;
    };

    int charLoop = 1; // Already tested the first char so not '0'...

    boolean isInt = true;
    boolean foundExp = false;

    boolean hadNumber = (testChar != '-');

    while ( (isInt == true) && (charLoop < testInt.length()) )
    {
      testChar = testInt.charAt(charLoop);

      if (
           !Character.isDigit(testChar) &&
           !(testChar == '-') &&
           !(testChar == 'e') &&
           !(testChar == 'E')
         )
      {
        isInt = false;
      }
      else if (testChar == '-')
      {
        // The only time we allow a second '-' in the double is
        // immediately following the exponent 'e'...

        // We know we started at char position '1' in the string so if
        // we are here it is safe to look at the previous character!
        if (
             (testInt.charAt(charLoop - 1) != 'e') &&
             (testInt.charAt(charLoop - 1) != 'E') 
           )
        {
          isInt = false;
        };
      }
      else if ( (testChar == 'e') || (testChar == 'E') )
      {
        if ( (hadNumber == false) || (foundExp == true) )
        {
          // Already found a previous exponent, can't have more than 1!
          isInt = false;
        }
        else
        {
          foundExp = true;
        };
      }
      else if (hadNumber == false)
      {
        hadNumber = true;
      };

      charLoop++;
    } 
    
    return isInt;
 }
  
  /**
   * Tests to see if string testDouble *COULD* parse as a double number.
   * 
   * 'Could' is used above because this function does not assume the 
   * number passed is completed yet, so it checks if the passed string is
   * either a double number, or a valid START to a double number.
   *
   * This is used in some UI components when the user is required to enter
   * a double in a text field...
   */
  public static boolean isPotentialDouble(String testDouble)
  {
    if (testDouble.length() < 1)
    {
      return true;
    };

    char testChar = testDouble.charAt(0);
       
    if ( !Character.isDigit(testChar) && !(testChar == '-') )
    {
      return false;
    };

    int charLoop = 1; // Already tested the first char so not '0'...

    boolean isDouble = true;
    boolean foundPoint = false;
    boolean foundExp = false;

    boolean hadNumber = (testChar != '-');

    while ( (isDouble == true) && (charLoop < testDouble.length()) )
    {
      testChar = testDouble.charAt(charLoop);

      if (
           !Character.isDigit(testChar) &&
           !(testChar == '-') &&
           !(testChar == '.') &&
           !(testChar == 'e') &&
           !(testChar == 'E')
         )
      {
        isDouble = false;
      }
      else if (testChar == '-')
      {
        // The only time we allow a second '-' in the double is
        // immediately following the exponent 'e'...

        // We know we started at char position '1' in the string so if
        // we are here it is safe to look at the previous character!
        if (
             (testDouble.charAt(charLoop - 1) != 'e') &&
             (testDouble.charAt(charLoop - 1) != 'E') 
           )
        {
          isDouble = false;
        };
      }
      else if (testChar == '.')
      {
        if ( (hadNumber == false) || (foundPoint == true) || (foundExp == true) )
        {
          // Already found another point OR have found the exponent 'e'
          // BEFORE the point, which can't be right...
          isDouble = false;
        }
        else
        {
          foundPoint = true;
        };
      }
      else if ( (testChar == 'e') || (testChar == 'E') )
      {
        if ( (hadNumber == false) || (foundExp == true) )
        {
          // Already found a previous exponent, can't have more than 1!
          isDouble = false;
        }
        else
        {
          foundExp = true;
        };
      }
      else if (hadNumber == false)
      {
        hadNumber = true;
      };

      charLoop++;
    } 
    
    return isDouble;
 }
}
