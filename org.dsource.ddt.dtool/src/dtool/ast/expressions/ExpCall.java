package dtool.ast.expressions;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import melnorme.utilbox.tree.TreeVisitor;
import dtool.ast.IASTNeoVisitor;
import dtool.ast.SourceRange;
import dtool.ast.definitions.DefUnit;
import dtool.ast.definitions.DefinitionFunction;
import dtool.refmodel.DefUnitSearch;
import dtool.refmodel.ReferenceResolver;
import dtool.util.ArrayView;

public class ExpCall extends Expression {
	
	public final Expression callee;
	public final ArrayView<Resolvable> args;
	
	public ExpCall(Expression callee, ArrayView<Resolvable> args, SourceRange sourceRange) {
		initSourceRange(sourceRange);
		this.callee = callee; parentize(this.callee);
		this.args = args; parentize(this.args);
	}
	
	@Override
	public void accept0(IASTNeoVisitor visitor) {
		boolean children = visitor.visit(this);
		if (children) {
			TreeVisitor.acceptChildren(visitor, callee);
			TreeVisitor.acceptChildren(visitor, args);
		}
		visitor.endVisit(this);
	}
	
	@Override
	public Collection<DefUnit> findTargetDefUnits(boolean findFirstOnly) {
		DefUnit defUnit = callee.findTargetDefUnit();
		if(defUnit == null)
			return null;		
		if (defUnit instanceof DefinitionFunction) {
			DefinitionFunction defOpCallFunc = (DefinitionFunction) defUnit;
			DefUnit targetDefUnit = defOpCallFunc.rettype.findTargetDefUnit();
			return Collections.singleton(targetDefUnit);
		}
		
		DefUnitSearch search = new DefUnitSearch("opCall", null);
		ReferenceResolver.findDefUnitInScope(defUnit.getMembersScope(), search);
		for (Iterator<DefUnit> iter = search.getMatchDefUnits().iterator(); iter.hasNext();) {
			DefUnit defOpCall = iter.next();
			if (defOpCall instanceof DefinitionFunction) {
				DefinitionFunction defOpCallFunc = (DefinitionFunction) defOpCall;
				DefUnit targetDefUnit = defOpCallFunc.rettype.findTargetDefUnit();
				return Collections.singleton(targetDefUnit);
			}
		}
		return null;
	}
	
}
