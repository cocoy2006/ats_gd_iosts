package molab.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import molab.util.Dir;
import molab.util.Util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Upfiles extends HttpServlet {

	/**
	 * <form action="/servlet/Upfiles" enctype="multipart/form-data" method="post">
	 * 		<input type="file" name="file1"><br>
	 * 		<input type="file" name="file2"><br>
	 * 		<input type="file" name="file3"><br>
	 * 		<input type="submit">
	 * </form>
	 */
	private final long SIZE_MAX = 1024 * 1024 * 1000;
	private final long FILE_SIZE_MAX = 1024 * 1024 * 1000;
	
	private final String UPLOAD_DIR = "/sdcard/";
	private final String INSTALL_DIR = "/data/local/tmp/";
	private final String CHARSET = "UTF-8";
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		if(request.getSession().getAttribute("percentage") != null) 
			request.getSession().removeAttribute("percentage");
		request.setCharacterEncoding(CHARSET);
		File file = upfile(request);
		
		String js = "<script type='text/javascript'>parent.uploadResult('"+file.getName()+"')</script>";
				
		response.setContentType("text/html;charset=" + CHARSET);
		response.getWriter().println(js);
	}

	private File upfile(final HttpServletRequest request) {
		// ����Ŀ¼
		String uploadDir = Util.getInstance().getConf().getProperty("BaseUploadRealpath");
//		String sessionId = request.getSession().getId();
		String username = request.getSession().getAttribute("username").toString();
		
		new Dir().newDir(uploadDir, username);
		uploadDir = uploadDir.concat("/").concat(username);
		// ���ò���
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// �����ڴ�����С4KB
		factory.setSizeThreshold(4 * 1024);
		// �����ݴ����������ϴ��ļ��������õ��ڴ���Сʱ�����ݴ���������ת
		factory.setRepository(new File(uploadDir));
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		fileUpload.setHeaderEncoding(CHARSET);
		// ���ý�ȼ���
		ProgressListener progressListener = new ProgressListener(){
		   private long megaBytes = -1;
		   public void update(long pBytesRead, long pContentLength, int pItems) {
		       long mBytes = pBytesRead / 10000;
		       if (megaBytes == mBytes && pBytesRead != pContentLength) {
		           return;
		       }
		       megaBytes = mBytes;
		       if (pContentLength == -1) {

		       } else {
		           double percentage = ((double) pBytesRead / (double) pContentLength);
		           request.getSession().setAttribute("percentage", percentage);
		       }
		   }
		};
		fileUpload.setProgressListener(progressListener);
		
//		fileUpload.setSizeMax(SIZE_MAX);
//		fileUpload.setFileSizeMax(FILE_SIZE_MAX);
		List<FileItem> fileItemList = null;
		try {
			fileItemList = fileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		FileItem fileItem = null;
		File writeToFile = null;
		
		Iterator<FileItem> fileItemIterator = fileItemList.iterator();
		while (fileItemIterator.hasNext()) {
			fileItem = fileItemIterator.next();
			// ��ͨ�ļ����ϴ�
			if (fileItem.isFormField()) {

			} else {
				// ��ȡ�ļ��ϴ����ļ���
				String OriginalFileName = takeOutFileName(fileItem.getName());
				if (!"".equals(OriginalFileName)) {					
					writeToFile = new File(uploadDir + File.separator + OriginalFileName);
					try {
						fileItem.write(writeToFile);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return writeToFile;
	}
	
	private String takeOutFileName(String filePath) {
		String fileName = filePath;
		if (null != filePath && !"".equals(filePath)) {
			int port = filePath.lastIndexOf("\\" + 1);
			if(port != -1){
				fileName = filePath.substring(port);
			}
		}
		return replace(fileName);
	}
	
	private String replace(String ori) {
		ori = ori.trim();
		char[] oriChar = ori.toCharArray();
		int len = oriChar.length;
		char[] fileChar = new char[len];
		for(int i = 0; i < len; i++) {
			if(oriChar[i] == ' ') {
				fileChar[i] = '_';
			} else {
				fileChar[i] = oriChar[i];
			}			
		}
		return String.copyValueOf(fileChar);
	}
}
