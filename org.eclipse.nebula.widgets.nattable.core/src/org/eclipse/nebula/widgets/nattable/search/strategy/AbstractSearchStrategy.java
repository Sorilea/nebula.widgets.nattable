/*******************************************************************************
 * Copyright (c) 2012 Original authors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Original authors and others - initial API and implementation
 ******************************************************************************/
package org.eclipse.nebula.widgets.nattable.search.strategy;

import java.util.Comparator;

import org.eclipse.nebula.widgets.nattable.layer.ILayer;


public abstract class AbstractSearchStrategy implements ISearchStrategy {
	private ILayer contextLayer;
	protected String searchDirection;
	protected boolean caseSensitive;
	protected boolean wrapSearch;
	protected boolean wholeWord;
	protected boolean incremental;
	protected boolean regex;
	protected boolean includeCollapsed;
	protected boolean columnFirst;
	protected Comparator<?> comparator;
	
	public void setContextLayer(ILayer contextLayer) {
		this.contextLayer = contextLayer;
	}
	
	public ILayer getContextLayer() {
		return contextLayer;
	}
	
	public void setSearchDirection(String searchDirection) {
		this.searchDirection = searchDirection;
	}
	
	public String getSearchDirection() {
		return searchDirection;
	}
	
	public void setWrapSearch(boolean wrapSearch) {
		this.wrapSearch = wrapSearch;
	}
	
	public boolean isWrapSearch() {
		return wrapSearch;
	}
	
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}
	
	public boolean isCaseSensitive() {
		return caseSensitive;
	}
	
	public void setWholeWord(boolean wholeWord) {
		this.wholeWord = wholeWord;
	}
	
	public boolean isWholeWord() {
		return wholeWord;
	}
	
	public void setIncremental(boolean incremental) {
		this.incremental = incremental;
	}
	
	public boolean isIncremental() {
		return incremental;
	}
	
	public void setRegex(boolean regex) {
		this.regex = regex;
	}
	
	public boolean isRegex() {
		return regex;
	}
	
	public void setIncludeCollapsed(boolean includeCollapsed) {
		this.includeCollapsed = includeCollapsed;
	}
	
	public boolean isIncludeCollapsed() {
		return includeCollapsed;
	}
	
	public void setColumnFirst(boolean columnFirst) {
		this.columnFirst = columnFirst;
	}
	
	public boolean isColumnFirst() {
		return columnFirst;
	}
	
	public Comparator<?> getComparator() {
		return comparator;
	}
	
	public void setComparator(Comparator<?> comparator) {
		this.comparator = comparator;
	}
}
