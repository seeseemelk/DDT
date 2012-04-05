package dtool.ast.definitions;

import java.util.ArrayList;
import java.util.List;

import melnorme.utilbox.tree.TreeVisitor;
import descent.internal.compiler.parser.PROT;
import dtool.ast.ASTNeoNode;
import dtool.ast.IASTNeoVisitor;
import dtool.refmodel.IScope;

/**
 * A definition of a class aggregate.
 */
public class DefinitionClass extends DefinitionAggregate {

	public final BaseClass[] baseClasses;
	
	public DefinitionClass(DefUnitDataTuple dudt, PROT prot, ASTNeoNode[] members, BaseClass[] baseClasses) {
		super(dudt, prot, members);
		this.baseClasses = baseClasses; parentize(this.baseClasses);
		// TODO: where did template Parameters go
		//if(elem.templateParameters != null)
		//	this.templateParams = ?? TemplateParameter.convertMany(elem.templateParameters);

	}
	
	@Override
	public void accept0(IASTNeoVisitor visitor) {
		boolean children = visitor.visit(this);
		acceptNodeChildren(visitor, children);
		visitor.endVisit(this);
	}
	
	@Override
	public EArcheType getArcheType() {
		return EArcheType.Class;
	}
	
	@Override
	protected void acceptNodeChildren(IASTNeoVisitor visitor, boolean children) {
		if (children) {
			TreeVisitor.acceptChildren(visitor, defname);
			TreeVisitor.acceptChildren(visitor, templateParams);
			TreeVisitor.acceptChildren(visitor, baseClasses);
			TreeVisitor.acceptChildren(visitor, members);
		}
	}
	
	@Override
	public List<IScope> getSuperScopes() {
		if(baseClasses == null || baseClasses.length < 0)
			return null;

		List<IScope> scopes = new ArrayList<IScope>();
		for(BaseClass baseclass: baseClasses) {
			DefUnit defunit = baseclass.type.findTargetDefUnit();
			if(defunit == null)
				continue;
			scopes.add(defunit.getMembersScope());
		}
		return scopes;
		// TODO add Object super scope.
	}
	
}
