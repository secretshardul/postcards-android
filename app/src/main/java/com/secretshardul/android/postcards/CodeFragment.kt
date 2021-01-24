package com.secretshardul.android.postcards

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Options


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
                displayCopiedTextMessage(view.context)
            }

            copyCodeButton.setOnClickListener {
                val clip = ClipData.newPlainText("postcards-curl-code", curlCode)
                clipboard?.setPrimaryClip(clip)
                displayCopiedTextMessage(view.context)
            }
        }
    }

    private fun displayCopiedTextMessage(context: Context) {
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}
