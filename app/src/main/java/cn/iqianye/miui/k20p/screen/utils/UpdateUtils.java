package cn.iqianye.miui.k20p.screen.utils;

import cn.iqianye.miui.k20p.screen.utils.DownloadUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateUtils
{
	static String rootUrl = "https://qianyegroup.gitee.io/k20p75hz_installer/";

	static String latestVerName = null;
	static String latestVerCode = null;
	static String latestVerInfo = null;
	static String latestVerUrl = null;
	public static String[] getNewVersionInfo(String version)
	{
		DownloadUtils down = new DownloadUtils();
		String json = down.download(rootUrl + "latest.json");
		try
		{
			JSONObject jo = new JSONObject(json);
			latestVerName = jo.getString("latestVerName");
			latestVerCode = jo.getString("latestVerCode");
			latestVerInfo = jo.getString("latestVerInfo");
			latestVerUrl = jo.getString("latestVerUrl");
			String[] StrArr = {latestVerName,latestVerCode,latestVerInfo,latestVerCode};
			return StrArr;
		}
		catch (JSONException e)
		{
			return null;
		}
	}
}
