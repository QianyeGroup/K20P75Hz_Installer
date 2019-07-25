package cn.iqianye.miui.k20p.screen.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Create By HaiyuKing
 * @Used AssetsUtils【读取assets、res/raw、./data/data/包名/目录下的文件】
 * <ui>
 * <li>1、读取assets目录下的资源html、文件、图片；将文件复制到SD卡目录中</li>
 * <li>2、读取res/raw目录下的文件内容</li>
 * <li>3、读写./data/data/包名/下的文件</li>
 * </ui>
 */
public class AssetsUtils
{

	/**
	 * 获取assets的指定目录中的所有文件及子目录名数组
	 * @param assetsFolderPath - 目录的相对路径（目录），例如："why"
	 * @注意 子目录中必须有文件，否则不会将子目录名称写入数组中
	 * @return [img,listdata.txt,test.html]
	 * */
	public static String[] getFileNamesArray(Context mContext, String assetsFolderPath)
    {
		String fileNames[] = null;
		try
        {
			fileNames = mContext.getResources().getAssets().list(assetsFolderPath);// 获取assets目录下的所有文件及子目录名
		}
        catch (IOException e)
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileNames;
	}
	/**
	 * 使用webview加载assets目录下的html网页。比如想要打开assets/why/test.html
	 * @param assetsFilePath - html文件的相对路径，例如："why/test.html"或者"test.html"
	 * */
	public static void loadAssetsHtml(WebView id_webview, String assetsFilePath)
    {
		id_webview.loadUrl("file:///android_asset/" + assetsFilePath);
	}

	/**
	 * 访问assets目录下的资源文件，获取文件中的字符串
	 * @param assetsFilePath - 文件的相对路径，例如："listitemdata.txt或者"/why/listdata.txt"
	 * @return 内容字符串
	 * */
	public static String getStringFromAssert(Context mContext, String assetsFilePath)
    {

		String content = ""; // 结果字符串
		try
        {
			InputStream is = mContext.getResources().getAssets().open(assetsFilePath);// 打开文件
			int ch = 0;
			ByteArrayOutputStream out = new ByteArrayOutputStream(); // 实现了一个输出流
			while ((ch = is.read()) != -1)
            {
				out.write(ch); // 将指定的字节写入此 byte 数组输出流
			}
			byte[] buff = out.toByteArray();// 以 byte 数组的形式返回此输出流的当前内容
			out.close(); // 关闭流
			is.close(); // 关闭流
			content = new String(buff, "UTF-8"); // 设置字符串编码
		}
        catch (Exception e)
        {
			Toast.makeText(mContext, "对不起，没有找到指定文件！", Toast.LENGTH_SHORT)
                .show();
		}
		return content;
	}

	/** 
	 *  从assets目录中复制整个文件夹内容到SD卡中
	 *  @param  mContext  Context 使用CopyFiles类的Activity
	 *  @param  assetsFolderPath  String  assets的相对路径(目录)  如："why"
	 *  @param  sdFolderPath  String  复制到的sd路径（目录）  如：Environment.getExternalStorageDirectory() + "/why"
	 */
	public static void copyFolderFromAssetsToSD(Context mContext, String assetsFolderPath, String sdFolderPath)
    {
		try
        {
			String fileNames[] = mContext.getResources().getAssets().list(assetsFolderPath);// 获取assets目录下的所有文件及目录名
			if (fileNames.length > 0)
            {//如果是目录，则新建目录
				File file = new File(sdFolderPath);
				if (! file.exists())
                {
					file.mkdirs();//如果文件夹不存在，则创建目录
				}
				//递归，将目录下的单个文件复制到目录中
				for (String fileName : fileNames)
                {
					copyFolderFromAssetsToSD(mContext, assetsFolderPath + "/" + fileName, sdFolderPath + "/" + fileName);
				}
			}
            else
            {//如果是文件

				InputStream is = mContext.getResources().getAssets().open(assetsFolderPath);
				FileOutputStream fos = new FileOutputStream(new File(sdFolderPath));
				byte[] buffer = new byte[1024];
				int byteCount = 0;
				while ((byteCount = is.read(buffer)) != -1)
                {// 循环从输入流读取
                    // buffer字节
					fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
				}
				fos.flush();// 刷新缓冲区
				is.close();
				fos.close();
			}
		}
        catch (Exception e)
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 
	 *  从assets目录中复制单个文件到SD卡中
	 *  @param  mContext  Context 使用CopyFiles类的Activity
	 *  @param  assetsFilePath  String  assets的相对路径(目录)  如："why/img/image.png"
	 *  @param  sdFilePath  String  复制到的sd路径（目录）  如：Environment.getExternalStorageDirectory() + "/why/img.png"
	 */
	public static boolean copyOneFileFromAssetsToSD(Context mContext, String assetsFilePath, String sdFilePath)
    {

		try
        {

			InputStream stream = mContext.getResources().getAssets().open(assetsFilePath);

			File file = new File(sdFilePath);

        	OutputStream o = null;
            try
            {
            	//创建父目录
            	String parentPath = file.getAbsolutePath();
            	String getFolderName = "";
                if (parentPath == null || parentPath.length() == 0)
                {
                	getFolderName = parentPath;
                }
                else
                {
                	int filePosi = parentPath.lastIndexOf(File.separator);
                	getFolderName = (filePosi == -1) ? "" : parentPath.substring(0, filePosi);
                }

                Boolean makeDirs = false;
                if (getFolderName == null || getFolderName.length() == 0)
                {
                	makeDirs = false;
                }
                else
                {
                	File folder = new File(getFolderName);
                	makeDirs = (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
                }
                if (makeDirs)
                {
                	o = new FileOutputStream(file, false);
                    byte data[] = new byte[1024];
                    int length = -1;
                    while ((length = stream.read(data)) != -1)
                    {
                        o.write(data, 0, length);
                    }
                    o.flush();
                    return true;
                }

            }
            catch (FileNotFoundException e)
            {
                throw new RuntimeException("FileNotFoundException occurred. ", e);
            }
            catch (IOException e)
            {
                throw new RuntimeException("IOException occurred. ", e);
            }
            finally
            {
                if (o != null)
                {
                    try
                    {
                        o.close();
                        stream.close();
                    }
                    catch (IOException e)
                    {
                        throw new RuntimeException("IOException occurred. ", e);
                    }
                }
            }
		}
        catch (Exception e)
        {
			throw new RuntimeException("Exception occurred. ", e);
		}
		return false;
	}


	/**
	 * 获取assets目录下的图片资源的bitmap对象
	 * @param assetsImgPath - 文件的相对路径，例如：image.png或者www/img/image.png
	 * 使用方式：id_imageview.setImageBitmap(bitmap);
	 * */
	public static Bitmap getImageBitmapFromAssetsFile(Context mContext, String assetsImgPath)
    {
		Bitmap bitmap = null;
		try
        {
			InputStream is = mContext.getResources().getAssets().open(assetsImgPath);
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		}
        catch (IOException e)
        {
			e.printStackTrace();
		}
		return bitmap;
	}


	/**
	 * 从res中的raw文件夹中获取文件并读取数据（资源文件只能读不能写）
	 * @param rawFileId - 文件的ID值：R.raw.xxx；例如：R.raw.rawtext
	 * @return 文件中的内容字符串
	 */
	/*
	 * 首先调用Context.getResource获得当前应用程序上下文的Resources引用.
	 * 然后调用openRawResource(int id)得到InputStream.
	 * 最后，操作InputStream得到数据。
	 * 注意：把文件放在res/raw目录下，则R类会自动提供该id.*/
	public static String getStringFromRaw(Context mContext, int rawFileId)
    {
		String res = "";
		try
        {
			InputStream in = mContext.getResources().openRawResource(rawFileId);
			int length = in.available();
			byte[] buffer = new byte[length];
			in.read(buffer);
			//res = EncodingUtils.getString(buffer, "GBK");
			res = new String(buffer, "UTF-8");
			in.close();
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 写入（./data/data/包名/file）文件里面内容
	 * 
	 * @param file - 私有文件夹下的文件名，例如：datatext.txt【在/data/data/{package}/目录下】
	 * @param message - 想要写入的数据字符串
	 */
	public static void writeFileToData(Context mContext, String file, String message)
    {
		try
        {
			/* 
             * MODE_APPEND 追加模式 - 如果已经存在的文件，将数据写入到现有文件的末尾而不是抹去它。 
             * MODE_PRIVATE 私有模式 - 只有本程序或包名相同的程序才能访问 
             * MODE_WORLD_READABLE 读取模式 - 其他程序可以读取此文件 
             * MODE_WORLD_WRITEABLE - 写入模式 其他程序可以修改此文件 
             * 除了追加模式其他模式都会将内容全部覆盖 
             */  
			FileOutputStream fout = mContext.openFileOutput(file, Context.MODE_PRIVATE);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
	}

	/**
	 * 读取./data/data/包名/file/下面的文件内容
	 * 
	 * @param fileName - 私有文件夹下的文件名，例如：datatext.txt
	 * @return
	 */
	public static String getStringFileFromData(Context mContext, String fileName)
    {
		String res = "";
		try
        {
			FileInputStream fin = mContext.openFileInput(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			//res = EncodingUtils.getString(buffer, "UTF-8");
			res = new String(buffer, "UTF-8");
			fin.close();
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
		return res;
	}

}
