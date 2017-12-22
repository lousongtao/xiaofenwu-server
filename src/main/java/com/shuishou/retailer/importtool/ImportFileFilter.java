package com.shuishou.retailer.importtool;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ImportFileFilter extends FileFilter {

	private String ext;
	
	public ImportFileFilter(String _ext){
		ext = _ext;
	}
	
	@Override
	public boolean accept(File f) {  
        if (f.isDirectory()) {  
            return true;  
        }  
  
        String extension = getExtension(f);  
        if (extension.toLowerCase().equals(this.ext.toLowerCase()))  
        {  
            return true;  
        }  
        return false;  
    }  

	@Override
	public String getDescription() {
		return ext.toUpperCase();
	}
	

	private String getExtension(File f) {  
        String name = f.getName();  
        int index = name.lastIndexOf('.');  
  
        if (index == -1)  
        {  
            return "";  
        }  
        else  
        {  
            return name.substring(index + 1).toLowerCase();  
        }  
    }  
}
