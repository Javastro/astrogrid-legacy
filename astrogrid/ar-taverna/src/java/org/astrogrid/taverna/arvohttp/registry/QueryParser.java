/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.Stack;

/** Parses a search string into an expression tree.
 * not thread safe
 * makes a best effort to be forgiving of errors.
 * 
 * 
 * query ::= expression {['or'] expression}
 * expression ::= primExp {'and' primExp}
 * primExp ::= term | phrase | '(' query ')' | 'not' primExp
 * term ::= any string of letters and digits
 * 			 |  any string in double or single quotes  
 *          | some notation for wildcards.
* @author Noel Winstanley
 * @since Aug 9, 20062:47:14 PM
 */
class QueryParser {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(QueryParser.class);

	public QueryParser(String s) {
		if (s == null) {
			throw new IllegalArgumentException("null not allowed");
		}
		Reader r = new BufferedReader(new StringReader(s));
		st = new StreamTokenizer(r);
		st.resetSyntax();
		st.quoteChar('\'');
		st.quoteChar('\"');
		st.wordChars('A','z');
		st.wordChars('0','9');
		st.wordChars('+','@');
		st.whitespaceChars(' ',' ');
		st.lowerCaseMode(true);
	}

	protected final StreamTokenizer st;
	
	
	public AbstractQuery parse() throws InvalidArgumentException{
		try {
		st.nextToken();
		return query();
		} catch (IOException e) {
			throw new InvalidArgumentException("Failed to parse search expression",e);
		}
	}
	
	private AbstractQuery query() throws IOException, InvalidArgumentException {
		AbstractQuery result = null;
		OrQuery accum = null;
		AbstractQuery latest = null;
		while (st.ttype != StreamTokenizer.TT_EOF && st.ttype != ')') {
			if (st.ttype == StreamTokenizer.TT_WORD && st.sval.equals("or")) {
				st.nextToken();//flush
				continue;
			}
			AbstractQuery q = expression();
			if (latest == null) {
				latest = q;
				result = q;
			} else if (accum == null) { // got two - add them together.
				accum = new OrQuery();
				accum.setLeft(latest);
				result = accum;
				latest = q;
			} else { // add these two to what's alreeady accumulater
				OrQuery accum1 = new OrQuery();
				accum.setRight(accum1);
				accum = accum1;
				accum.setLeft(latest);
				latest = q;
			}
			//st.nextToken();//flush
		} // end while.
		
		// mop up the last one, if there..
		if (accum != null) {
			accum.setRight(latest);
		}
		
		return result;
	}
	
	private AbstractQuery expression() throws IOException, InvalidArgumentException {
		AbstractQuery latest = primExp();
		if (latest == null) {
			return null;
		}
		AndQuery accum = null;
		AbstractQuery result = latest;
		while (st.ttype== StreamTokenizer.TT_WORD && st.sval.equals("and")) {
			st.nextToken(); //clears and
			AbstractQuery right = primExp();
			if (right == null) {
				break;
			}
			AndQuery aq = new AndQuery();
			if (accum == null) {
				accum = aq;
				accum.setLeft(latest);
				result = accum;
				latest = right;
			} else {
				accum.setRight(aq);
				accum = aq;
				accum.setLeft(latest);
				latest = right;
			}
		}// end while
		// mop up.
		if (accum != null) {
			accum.setRight(latest);
		}
		
		return result;
	}
	

	public AbstractQuery primExp() throws IOException, InvalidArgumentException  {
			switch (st.ttype) { 
			case StreamTokenizer.TT_NUMBER: 
				throw new InvalidArgumentException("Got a number - impossible");
			case StreamTokenizer.TT_WORD: 
					if (st.sval.equals("not")) {
						NotQuery nq = new NotQuery();
						st.nextToken();
						AbstractQuery aq = primExp();
						nq.setChild(aq);
						return nq;
					} else if (st.sval.equals("or") || st.sval.equals("and") || st.sval.equals("not")){ // check for a keyword in the wrong place
						st.nextToken(); // flush this keyword
						return null;
					} else {
					TermQuery tq = new TermQuery();
					tq.setTerm(st.sval);
					st.nextToken();
					return tq;
					}
			case '\'':
			case '\"':
				PhraseQuery pq = new PhraseQuery();
				pq.setPhrase(st.sval);
				st.nextToken();
				return pq;
			case '(':	
				st.nextToken();
				AbstractQuery q =  query();
					st.nextToken(); // flush the ')
				return q;
			case ')':
				st.nextToken();
					return null;
			default:
					st.nextToken();
				return null;
			} // end case

	}
}
