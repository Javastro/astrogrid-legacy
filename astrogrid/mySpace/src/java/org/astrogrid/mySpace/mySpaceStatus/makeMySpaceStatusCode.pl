#!/usr/local/bin/perl

#+
#  Name:
#     makeMySpaceStatusCode
#  Purpose:
#     Generate MySpaceStatusCode.java from statuscodes.properties.
#  Language:
#     Perl 5.
#  Type of Module:
#     Perl script.
#  Invocation:
#     makeMySpaceStatusCode
#  Arguments:
#     None.
#  Description:
#     makeMySpaceStatusCode.pl is a script which takes a template for the
#     java class MySpaceStatusCode and a properties file containing a list
#     of all the error codes known to the mySpace system and generates the
#     java source for the MySpaceStatusCode class.
#     
#     The MySpaceStatusCode template contains mostly java code, but it also
#     includes a number of tags.  makeMySpaceStatusCode.pl substitutes
#     these tags with java code-fragments generated from the list of error
#     codes.  The tags are:
#     
#       // ++-- Codes tag --
#       // ++-- getCode tag --
#       // ++-- getCodeMessage tag --
#     
#     Note that the format of these tags is such that the template can be
#     compiled by a java compiler.  This feature is provided as a
#     de-bugging aid.
#     
#     The properties file containing the error code has the usual format
#     for a properties file.  Its entries are of the form:
#     
#       error_code=description
#     
#     Note that:
#     
#     * The error codes are turned into a java variables and hence should
#       conform to the naming conventions for java variables (for example,
#       they should not contain embedded `-' characters).  Also, the error
#       codes are forced to be unique; the resulting java class will not
#       compile if there are duplicates.
#     
#     * The description should be enclosed in double-quotes (").
#     
#     * There should be no spaces on either side of the equals sign (=).
#     
#     * Blank lines and comment lines (which start with a `#') may be
#       introduced as required.
#  Input Files:
#     statuscodes.properties
#       Properties file containing a list of all the error codes in the
#       mySpace system.
#     
#     MySpaceStatusCode.template
#       Template for the MySpaceStatusCode class.
#  Output File:
#     MySpaceStatusCode.java
#       Java source file for the MySpaceStatusCode class generated from
#       the properties file and template.
#  Algorithm:
#     Intialise the global variables.
#     Read and decompose the properties file.
#     Generate the list of integer codes.
#     Generate the getCode method.
#     Generate the getCodeMessage method.
#     Generate MySpaceStatusCode.java from the template.
#     Report any error.
#  Authors:
#     ACD: A C Davenhall (Edinburgh)
#  History:
#     2/7/03  (ACD): Original version.
#     4/7/03  (ACD): First stable version.
#     30/7/03 (ACD): Modified to make double-quotes around the message
#        optional.
#-

#
#  Initialise the command line and other variables.

    $status = 0;
    $statusMessage = "Success.";

    $true = 1;
    $false = 0;

#
#  Read and decompose the properties file.

    $status = &ReadAndDecodeProperties();

#
#  Generate the list of integer codes, the getCode method and the.
#  getCodeMessage method.

    $status = &GenerateCodes();
    $status = &GenerateGetCode();
    $status = &GenerateGetCodeMessage();

#
#  Generate MySpaceStatusCode.java from the template.

    $status = &GenerateMySpaceStatusCode();

#
#  Report any error.

    if ($status != 0)
    {  print "! (**ERROR**) $statusMessage \n";
    }

#
# -- Functions ----------------------------------------------------------
#
# ReadAndDecodeProperties
#
# Algorithm:
#  Initialise the number of error codes.
#  Attempt to open the properties file.
#  If ok then
#    Do while (more lines are to be read)
#      Obtain the current line.
#      If the line is not blank then
#        Remove any leading spaces.
#        If the line is not a comment then
#          Obtain the code.
#          Obtain the message.
#          Add the code and message to the internal arrays.
#        end if
#      end if
#    end do
#    Close the properties file.
#    If no properties were found then
#      Set the status; empty properties file.
#    end if
#  else
#    Set the status; failed to open properties file.
#  end if


sub ReadAndDecodeProperties
{  if ($status == 0)
   {

#
#    Initialise the number of error codes.

      $numCodes = -1;

#
#    Attempt to open the properties file and proceed if ok.

      $openStatus = open (PROP, "statuscodes.properties");
      if ($openStatus != 0)
      {

#
#       Continue while there are more lines in the properties file to
#       read.

         while (<PROP>)
         {  chop($_);
            $currentLine = $_;

#
#          Proceed if the line is not blank.

            if ($currentLine =~ /\S/)
            {

#
#             Remove any leading spaces.

               $currentLine =~ s/ *//;

#
#             Proceed if the line is not a comment.

               $hashPos = index($currentLine, "#");
               if ($hashPos != 0)
               {

#
#                Obtain the code and message and append them to the
#                arrays.

	          $equalPos = index($currentLine, "=");
                  if ($equalPos > -1)
                  {  $numCodes = $numCodes + 1;

                     $currentCode = substr($currentLine, 0, $equalPos);
                     $currentMessage =
                       substr($currentLine, $equalPos + 1, 300);

#
#                   Remove any double quotes.

                     $currentMessage =~ s/"//g;

                     $codes[$numCodes] = $currentCode;
                     $messages[$numCodes] = $currentMessage;
		  }
	       }
	    }
	 }

#
#       Close the properties file.

	 close (PROP);

	 $numCodes = $numCodes + 1;

#
#       Check that some codes were found.

         if ($numCodes <= 0)
	 {  $status = 1;
            $statusMessage = "Empty properties file.";
         }
      }
      else
      {  $status = 1;
         $statusMessage = "Failed to open the properties file.";
      }
   }
   $status;
}

#
# -----------------------------------------------------------------------
#
# GenerateCodes
#
# Generate the list of codes.

sub GenerateCodes
{  if ($status == 0)
   {  $codesJava = "";

      for ($loop = 0; $loop < $numCodes; $loop++)
      {  $javaLine = "   public static final int " . $codes[$loop]
           . " = " . $loop . ";\n";
         $codesJava = $codesJava . $javaLine;
      } 
   }

   $status;
}


#
# -----------------------------------------------------------------------
#
# GenerateGetCode
#
# Generate the list of codes.

sub GenerateGetCode
{  if ($status == 0)
   {  $getCodeJava = "";

      for ($loop = 0; $loop < $numCodes; $loop++)
      {  if ($loop == 0)
         {  $javaLine =
              "      if (code == MySpaceStatusCode." . $codes[$loop]
              . ")\n" . "      {  codeString = \"" . $codes[$loop]
              . "\";\n" . "      }\n";
         }
         else
         {  $javaLine =
              "      else if (code == MySpaceStatusCode." . $codes[$loop]
              . ")\n" . "      {  codeString = \"" . $codes[$loop]
              . "\";\n" . "      }\n";
         }

         $getCodeJava = $getCodeJava . $javaLine;
      } 
   }

   $status;
}

#
# -----------------------------------------------------------------------
#
# GenerateGetCodeMessage
#
# Generate the list of codes.

sub GenerateGetCodeMessage
{  if ($status == 0)
   {  $getCodeMessageJava = "";

      for ($loop = 0; $loop < $numCodes; $loop++)
      {  if ($loop == 0)
         {  $javaLine =
              "      if (code == MySpaceStatusCode." . $codes[$loop]
              . ")\n" . "      {  message = \"" . $messages[$loop]
              . "\";\n" . "      }\n";
         }
         else
         {  $javaLine =
              "      else if (code == MySpaceStatusCode." . $codes[$loop]
              . ")\n" . "      {  message = \"" . $messages[$loop]
              . "\";\n" . "      }\n";
         }

         $getCodeMessageJava = $getCodeMessageJava . $javaLine;
      } 
   }

   $status;
}

#
# -----------------------------------------------------------------------
#
# GenerateMySpaceStatusCode
#
# Attempt to open to open the MySpaceStatusCode template.
# If ok then
#   Read the MySpaceStatusCode template.
#   Close the MySpacesStatusCode template.
#   Substitute the codes tag for the actual integer codes.
#   Attempt to open the MySpaceStatusCode java file.
#     Write the modified java code.
#     Close the java file.
#   else
#     Error: failed to open java file.
#   end if
# else
#   Error: failed to open input template file.
# end if

sub GenerateMySpaceStatusCode
{  if ($status == 0)
   {

#
#    Attempt to open to open the MySpaceStatusCode template and proceed
#    if ok.

      $openStatus = open (TEMPLATE, "MySpaceStatusCode.template");
      if ($openStatus != 0)
      {  

#
#       Read the MySpaceStatusCode template.

         @template = <TEMPLATE>;

#
#       Close the MySpacesStatusCode template.

         close (TEMPLATE);

#
#       Substitute the codes tag for the actual integer codes.

         @java = @template;

         for ($loop = 0; $loop < $#java; $loop++)
         {  $java[$loop] =~ s/\/\/ \+\+-- Codes tag --/$codesJava/;
            $java[$loop] =~ s/\/\/ \+\+-- getCode tag --/$getCodeJava/;
            $java[$loop] =~
              s/\/\/ \+\+-- getCodeMessage tag --/$getCodeMessageJava/;
	 }

#
#       Attempt to open the MySpaceStatusCode java file.  If ok write
#       and then close the java file.

         $openStatus = open (JAVA, ">MySpaceStatusCode.java");
         if ($openStatus != 0)
         {  print JAVA @java;
            close (JAVA);
         }
         else
         {  $status = 1;
            $statusMessage = "Failed to open MySpaceStatusCode.java.";
         }
      }
      else
      {  $status = 1;
         $statusMessage = "Failed to open MySpaceStatusCode.template.";
      }
   }
   $status;
}

#
# -----------------------------------------------------------------------
