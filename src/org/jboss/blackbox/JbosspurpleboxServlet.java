package org.jboss.blackbox;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

@SuppressWarnings("serial")
public class JbosspurpleboxServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
	
	
	public static void main(String[] args) throws Exception {
		
		System.out.println(makeRequestId());
		
		
		/*
		HttpURLConnection conn = (HttpURLConnection) (new URL("http://jbossblackbox.appspot.com/create_usage")).openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "text/plain");
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		
		out.write("hello world !");
		out.close();
		
		conn.getInputStream().read();
		conn.disconnect();
		System.out.println("Done !");
		*/
		
	}
	
	private static int makeRequestId() {
		Random rnd = new Random();
		rnd.setSeed(System.currentTimeMillis());
		return rnd.nextInt(Integer.MAX_VALUE);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ServletFileUpload upload = new ServletFileUpload();
		upload.setFileSizeMax(500000);
		
		
		try {
			FileItemIterator items = upload.getItemIterator(req);
			
			int correlationId = makeRequestId();
			
			while(items.hasNext()) {
				FileItemStream item = (FileItemStream) items.next();

				HttpURLConnection conn = (HttpURLConnection) (new URL("http://jbossblackbox.appspot.com/create_usage?" + req.getQueryString() + "&correlation_id=" + correlationId)).openConnection();
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "text/plain");
				OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
				InputStream ins = item.openStream();
				String data = IOUtils.toString(ins);
				out.write(data);
			    IOUtils.closeQuietly(ins);
				out.close();
				conn.getInputStream().read();
				conn.disconnect();
				
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}

	}
}
