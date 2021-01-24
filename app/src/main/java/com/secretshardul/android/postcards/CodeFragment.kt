package com.secretshardul.android.postcards

import android.app.AlertDialog
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Options
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber


class CodeFragment : Fragment() {

    private val viewModel: CodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiKeyView: TextView = view.findViewById(R.id.api_key)
        val codeView: CodeView = view.findViewById(R.id.code_view)
        val copyCodeButton: Button = view.findViewById(R.id.copy_code_button)
        val copyKeyButton: Button = view.findViewById(R.id.copy_key_button)
        val postmanButton: Button = view.findViewById(R.id.postman_button)
        val emailButton: Button = view.findViewById(R.id.email_button)

        codeView.setOptions(
            Options.Default.get(view.context)
                .withLanguage("sh")
        )

        this.activity?.let { activity ->
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            val savedDocumentId = sharedPref.getString(MainActivity.DOCUMENT_ID_KEY, null)

            apiKeyView.text = savedDocumentId

            val curlCode =
                "curl -G -X  POST 'https://postcards-api-notifications.web.app/api'\\\n" +
                        "\t--data-urlencode 'key=${savedDocumentId}'\\\n" +
                        "\t--data-urlencode 'title=hello world'\\\n" +
                        "\t--data-urlencode 'body=hello frompostcards'\\\n" +
                        "\t--data-urlencode 'imageUrl=https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/260px-Cat_poster_1.jpg' # optional"
            codeView.setCode(curlCode)

            val clipboard = getSystemService(view.context, ClipboardManager::class.java)
            copyKeyButton.setOnClickListener {
                val clip = ClipData.newPlainText("postcards-api-key", savedDocumentId)
                clipboard?.setPrimaryClip(clip)
                displayToast(view.context, "Copied to clipboard")
            }

            copyCodeButton.setOnClickListener {
                val clip = ClipData.newPlainText("postcards-curl-code", curlCode)
                clipboard?.setPrimaryClip(clip)
                displayToast(view.context, "Copied to clipboard")
            }

            postmanButton.setOnClickListener {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.postman.com/cloudy-firefly-3880/workspace/postcards-app/overview")
                )
                startActivity(browserIntent)
            }

            emailButton.setOnClickListener {
                val alert = AlertDialog.Builder(it.context)

                alert.setTitle("Enter email")
                alert.setMessage("API key and instructions will be sent")
                val input = EditText(it.context)
                alert.setView(input)

                alert.setPositiveButton("Ok") { _, _ ->
                    val email = input.text.toString()
                    Timber.d("Got email $email")
                    if(savedDocumentId != null) {
                        MainScope().launch {
                            try {
                                viewModel.sendEmail(email, savedDocumentId)
                                Timber.d("Email sent")
                                displayToast(it.context, "Email sent")
                            } catch (exception: Exception) {
                                Timber.d("Failed to send mail ${exception.message}")
                                displayToast(it.context, "Failed to send email")
                            }

                        }
                    }

                }
                alert.setNegativeButton("Cancel") { _, _ ->
                }
                alert.show()
            }
        }


    }

    private fun displayToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
