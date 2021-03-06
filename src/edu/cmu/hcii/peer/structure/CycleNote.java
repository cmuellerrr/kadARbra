/**
 * 
 */
package edu.cmu.hcii.peer.structure;

import java.io.Serializable;

/**
 * An object representing a note specific to a cycle
 * or steps.  Has general text notes and an optional
 * reference.
 * 
 * @author Chris
 *
 */
public class CycleNote implements Serializable {
	
	/**
	 * Auto-Generated serial id
	 */
	private static final long serialVersionUID = -4359589324513105977L;
	private String text;
	private Reference reference;
	
	
	
	/**
	 * Create a new cycle note with the given text and reference.
	 */
	public CycleNote(String text, Reference reference) {
		this.text = text;
		this.reference = reference;
	}

	
	
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	
	

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	
	
	/**
	 * @return the reference
	 */
	public Reference getReference() {
		return reference;
	}
	
	

	/**
	 * @param reference the reference to set
	 */
	public void setReference(Reference reference) {
		this.reference = reference;
	}
}
