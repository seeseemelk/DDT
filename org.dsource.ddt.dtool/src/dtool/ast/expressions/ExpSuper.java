package dtool.ast.expressions;

import dtool.ast.ASTCodePrinter;
import dtool.ast.ASTNodeTypes;
import dtool.ast.IASTVisitor;
import dtool.ast.SourceRange;

public class ExpSuper extends Expression {
	
	public ExpSuper(SourceRange sourceRange) {
		initSourceRange(sourceRange);
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.EXP_SUPER;
	}
	
	@Override
	public void accept0(IASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);	
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.append("super");
	}
	
}