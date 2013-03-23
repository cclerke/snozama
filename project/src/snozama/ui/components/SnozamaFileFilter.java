package snozama.ui.components;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.management.ImmutableDescriptor;
import javax.swing.filechooser.FileFilter;

public class SnozamaFileFilter extends FileFilter
{
	
	private static final String[] acceptedExtensions = { ".snozama" };

	@Override
	public boolean accept(File file) {
		
		if( file.getName().lastIndexOf(".") < 0 ) return false;
		
		String extension = file.getName().substring(file.getName().lastIndexOf("."));
		return Arrays.asList( acceptedExtensions ).contains( extension );
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Snozama Game Files";
	}

}
