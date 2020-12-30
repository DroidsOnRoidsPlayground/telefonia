package pl.droidsonroids.phonetest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status

/**
 * BroadcastReceiver to wait for SMS messages. This can be registered either
 * in the AndroidManifest or at runtime.  Should filter Intents on
 * SmsRetriever.SMS_RETRIEVED_ACTION.
 */
class SMSBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action == SmsRetriever.SMS_RETRIEVED_ACTION) {
            val extras = intent.extras
            val status: Status? = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status?.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    val code = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    Log.e("verification", "code: $code")
                }
                CommonStatusCodes.TIMEOUT -> {
                    Log.e("verification", "timeout")
                }
            }
        }
    }
}

// hash generation
// echo -n pl.droidsonroids.phonetest $(keytool -exportcert -alias androiddebugkey -keystore debug.keystore -storepass android | xxd -p | tr -d "[:space:]")| sha256sum | tr -d "[:space:]-" | xxd -r -p | base64 | cut -c1-11