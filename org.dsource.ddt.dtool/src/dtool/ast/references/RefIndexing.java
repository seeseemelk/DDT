package dtool.ast.references;

import java.util.Collection;

import dtool.ast.ASTCodePrinter;
import dtool.ast.ASTNodeTypes;
import dtool.ast.IASTVisitor;
import dtool.ast.definitions.DefUnit;
import dtool.ast.expressions.Resolvable;
import dtool.resolver.api.DefUnitDescriptor;
import dtool.resolver.api.IModuleResolver;

/**
 * An reference consisting of an element reference and an indexing paramater .
 * Can represent a static array, associative array (aka map), or tuple indexing. 
 * It can be possible to determine which one it represents by syntax analysis only (example: foo[int] or foo[4]), 
 * but sometimes semantic analysis is necessary 
 * (example foo[bar] - is bar a number or a type? is foo a type or a tuple?)
 */
public class RefIndexing extends Reference {
	
	public final Reference elemType;
	public final Resolvable indexArg;
	
	public RefIndexing(Reference keyType, Resolvable indexArg) {
		this.elemType = parentize(keyType);
		this.indexArg = parentize(indexArg);
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.REF_INDEXING;
	}
	
	@Override
	public void visitChildren(IASTVisitor visitor) {
		acceptVisitor(visitor, elemType);
		acceptVisitor(visitor, indexArg);
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.append(elemType);
		cp.append("[", indexArg, "]");
	}
	
	@Override
	public Collection<DefUnit> findTargetDefUnits(IModuleResolver moduleResolver, boolean findFirstOnly) {
		return null; // TODO:
	}
	
	@Override
	public boolean canMatch(DefUnitDescriptor defunit) {
		return false; // TODO:
	}
	
}