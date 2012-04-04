package dtool.ast.statements;

import melnorme.utilbox.tree.TreeVisitor;
import dtool.ast.IASTNeoVisitor;
import dtool.ast.SourceRange;
import dtool.ast.expressions.Resolvable;

public class StatementFor extends Statement {

	public final IStatement init;
	public final Resolvable cond;
	public final Resolvable inc;
	public final IStatement body;
	
	public StatementFor(IStatement init, Resolvable cond, Resolvable inc, IStatement body, SourceRange sourceRange) {
		initSourceRange(sourceRange);
		this.init = init; parentize(this.init);
		this.cond = cond; parentize(this.cond);
		this.inc = inc; parentize(this.inc);
		this.body = body; parentize(this.body);
	}

	@Override
	public void accept0(IASTNeoVisitor visitor) {
		boolean children = visitor.visit(this);
		if (children) {
			TreeVisitor.acceptChildren(visitor, init);
			TreeVisitor.acceptChildren(visitor, cond);
			TreeVisitor.acceptChildren(visitor, inc);
			TreeVisitor.acceptChildren(visitor, body);
		}
		visitor.endVisit(this);
	}

}
