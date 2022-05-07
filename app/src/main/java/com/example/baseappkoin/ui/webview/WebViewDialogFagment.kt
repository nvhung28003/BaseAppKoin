package com.example.baseappkoin.ui.webview


import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import com.example.baseappkoin.R


import com.example.baseappkoin.databinding.FragmentWebViewBinding
import com.example.baseappkoin.databinding.FragmentWebViewBinding.inflate


class WebViewDialogFragment() : DialogFragment() {
    private lateinit var binding: FragmentWebViewBinding
    private var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        link = arguments?.getString(WEB_VIEW_LINK)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        binding = inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupView() {
        binding.imageClose.setOnClickListener {
            dismiss()
        }
        if (link != null) {
            binding.webView.loadUrl(link!!)
        }
        binding.run {

            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        private const val WEB_VIEW_LINK = "WEB_VIEW_LINK"
        fun newInstance(
            link: String,
        ): WebViewDialogFragment = WebViewDialogFragment().apply {
            arguments = Bundle().apply {
                putString(WEB_VIEW_LINK, link)
            }
        }
    }
}