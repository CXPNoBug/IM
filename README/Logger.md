# Logger插件
## 插件地址
[地址](https://github.com/orhanobut/logger)

## 基础使用
### 初始化
```
Logger.addLogAdapter(new AndroidLogAdapter());
```

### 全局配置
```
FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
  .showThreadInfo(false)  // (可选) 是否显示线程信息。默认值true 
  .methodCount(0)         // (可选) 要显示的方法行数。默认值2 
  .methodOffset(7)        // (可选) 隐藏内部方法调用，直到offset为止。默认值5 
  .logStrategy(customLog) // (可选) 更改日志策略以打印出来。默认LogCat 
  .tag("My custom tag")   // (可选) 每个日志的全局标记。默认PRETTY_LOGGER.build 
  .build();

Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
```

### 使用
```
//常量
Logger.d("debug");
Logger.e("error");
Logger.w("warning");
Logger.v("verbose");
Logger.i("information");
Logger.wtf("What a Terrible Failure");

//占位符
Logger.d("hello %s", "world");

//集合
Logger.d(MAP);
Logger.d(SET);
Logger.d(LIST);
Logger.d(ARRAY);

//json和xml
Logger.json(JSON_CONTENT);
Logger.xml(XML_CONTENT);
```

### 显示隐藏日志
//日志适配器通过检查此功能来检查是否应打印日志。如果要禁用/隐藏日志以进行输出，请重写isLoggable方法。 true将打印日志消息，false将其忽略。

```
Logger.addLogAdapter(new AndroidLogAdapter() {
  @Override public boolean isLoggable(int priority, String tag) {
    return BuildConfig.DEBUG;
  }
});
```
