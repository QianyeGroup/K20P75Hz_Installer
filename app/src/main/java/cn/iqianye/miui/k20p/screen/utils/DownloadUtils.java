package cn.iqianye.miui.k20p.screen.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL; 

public class DownloadUtils
{ 
	private URL url = null; 

	/** 
	 * 根据URL下载文件，前提是这个文件当中的内容是文本，函数的返回值就是文件当中的内容 
	 * 1.创建一个URL对象 
	 * 2.通过URL对象，创建一个HttpURLConnection对象 
	 * 3.得到InputStram 
	 * 4.从InputStream当中读取数据 
	 * @param urlStr 
	 * @return 
	 */ 
	public String download(String urlStr)
	{ 
		StringBuffer sb = new StringBuffer(); 
		String line = null; 
		BufferedReader buffer = null; 
		try
		{ 
			// 创建一个URL对象 
			url = new URL(urlStr); 
			// 创建一个Http连接 
			HttpURLConnection urlConn = (HttpURLConnection) url.openConnection(); 
			// 使用IO流读取数据 
			buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream())); 
			while ((line = buffer.readLine()) != null)
			{ 
				sb.append(line); 
			} 
		}
		catch (Exception e)
		{ 
			e.printStackTrace(); 
		}
		finally
		{ 
			try
			{ 
				buffer.close(); 
			}
			catch (Exception e)
			{ 
				e.printStackTrace(); 
			} 
		} 
		return sb.toString(); 
	}
}
