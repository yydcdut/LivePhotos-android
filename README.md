# RxMarkdown

[![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE.txt) [![API](https://img.shields.io/badge/API-9%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=9)  [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-RxMarkdown-green.svg?style=true)](https://android-arsenal.com/details/1/3967)

RxMarkdown is an Android library that helps to display simple markdown text in `android.widget.EditText` or `android.widget.TextView` .

It is backed by RxJava, implementing complicated APIs as handy reactive observables.

中文：[README-zh-rCN.md](./README-zh-rCN.md)

Demo apk : [DOWNLOAD](https://github.com/yydcdut/RxMarkdown/blob/master/apk/demo.apk?raw=true)

QR Code : [CLICK](http://fir.im/nh4c)

Change Log : [SEE](./CHANGELOG.md)

![RxMarkdown.gif](https://raw.githubusercontent.com/yydcdut/RxMarkdown/master/art/rxmarkdown.gif)

# Gradle

```groovy
compile 'com.yydcdut:rxmarkdown:0.0.9-beta'
```

## Support Grammar 

RxMarkdown now provides 2 factories to parse markdown,  `TextFactory` and `EditFactory` .

`TextFactory` : Supports most of the markdown grammars，but it will destroy the integrity of the content. So, it applies to render in `TextView` .

`EditFactory` : Supports some grammars，and it won't destroy the integrity of the content, the parsing speed is faster than `TextFactory` , So, it applies to real-time preview in `EditText` .

### TextFactory

- [x] Header `# ` / `## ` / `### ` / `#### ` / `##### ` / `####### `
- [x] BlockQuote `>  `
- [x] Nested BlockQuote `> >  `
- [x] Emphasis Bold `**` `__`
- [x] Emphasis Italic `*` `_`
- [x] Nested Bold && Italic
- [x] Ordered List `1. `
- [x] Nested Ordered List
- [x] UnOrdered List `* ` /  `+ ` / `- `
- [x] Nested UnOrdered List
- [x] Image `![]()`
- [x] Hyper Link `[]()`
- [x] Inline Code 
- [x] Code 
- [x] Backslash `\`
- [x] Horizontal Rules `***` / `*****` / `---` / `-----------------`
- [x] Strike Through `~~` 
- [x] Footnote `[^]`
- [x] Todo `- [ ] ` / `- [x] `
- [ ] Table `| Table | Table |`

#### Other Grammar

- [x] Center Align `[]`

### EditFactory

- [x] Header `# ` / `## ` / `### ` / `#### ` / `##### ` / `####### `
- [x] BlockQuote `>  `
- [x] Nested BlockQuote `> >  `
- [x] Emphasis Bold `**` `__`
- [x] Emphasis Italic `*` `_`
- [x] Nested Bold && Italic
- [x] Ordered List `1. `
- [x] Nested Ordered List
- [x] UnOrdered List `* ` /  `+ ` / `- `
- [x] Nested UnOrdered List
- [ ] Image `![]()`
- [ ] Hyper Link `[]()`
- [x] Inline Code 
- [x] Code 
- [ ] Backslash `\`
- [x] Horizontal Rules `***` / `*****` / `---` / `-----------------`
- [x] Strike Through `~~` 
- [ ] Footnote `[^]`
- [ ] Todo `- [ ] ` / `- [x] `
- [ ] Table `| Table | Table |`

#### Other Grammar

- [x] Center Align `[]`


### HtmlFactory

//TODO

## Quick Start

### Setup

```groovy
compile 'com.yydcdut:rxmarkdown:0.0.7'

compile 'io.reactivex:rxandroid:1.2.0'
compile 'io.reactivex:rxjava:1.1.5'
```

### Configuration

All options in Configuration builder are optional. Use only those you really want to customize.

```java
RxMDConfiguration rxMDConfiguration = new RxMDConfiguration.Builder(context)
        .setDefaultImageSize(100, 100)//default image width & height
        .setBlockQuotesColor(Color.LTGRAY)//default color of block quotes
        .setHeader1RelativeSize(1.6f)//default relative size of header1
        .setHeader2RelativeSize(1.5f)//default relative size of header2
        .setHeader3RelativeSize(1.4f)//default relative size of header3
        .setHeader4RelativeSize(1.3f)//default relative size of header4
        .setHeader5RelativeSize(1.2f)//default relative size of header5
        .setHeader6RelativeSize(1.1f)//default relative size of header6
        .setHorizontalRulesColor(Color.LTGRAY)//default color of horizontal rules's background
        .setInlineCodeBgColor(Color.LTGRAY)//default color of inline code's background
        .setCodeBgColor(Color.LTGRAY)//default color of code's background
        .setTodoColor(Color.DKGRAY)//default color of todo
        .setTodoDoneColor(Color.DKGRAY)//default color of done
        .setUnOrderListColor(Color.BLACK)//default color of unorder list
        .setLinkColor(Color.RED)//default color of link text
        .setLinkUnderline(true)//default value of whether displays link underline
        .setRxMDImageLoader(new DefaultLoader(context))//default image loader
        .setDebug(true)//default value of debug
        .setOnLinkClickCallback(new OnLinkClickCallback() {//link click callback
        	@Override
        	public void onLinkClicked(View view, String link) {
        	}
        })
        .build();
```

### Usage

* `EditText` , live preview :

  ```java
  RxMarkdown.live(rxMDEditText)
          .config(rxMDConfiguration)
          .factory(EditFactory.create())
          .intoObservable()
          .subscribe();
  ```


* cancel real-time preview :

  ```java
  rxMDEditText.clear();
  ```

* `TextView` render :

  ```java
  RxMarkdown.with(content, this)
          .config(rxMDConfiguration)
          .factory(TextFactory.create())
          .intoObservable()
          .subscribeOn(Schedulers.computation())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Subscriber<CharSequence>() {
              @Override
              public void onCompleted() {}

              @Override
              public void onError(Throwable e) {}

              @Override
              public void onNext(CharSequence charSequence) {
                  rxMDTextView.setText(charSequence, TextView.BufferType.SPANNABLE);
              }
          });
  ```

### Note

#### RxMDImageLoader

* Acceptable URIs examples 

  ```c
  "http://web.com/image.png" // from Web
  "file:///mnt/sdcard/image.png" // from SD card
  "assets://image.png" // from assets
  "drawable://" + R.drawable.img // from drawables (non-9patch images)
  ```

* Custom image loader

  ```java
  public class MDLoader implements RxMDImageLoader {
      @Nullable
      @Override
      public byte[] loadSync(@NonNull String url) throws IOException {
          return new byte[0];
      }
  }
  ```

#### Image Size

The image of 320 pixels width and 320 pixels height will display on the screen :

```markdown
![image](http://web.com/image.png/320$320)
```

# License

Copyright 2016 yydcdut

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.