package com.complete.newsreporter.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.complete.newsreporter.R
import com.complete.newsreporter.adapter.NewsAdapter
import com.complete.newsreporter.ui.activities.NewsActivity
import com.complete.newsreporter.ui.viewmodels.NewsViewModel
import com.complete.newsreporter.utils.Constants.REGION
import com.complete.newsreporter.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_breaking_news.*

@AndroidEntryPoint
class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private var position: Int = 0
    private var isScrolling: Boolean = false
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter : NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViews()
        str.setOnRefreshListener {
            setViews()
            str.isRefreshing = false
        }
    }
    private fun setViews(){
        viewModel = (activity as NewsActivity).newsViewModel
        setUpRecyclerView()
        viewModel.getBreakingNews(REGION)
        viewModel.breakingNews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.setList(newsResponse.articles?: listOf())
                        Log.d("taget",newsResponse.totalResults.toString())
                    }
                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.data?.let {
                        Toast.makeText(requireContext(), "An Error occured $it", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }
        }
    }
    private fun hideProgressBar(){
        avi.visibility = View.GONE
    }
    private fun showProgressBar(){
        avi.visibility = View.VISIBLE
    }
    private val onScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val scrolledOutItems = layoutManager.findFirstVisibleItemPosition()
            val currentItems = layoutManager.childCount
            val totalItems = layoutManager.itemCount

            if((currentItems + scrolledOutItems == totalItems)){
               Handler().postDelayed({
                   viewModel.breakingNewsPage++
                   viewModel.getBreakingNews(REGION)
                   isScrolling = false
                   position = scrolledOutItems
               },1000)
            }
        }
    }
    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter(requireActivity())
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(onScroll)
            scrollToPosition(position)
        }

        newsAdapter.setOnClickListener {article->
            val bundle = Bundle().apply {
                putSerializable("article",article)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )

        }
    }
}