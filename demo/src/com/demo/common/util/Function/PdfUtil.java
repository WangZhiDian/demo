package com.demo.common.util.Function;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/*import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;*/

public class PdfUtil
{
	//文件存储,返回文件字节数组，argvs为pdf文件路径列表
	static public byte[] GenerateInsurance(
			String[] argvs)
    {
		ByteArrayOutputStream baos = null;	
    	try{
    		baos = new ByteArrayOutputStream();
    		
	        boolean result  = merge(argvs,baos);
	        if(result)
	        {
	        	return baos.toByteArray();
	        }else{
	        	return null;
	        }
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		return null;
    	}finally{
    		try{ if(baos!=null) baos.close(); }catch(Exception e){e.printStackTrace();};    		
    	}
    }
	
	//将多个pdf文件读取到一个文件流中
	public static boolean merge(String[] argvs,ByteArrayOutputStream baos) throws Exception{
		
		/*int pageOffset = 0;
        ArrayList master = new ArrayList();
        int f = 0;
        
        Document document = null;
        PdfCopy  writer = null;
        
		 while (f < argvs.length) {
            // we create a reader for a certain document
            PdfReader reader = new PdfReader(argvs[f]);
            reader.consolidateNamedDestinations();
            // we retrieve the total number of pages
            int n = reader.getNumberOfPages();
            List bookmarks = SimpleBookmark.getBookmark(reader);
            if (bookmarks != null) {
                if (pageOffset != 0)
                    SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                master.addAll(bookmarks);
            }
            pageOffset += n;
            
            if (f == 0) {
                // step 1: creation of a document-object
                document = new Document(reader.getPageSizeWithRotation(1));
                // step 2: we create a writer that listens to the document
                writer = new PdfCopy(document, baos);
                // step 3: we open the document
                document.open();
            }
            // step 4: we add content
            PdfImportedPage page;
            for (int i = 0; i < n; ) {
                ++i;
                page = writer.getImportedPage(reader, i);
                writer.addPage(page);
            }
            PRAcroForm form = reader.getAcroForm();
            if (form != null)
                writer.copyAcroForm(reader);
            f++;
        }
		 if (!master.isEmpty())
            writer.setOutlines(master);
        // step 5: we close the document
        document.close();*/
		return true;
	}
	
	
	//文件存储，将合成pdf存储到制定文件名称下
	static public boolean GenerateInsuranceFile(
			String[] argvs,
    		String saveto)
    {
		java.io.FileOutputStream out = null;
		ByteArrayOutputStream baos = null;	

    	try{
    		baos = new ByteArrayOutputStream();
    		
	        boolean result  = merge(
	        		argvs,
	        		baos);
	        if(result)
	        {
	        	out = new java.io.FileOutputStream(saveto);
	        	baos.writeTo(out);
	            out.flush();
	            return true;
	        }else
	        	return false;
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    		return false;
    	}finally{
    		try{ if(baos!=null) baos.close(); }catch(Exception e){e.printStackTrace();};    		
    		try{ if(out!=null) out.close(); }catch(Exception e){e.printStackTrace();};
    	}
    }
	
	//测试pdf合成
	public static void main(String argvs[])
	{
		String saveto = new String("F:\\pdftest\\merge.pdf");
		String[] insuranceformIpa = 
		{
			"F:\\pdftest\\page1.pdf"
			,"F:\\pdftest\\page2.pdf"
		};
		GenerateInsuranceFile(insuranceformIpa,saveto);
	}
	
}
