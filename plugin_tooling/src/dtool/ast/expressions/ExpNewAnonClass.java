/*******************************************************************************
 * Copyright (c) 2012, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package dtool.ast.expressions;

import melnorme.lang.tooling.ast.IASTVisitor;
import melnorme.lang.tooling.ast.util.ASTCodePrinter;
import melnorme.lang.tooling.ast_actual.ASTNodeTypes;
import melnorme.utilbox.collections.ArrayView;
import dtool.ast.declarations.DeclBlock;
import dtool.ast.references.Reference;

public class ExpNewAnonClass extends Expression {
	
	public final ArrayView<Expression> allocArgs;
	public final ArrayView<Expression> args;
	public final ArrayView<Reference> baseClasses;
	public final DeclBlock declBody;
	
	public ExpNewAnonClass(ArrayView<Expression> allocArgs, ArrayView<Expression> args,
			ArrayView<Reference> baseClasses, DeclBlock declBody) {
		this.allocArgs = parentize(allocArgs);
		this.args = parentize(args);
		this.baseClasses = parentize(baseClasses);
		this.declBody = parentize(declBody);
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.EXP_NEW_ANON_CLASS;
	}
	
	@Override
	public void visitChildren(IASTVisitor visitor) {
		acceptVisitor(visitor, allocArgs);
		acceptVisitor(visitor, args);
		acceptVisitor(visitor, baseClasses);
		acceptVisitor(visitor, declBody);
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.append("new ");
		cp.appendList("(", allocArgs, ",", ")");
		cp.append("class");
		cp.appendList("(", args, ",", ")");
		cp.append(" ");
		cp.appendList(baseClasses, ",");
		cp.append(declBody);
	}
	
}