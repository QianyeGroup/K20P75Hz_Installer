package cn.iqianye.miui.k20p.screen.utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import com.jaredrummler.android.shell.Shell;
import com.stericson.RootTools.RootTools;

public class OtherUtils
{
	public static void openUrl(Activity activity, String url)
	{
		Intent intent;
		try
		{
			intent = Intent.parseUri(url,
									 Intent.URI_INTENT_SCHEME);
			intent.addCategory(Intent.CATEGORY_BROWSABLE);
			intent.setComponent(null);
			activity.startActivity(intent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}



	public static Boolean isRaphael()
	{
		String s = Build.DEVICE;
		if ("raphael".equals(s))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static Boolean isSupport()
	{
		String ver = 
			Shell.SH.run
		(
			"getprop ro.build.version.incremental"
		).toString();
		switch (ver)
		{
			case "9.7.15":
			case "9.7.16":
			case "9.7.17":
			case "9.7.18":
			case "9.7.19":
			case "9.7.20":
			case "9.7.21":
			case "9.7.22":
			case "9.7.23":
			case "9.7.24":
			case "9.7.25":
			case "9.7.26":
			case "9.7.27":
			case "9.7.28":
			case "9.7.29":
			case "9.7.15":
			case "9.7.16":
			case "9.7.17":
			case "9.7.18":
			case "9.7.19":
			case "9.7.20":
			case "9.7.21":
			case "9.7.22":
			case "9.7.23":
			case "9.7.24":
			case "9.7.25":
			case "9.7.26":
			case "9.7.27":
			case "9.7.28":
			case "9.7.29":
			case "9.7.30":
			case "9.8.1":
			case "9.8.2":
			case "9.8.3":
			case "9.8.4":
			case "9.8.5":
			case "9.8.6":
			case "9.8.7":
			case "9.8.9":
			case "9.8.8":
			case "9.8.10":
			case "9.8.11":
			case "9.8.12":
			case "9.8.13":
				return false;
			default:
				return true;

		}
	}
    public static Boolean checkRoot()
    {
        if (RootTools.isRootAvailable())
        {
            if (!RootTools.isAccessGiven())
            {
                return false;
            }
        }
        else
        {
            return false;
        }
        return true;
    }
}
