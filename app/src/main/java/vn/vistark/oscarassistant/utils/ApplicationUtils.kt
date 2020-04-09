package vn.vistark.oscarassistant.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import androidx.core.content.ContextCompat.startActivity


class ApplicationUtils {
    companion object {
        val TAG = ApplicationUtils::class.java.simpleName

        class AppInfo {
            var packageName: String = ""
            var labelName: String = ""
        }

        fun getAllApps(context: Context): ArrayList<AppInfo> {
            val apps = ArrayList<AppInfo>()
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val activities: List<ResolveInfo> = context.packageManager
                .queryIntentActivities(mainIntent, 0)
            for (resolveInfo in activities) {
                val applications = AppInfo()
                applications.labelName = resolveInfo.loadLabel(context.packageManager)
                    .toString()
                applications.packageName = resolveInfo.activityInfo.packageName
                    .toString()
                apps.add(applications)
            }
            return apps
        }

        @SuppressLint("DefaultLocale")
        fun openApplication(context: Context, appName: String): Boolean {
            var packageName = ""

            val apps = getAllApps(context)
            // matching the package name with label name
            for (i in 0 until apps.size) {
                Log.w(
                    TAG,
                    "${apps[i].labelName.toLowerCase().trim()} AND ${appName.toLowerCase().trim()}"
                )
                if ((apps[i].labelName.toLowerCase().trim() == appName.toLowerCase().trim()) ||
                    (apps[i].labelName.toLowerCase().replace(
                        " ",
                        ""
                    ).trim() == appName.toLowerCase().replace(
                        " ",
                        ""
                    ).trim() ||
                            appName.toLowerCase().replace(" ", "").trim().contains(
                                apps[i].labelName.toLowerCase().replace(
                                    " ",
                                    ""
                                ).trim()
                            )) ||
                    appName.toLowerCase().trim().contains(apps[i].labelName.toLowerCase().trim())
                ) {
                    packageName = apps[i].packageName
                    break
                }
            }
            if (packageName.isEmpty())
                return false
            // to launch the application
            val i: Intent?
            val manager: PackageManager = context.packageManager
            i = manager.getLaunchIntentForPackage(packageName)
            if (i == null) return false
            i.addCategory(Intent.CATEGORY_LAUNCHER)
            context.startActivity(i)
            return true
        }

    }
}