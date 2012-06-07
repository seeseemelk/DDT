package dtool.ast.definitions;

import java.util.Iterator;

import melnorme.utilbox.misc.IteratorUtil;
import melnorme.utilbox.tree.TreeVisitor;
import descent.internal.compiler.parser.PROT;
import dtool.ast.ASTNeoNode;
import dtool.ast.ASTPrinter;
import dtool.ast.IASTNeoVisitor;
import dtool.ast.statements.IStatement;
import dtool.refmodel.IScopeNode;
import dtool.util.ArrayView;

/**
 * A definition of a aggregate. 
 */
public abstract class DefinitionAggregate extends Definition implements IScopeNode, IStatement {
	
	public final ArrayView<TemplateParameter> templateParams; 
	public final ArrayView<ASTNeoNode> members; // can be null. (bodyless aggregates)
	
	public DefinitionAggregate(DefUnitDataTuple defunit, PROT prot, ArrayView<TemplateParameter> templateParams,
			ArrayView<ASTNeoNode> members) {
		super(defunit, prot);
		this.templateParams = templateParams; parentize(this.templateParams);
		this.members = members; parentize(this.members);
	}
	
	@Deprecated
	public DefinitionAggregate(DefUnitDataTuple defunit, PROT prot, ArrayView<ASTNeoNode> members) {
		this(defunit, prot, null, members);
	}
	
	protected void acceptNodeChildren(IASTNeoVisitor visitor, boolean children) {
		if (children) {
			TreeVisitor.acceptChildren(visitor, defname);
			TreeVisitor.acceptChildren(visitor, templateParams);
			TreeVisitor.acceptChildren(visitor, members);
		}
	}
	
	@Override
	public IScopeNode getMembersScope() {
		return this;
	}
	
	@Override
	public Iterator<ASTNeoNode> getMembersIterator() {
		if(members == null)
			return IteratorUtil.getEMPTY_ITERATOR();
		return members.iterator();
	}
	
	@Override
	public boolean hasSequentialLookup() {
		return false;
	}
	
	@Override
	public String toStringForHoverSignature() {
		String str = getModuleScope().toStringAsElement() +"."+ getName()
				+ ASTPrinter.toStringParamListAsElements(templateParams);
		return str;
	}
	
	@Override
	public String toStringForCodeCompletion() {
		return getName() + " - " + getModuleScope().toStringAsElement();
	}
	
}
