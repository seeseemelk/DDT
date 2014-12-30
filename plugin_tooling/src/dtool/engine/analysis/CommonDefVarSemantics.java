/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package dtool.engine.analysis;

import static melnorme.utilbox.misc.CollectionUtil.getFirstElementOrNull;
import melnorme.lang.tooling.engine.ErrorElement;
import melnorme.lang.tooling.engine.PickedElement;
import melnorme.lang.tooling.engine.resolver.IResolvable;
import melnorme.lang.tooling.engine.resolver.VarSemantics;
import melnorme.lang.tooling.symbols.INamedElement;
import dtool.ast.expressions.IInitializer;
import dtool.ast.expressions.Resolvable;

public class CommonDefVarSemantics extends VarSemantics {
	
	protected final IVarDefinitionLike varDef;
	
	public CommonDefVarSemantics(IVarDefinitionLike varDef, PickedElement<?> pickedElement) {
		super(varDef, pickedElement);
		this.varDef = varDef;
	}
	
	@Override
	public INamedElement resolveTypeForValueContext() {
		if(getTypeReference() != null) {
			return super.resolveTypeForValueContext();
		}
		
		IInitializer initializer = varDef.getDeclaredInitializer();
		if(initializer instanceof IResolvable) {
			IResolvable initializerR = (IResolvable) initializer;
			return getFirstElementOrNull(initializerR.getSemantics(context).resolveTypeOfUnderlyingValue());
		}
		
		return ErrorElement.newNotFoundError(varDef, null);
	}
	
	@Override
	protected Resolvable getTypeReference() {
		return varDef.getDeclaredType();
	}
	
}