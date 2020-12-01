# VideoView
## 简介
- VideoView是使用MediaPlayer来对视频文件进行控制的。
- VideoView只支持mp4、avi、3gp格式的视频，支持格式相对单一，VideoView支持的格式可以参考MediaPlayer。
- VideoView可以播放网络视频，支持的网络视频的协议为：Http协议和RTSP协议两种。

## 常用方法
- `setVideoPath`：设置要播放的视频文件的位置
- `start`：开始或继续播放视频
- `pause`：暂停播放视频
- `resume`：将视频从头开始播放
- `seekTo`：从指定的位置开始播放视频
- `isPlaying`：判断当前是否正在播放视频
- `getCurrentPosition`：获取当前播放的位置
- `getDuration`：获取载入的视频文件的时长
- `setVideoPath(String path)`：以文件路径的方式设置VideoView播放的视频源
- `setVideoURI(Uri uri)`：以Uri的方式设置视频源，可以是网络Uri或本地Uri
- `setMediaController(MediaController controller)`：设置MediaController控制器
- `setOnCompletionListener(MediaPlayer.onCompletionListener l)`：监听播放完成的事件
- `setOnErrorListener(MediaPlayer.OnErrorListener l)`：监听播放发生错误时候的事件
- `setOnPreparedListener(MediaPlayer.OnPreparedListener l)`：监听视频装载完成的事件

## Media Controller(控制栏)
> Media Controller类则为我们提供了一个悬浮的操作栏，包含了播放，暂停，快进，快退，上一个，下一个等功能键。默认情况下，Media Controller悬浮显示3s后隐藏，触摸响应的VideoView呼出。默认上一个，下一个按钮隐藏。

- `boolean isShowing`：当前悬浮控制栏是否显示
- `setMediaPlayer(MediaController.MediaPlayerControl player)`：设置控制的组件
- `setPrevNextListeners(View.OnClickListener next,View.OnClickListener prev)`：设置上一个视频、下一个视频的切换事件。
## 权限
```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> 
```