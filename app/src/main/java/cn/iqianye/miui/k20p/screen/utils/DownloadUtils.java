package cn.iqianye.miui.k20p.screen.utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL; 

public class DownloadUtils { 
    /**
     * 从服务器下载文件
     * @param path 下载文件的地址
     * @param FileName 文件名字
     */
    public void downLoad(final String netUrl ,final String filePath, final String FileName) {
        new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(netUrl);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setReadTimeout(5000);
                        con.setConnectTimeout(5000);
                        con.setRequestProperty("Charset", "UTF-8");
                        con.setRequestMethod("GET");
                        if (con.getResponseCode() == 200) {
                            InputStream is = con.getInputStream();//获取输入流
                            FileOutputStream fileOutputStream = null;//文件输出流
                            if (is != null) {
                                fileOutputStream = new FileOutputStream(createFile(filePath,FileName));//指定文件保存路径，代码看下一步
                                byte[] buf = new byte[1024];
                                int ch;
                                while ((ch = is.read(buf)) != -1) {
                                    fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                                }
                            }
                            if (fileOutputStream != null) {
                                fileOutputStream.flush();
                                fileOutputStream.close();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }
    /**
     * 创建一个文件
     * @param filePath 文件路径
     * @param FileName 文件名
     * @return
     */
    public File createFile(String filePath, String FileName) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return new File(filePath, FileName);
    }

}
