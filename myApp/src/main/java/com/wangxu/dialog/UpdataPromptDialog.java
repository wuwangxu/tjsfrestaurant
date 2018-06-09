package com.wangxu.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class UpdataPromptDialog{
	public UpdataPromptDialog(final Context context) {
		AlertDialog dialog=new AlertDialog.Builder(context).setTitle("数据需要更新").setMessage("请您更新菜单数据")
				.setPositiveButton("更新", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//点击确认更新。则跳出更新dialog
						new UpdataDataDialog(context);
						dialog.dismiss();
					}
				})
				.create();
		dialog.show();
		
	}
}
