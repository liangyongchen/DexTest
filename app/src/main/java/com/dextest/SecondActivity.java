package com.dextest;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import estthgapp.com.fixlib.Constants;
import estthgapp.com.fixlib.FileUtils;
import estthgapp.com.fixlib.FixDexUtils;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClacUtil clacUtil = new ClacUtil();
                clacUtil.match(SecondActivity.this);
            }
        });

        findViewById(R.id.fix).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 将修复包放到sd卡中
                File sourceFile = new File(Environment.getExternalStorageDirectory(), Constants.DEX_NAME);
                // 私有目录
                File targetFile = new File(getDir(Constants.DEX_DIR,
                        Context.MODE_PRIVATE).getAbsolutePath() + File.separator + Constants.DEX_NAME);

                // 如果存在之前修复过的dex
                if (targetFile.exists()) {
                    targetFile.delete();
                    Toast.makeText(SecondActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                }

                // 将下载的修复包 复制到私有目录，然后在解压
                try {
                    FileUtils.copyFile(sourceFile, targetFile);
                    Toast.makeText(SecondActivity.this, "复制dex文件完成", Toast.LENGTH_SHORT).show();
                    // 开始修复
                    FixDexUtils.loadFixedDex(SecondActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
