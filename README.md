# Kotlin-lite-lib
Android依赖库，Android-library升级Kotlin版。

## 使用说明

### 两种方式引入
#### 一、通过module引入
1、依次点击File–>new–>Import Module

2、在弹出窗口中选中Kotlin-lite-lib的lite文件夹，并点击OK

3、双击导入Module文件夹下的build.gradle文件，改为和主项目一致

4、最后点击Sync同步一下就可以用了

#### 二、通过aar引入
1、在APP的build.gradle中添加如下代码块：
```gradle
dependencies {
    //基础依赖库
    implementation files('libs/lite-release.aar')
}
```
2、最后点击Sync同步一下就可以用了，例如要跳转页面，一行代码解决：
#### 在Activity中跳转
```Kotlin
  navigatePageTo<MainActivity>()
```
#### 在其他非Activity中跳转
```Kotlin
  requireContext().navigatePageTo<TestActivity>()
```
又或者要使用对话框
```Kotlin
AlertControlDialog.Builder()
  .setContext(requireContext())
  .setTitle("退出登录")
  .setMessage("确定要退出吗？")
  .setNegativeButton("取消")
  .setPositiveButton("确定")
  .setOnDialogButtonClickListener(object : AlertControlDialog.OnDialogButtonClickListener {
    override fun onConfirmClick() {
      loginViewModel.out()
    }

    override fun onCancelClick() {
    
      }
    }).build().show()
```
其他的就不一一列举了，有用得到的可以自己试试~~~

详细功能架构图如下所示：
![框架功能图](https://github.com/AndroidCoderPeng/Kotlin-lite-lib/blob/main/app/src/main/assets/Android-library-Kotlin.png)
