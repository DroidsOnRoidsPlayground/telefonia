package pl.droidsonroids.phonetest

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.auth.api.credentials.*
import com.google.android.gms.auth.api.credentials.CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE
import com.google.android.gms.auth.api.credentials.CredentialsApi.CREDENTIAL_PICKER_REQUEST_CODE
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import pl.droidsonroids.phonetest.databinding.ActivityMainBinding


private const val RESOLVE_HINT = 1

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fab.setOnClickListener { view ->
            requestHint()
        }

        binding.fab1.setOnClickListener { view ->
            requestSms()
        }
    }

    private fun requestSms() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        val client = SmsRetriever.getClient(this)

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.

        val task: Task<Void> = client.startSmsRetriever()

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);

        task.addOnSuccessListener {
            // Successfully started retriever, expect broadcast intent
            // ...
        }

        task.addOnFailureListener {
            // Failed to start retriever, inspect Exception for more details
            // ...
        }
    }

    private fun requestHint() {
        val hintRequest = HintRequest.Builder()
            .setHintPickerConfig(
                CredentialPickerConfig.Builder()
                    .setShowCancelButton(true)
                    .build()
            )
            .setPhoneNumberIdentifierSupported(true)
            .build()

        val intent = Credentials.getClient(this).getHintPickerIntent(hintRequest)
        startIntentSenderForResult(
            intent.intentSender,
            CREDENTIAL_PICKER_REQUEST_CODE, null, 0, 0, 0
        )
    }

    // Obtain the phone number from the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREDENTIAL_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
                println(credential)
            } else if (resultCode == ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
                Snackbar.make(binding.root, "no hints", Snackbar.LENGTH_LONG).show()
            }
        }
    }
}

