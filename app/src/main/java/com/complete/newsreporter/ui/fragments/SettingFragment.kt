package com.complete.newsreporter.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.complete.newsreporter.R
import com.complete.newsreporter.databinding.FragmentSettingBinding
import com.complete.newsreporter.ui.activities.LoginActivity
import com.complete.newsreporter.ui.activities.NewsActivity
import com.complete.newsreporter.ui.viewmodels.NewsViewModel
import com.complete.newsreporter.utils.Constants.firebaseAuth
import com.complete.newsreporter.utils.Constants.setRegion
import com.complete.newsreporter.utils.readPos
import com.royrodriguez.transitionbutton.TransitionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val b = FragmentSettingBinding.inflate(inflater)
        viewModel = (activity as NewsActivity).newsViewModel

        b.switchNotification.setOnCheckedChangeListener(object:CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                val enabled = "enabled"
                val disabled = "diabled"
                if(isChecked){
                    Toast.makeText(requireContext(),"Your notification will be "+enabled ,Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(),"Your notification will be "+disabled ,Toast.LENGTH_SHORT).show()
                }
            }
        })
        b.ll4.setOnClickListener {
            Navigation.findNavController(b.root).navigate(R.id.action_settingFragment_to_savedNewsFragment)
        }
        b.backbutton.setOnClickListener {
            Navigation.findNavController(b.root).navigate(R.id.action_settingFragment_to_breakingNewsFragment)
        }
        b.profile.setOnClickListener {
            Navigation.findNavController(b.root).navigate(R.id.action_settingFragment_to_profileFragment)
        }
        b.regionSpinner.setSelection(requireActivity().readPos("pos"))
        b.regionSpinner.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.breakingNewsResponse = null
                viewModel.searchNewsResponse = null
                setRegion(requireActivity(), position)
                viewModel.breakingNewsPage = 1
                viewModel.searchedPageNumber = 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        b.logoutButton.setOnClickListener {
            b.logoutButton.startAnimation()
            b.logoutButton.stopAnimation(
                TransitionButton.StopAnimationStyle.EXPAND,
                TransitionButton.OnAnimationStopEndListener {
                    firebaseAuth.signOut()
                    val intent = Intent(activity, LoginActivity::class.java)
                    activity?.finish()
                    startActivity(intent)

                })
        }
        return b.root
    }
}