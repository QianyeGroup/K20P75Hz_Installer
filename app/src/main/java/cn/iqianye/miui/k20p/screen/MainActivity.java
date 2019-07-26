package cn.iqianye.miui.k20p.screen;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.legacy.v4.R;
import cn.iqianye.miui.k20p.screen.utils.AssetsUtils;
import cn.iqianye.miui.k20p.screen.utils.MarketUtils;
import cn.iqianye.miui.k20p.screen.utils.OtherUtils;
import cn.iqianye.miui.k20p.screen.utils.UriToPathUtils;
import com.jaredrummler.android.shell.Shell;
import java.io.File;

public class MainActivity extends AppCompatActivity
{
	String imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		if (!OtherUtils.isRaphael())
        {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("错误");
			b.setMessage("不支持您的设备，只支持Redmi K20 Pro(raphael)！");
			b.setCancelable(false);
			b.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int i)
					{
						finish();
					}
				});
			b.show();
        }
		else
		if (!OtherUtils.isSupport())
        {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("错误");
			b.setMessage("不支持401驱动的MIUI版本，请降级至9.7.11(包括)之前的版本！");
			b.setCancelable(false);
			b.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int i)
					{
						finish();
					}
				});
			b.show();
        }
		else
		if (!OtherUtils.checkRoot())
        {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("错误");
			b.setMessage("获取ROOT权限失败，请检查是否给予本软件ROOT权限！");
			b.setCancelable(false);
			b.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int i)
					{
						finish();
					}
				});
			b.show();
        }
		AssetsUtils.copyFolderFromAssetsToSD(this, "files", getExternalCacheDir().getAbsolutePath() + "/");

		Display display = getWindowManager().getDefaultDisplay();
		float refreshRate = display.getRefreshRate();
		TextView t = findViewById(R.id.refresh);
		t.setText("当前刷新率(仅供参考)\n" + refreshRate + " Hz");
		String lanucherNumberPath = getExternalFilesDir("").getAbsolutePath() + "/.lanucherNumber";
        int lanucherNumber = 0;
        if (new File(lanucherNumberPath).exists())
        {
			lanucherNumber = Integer.parseInt(Shell.SH.run("cat " + lanucherNumberPath).toString());
            if (lanucherNumber == 2)
            {
				AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("提示");
                b.setMessage("喜欢这个APP吗？给个五星好评可好！");
                b.setCancelable(false);
                b.setPositiveButton("好", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface d, int i)
                        {
							MarketUtils.launchAppDetail(MainActivity.this, "cn.iqianye.miui.k20p.screen", "com.coolapk.market");
						}
					});
				b.setNegativeButton("我就不！", null);
                b.show();
			}
            lanucherNumber++;
            Shell.SH.run("echo " + Integer.toString(lanucherNumber) + " > " + lanucherNumberPath);
		}
        else
        {
			Shell.SH.run("echo 1 > " + lanucherNumberPath);
		}
	}
	public void install(View view)
	{
		RadioButton seven = findViewById(R.id.seven);
		RadioButton custom = findViewById(R.id.custom);
		if (seven.isChecked())
		{
			imgFile = getExternalCacheDir().getAbsolutePath() + "/dtbo_75Hz.img";
			Shell.SU.run("dd if=" + imgFile + " of=/dev/block/by-name/dtbo");
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("成功");
			b.setMessage("刷入dtbo分区成功，请点击确认重启查看效果！");
			b.setPositiveButton("确认", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int i)
					{
						Shell.SU.run("reboot");
					}
				});
			b.show();
			imgFile = null;
		}
		else
		if (custom.isChecked())
		{
			if (imgFile == null)
			{
				Toast.makeText(this, "请选择文件", Toast.LENGTH_LONG).show();
			}
			else
			{
				Shell.SU.run("dd if=" + imgFile + " of=/dev/block/by-name/dtbo");
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle("成功");
				b.setMessage("刷入dtbo分区成功，请点击确认重启查看效果！");
				b.setPositiveButton("确认", new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface d, int i)
						{
							Shell.SU.run("reboot");
						}
					});
				b.show();
				imgFile = null;
				EditText e = findViewById(R.id.custom_input);
				e.setText(imgFile);

			}
		}
	}

	public void uninstall(View view)
	{
		Shell.SU.run("dd if=" + getExternalCacheDir().getAbsolutePath() + "/dtbo_60Hz.img" + " of=/dev/block/by-name/dtbo");
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("成功");
		b.setMessage("刷入dtbo分区成功，请点击确认重启查看效果！");
		b.setPositiveButton("确认", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface d, int i)
				{
					Shell.SU.run("reboot");
				}
			});
		b.show();
	}

	public void test(View view)
	{
		MarketUtils.launchAppDetail(this, "cn.iqianye.displaytest", "com.coolapk.market");
	}

	public void pay(View view)
	{
		OtherUtils.openUrl(this, "https://qr.alipay.com/tsx08529pzn1idznbmfobf7");
	}

	public void pay_atomSand(View view)
	{
		OtherUtils.openUrl(this, "https://qr.alipay.com/fkx049215mgig1vmq2yeu46");
	}

	public void atomSand(View view)
	{
		OtherUtils.openUrl(this, "http://www.coolapk.com/u/2794437");
	}

	public void get81(View view)
	{
		OtherUtils.openUrl(this, "https://www.coolapk.com/feed/12920689?shareKey=NDEzMWFmOWI2YjE4NWQzOTQ1Yjk~&shareUid=2042584&shareFrom=com.coolapk.market_9.4");
	}

	public void join(View view)
	{
		joinQQGroup("POi0sGneG6tKk4sDLTHDevyaIFWH6C4a");
	}

	public void seven_onClick(View view)
	{
		RadioButton seven = findViewById(R.id.seven);
		RadioButton custom = findViewById(R.id.custom);
		if (custom.isChecked())
		{
			custom.setChecked(false);
			seven.setChecked(true);
			Button select = findViewById(R.id.custom_select);
			TextView tips = findViewById(R.id.custom_tips);
			EditText input = findViewById(R.id.custom_input);
			select.setVisibility(View.GONE);
			tips.setVisibility(View.GONE);
			input.setVisibility(View.GONE);
		}
	}

	public void custom_onClick(View view)
	{
		RadioButton seven = findViewById(R.id.seven);
		RadioButton custom = findViewById(R.id.custom);
		if (seven.isChecked())
		{
			custom.setChecked(true);
			seven.setChecked(false);
			Button select = findViewById(R.id.custom_select);
			TextView tips = findViewById(R.id.custom_tips);
			EditText input = findViewById(R.id.custom_input);
			select.setVisibility(View.VISIBLE);
			tips.setVisibility(View.VISIBLE);
			input.setVisibility(View.VISIBLE);
		}
	}

	public void select_onClick(View view)
	{
		EditText e = findViewById(R.id.custom_input);
		selectFile(this);
	}

	public void selectFile(Activity activity)
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		intent.setType("*/*"); 
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try
		{
			startActivityForResult(Intent.createChooser(intent, "选择img文件"), 0);
		}
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case 0:
				if (resultCode == RESULT_OK)
				{
					Uri uri = data.getData();
					imgFile = UriToPathUtils.getRealFilePath(this, uri);
					EditText e = findViewById(R.id.custom_input);
					e.setText(imgFile);
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/****************
     *
     * 发起添加群流程。群号：真心极客 - Redmi K20/Pro |(691802087) 的 key 为： POi0sGneG6tKk4sDLTHDevyaIFWH6C4a
     * 调用 joinQQGroup(POi0sGneG6tKk4sDLTHDevyaIFWH6C4a) 即可发起手Q客户端申请加群 真心极客 - Redmi K20/Pro |(691802087)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key)
    {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try
        {
            startActivity(intent);
            return true;
        }
        catch (Exception e)
        {
            // 未安装手Q或安装的版本不支持
            MarketUtils.launchAppDetail(this, "com.tencent.mobileqq", null);
			return false;
        }
    }
}
