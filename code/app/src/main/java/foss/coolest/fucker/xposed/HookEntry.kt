package foss.coolest.fucker.xposed

import com.github.kyuubiran.ezxhelper.BuildConfig
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage
import foss.coolest.fucker.xposed.hooks.hookPangGuaiShengHuo

class HookEntry : IXposedHookLoadPackage, IXposedHookZygoteInit  {

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        EzXHelperInit.initZygote(startupParam)
    }

    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        /*
        lpparam: XC_LoadPackage.LoadPackageParam
         */
        val packageName: String = lpparam.packageName
        EzXHelperInit.initHandleLoadPackage(lpparam)

        //模块激活状态
        if (lpparam.packageName == "foss.coolest.fucker") {
            findMethod("xposed.fucker.ui.MainActivity") {
                name == "isActive" && returnType == Boolean::class.java
            }.hookBefore {

                it.result = true
            }
            /*XposedBridge.log("                                                   QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQf")
            XposedBridge.log("                                                   QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQf")
            XposedBridge.log("                    aa qap                         QQQQQQQQQQQQQQQQ??4QQQP????QQQQ??4QQQQQQf")
            XposedBridge.log("                   yQP QQf       qQQQQQQQQQQQQQQp  QQQQf      QQQQQ  ]QQ6       Qf  ]QQQQQQf")
            XposedBridge.log("                 qyQQ yQQQQQQQQ6       aQQ”        QQQQP?QQf           QQQQ?”  J       4QQQf")
            XposedBridge.log("                yQQQQQQP ]QQ QQ”     qyQQQ6Q6a     QQQQp  )” qP?4QQ  ]QQ?   qp  )”  ]f  QQQf")
            XposedBridge.log("               )P?]QQ qQQ]QQQQa    ayQQ?QQf?4QQ6p  QQQQQQa   )6  )W  ]QQp )???? qf  Qf  QQQf")
            XposedBridge.log("                  ]QfqQQP]QQ)WQ6 yQQQ?  QQf  )4QP  QQQQQQP     /  y  ]QQQaap  aay”  Qf qQQQf")
            XposedBridge.log("                  ]QfQQP ]QQ )WQ )?”    QQf        QQQQQ?  qQaayQQQ  ]QQQ??”  ? )  yQ” ]QQQf")
            XposedBridge.log("                  yQf qQQQQf            QQf        QQQP”  yQQQWP     yQQp   qaap       yQQQf")
            XposedBridge.log("                  )?”  ????             )?         QQQQaaQQQQQQ6aaaaQQQQQQQQQQW6ayQaaaQQQQQf")
            XposedBridge.log("                                                   QQQQQQQQQQQQQQQWQQQQQQQQQQQQQQQQQWQQQQQQf")
            XposedBridge.log("                                                   QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQf")*/
        }

        // 胖乖生活
        if("com.qiekj.user" == packageName){
            hookPangGuaiShengHuo(lpparam)
        }
    }
}