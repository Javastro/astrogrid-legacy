package org.astrogrid.io.ascii;


/**
 * An InputStream that provides a set of convenience routines for reading from
 * an ASCII file.  Such files may be separated by some character (often a comma
 * for comma-separated variables) or may be arranged in columns.  This stream
 * provides methods for reading type-specific variables (eg, Integers, Strings,
 * Booleans, etc) from both.
 * Creation date: Jan 2001
 * @author: M Hill
 */

import java.io.*;
import org.astrogrid.io.ascii.AsciiCodes;

public class AsciiInputStream extends FilterInputStream
{
    public static int c_eoLine = AsciiCodes.LF;
    public static int c_eoFile = -1;
    private char defaultEoField = ',';
    private boolean isEOL = false;
    private boolean checkForEOLine = true;
    
    /**
     * Constructor - pass in the stream that it should read from
     */
    public AsciiInputStream(java.io.InputStream in) {
        super(in);
    }

    /**
     * Private method used to see if the given character is an end of line
     * marker (ie, a line feed) or indicates the end of file when no more
     * characters are available
     * @return boolean true if given character is end of line
     * @param c int
     */
    private boolean charIsEOLine(int c) {
        if (checkForEOLine)
            return ((c == c_eoLine) || (c == c_eoFile));
        else
            return false;
    }

    /**
     * Returns true if the end of line marker is set
     */
    public boolean isEOLine() {
        return isEOL;
    }

    /**
     * Sets the eoField character
     */
    public void setEoField(char newEoFieldMarker)
    {
        defaultEoField = newEoFieldMarker;
    }
    
    /**
     * Returns true/false if it thinks the given string indicates true/false,
     * or throws exception if it can't tell
     */
    private boolean isFieldBoolean(String field) throws IOException
    {
        String firstChar = field.substring(0,0);
        
        if (firstChar.equalsIgnoreCase("Y") || firstChar.equalsIgnoreCase("T")) //yes or true
            return true;
        
        if (firstChar.equalsIgnoreCase("N") || firstChar.equalsIgnoreCase("F")) //yes or true
            return false;

        throw new IOException("Cannot resolve "+field+" to boolean");
    }
    
    /**
     * Reads up to next defaultEoField character and returns whether it thinks
     * that field indicates true or false.
     * @see isFieldBoolean
     */
    public boolean readBoolean() throws IOException
    {
        return isFieldBoolean(readString().trim());
    }
    
    /**
     * Reads up to the next occurence of the given character, and returns whether
     * it thinks that field indicates true or false
     * @see isFieldBoolean
     */
    public boolean readBoolean(char eoField) throws IOException
    {
        return isFieldBoolean(readString(eoField).trim());
    }

    /**
     * Reads up to the next defaultEoField character and returns a long
     * representation of it
     */
    public long readNum() throws IOException {
        return readNum(defaultEoField);
    }

    /**
     * Reads an integer from the ASCII stream, by reading a string until
     * the given end-of-field marker and then converting it to an integer.
     * @return int
     */
    public long readNum(char eoField) throws IOException {
        
        //read string of given width
        //trim cuts spaces and all chars less than a space (eg cr/lf)
        String numString = readString(eoField).trim();
        
        if (numString.length() == 0)
            return 0;
        
        //convert to integer object...
        Long numLong = new Long(numString);
        
        //,,,so that we can convert that to an int.
        return numLong.longValue();
    }

    /**
     * Reads an integer from the ASCII stream, by reading a string of the
     * given length (ie, column width) and then converting it to an integer.
     */
    public long readNum(int width) throws IOException {
        
        //read string of given width
        //trim cuts spaces and all chars less than a space (eg cr/lf)
        String numString = readString(width).trim();
        
        if (numString.length() == 0)
            return 0;
        
        //convert to integer object...
        Long numLong = new Long(numString);
        
        //,,,so that we can convert that to an int.
        return numLong.longValue();
    }

    /**
     * Reads the characters up to the next default end-of-field marker
     */
    public String readString() throws IOException
    {
        return readString(defaultEoField);
    }

    /**
     * Reads the characters up to the next occurence of the given end-of-field
     * marker, ignoring all initial spaces.
     */
    public String readString(char eoFieldMarker) throws IOException
    {
        StringBuffer returnString = new StringBuffer();
        
        int c = read();
        
        //read spaces
        while ( c == AsciiCodes.SPACE) c = read();
        
        //read until end of field marker found
        while (( c != eoFieldMarker) && !charIsEOLine(c)) {
            returnString.append((char) c);
            c =  read();
        }
        
        isEOL = charIsEOLine(c);    //mark if reached end of line
        
        return returnString.toString();
    }
    
    /**
     * Reads the next number of characters corresponding to the given column
     * width
     */
    public String readString(int width) throws IOException
    {
        StringBuffer returnString = new StringBuffer();
        
        int c = AsciiCodes.NUL;
        
        while ((width > 0) && !charIsEOLine(c)) {
            c = read();
            
            if (!charIsEOLine(c)) {
                returnString.append( (char) c);
            }
            
            width --;
        }
        
        isEOL = charIsEOLine(c);    //mark if reached end of line
        
        return returnString.toString();
    }
    
    /**
     * Reads to the end of the line, returning all characters from the current
     * point to there.  If the pointer is already at the end of the line (due
     * to reading the last field, for example), nothing happens but the next
     * read will start the next line.
    */
    public String readToEOL() throws IOException {
        
        int c = AsciiCodes.NUL;
        StringBuffer line = new StringBuffer();
        
        //if we've already reached the end of line, eg due to a readString(),
        //we don't want to do this as we'll end up reading the complete next
        //line
        if (!isEOL) {
            while ((c != c_eoFile) && (c != c_eoLine)) {
                c = read();
                line.append( (char) c);
            }
        }
        
        isEOL = false;
        
        return line.toString();
    }
}

