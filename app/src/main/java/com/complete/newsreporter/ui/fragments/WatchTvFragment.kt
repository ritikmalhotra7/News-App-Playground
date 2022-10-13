package com.complete.newsreporter.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.complete.newsreporter.R
import com.complete.newsreporter.databinding.FragmentWatchTvBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchTvFragment : Fragment(R.layout.fragment_watch_tv) {
    val args : WatchTvFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val b = FragmentWatchTvBinding.inflate(inflater)
        Log.d("taget",args.urlToWebsite)
        val url = args.urlToWebsite
        b.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)
            Log.d("taget","this")
        }
        return b.root
    }

}