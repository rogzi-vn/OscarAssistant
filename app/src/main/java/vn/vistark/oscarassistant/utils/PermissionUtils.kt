package vn.vistark.oscarassistant.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionUtils {
    companion object {
        val requestCode = 3234

        fun check(context: Context, permissons: Array<String>): Boolean {
            var sum = 0
            for (permission in permissons) {
                sum += ContextCompat.checkSelfPermission(context, permission)
            }
            return sum == 0
        }

        fun request(context: Activity, permissons: Array<String>) {
            ActivityCompat.requestPermissions(context, permissons, requestCode)
        }

        fun checkAndRequest(context: Activity, permissons: Array<String>) {
            if (!check(context, permissons)) {
                request(context, permissons)
            }
        }

        fun onResult(
            requestCode: Int,
            grantResults: IntArray
        ): Boolean {
            if (requestCode == PermissionUtils.requestCode) {
                var sum = 0
                for (grantResult in grantResults) {
                    sum += grantResult
                }
                return sum == 0
            } else {
                return false
            }
        }
    }
}