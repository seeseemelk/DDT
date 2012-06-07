package dtool.ast.definitions;

import descent.internal.compiler.parser.PROT;
import dtool.ast.ASTNeoNode;
import dtool.ast.IASTNeoVisitor;
import dtool.util.ArrayView;

/**
 * A definition of an interface aggregate. 
 */
public class DefinitionInterface extends DefinitionClass {
	
	public DefinitionInterface(DefUnitDataTuple dudt, PROT prot, ArrayView<ASTNeoNode> members,
			ArrayView<BaseClass> superClasses) {
		super(dudt, prot, members, superClasses);
	}
	
	@Override
	public void accept0(IASTNeoVisitor visitor) {
		boolean children = visitor.visit(this);
		acceptNodeChildren(visitor, children);
		visitor.endVisit(this);
	}
	
	@Override
	public EArcheType getArcheType() {
		return EArcheType.Interface;
	}

}
