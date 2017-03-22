package com.yydcdut.markdowndemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;

import com.yydcdut.markdowndemo.loader.OKLoader;
import com.yydcdut.markdowndemo.view.HorizontalEditScrollView;
import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.RxMDEditText;
import com.yydcdut.rxmarkdown.RxMDTextView;
import com.yydcdut.rxmarkdown.RxMarkdown;
import com.yydcdut.rxmarkdown.factory.EditFactory;
import com.yydcdut.rxmarkdown.factory.TextFactory;

import rx.Subscriber;

/**
 * Created by yuyidong on 16/7/23.
 */
public class CompareActivity extends AppCompatActivity implements TextWatcher {
    private RxMDEditText mRxMDEditText;
    private RxMDTextView mRxMDTextView;
    private RxMDConfiguration mRxMDConfiguration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Compare");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        HorizontalEditScrollView horizontalEditScrollView = (HorizontalEditScrollView) findViewById(R.id.scroll_edit);
        mRxMDEditText = (RxMDEditText) findViewById(R.id.edit_md);
        mRxMDEditText.addTextChangedListener(this);
        mRxMDTextView = (RxMDTextView) findViewById(R.id.txt_md_show);

        mRxMDConfiguration = new RxMDConfiguration.Builder(this)
                .setDefaultImageSize(400, 400)
                .setBlockQuotesColor(0xff33b5e5)
                .setHeader1RelativeSize(2.2f)
                .setHeader2RelativeSize(2.0f)
                .setHeader3RelativeSize(1.8f)
                .setHeader4RelativeSize(1.6f)
                .setHeader5RelativeSize(1.4f)
                .setHeader6RelativeSize(1.2f)
                .setHorizontalRulesColor(0xffaa66cc)
                .setInlineCodeBgColor(0x33CCCCCC)
                .setCodeBgColor(0x59f0f0f0)
                .setTodoColor(0xff669900)
                .setTodoDoneColor(0xffff4444)
                .setUnOrderListColor(0xffffbb33)
                .setRxMDImageLoader(new OKLoader(this))
                .setLinkUnderline(true)
                .setLinkColor(0xff00ddff)
                .build();
        horizontalEditScrollView.setEditTextAndConfig(mRxMDEditText, mRxMDConfiguration);
//        mRxMDEditText.setText(Const.MD_SAMPLE);
        mRxMDEditText.setText("");
        RxMarkdown.live(mRxMDEditText)
                .config(mRxMDConfiguration)
                .factory(EditFactory.create())
                .intoObservable()
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        RxMarkdown.with(s.toString(), this)
                .config(mRxMDConfiguration)
                .factory(TextFactory.create())
                .intoObservable()
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        mRxMDTextView.setText(charSequence, TextView.BufferType.SPANNABLE);
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
