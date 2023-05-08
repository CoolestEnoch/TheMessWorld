package foss.coolest.fucker.xposed.hooks

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.view.View
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import com.github.kyuubiran.ezxhelper.utils.findConstructor
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookReplace
import com.github.kyuubiran.ezxhelper.utils.isNotStatic
import com.github.kyuubiran.ezxhelper.utils.paramCount
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import foss.coolest.fucker.xposed.hooks.helper.getCurrentContext


const val neverGonnaGiveYouUp =
    "https://vdse.bdstatic.com//192d9a98d782d9c74c96f09db9378d93.mp4?authorization=bce-auth-v1/40f207e648424f47b2e3dfbb1014b1a5/2021-07-12T02:14:24Z/-1/host/530146520a1c89fb727fbbdb8a0e0c98ec69955459aed4b1c8e00839187536c9"

fun hookPangGuaiShengHuo(lpparam: XC_LoadPackage.LoadPackageParam) {
    // 开屏广告
    findMethod("com.qiekj.user.ui.activity.SplashActivity") {
        name == "initView" && isNotStatic
    }.hookReplace {
//        Toast.makeText(getCurrentContext(), "fuck you", Toast.LENGTH_SHORT).show()
        getCurrentContext().startActivity(Intent().apply {
            action = "com.qiekj.user"
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            component =
                ComponentName("com.qiekj.user", "com.qiekj.user.MainActivity")
        })
    }

    // 首页去更新: 将BaseDialog的setCancelable置空
    findMethod("com.hjq.base.BaseDialog\$Builder") {
        name == "setCancelable" && isNotStatic
    }.hookBefore {
        XposedHelpers.setBooleanField(it.thisObject, "mCancelable", true)
//        Toast.makeText(getCurrentContext(), "fuck update", Toast.LENGTH_SHORT).show()
        it.result = it.thisObject
    }
    findMethod("com.hjq.base.BaseDialog\$Builder") {
        name == "setCanceledOnTouchOutside" && isNotStatic
    }.hookBefore {
        XposedHelpers.setBooleanField(it.thisObject, "mCanceledOnTouchOutside", true)
//        Toast.makeText(getCurrentContext(), "fuck update2", Toast.LENGTH_SHORT).show()
        it.result = it.thisObject
    }
    findMethod("com.hjq.base.BaseDialog\$Builder") {
        name == "show" && isNotStatic
    }.hookBefore {
        XposedHelpers.setBooleanField(it.thisObject, "mCancelable", true)
        XposedHelpers.setBooleanField(it.thisObject, "mCanceledOnTouchOutside", true)
        val mClass = it.thisObject.getObjectAs<Activity>("mActivity", Activity::class.java)
        if (mClass.javaClass.simpleName == "MainActivity") {
            //it.result = null
        }
    }

    // 首页去更新：修改下载链接
    findMethod("com.qiekj.user.entity.my.VersionBean") {
        name == "getUpdateUrl" && isNotStatic && returnType == String::class.java
    }.hookBefore {
        // never gonna give you up
        it.result = neverGonnaGiveYouUp
    }

    // 首页去更新：修改下载逻辑
    findMethod("com.qiekj.user.MainActivity") {
        name == "downloadApp" && isNotStatic
    }.hookReplace {

        val tvProgress = it.thisObject.getObjectAs<TextView>("tvProgress", TextView::class.java)
        Toast.makeText(getCurrentContext(), "fuck downloadApp()", Toast.LENGTH_SHORT).show()
        val videoview = VideoView(tvProgress.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setVideoPath(neverGonnaGiveYouUp)
            setMediaController(MediaController(tvProgress.context))
        }
        AlertDialog.Builder(tvProgress.context).apply {
            setView(videoview)
        }.show()
        videoview.start()

        /*getCurrentContext().startActivity(Intent().apply {
                action = "android.intent.action.VIEW"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                // never gonna give you up
                data = Uri.parse(it.args[0].toString())
            })*/
    }

    // 改更新日志的布局
    val setContentViewMethod = findMethod("com.hjq.base.BaseDialog\$Builder") {
        name == "setContentView" && parameterTypes.contentEquals(arrayOf(View::class.java))
    }
    /*    findMethod("com.hjq.base.BaseDialog\$Builder"){
            name == "setContentView" && parameterTypes.contentEquals(arrayOf(Int::class.java))
        }.hookBefore {
            val mVideoView = VideoView(getCurrentContext()).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                setVideoPath(neverGonnaGiveYouUp)
                setMediaController(MediaController(getCurrentContext()))
            }
    //        XposedHelpers.callMethod(it.thisObject, "setContentView", arrayOf(Int::class.java), mVideoView)
            it.result = setContentViewMethod.invoke(it.thisObject, mVideoView)
        }*/
    findMethod("com.hjq.base.BaseDialog\$Builder") {
        name == "setContentView" && isNotStatic && parameterTypes.contentEquals(arrayOf(View::class.java))
    }.hookBefore {
//        Toast.makeText(getCurrentContext(), "fuck video view", Toast.LENGTH_SHORT).show()
        val videoview = VideoView(getCurrentContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setVideoPath(neverGonnaGiveYouUp)
            setMediaController(MediaController(getCurrentContext()))
        }
//        it.args[0] = videoview as View
//        videoview.start()
    }

    // 改更新日志-构造方法
    findConstructor("com.qiekj.user.entity.my.VersionBean") {
        paramCount == 10
    }.hookBefore {
        val sb = StringBuilder()
        sb.append("点击返回键可关闭此弹窗\n许光磊你古来啊，我在杭州给你吃豆栗子\n\n")
        sb.append(it.args[7])
//        Toast.makeText(getCurrentContext(), sb.toString(), Toast.LENGTH_SHORT).show()
        it.args[7] = sb.toString()
    }

    // 首页去更新：修改版本更新日志-gets
    /*    findMethod("com.qiekj.user.entity.my.VersionBean") {
            name == "getUpdateLog" && isNotStatic && returnType == String::class.java
        }.hookBefore {
            it.result = "wdnmd"
        }*/
}