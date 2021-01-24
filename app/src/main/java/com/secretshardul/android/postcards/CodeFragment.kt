package com.secretshardul.android.postcards

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import io.github.kbiakov.codeview.CodeView
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme

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
        codeView.setOptions(
            Options.Default.get(view.context)
                .withLanguage("sh")
        )

        this.activity?.let { activity ->
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            val savedDocumentId = sharedPref.getString(MainActivity.DOCUMENT_ID_KEY, null)

            apiKeyView.text = savedDocumentId
            val curlCode = "curl -G -X  POST 'https://postcards-api-notifications.web.app/api'\\\n" +
                    "\t--data-urlencode 'key=${savedDocumentId}'\\\n" +
                    "\t--data-urlencode 'title=hello world'\\\n" +
                    "\t--data-urlencode 'body=hello frompostcards'\\\n" +
                    "\t--data-urlencode 'imageUrl=https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/260px-Cat_poster_1.jpg' # optional"
            codeView.setCode(curlCode)
        }

    }

}
