▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂
foo.foo dummy;
FooBar.Foo.FooBar dummy;

#AST_STRUCTURE_EXPECTED:
DefVariable(RefQualified(RefIdentifier RefIdentifier) DefSymbol)
DefVariable(RefQualified(RefQualified(RefIdentifier RefIdentifier) RefIdentifier) DefSymbol)

▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂
Foo. #error:EXP_ID int dummy;

FooBar.Bar. #error:EXP_ID int dummy; // Interaction with primitive reference

#AST_STRUCTURE_EXPECTED:
InvalidDeclaration( RefQualified(RefIdentifier RefIdentifier) )
DefVariable(RefPrimitive DefSymbol)
InvalidDeclaration( RefQualified(RefQualified(RefIdentifier RefIdentifier) RefIdentifier) )
DefVariable(RefPrimitive DefSymbol)

#AST_SOURCE_EXPECTED:
Foo.  int dummy;
FooBar.Bar.   int dummy;

▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂

#@TYPE_REFS•.foo dummy;

#AST_STRUCTURE_EXPECTED:
DefVariable(RefQualified(#@TYPE_REFS RefIdentifier) DefSymbol)

