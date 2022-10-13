package com.complete.newsreporter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import coil.load
import com.complete.newsreporter.R
import com.complete.newsreporter.databinding.FragmentTvBinding
import com.complete.newsreporter.utils.Constants.AAJ_TAK
import com.complete.newsreporter.utils.Constants.ABP_NEWS
import com.complete.newsreporter.utils.Constants.CNN
import com.complete.newsreporter.utils.Constants.GNT_NEWS
import com.complete.newsreporter.utils.Constants.INDIA_TODAY_NEWS
import com.complete.newsreporter.utils.Constants.NDTV_NEWS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvFragment : Fragment(R.layout.fragment_tv) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTvBinding.inflate(inflater)
        val listOfChannelName =
            listOf("ABP News", "NDTV News", "India Today News", "Ajj Tak News", "GNT News", "CNN")

        binding.tv1.text = listOfChannelName[0]
        binding.iv1.load(R.drawable.abp)
        binding.tv2.text = listOfChannelName[1]
        binding.iv2.load(R.drawable.ndtv)
        binding.tv3.text = listOfChannelName[2]
        binding.iv3.load(R.drawable.india_today)
        binding.tv4.text = listOfChannelName[3]
        binding.iv4.load(R.drawable.aaj_tak)
        binding.tv5.text = listOfChannelName[4]
        binding.iv5.load(R.drawable.gnt)
        binding.tv6.text = listOfChannelName[5]
        binding.iv6.load(R.drawable.download)
        binding.cv1.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(TvFragmentDirections.actionTvFragmentToWatchTvFragment(ABP_NEWS))
        }
        binding.cv2.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(TvFragmentDirections.actionTvFragmentToWatchTvFragment(NDTV_NEWS))
        }
        binding.cv3.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(TvFragmentDirections.actionTvFragmentToWatchTvFragment(INDIA_TODAY_NEWS))
        }
        binding.cv4.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(TvFragmentDirections.actionTvFragmentToWatchTvFragment(AAJ_TAK))
        }
        binding.cv5.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(TvFragmentDirections.actionTvFragmentToWatchTvFragment(GNT_NEWS))
        }
        binding.cv6.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(TvFragmentDirections.actionTvFragmentToWatchTvFragment(CNN))
        }
        return binding.root
    }
}