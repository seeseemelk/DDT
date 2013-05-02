package dtool.refmodel;

import dtool.ast.DeclList;
import dtool.ast.IASTNeoNode;
import dtool.ast.definitions.DefinitionClass;

// TODO: rewrite this
public class ScopeUtil {

	/** Finds the first outer scope of the given element 
	 * (navigating through the element's parents). */
	public static IScopeNode getOuterScope(IASTNeoNode elem) {
		return getScopeNode(getNextParent(elem));
	}
	
	/** Finds the first IScopeNode in the given elem chain of parents, 
	 * including elem itself. This corresponds to the innermost lexical
	 * scope available from elem. */
	public static IScopeNode getScopeNode(IASTNeoNode elem) {
	
		while(elem != null) {
			if (elem instanceof IScopeNode)
				return (IScopeNode) elem;
			
			elem = getNextParent(elem);
		}
		return null;
	}

	public static IASTNeoNode getNextParent(IASTNeoNode elem) {
		/*BUG here should be:*/
		//if (elem.getParent() instanceof DefinitionAggregate) {
		if (elem.getParent() instanceof DefinitionClass && !(elem instanceof DeclList)) {
			// Need to skip aggregate defunit scope 
			elem = elem.getParent().getParent();
			return elem;
		}
		
		elem = elem.getParent();
		return elem;
	}
	
}
