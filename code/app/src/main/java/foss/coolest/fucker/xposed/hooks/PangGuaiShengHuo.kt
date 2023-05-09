package xposed.fucker.xposed.hooks

import android.app.Activity
import android.app.AlertDialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.view.marginRight
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.github.kyuubiran.ezxhelper.init.InitFields
import com.github.kyuubiran.ezxhelper.utils.args
import com.github.kyuubiran.ezxhelper.utils.findConstructor
import com.github.kyuubiran.ezxhelper.utils.findField
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.getObjectAs
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookReplace
import com.github.kyuubiran.ezxhelper.utils.isNotStatic
import com.github.kyuubiran.ezxhelper.utils.isProtected
import com.github.kyuubiran.ezxhelper.utils.paramCount
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import foss.coolest.fucker.utils.EatDouLieZi
import foss.coolest.fucker.utils.neverGonnaGiveYouUp
import foss.coolest.fucker.xposed.hooks.helper.getCurrentContext


fun hookPangGuaiShengHuo(lpparam: XC_LoadPackage.LoadPackageParam) {
    val isShell = try {
        findMethod("com.qiekj.user.ui.activity.SplashActivity") {
            name == "initView" && isNotStatic
        }
        false
    }catch (e:ClassNotFoundException){
        true
    }

    when(isShell){
        true -> {
            XposedBridge.log("fuck you 许光磊，加壳了")
            findMethod("com.netease.nis.wrapper.MyApplication"){
                name == "attachBaseContext" &&isProtected
            }.hookAfter {
                val classloader = (it.args[0] as Context).classLoader
                hookPangGuaiShengHuo_main(lpparam,classloader)
            }
        }
        false -> hookPangGuaiShengHuo_main(lpparam)

    }

}

fun hookPangGuaiShengHuo_main(lpparam: XC_LoadPackage.LoadPackageParam, mClassLoader: ClassLoader = InitFields.ezXClassLoader) {
    // 开屏广告
    findMethod("com.qiekj.user.ui.activity.SplashActivity", classLoader = mClassLoader) {
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
    findMethod("com.hjq.base.BaseDialog\$Builder", classLoader = mClassLoader) {
        name == "setCancelable" && isNotStatic
    }.hookBefore {
        XposedHelpers.setBooleanField(it.thisObject, "mCancelable", true)
//        Toast.makeText(getCurrentContext(), "fuck update", Toast.LENGTH_SHORT).show()
        it.result = it.thisObject
    }
    findMethod("com.hjq.base.BaseDialog\$Builder", classLoader = mClassLoader) {
        name == "setCanceledOnTouchOutside" && isNotStatic
    }.hookBefore {
        XposedHelpers.setBooleanField(it.thisObject, "mCanceledOnTouchOutside", true)
//        Toast.makeText(getCurrentContext(), "fuck update2", Toast.LENGTH_SHORT).show()
        it.result = it.thisObject
    }
    findMethod("com.hjq.base.BaseDialog\$Builder", classLoader = mClassLoader) {
        name == "show" && isNotStatic
    }.hookBefore {
        XposedHelpers.setBooleanField(it.thisObject, "mCancelable", true)
        XposedHelpers.setBooleanField(it.thisObject, "mCanceledOnTouchOutside", true)
        val mClass = it.thisObject.getObjectAs<Activity>("mActivity", Activity::class.java)
        if (mClass.javaClass.simpleName == "MainActivity") {
            var mContentView = it.thisObject.getObjectAs<View>("mContentView", View::class.java)
            val mLinearLayout = LinearLayout(mContentView.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
            }
            val videoview = VideoView(mContentView.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                setVideoPath(neverGonnaGiveYouUp)
                setMediaController(MediaController(mContentView.context))
            }
            val mImageView = ImageView(mContentView.context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                adjustViewBounds = true
                scaleType = ImageView.ScaleType.FIT_CENTER
            }

            Glide.with(mContentView.context).load(EatDouLieZi).into(mImageView)

            mLinearLayout.addView(mImageView)
            mLinearLayout.addView(videoview)
            mLinearLayout.addView(mContentView)

            XposedHelpers.setObjectField(it.thisObject,"mContentView", mLinearLayout)
            videoview.start()

            //it.result = null
        }
    }
    /*    findMethod("com.hjq.base.BaseDialog\$Builder", classLoader = mClassLoader) {
            name == "setOnClickListener" && isNotStatic
        }.hookBefore {
            val mClass = it.thisObject.getObjectAs<Activity>("mActivity", Activity::class.java)
            if (mClass.javaClass.simpleName == "MainActivity") {
                val videoview = VideoView(mClass.baseContext).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    setVideoPath(neverGonnaGiveYouUp)
                    setMediaController(MediaController(getCurrentContext()))
                }
                AlertDialog.Builder(getCurrentContext()).apply {
                    setView(videoview)
                }.show()
                videoview.start()

            }
        }*/

    // 首页去更新：修改下载链接
    findMethod("com.qiekj.user.entity.my.VersionBean", classLoader = mClassLoader) {
        name == "getUpdateUrl" && isNotStatic && returnType == String::class.java
    }.hookBefore {
        // never gonna give you up
        it.result = neverGonnaGiveYouUp
    }

    // 首页去更新：修改下载逻辑
    findMethod("com.qiekj.user.MainActivity", classLoader = mClassLoader) {
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
    val setContentViewMethod = findMethod("com.hjq.base.BaseDialog\$Builder", classLoader = mClassLoader) {
        name == "setContentView" && parameterTypes.contentEquals(arrayOf(View::class.java))
    }
    /*        findMethod("com.hjq.base.BaseDialog\$Builder"){
                name == "setContentView" && parameterTypes.contentEquals(arrayOf(Int::class.java))
            }.hookBefore {
                val mVideoView = VideoView(mContext).apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                    setVideoPath(neverGonnaGiveYouUp)
                    setMediaController(MediaController(mContext))
                }
        //        XposedHelpers.callMethod(it.thisObject, "setContentView", arrayOf(Int::class.java), mVideoView)
                it.result = setContentViewMethod.invoke(it.thisObject, mVideoView)
            }*/
    findMethod("com.hjq.base.BaseDialog\$Builder", classLoader = mClassLoader) {
        name == "setContentView" && isNotStatic && parameterTypes.contentEquals(arrayOf(View::class.java))
    }.hookBefore {
//        Toast.makeText(getCurrentContext(), "fuck video view", Toast.LENGTH_SHORT).show()
//        val mContext = (it.args[0] as View).context
//        val mContext = it.thisObject.getObjectAs<Context>("mContext",Context::class.java)
//        val videoview = VideoView(mContext).apply {
//            layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT
//            )
//            setVideoPath(neverGonnaGiveYouUp)
//            setMediaController(MediaController(mContext))
//        }
//        it.args[0] = videoview as View
//        videoview.start()
    }

    // 改更新日志-构造方法
    findConstructor("com.qiekj.user.entity.my.VersionBean", classLoader = mClassLoader) {
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