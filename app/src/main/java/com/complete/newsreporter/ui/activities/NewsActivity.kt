package com.complete.newsreporter.ui.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.*
import com.complete.newsreporter.R
import com.complete.newsreporter.databinding.ActivityNewsBinding
import com.complete.newsreporter.ui.viewmodels.NewsViewModel
import com.complete.newsreporter.utils.Constants
import com.complete.newsreporter.utils.readPos
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_news.*

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {
    private var _binding:ActivityNewsBinding? = null
    private val binding : ActivityNewsBinding get() = _binding!!

    val newsViewModel : NewsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavigationView.background = null
        bottomNavigationView.menu.get(2).isEnabled = false
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
        fab.setOnClickListener {
           newsNavHostFragment.findNavController().navigate(R.id.searchNewsFragment)
        }
        Constants.setRegion(this, this.readPos("pos"))

        /*bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.breakingNewsFragment -> newsNavHostFragment.findNavController().navigate()
            }
            true
        }*/
       /* setCurrentFragment(BreakingNewsFragment())
        binding.fab.setOnClickListener {
            setCurrentFragment(SearchNewsFragment())
        }
        bottomAppBar.bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.breakingNewsFragment -> setCurrentFragment(BreakingNewsFragment())
                R.id.sosFragment -> setCurrentFragment(SosFragment())
                R.id.tvFragment -> setCurrentFragment(TvFragment())
                R.id.savedNewsFragment -> setCurrentFragment(SavedNewsFragment())
            }
            true
        }*/
        /*val navView: BottomNavigationView = binding.bottomNavigationView

        val navController = findNavController(R.id.newsNavHostFragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.breakingNewsFragment, R.id.tvFragment, R.id.searchNewsFragment,R.id.sosFragment,R.id.savedNewsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/
    }
    private fun setCurrentFragment(fragment : Fragment) : Boolean {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.newsNavHostFragment,fragment)
            commit()
        }
        Log.d("taget","hogya change")
        return true
    }
}