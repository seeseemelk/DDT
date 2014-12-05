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
package dtool.engine;

import java.util.concurrent.ExecutionException;

import dtool.engine.util.CachingRegistry;


public abstract class AbstractCachingManager<KEY, VALUE> {
	
	public AbstractCachingManager() {
	}
	
	protected final CachingRegistry<KEY, VALUE> infos = new CachingRegistry<KEY, VALUE>() {
		
		@Override
		protected VALUE createEntry(KEY key) {
			return doCreateEntry(key);
		}
	};
	
	protected abstract VALUE doCreateEntry(KEY key);
	
	protected VALUE getEntry(KEY key) {
		return infos.getEntry(key);
	}
	
	public boolean checkIsEntryStale(KEY bundleKey) {
		VALUE entry = infos.getEntryOrNull(bundleKey);
		return entry == null ? true : doCheckIsEntryStale(entry);
	}
	
	/** Lock for reading/modifying the whole registry. */
	protected final Object entriesLock = new Object();
	
	public abstract boolean doCheckIsEntryStale(VALUE entry);
	
	public VALUE getUpdatedEntry(KEY key) throws ExecutionException {
		VALUE entry = getEntry(key);
		if(doCheckIsEntryStale(entry)) {
			return updateEntry(key);
		}
		return entry;
	}
	
	/** Lock for performing the computation of update operations. */
	protected final Object updateOperationLock = new Object();
	
	protected VALUE updateEntry(KEY key) throws ExecutionException {
		synchronized(updateOperationLock) {
			VALUE staleInfo = getEntry(key);
			// Recheck stale status after acquiring lock, it might have been updated in the meanwhile.
			// Otherwise unnecessary update operations might occur if two threads tried to update at the same time.
			if(doCheckIsEntryStale(staleInfo) == false)
				return staleInfo; // No longer stale.
			
			doUpdateEntry(key, staleInfo);
			return staleInfo;
		}
	}
	
	protected abstract void doUpdateEntry(KEY key, VALUE staleInfo) throws ExecutionException;
	
}