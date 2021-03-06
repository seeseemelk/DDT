/*******************************************************************************
 * Copyright (c) 2015 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package dtool.parser.structure;

import static melnorme.lang.tooling.EAttributeFlag.ABSTRACT;
import static melnorme.lang.tooling.EAttributeFlag.ALIASED;
import static melnorme.lang.tooling.EAttributeFlag.IMMUTABLE;
import static melnorme.lang.tooling.EAttributeFlag.STATIC;
import static melnorme.lang.tooling.structure.StructureElementKind.ALIAS;
import static melnorme.lang.tooling.structure.StructureElementKind.CLASS;
import static melnorme.lang.tooling.structure.StructureElementKind.CONSTRUCTOR;
import static melnorme.lang.tooling.structure.StructureElementKind.ENUM_TYPE;
import static melnorme.lang.tooling.structure.StructureElementKind.FUNCTION;
import static melnorme.lang.tooling.structure.StructureElementKind.STRUCT;
import static melnorme.lang.tooling.structure.StructureElementKind.VARIABLE;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import org.junit.Test;

import dtool.parser.DeeParser;
import dtool.parser.DeeParserResult.ParsedModule;
import dtool.tests.DToolTestResources;
import melnorme.lang.tooling.EAttributeFlag;
import melnorme.lang.tooling.EProtection;
import melnorme.lang.tooling.ElementAttributes;
import melnorme.lang.tooling.ast.SourceRange;
import melnorme.lang.tooling.common.ParserError;
import melnorme.lang.tooling.structure.AbstractStructureParser;
import melnorme.lang.tooling.structure.AbstractStructureParser_Test;
import melnorme.lang.tooling.structure.SourceFileStructure;
import melnorme.lang.tooling.structure.StructureElement;
import melnorme.utilbox.collections.ArrayList2;
import melnorme.utilbox.collections.Indexable;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.misc.ArrayUtil;
import melnorme.utilbox.misc.Location;

public class DeeStructureCreator_Test extends AbstractStructureParser_Test {
	
	public static ElementAttributes attrib(EProtection protection, EAttributeFlag... flags) {
		return new ElementAttributes(protection, flags);
	}
	
	public static ElementAttributes att(EAttributeFlag... flags) {
		return new ElementAttributes(EProtection.PUBLIC, flags);
	}
	
	protected static ElementAttributes eat(EAttributeFlag... flags) {
		return att(ArrayUtil.concat(flags, EAttributeFlag.TEMPLATED));
	}
	
	protected ArrayList2<StructureElement> elems(StructureElement... expectedElements) {
		return ArrayList2.create(expectedElements);
	}
	
	
	protected int indexOf(String str) {
		int indexOf = source.indexOf(str);
		assertTrue(indexOf != -1);
		return indexOf;
	}
	
	protected int startOf(String str) {
		return indexOf(str);
	}
	
	protected int endOf(String str) {
		return indexOf(str) + str.length();
	}
	
	protected SourceRange sr(String start, String end) {
		return SourceRange.srStartToEnd(startOf(start), endOf(end));
	}
	
	protected SourceRange sr(String start, int length) {
		return new SourceRange(startOf(start), length);
	}
	
	protected void testParseStructure(String source, StructureElement... expectedElements) throws CommonException {
		this.source = source;
		SourceFileStructure structure = createStructureParser().parse("XXXXX NOT_APPLICABLE");
		
		ArrayList2<StructureElement> expectedStructure = ArrayList2.create(expectedElements);
		SourceFileStructure expected = new SourceFileStructure(structure.getLocation(), expectedStructure, 
			(Indexable<ParserError>) null);
		
		assertAreEqual(expected.getChildren(), structure.getChildren());
		assertEquals(structure, expected);
	}
	
	@Override
	protected AbstractStructureParser createStructureParser() {
		// dummy loc
		Location loc = DToolTestResources.getTestResourceLoc().resolve_fromValid("parser-structure/foo.d");
		return new AbstractStructureParser(loc, source) {
			String source = DeeStructureCreator_Test.this.source;
			
			@Override
			public SourceFileStructure parse(String outputParseSource) throws CommonException {
				ParsedModule parsedModule = DeeParser.parseSourceModule(source, loc.toPath());
				return new DeeStructureCreator().createStructure(parsedModule, loc);
			}
		};
	}
	
	@Test
	public void testname() throws Exception { testname$(); }
	public void testname$() throws Exception {
		source = "int foo; string[] func() {/*2*/} this() {/*3*/}";
		testParseStructure(
			source, 
			new StructureElement("foo", sr(4,3), sr(0,8), VARIABLE, att(), "int", null),
			new StructureElement("func()", sr("func()", 4), sr("string[] f","2*/}"), FUNCTION, att(), "string[]", null),
			new StructureElement("this()", sr("this()", 4), sr("this()","3*/}"), CONSTRUCTOR, att(), null, null)
		);
		
		
		// Test attributes
		source = "public: static const(int) foo; private abstract immutable int bar; ";
		ElementAttributes fooAttribs = attrib(EProtection.PUBLIC, STATIC);
		ElementAttributes barAttribs = attrib(EProtection.PRIVATE, ABSTRACT, IMMUTABLE);
		
		testParseStructure(
			source, 
			new StructureElement("foo", sr("foo", 3), sr("const(","foo;"), VARIABLE, fooAttribs, "const(int)", null),
			new StructureElement("bar", sr("bar", 3), sr("int bar","bar;"), VARIABLE, barAttribs, "int", null)
		);
		
		
		source = "string[] func(int a, char) {/*2*/} this(int[] a, char b...) {/*3*/}";
		testParseStructure(
			source, 
			elem("func(int, char)", sr("string[] f","2*/}"), sr("func(", 4), FUNCTION, att(), "string[]", null),
			elem("this(int[], char...)", sr("this(","3*/}"), sr("this(", 4), CONSTRUCTOR, att(), null, null)
		);
		
		source = 
			"struct Xpto { int foo2; void func(int a){}  /*Xpto*/} \n " +
			"class Foo { int fox;  class Inner(T) { int xxx; }  /*Foo*/} \n ";
		testParseStructure(
			source, 
			new StructureElement("Xpto", sr("Xpto", 4), sr("struct Xpto","/*Xpto*/}"), STRUCT, att(), null, elems(
				new StructureElement("foo2", sr("foo2;", 4), sr("int foo2;","foo2;"), VARIABLE, att(), "int", null),
				new StructureElement("func(int)", sr("func", 4), sr("void func","a){}"), FUNCTION, att(), "void", null)
			)),
			new StructureElement("Foo", sr("Foo", 3), sr("class F","/*Foo*/}"), CLASS, att(), null, elems(
				new StructureElement("fox", sr("fox;", 3), sr("int fox;","fox;"), VARIABLE, att(), "int", null),
				new StructureElement("Inner", sr("Inner", 5), sr("class In","xxx; }"), CLASS, eat(), null, elems(
					new StructureElement("xxx", sr("xxx;", 3), sr("int xxx","xxx;"), VARIABLE, att(), "int", null)
				))
			))
		);
		
		// Test multiple var decl.
		source = "static int foo, bar; ";
		testParseStructure(
			source, 
			new StructureElement("foo", sr("foo", 3), sr("int foo","bar;"), VARIABLE, att(STATIC), "int", elems(
				new StructureElement("bar", sr("bar", 3), sr("bar;","bar"), VARIABLE, attrib(null), "int", null)
			)
		));
		/* TODO: DTool: attribute of fragmentDefUnits*/
		
		// Test enum.
		source = "enum Foo : int { ONE, TWO }";
		testParseStructure(
			source, 
			new StructureElement("Foo", sr("Foo", 3), sr("enum","}"), ENUM_TYPE, att(), "int", elems(
				new StructureElement("ONE", sr("ONE", 3), sr("ONE","ONE"), VARIABLE, attrib(null), null, null),
				new StructureElement("TWO", sr("TWO", 3), sr("TWO","TWO"), VARIABLE, attrib(null), null, null)
			))
		);
		source = "enum Foo = 123, Bar = 'abc'";
		testParseStructure(
			source, 
			new StructureElement("Foo", sr("Foo", 3), sr("Foo","123"), VARIABLE, attrib(null), null, null),
			new StructureElement("Bar", sr("Bar", 3), sr("Bar","'abc'"), VARIABLE, attrib(null), null, null)
		);
		
		
		// Test aliases:
		source = "alias Foo = Bar;";
		testParseStructure(
			source, 
			new StructureElement("Foo", sr("Foo", 3), sr("Foo","Bar"), ALIAS, attrib(null, ALIASED), "Bar", null)
		);
		
		source = "alias Foo = Bar, XXX = XPTO;";
		testParseStructure(
			source, 
			new StructureElement("Foo", sr("Foo", 3), sr("Foo","Bar"), ALIAS, attrib(null, ALIASED), "Bar", null),
			new StructureElement("XXX", sr("XXX", 3), sr("XXX","XPTO"), ALIAS, attrib(null, ALIASED), "XPTO", null)
		);

	}
	
}