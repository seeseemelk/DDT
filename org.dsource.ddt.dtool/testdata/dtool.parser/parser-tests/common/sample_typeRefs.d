Ⓗ▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂▂ 

#@TYPE_REFS《
  ►#?AST_STRUCTURE_EXPECTED!【int●RefPrimitive】●
  ►#?AST_STRUCTURE_EXPECTED!【Foo●RefIdentifier】●
  ►#?AST_STRUCTURE_EXPECTED!【.Foo●RefModuleQualified(?)】●
  ►#?AST_STRUCTURE_EXPECTED!【Bar.foo●RefQualified(RefIdentifier RefIdentifier)】●
  ►#?AST_STRUCTURE_EXPECTED!【Bar.foo.Foobar●RefQualified(RefQualified(RefIdentifier RefIdentifier) RefIdentifier)】●
  
  ►#?AST_STRUCTURE_EXPECTED!【int*●RefTypePointer(RefPrimitive)】●
  ►#?AST_STRUCTURE_EXPECTED!【arrayElem[]●RefTypeDynArray(RefIdentifier)】●
  ►#?AST_STRUCTURE_EXPECTED!【arrayElem[123 ]●RefIndexing(RefIdentifier ExpLiteralInteger)】●
  ►#?AST_STRUCTURE_EXPECTED!【arrayElem[int]●RefIndexing(RefIdentifier RefPrimitive)】●
  ►#?AST_STRUCTURE_EXPECTED!【arrayElem[Boo.foo]●RefIndexing(RefIdentifier RefQualified(RefIdentifier RefIdentifier))】●
  
  ►#?AST_STRUCTURE_EXPECTED!【Bar.foo[.x[12]][Bar.foo*]●
RefIndexing(
 	RefIndexing(RefQualified(RefIdentifier RefIdentifier) RefIndexing(RefModuleQualified(?) ExpLiteralInteger)) 
	RefTypePointer(RefQualified(RefIdentifier RefIdentifier))
)】●
  ►#?AST_STRUCTURE_EXPECTED!【.Bar.foo[]*[123][][1]●
RefIndexing(RefTypeDynArray(RefIndexing(
	RefTypePointer(RefTypeDynArray(RefQualified(RefModuleQualified(?) RefIdentifier)))
	ExpLiteralInteger
))
ExpLiteralInteger)】●

¤》

 	《const●immutable●shared●inout》( )●
 	
 	@(TYPE_REF_SAMPLES)[123/* TODO assign expressions*/]●
 	@(TYPE_REF_SAMPLES)[2..12]●

