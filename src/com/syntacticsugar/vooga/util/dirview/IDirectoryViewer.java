// This entire file is part of my masterpiece.
// Brody Kellish

package com.syntacticsugar.vooga.util.dirview;

import java.io.File;

/**
 * Interface used to show the contents of a File directory. Requires an IConverter
 * to define how to build visual representations of the Files in the directory.
 * 
 * @author Brody Kellish
 *
 * @param <T> : The type of visual data to generate from Files in the directory.
 */
public interface IDirectoryViewer<T> {

	/**
	 * Builds a viewable structure of a File directory using the simplified
	 * data representations generated by the IConverter interface. Usually
	 * implemented alongside UI elements (JavaFX Nodes).
	 * 
	 * @param directory : The directory containing the Files to display
	 * @param fileConverter : The method that defines how to convert Files to some
	 * target data type
	 */
	public void showDirectoryContents(File directory, IConverter<T> fileConverter);
	
}
