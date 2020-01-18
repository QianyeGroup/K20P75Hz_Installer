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
import cn.iqianye.miui.k20p.screen.utils.AssetsUtils;
import cn.iqianye.miui.k20p.screen.utils.FileUtils;
import cn.iqianye.miui.k20p.screen.utils.MarketUtils;
import cn.iqianye.miui.k20p.screen.utils.OtherUtils;
import com.jaredrummler.android.shell.Shell;
import java.io.File;
import cn.iqianye.miui.k20p.screen.utils.DownloadUtils;
import android.os.Environment;

public class MainActivity extends AppCompatActivity {
	String imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		if (!OtherUtils.isRaphael()) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle(R.string.dialog_title);
			b.setMessage(R.string.no_raphael);
			b.setCancelable(false);
			b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int i) {
						finish();
					}
				});
			b.show();
        } else
		if (!OtherUtils.checkRoot()) {
            AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle(R.string.dialog_title);
			b.setMessage(R.string.no_root);
			b.setCancelable(false);
			b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface d, int i) {
						finish();
					}
				});
			b.show();
        }
		AssetsUtils.copyFolderFromAssetsToSD(this, "files", getExternalCacheDir().getAbsolutePath() + "/");

        
        
		Display display = getWindowManager().getDefaultDisplay();
		float refreshRate = display.getRefreshRate();
		TextView t = findViewById(R.id.refresh);
		t.setText(this.getString(R.string.refresh_rate) + refreshRate + " Hz");
		String lanucherNumberPath = getExternalFilesDir("").getAbsolutePath() + "/.lanucherNumber";
        int lanucherNumber = 0;
        if (new File(lanucherNumberPath).exists()) {
			lanucherNumber = Integer.parseInt(Shell.SH.run("cat " + lanucherNumberPath).toString());
            if (lanucherNumber == 2) {
				AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle(R.string.dialog_title);
                b.setMessage(R.string.score);
                b.setCancelable(false);
                b.setPositiveButton(R.string.good, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface d, int i) {
							MarketUtils.launchAppDetail(MainActivity.this, "cn.iqianye.miui.k20p.screen", "com.coolapk.market");
						}
					});
				b.setNegativeButton(R.string.no_good, null);
                b.show();
			}
            lanucherNumber++;
            Shell.SH.run("echo " + Integer.toString(lanucherNumber) + " > " + lanucherNumberPath);
		} else {
			Shell.SH.run("echo 1 > " + lanucherNumberPath);
		}
	}
	public void install(View view) {
		RadioButton seven = findViewById(R.id.seven);
		RadioButton eight = findViewById(R.id.eight);
		RadioButton custom = findViewById(R.id.custom);
		if (seven.isChecked()) {
			if (!OtherUtils.isSupport()) {
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle(R.string.dialog_title);
				b.setMessage(R.string.no_support_711);
				b.setCancelable(false);
				b.setPositiveButton(R.string.yes, null);
				b.show();
			} else {
				imgFile = getExternalCacheDir().getAbsolutePath() + "/dtbo_75Hz.img";
				Shell.SU.run("dd if=" + imgFile + " of=/dev/block/by-name/dtbo");
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle(R.string.dialog_title);
				b.setMessage(R.string.flash_success);
				b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface d, int i) {
							Shell.SU.run("reboot");
						}
					});
				b.show();
				imgFile = null;
			}
		} else
		if (eight.isChecked()) {
			if (!OtherUtils.isSupport()) {
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle(R.string.dialog_title);
				b.setMessage(R.string.no_support_711);
				b.setCancelable(false);
				b.setPositiveButton(R.string.yes, null);
				b.show();
			} else {
				imgFile = getExternalCacheDir().getAbsolutePath() + "/dtbo_81Hz.img";
				Shell.SU.run("dd if=" + imgFile + " of=/dev/block/by-name/dtbo");
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle(R.string.dialog_title);
				b.setMessage(R.string.flash_success);
				b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface d, int i) {
							Shell.SU.run("reboot");
						}
					});
				b.show();
				imgFile = null;
			}
		} else
		if (custom.isChecked()) {
			if (imgFile == null) {
				Toast.makeText(this, R.string.no_select_file, Toast.LENGTH_LONG).show();
			} else {
				Shell.SU.run("dd if=" + imgFile + " of=/dev/block/by-name/dtbo");
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle(R.string.dialog_title);
				b.setMessage(R.string.flash_success_custom);
				b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface d, int i) {
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

	public void uninstall(View view) {
		Shell.SU.run("dd if=" + getExternalCacheDir().getAbsolutePath() + "/dtbo_60Hz.img" + " of=/dev/block/by-name/dtbo");
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle(R.string.dialog_title);
		b.setMessage(R.string.flash_success);
		b.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface d, int i) {
					Shell.SU.run("reboot");
				}
			});
		b.show();
	}

	public void test(View view) {
		try {
			Intent intent = new Intent();
			intent.setClassName("cn.iqianye.displaytest", "com.androlua.Welcome");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			MarketUtils.launchAppDetail(this, "cn.iqianye.displaytest", "com.coolapk.market");
		}
	}

	public void pay(View view) {
		OtherUtils.openUrl(this, "https://qr.alipay.com/tsx08529pzn1idznbmfobf7");
	}

	public void pay_atomSand(View view) {
		OtherUtils.openUrl(this, "https://qr.alipay.com/fkx049215mgig1vmq2yeu46");
	}

	public void atomSand(View view) {
		OtherUtils.openUrl(this, "http://www.coolapk.com/u/2794437");
	}

	public void join(View view) {
        String groupPath = getExternalCacheDir().getAbsolutePath();
        DownloadUtils d = new DownloadUtils();
        d.downLoad("https://api.zhenxin.xyz/group/zhenxin.group", groupPath,".group");
        String group = Shell.SH.run("cat " + groupPath + "/.group").toString();
		joinQQGroup(group);
	}

	public void seven_onClick(View view) {
		RadioButton seven = findViewById(R.id.seven);
		RadioButton eight = findViewById(R.id.eight);
		RadioButton custom = findViewById(R.id.custom);
		if (custom.isChecked() || eight.isChecked()) {
			custom.setChecked(false);
			seven.setChecked(true);
			eight.setChecked(false);
			Button select = findViewById(R.id.custom_select);
			TextView tips = findViewById(R.id.custom_tips);
			EditText input = findViewById(R.id.custom_input);
			select.setVisibility(View.GONE);
			tips.setVisibility(View.GONE);
			input.setVisibility(View.GONE);
		}
	}

	public void eight_onClick(View view) {
		RadioButton seven = findViewById(R.id.seven);
		RadioButton eight = findViewById(R.id.eight);
		RadioButton custom = findViewById(R.id.custom);
		if (seven.isChecked() || custom.isChecked()) {
			eight.setChecked(true);
			seven.setChecked(false);
			custom.setChecked(false);
			Button select = findViewById(R.id.custom_select);
			TextView tips = findViewById(R.id.custom_tips);
			EditText input = findViewById(R.id.custom_input);
			select.setVisibility(View.GONE);
			tips.setVisibility(View.GONE);
			input.setVisibility(View.GONE);
		}
	}

	public void custom_onClick(View view) {
		RadioButton seven = findViewById(R.id.seven);
		RadioButton eight = findViewById(R.id.eight);
		RadioButton custom = findViewById(R.id.custom);
		if (seven.isChecked() || eight.isChecked()) {
			custom.setChecked(true);
			seven.setChecked(false);
			eight.setChecked(false);
			Button select = findViewById(R.id.custom_select);
			TextView tips = findViewById(R.id.custom_tips);
			EditText input = findViewById(R.id.custom_input);
			select.setVisibility(View.VISIBLE);
			tips.setVisibility(View.VISIBLE);
			input.setVisibility(View.VISIBLE);
		}
	}


	public void select_onClick(View view) {
		selectFile(this);
	}

	public void selectFile(Activity activity) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
		intent.setType("*/*"); 
		intent.addCategory(Intent.CATEGORY_OPENABLE);

		try {
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case 0:
				if (resultCode == RESULT_OK) {
					Uri uri = data.getData();
					imgFile = FileUtils.getFilePathByUri(this, uri);
					EditText e = findViewById(R.id.custom_input);
					e.setText(imgFile);
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/****************
     *
     * 发起添加群流程。
     * 
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回false表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            MarketUtils.launchAppDetail(this, "com.tencent.mobileqq", null);
			return false;
        }
    }
}
