package com.jiek.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends Activity {

    private JkLinearLayout mLinearLayout, mLinearLayout_shadow;//不折叠与折叠的LinearLayout

    int mTextSize = 12;//TextView的文字大小
    int[] mTextViewPaddings = new int[]{15, 10, 15, 10};//TextView的padding

    private View btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btn = findViewById(R.id.tv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof TextView) {
                    if ("折叠".equals(((TextView) v).getText().toString())) {
                        mLinearLayout_shadow.setCollapseFlag(false);
                        ((TextView) v).setText("all");
                    } else {
                        mLinearLayout_shadow.setCollapseFlag(true);
                        ((TextView) v).setText("折叠");
                    }
                }
            }
        });

        mLinearLayout_shadow = (JkLinearLayout) findViewById(R.id.linearlayout_shadow);
        mLinearLayout_shadow.setShowMaxLines(3);//设置折叠时显示行数
        mLinearLayout_shadow.setYllyMargin(15, 15);//设置间距

        fillView(mLinearLayout_shadow);
        mLinearLayout_shadow.setCollapseFlag(true);//默认折叠状态
    }

    private void fillView(LinearLayout layout) {
        String tmp = "但是可以从onDraw()方法或者dispatchDraw()方法";
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            TextView textView = new TextView(MainActivity.this);
            textView.setBackgroundResource(R.drawable.ylly_label_bg);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(new LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            textView.setPadding(mTextViewPaddings[0], mTextViewPaddings[1], mTextViewPaddings[2], mTextViewPaddings[3]);
            textView.setTextSize(mTextSize);
//            textView.setId(textView.hashCode());
            textView.setText(tmp.substring(0, random.nextInt(tmp.length() - 1) + 1));
            layout.addView(textView);
        }
    }

}
