package dtool.ast.definitions;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.util.Iterator;
import java.util.List;

import melnorme.utilbox.misc.ChainedIterator;
import melnorme.utilbox.tree.TreeVisitor;
import descent.internal.compiler.parser.PROT;
import descent.internal.compiler.parser.ast.IASTNode;
import dtool.ast.ASTNeoNode;
import dtool.ast.IASTNeoVisitor;
import dtool.ast.statements.IStatement;
import dtool.refmodel.IScope;
import dtool.refmodel.IScopeNode;

/**
 * Note, ATM only valid as a statement in the shorthand syntax for an eponymous template, like class(T) { ...
 */
public class DefinitionTemplate extends Definition implements IScopeNode, IStatement {
	
	public final ArrayView<TemplateParameter> templateParams; 
	public final ArrayView<ASTNeoNode> decls;
	public final boolean wrapper;
	
	public DefinitionTemplate(DefUnitDataTuple dudt, PROT prot, ASTNeoNode[] decls, TemplateParameter[] params, boolean wrapper) {
		super(dudt, prot);
		this.templateParams = new ArrayView<TemplateParameter>(params); parentize(this.templateParams);
		this.decls = new ArrayView<ASTNeoNode>(decls); parentize(this.decls);
		// Must define what it does!
		// this.wrapper = this.templateParams.size() != 1;
		this.wrapper = wrapper;
		if(wrapper)
			assertTrue(this.decls.size() == 1);
	}
	
	@Override
	public void accept0(IASTNeoVisitor visitor) {
		boolean children = visitor.visit(this);
		if (children) {
			TreeVisitor.acceptChildren(visitor, defname);
			TreeVisitor.acceptChildren(visitor, templateParams);
			TreeVisitor.acceptChildren(visitor, decls);
		}
		visitor.endVisit(this);
	}

	@Override
	public EArcheType getArcheType() {
		return EArcheType.Template;
	}

	@Override
	public IScopeNode getMembersScope() {
		return this;
	}
	
	@Override
	public List<IScope> getSuperScopes() {
		// TODO: template super scope
		return null;
	}
	
	@Override
	public boolean hasSequentialLookup() {
		return false;
	}

	@Override
	public Iterator<? extends IASTNode> getMembersIterator() {
		// TODO: check if in a template invocation
		if(wrapper) {
			// Go straight to decls member's members
			Iterator<? extends IASTNode> tplIter = templateParams.iterator();
			return ChainedIterator.create(tplIter, decls.iterator());
		}
		return new ChainedIterator<ASTNeoNode>(templateParams.iterator(), decls.iterator());
	}

}
