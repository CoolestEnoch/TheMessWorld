package foss.coolest.fucker.xposed.hooks.helper

import android.app.AndroidAppHelper
import android.content.Context

fun getCurrentContext() = AndroidAppHelper.currentApplication().createPackageContext(
    AndroidAppHelper.currentPackageName(),
    Context.CONTEXT_IGNORE_SECURITY
)