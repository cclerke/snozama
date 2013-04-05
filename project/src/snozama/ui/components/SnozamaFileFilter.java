package snozama.ui.components;

import java.io.File;
import java.util.Arrays;

import javax.swing.filechooser.FileFilter;

/**
 * File filter implementation for file saving.
 * 
 * @author Alex Yakovlev.
 * 
 */
public class SnozamaFileFilter extends FileFilter
{
	private static final String[] acceptedExtensions = { ".snozama" };
	
	@Override
	public boolean accept(File file)
	{

		if (file.getName().lastIndexOf(".") < 0)
			return false;

		String extension = file.getName().substring(
				file.getName().lastIndexOf("."));
		return Arrays.asList(acceptedExtensions).contains(extension);
	}

	@Override
	public String getDescription()
	{
		return "Snozama Game Files";
	}

}
