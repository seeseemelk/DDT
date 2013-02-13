package dtool.ast;

import dtool.ast.declarations.DeclarationAlign;
import dtool.ast.declarations.DeclarationBasicAttrib;
import dtool.ast.declarations.DeclarationConditional;
import dtool.ast.declarations.DeclarationEmpty;
import dtool.ast.declarations.DeclarationImport;
import dtool.ast.declarations.DeclarationInvariant;
import dtool.ast.declarations.DeclarationLinkage;
import dtool.ast.declarations.DeclarationMixinString;
import dtool.ast.declarations.DeclarationPragma;
import dtool.ast.declarations.DeclarationProtection;
import dtool.ast.declarations.DeclarationUnitTest;
import dtool.ast.declarations.ImportAlias;
import dtool.ast.declarations.ImportContent;
import dtool.ast.declarations.ImportSelective;
import dtool.ast.declarations.ImportSelectiveAlias;
import dtool.ast.definitions.DefUnit;
import dtool.ast.definitions.DefinitionAlias;
import dtool.ast.definitions.DefinitionClass;
import dtool.ast.definitions.DefinitionCtor;
import dtool.ast.definitions.DefinitionEnum;
import dtool.ast.definitions.DefinitionFunction;
import dtool.ast.definitions.DefinitionInterface;
import dtool.ast.definitions.DefinitionStruct;
import dtool.ast.definitions.DefinitionTemplate;
import dtool.ast.definitions.DefinitionTypedef;
import dtool.ast.definitions.DefinitionUnion;
import dtool.ast.definitions.DefinitionVarFragment;
import dtool.ast.definitions.DefinitionVariable;
import dtool.ast.definitions.Module;
import dtool.ast.definitions.Module.DeclarationModule;
import dtool.ast.definitions.Symbol;
import dtool.ast.expressions.ExpArrayLength;
import dtool.ast.expressions.ExpConditional;
import dtool.ast.expressions.ExpLiteralBool;
import dtool.ast.expressions.ExpLiteralChar;
import dtool.ast.expressions.ExpLiteralFloat;
import dtool.ast.expressions.ExpLiteralFunc;
import dtool.ast.expressions.ExpLiteralInteger;
import dtool.ast.expressions.ExpLiteralNewAnonClass;
import dtool.ast.expressions.ExpLiteralString;
import dtool.ast.expressions.ExpNull;
import dtool.ast.expressions.ExpReference;
import dtool.ast.expressions.ExpSuper;
import dtool.ast.expressions.ExpThis;
import dtool.ast.expressions.InfixExpression;
import dtool.ast.expressions.InitializerArray;
import dtool.ast.expressions.InitializerExp;
import dtool.ast.expressions.InitializerStruct;
import dtool.ast.expressions.InitializerVoid;
import dtool.ast.expressions.PostfixExpression;
import dtool.ast.expressions.PrefixExpression;
import dtool.ast.expressions.Resolvable;
import dtool.ast.references.RefIdentifier;
import dtool.ast.references.RefImportSelection;
import dtool.ast.references.RefIndexing;
import dtool.ast.references.RefModule;
import dtool.ast.references.RefModuleQualified;
import dtool.ast.references.RefPrimitive;
import dtool.ast.references.RefQualified;
import dtool.ast.references.RefTemplateInstance;
import dtool.ast.references.RefTypeDynArray;
import dtool.ast.references.RefTypePointer;
import dtool.ast.references.Reference;
import dtool.ast.references.TypeDelegate;
import dtool.ast.references.TypeFunction;
import dtool.ast.references.TypeTypeof;

/**
 * The classes that this visitor dispatches to are mostly abstract classes, not concrete ones: 
 * it doesn't dispatch only to the leaves of the AST hierarchy. This is because it is not necessary to do so, 
 * at the moment.
 */
public interface IASTVisitor {
	
	/** Returns whether to proceed with type-specific dispatch visit. 
	 * Note: if false, it implies children will not be visited. */
	public boolean preVisit(ASTNeoNode node);
	public void postVisit(ASTNeoNode node);
	
	public boolean visit(ASTNeoNode node);
	public void endVisit(ASTNeoNode node);
	
	/* ---------------------------------- */
	public boolean visit(Symbol node);
	
	public boolean visit(DefUnit node);
	public void endVisit(DefUnit node);
	
	public boolean visit(Module node);
	public void endVisit(Module node);
	
	public boolean visit(DeclarationModule node);
	public void endVisit(DeclarationModule node);
	
	public boolean visit(DeclarationImport node);
	public void endVisit(DeclarationImport node);
	
	public boolean visit(ImportContent node);
	public boolean visit(ImportAlias node);
	public boolean visit(ImportSelective node);
	public boolean visit(ImportSelectiveAlias node);
	
	public boolean visit(DeclarationEmpty node);
	
	//-- various Declarations
	public boolean visit(DeclarationLinkage node);
	public boolean visit(DeclarationAlign node);
	public boolean visit(DeclarationPragma node);
	public boolean visit(DeclarationProtection node);
	public boolean visit(DeclarationBasicAttrib node);
	
	//-- Aggregates
	public boolean visit(DefinitionStruct node);
	public void endVisit(DefinitionStruct node);
	
	public boolean visit(DefinitionUnion node);
	public void endVisit(DefinitionUnion node);
	
	public boolean visit(DefinitionClass node);
	public void endVisit(DefinitionClass node);
	
	public boolean visit(DefinitionInterface node);
	public void endVisit(DefinitionInterface node);
	//--
	
	
	public boolean visit(DefinitionTemplate node);
	public void endVisit(DefinitionTemplate node);
	
	
	public boolean visit(DefinitionVariable node);
	public void endVisit(DefinitionVariable node);
	
	public boolean visit(DefinitionVarFragment node);
	public boolean visit(InitializerExp node);
	public boolean visit(InitializerArray node);
	public boolean visit(InitializerStruct node);
	public boolean visit(InitializerVoid node);
	
	public boolean visit(DefinitionEnum node);
	public void endVisit(DefinitionEnum node);
	
	public boolean visit(DefinitionTypedef node);
	public void endVisit(DefinitionTypedef node);
	
	public boolean visit(DefinitionAlias node);
	public void endVisit(DefinitionAlias node);
	
	public boolean visit(DefinitionFunction node);
	public void endVisit(DefinitionFunction node);
	
	//--
	public boolean visit(DefinitionCtor node);
	public void endVisit(DefinitionCtor node);
	
	
	/* ---------------------------------- */
	
	public boolean visit(Resolvable node);
	public void endVisit(Resolvable node);
	
	public boolean visit(Reference node);
	public void endVisit(Reference node);
	
	public boolean visit(RefIdentifier node);
	public boolean visit(RefImportSelection node);
	public boolean visit(RefQualified node);
	public boolean visit(RefModuleQualified node);
	public boolean visit(RefPrimitive node);
	public boolean visit(RefModule node);
	
	public boolean visit(RefTypeDynArray node);
	public boolean visit(RefTypePointer node);
	public boolean visit(TypeDelegate node);
	public boolean visit(TypeFunction node);
	public boolean visit(RefIndexing node);
	
	public boolean visit(TypeTypeof node);
	public boolean visit(RefTemplateInstance node);
	
	/* ---------------------------------- */
	
	public boolean visit(ExpThis node);
	public boolean visit(ExpSuper node);
	public boolean visit(ExpNull node);
	public boolean visit(ExpArrayLength node);
	public boolean visit(ExpLiteralBool node);
	public boolean visit(ExpLiteralInteger node);
	public boolean visit(ExpLiteralString node);
	public boolean visit(ExpLiteralChar node);
	public boolean visit(ExpLiteralFloat node);
	
	public boolean visit(ExpReference node);
	
	public boolean visit(PrefixExpression node);
	public boolean visit(PostfixExpression node);
	public boolean visit(InfixExpression node);
	public boolean visit(ExpConditional node);
	
	public boolean visit(ExpLiteralFunc node);
	public boolean visit(ExpLiteralNewAnonClass node);
	
	/* ---------------------------------- */
	
	public boolean visit(DeclarationMixinString node);
	
	public boolean visit(DeclarationInvariant node);
	public void endVisit(DeclarationInvariant node);
	
	
	public boolean visit(DeclarationUnitTest node);
	public void endVisit(DeclarationUnitTest node);
	
	public boolean visit(DeclarationConditional node);
	public void endVisit(DeclarationConditional node);
	
}