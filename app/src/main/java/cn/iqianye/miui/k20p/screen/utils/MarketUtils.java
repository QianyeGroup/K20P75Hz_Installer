package cn.iqianye.miui.k20p.screen.utils;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

public class MarketUtils
{
    /**
     * 启动到应用商店app详情界面   
     * @param appPkg    目标App的包名
     * @param marketPkg 应用商店包名 ,如果为"" 则由系统弹出应用商店
     * 列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static void launchAppDetail(Context context, String appPkg, String marketPkg)
    {
        try
        {
            if (TextUtils.isEmpty(appPkg)) return;
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
            {
                intent.setPackage(marketPkg);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
			Intent intent;
			try
			{
				intent = Intent.parseUri("https://www.coolapk.com/apk/" + appPkg,
										 Intent.URI_INTENT_SCHEME);
				intent.addCategory(Intent.CATEGORY_BROWSABLE);
				intent.setComponent(null);
				context.startActivity(intent);
			}
			catch (Exception ee)
			{
				e.printStackTrace();
				Toast.makeText(context, "错误 " + ee.getMessage() + " 请联系开发者", Toast.LENGTH_LONG).show();
			}
			Toast.makeText(context, "错误，您未安装酷安，将通过浏览器打开", Toast.LENGTH_LONG).show();
        }
    }

}
