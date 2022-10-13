package com.complete.newsreporter.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.complete.newsreporter.R
import com.complete.newsreporter.databinding.FragmentSosBinding
import com.complete.newsreporter.ui.activities.NewsActivity
import com.complete.newsreporter.ui.viewmodels.NewsViewModel
import com.complete.newsreporter.utils.Resources
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SosFragment : Fragment(R.layout.fragment_sos) {
    lateinit var viewModel: NewsViewModel
    var ambulance = "102"
    var police = "100"
    var fire = "101"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val b = FragmentSosBinding.inflate(inflater)
        viewModel = (activity as NewsActivity).newsViewModel
        viewModel.getNumber()
        viewModel.numberReponse.observe(viewLifecycleOwner, Observer{
            when(it){
                is Resources.Success ->{
                    it.data?.let {sosResponse ->
                        ambulance = sosResponse.data!!.ambulance!!.all!!.get(0)
                        police = sosResponse.data!!.police!!.all!!.get(0)
                        fire = sosResponse.data!!.fire!!.all!!.get(0)
                    }
                }
                is Resources.Error ->{
                   Toast.makeText(activity,"Something is wrong",Toast.LENGTH_SHORT).show()
                }
                is Resources.Loading ->{
                    Log.d("taget","Loading")
                }
            }
        })
        b.ivAmbulance.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + ambulance)
            startActivity(intent)
        }
        b.ivPolice.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + police)
            startActivity(intent)
        }
        b.ivFire.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + fire)
            startActivity(intent)
        }
        b.firell.setOnClickListener {
            findNavController().navigate(
                R.id.action_sosFragment_to_emergencyFireFragment
            )
        }
        b.medicalll.setOnClickListener {
            findNavController().navigate(
                R.id.action_sosFragment_to_emergencyHospitalFragment
            )
        }

        return b.root
    }
}