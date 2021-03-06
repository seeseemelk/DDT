/*******************************************************************************
 * Copyright (c) 2011 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package dtool.ast.declarations;

import melnorme.lang.tooling.ast.CommonASTNode;
import melnorme.lang.tooling.ast.util.ASTCodePrinter;
import melnorme.lang.tooling.ast.util.NodeList;
import melnorme.lang.tooling.ast.util.NodeVector;
import melnorme.lang.tooling.ast_actual.ASTNode;
import melnorme.lang.tooling.ast_actual.ASTNodeTypes;
import melnorme.lang.tooling.engine.scoping.IScopeElement;
import melnorme.lang.tooling.engine.scoping.ScopeTraverser;
import dtool.ast.definitions.DefinitionAggregate.IAggregateBody;

public class DeclBlock extends NodeList<ASTNode> implements IAggregateBody, IScopeElement {
	
	public DeclBlock(NodeVector<ASTNode> nodes) {
		super(nodes);
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.DECL_BLOCK;
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.appendList("{\n", nodes, "\n", "\n}\n");
	}
	
	@Override
	protected CommonASTNode doCloneTree() {
		return new DeclBlock(clone(nodes));
	}
	
	/* -----------------  ----------------- */
	
	@Override
	public ScopeTraverser getScopeTraverser() {
		return new ScopeTraverser(nodes, true);
	}
	
}