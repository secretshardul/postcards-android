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

class CodeFragment : Fragment() {

    private val viewModel: CodeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_code, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiKeyView: TextView = view.findViewById(R.id.api_key)

        this.activity?.let { activity ->
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            val savedDocumentId = sharedPref.getString(MainActivity.DOCUMENT_ID_KEY, null)

            apiKeyView.text = savedDocumentId
        }

    }

}
