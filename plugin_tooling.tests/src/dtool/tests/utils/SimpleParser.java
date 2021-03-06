/*******************************************************************************
 * Copyright (c) 2012, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package dtool.tests.utils;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;

/** 
 * A very simple, unoptimized parser for simple languages.
 */
public class SimpleParser {
	
	public static final int EOF = -1;
	
	protected final String source;
	protected int pos;
	protected String lastToken;

	public SimpleParser(String source) {
		this.source = assertNotNull(source);
		this.pos = 0;
	}
	
	public final String getSource() {
		return source;
	}
	
	public final int getSourcePosition() {
		return pos;
	}
	
	@Override
	public String toString() {
		return source.substring(0, pos) + "<--parser-->" + source.substring(pos, source.length());
	}
	
	public SimpleParser copyState() {
		SimpleParser newParser = new SimpleParser(source);
		newParser.pos = pos;
		newParser.lastToken = lastToken;
		return newParser;
	}
	
	/** Gets the character from absolute position index, or EOF if index exceeds source.length. */
	public static int getCharacter(String source, int index) {
		if(index >= source.length()) {
			return EOF;
		}
		return source.charAt(index);
	}
	
	public final int lookAhead(int offset) {
		return getCharacter(source, pos + offset);
	}
	
	public final int lookAhead() {
		return getCharacter(source, pos);
	}
	
	public final boolean lookaheadIsEOF() {
		return pos >= source.length();
	}
	
	public final String consumeAmount(int length) {
		lastToken = source.substring(pos, pos + length); 
		pos += length;
		return lastToken;
	}
	
	public void resetToPosition(int sourcePosition) {
		pos = sourcePosition;
		lastToken = null;
	}
	
	public final boolean tryConsume(String string) {
		if(source.startsWith(string, pos)) {
			consumeAmount(string.length());
			return true;
		}
		return false;
	}
	
	public final int tryConsume(String... strings) {
		for (int ix = 0; ix < strings.length; ix++) {
			String string = strings[ix];
			if(tryConsume(string)) {
				return ix;
			}
		}
		return -1;
	}
	
	public final void consume(String string) {
		if(tryConsume(string) == false) {
			assertFail(); 
		}
		lastToken = string;
	}
	
	public final String getLastConsumedString() {
		return lastToken;
	}
	
	public String getRestOfInput() {
		return source.substring(pos, source.length());
	}
	
	public String consumeRestOfInput() {
		return consumeAmount(source.length() - pos);
	}
	
	public String consumeUntil(String string) {
		int startPos = pos;
		
		while(true) {
			if(lookaheadIsEOF()) {
				break;
			}
			if(source.startsWith(string, pos)) {
				break;
			}
			pos++;
		}
		lastToken = source.substring(startPos, pos);
		return lastToken;
	}
	
	public int consumeUntilAny(String strings[]) {
		int startPos = pos;
		
		while(true) {
			if(lookaheadIsEOF()) {
				lastToken = source.substring(startPos, pos);
				return EOF;
			}
			for (int i = 0; i < strings.length; i++) {
				String alt = strings[i];
				if(source.startsWith(alt, pos)) {
					lastToken = source.substring(startPos, pos);
					return i;
				}
			}
			pos++;
		}
	}
	
	public SimpleParser seekSpaceChars() {
		lastToken = null;
		while(true) {
			if(lookaheadIsEOF() || !Character.isSpaceChar(source.charAt(pos))) {
				break;
			}
			pos++;
		}
		return this;
	}
	
	public SimpleParser seekWhiteSpace() {
		lastToken = null;
		while(true) {
			if(lookaheadIsEOF() || !Character.isWhitespace(source.charAt(pos))) {
				break;
			}
			pos++;
		}
		return this;
	}
	
	public boolean seekToNewLine() {
		int newPos = findNewLineEnd(source, pos);
		if(newPos == EOF) {
			return false;
		}
		consumeAmount(newPos - pos);
		return true;
	}
	
	public static int findNewLineEnd(String source, int offset) {
		while(true) {
			int ch = getCharacter(source, offset);
			if(ch == EOF) {
				return EOF;
			}
			offset++;
			if(ch == '\r') {
				if(getCharacter(source, offset) == '\n') {
					offset++;
				}
				return offset;
			} else if(ch == '\n') {
				return offset;
			}
		}
	}
	
	public boolean tryConsumeNewlineRule() {
		int ch = lookAhead();
		if(ch == '\r') {
			if(lookAhead(1) == '\n') {
				consumeAmount(2);
				return true;
			}
			consumeAmount(1);
			return true;
		} else if(ch == '\n') {
			consumeAmount(1);
			return true;
		}
		return false;
	}
	
	public static String readNonWhiteSpace(String string, int offset) {
		int startPos = offset;
		while(true) {
			int ch = getCharacter(string, offset);
			if(ch == EOF || Character.isWhitespace(ch)) {
				break;
			}
			offset++;
		}
		return string.substring(startPos, offset);
	}
	
	public static String readAlphaNumericUS(String string, int offset) {
		int startPos = offset;
		while(true) {
			int ch = getCharacter(string, offset);
			if(ch == EOF || !(Character.isLetterOrDigit(ch) || ch == '_')) {
				break;
			}
			offset++;
		}
		return string.substring(startPos, offset);
	}
	
	public static String readInteger(String string, int offset) {
		int startPos = offset;
		while(true) {
			int ch = getCharacter(string, offset);
			if(ch == EOF || !(Character.isDigit(ch))) {
				break;
			}
			offset++;
		}
		return string.substring(startPos, offset);
	}
	
	public String consumeNonWhiteSpace(boolean skipSpaces) {
		if(skipSpaces) {
			seekSpaceChars();
		}
		String str = readNonWhiteSpace(source, pos);
		return consumeAmount(str.length());
	}
	
	public int consumeInteger(boolean skipSpaces) {
		if(skipSpaces) {
			seekSpaceChars();
		}
		String numberStr = readInteger(source, pos);
		consumeAmount(numberStr.length());
		return Integer.decode(numberStr);
	}
	
	public String consumeAlphaNumericUS(boolean skipSpaces) {
		if(skipSpaces) {
			seekSpaceChars();
		}
		String str = readAlphaNumericUS(source, pos);
		consumeAmount(str.length());
		return str;
	}
	
	public boolean tryConsumeKeyword(String string) {
		if(source.startsWith(string, pos)) {
			char charAfterString = source.charAt(pos + string.length());
			if(!Character.isJavaIdentifierPart(charAfterString)) {
				consumeAmount(string.length());
				return true;
			}
		}
		return false;
	}
	
}