package com.opencms.workplace;

import com.opencms.file.*;
import com.opencms.core.*;
import com.opencms.util.*;
import com.opencms.template.*;

import javax.servlet.http.*;

import java.util.*;

/**
 * Template class for displaying the file list tree of the OpenCms workplace.<P>
 * Reads template files of the content type <code>CmsXmlWpTemplateFile</code>.
 * 
 * 
 * @author Michael Emmerich
 * @version $Revision: 1.2 $ $Date: 2000/02/02 15:28:57 $
 */
public class CmsExplorerFileList extends CmsWorkplaceDefault implements I_CmsWpConstants,
                                                                I_CmsConstants{    

    /**
   * Overwrites the getContent method of the CmsWorkplaceDefault.<br>
   * Gets the content of the file list template and processe the data input.
   * @param cms The CmsObject.
   * @param templateFile The file list template file
   * @param elementName not used
   * @param parameters Parameters of the request and the template.
   * @param templateSelector Selector of the template tag to be displayed.
   * @return Bytearre containgine the processed data of the template.
   * @exception Throws CmsException if something goes wrong.
   */
   public byte[] getContent(A_CmsObject cms, String templateFile, String elementName, Hashtable parameters, String templateSelector) throws CmsException {
         
     CmsXmlWpTemplateFile xmlTemplateDocument = new CmsXmlWpTemplateFile(cms,templateFile);        
    
     // process the selected template
     return startProcessing(cms,xmlTemplateDocument,"",parameters,"template");
   }
    
   
   /** 
    * Collects all folders and files that are displayed in the file list.
    * @param cms The CmsObject.
    * @return A vector of folder and file objects.
    * @exception Throws CmsException if something goes wrong.
    */
   public Vector getFiles(A_CmsObject cms) 
       throws CmsException {
       Vector filesfolders=new Vector();
  
        String foldername;
        String currentFilerlist;
        // vectors to store all files and folders in the current folder.
        Vector files;
        Vector folders;
        Enumeration enum;
            
        // file and folder object required to create the filefolder list.
        CmsFile file;
        CmsFolder folder;
            
        HttpSession session= ((HttpServletRequest)cms.getRequestContext().getRequest().getOriginalRequest()).getSession(true);
              
        //check if a folder parameter was included in the request.
        // if a foldername was included, overwrite the value in the session for later use.
        foldername=cms.getRequestContext().getRequest().getParameter(C_PARA_FILELIST);
        if (foldername != null) {
        session.putValue(C_PARA_FILELIST,foldername);
        }

        // get the current folder 
        currentFilerlist=(String)session.getValue(C_PARA_FILELIST);
        if (currentFilerlist == null) {
            currentFilerlist=cms.getRequestContext().currentFolder().getAbsolutePath();
        }          
        
        // get all files and folders of the current folder
        folders=cms.getSubFolders(currentFilerlist);
        files=cms.getFilesInFolder(currentFilerlist);
       
        // combine both vectors
        enum=folders.elements();
        while (enum.hasMoreElements()) {
            folder = (CmsFolder)enum.nextElement();
            filesfolders.addElement(folder);
        }
        enum=files.elements();
        while (enum.hasMoreElements()) {
            file = (CmsFile)enum.nextElement();
            filesfolders.addElement(file);
        }
            
        return filesfolders;
   }
       
}
