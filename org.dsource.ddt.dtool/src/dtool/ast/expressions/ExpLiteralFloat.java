package dtool.ast.expressions;

import static dtool.util.NewUtils.assertNotNull_;
import dtool.ast.ASTCodePrinter;
import dtool.ast.ASTNodeTypes;
import dtool.ast.IASTVisitor;
import dtool.ast.SourceRange;
import dtool.parser.Token;

public class ExpLiteralFloat extends Expression {
	
	public final Token floatNum;
	
	public ExpLiteralFloat(Token floatNum, SourceRange sourceRange) {
		this.floatNum = assertNotNull_(floatNum);
		initSourceRange(sourceRange);
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.EXP_LITERAL_FLOAT;
	}
	
	@Override
	public void accept0(IASTVisitor visitor) {
		visitor.visit(this);
		visitor.endVisit(this);	 
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.append(floatNum);
	}
	
}