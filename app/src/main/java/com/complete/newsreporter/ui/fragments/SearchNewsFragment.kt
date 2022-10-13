package com.complete.newsreporter.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.complete.newsreporter.R
import com.complete.newsreporter.adapter.NewsAdapter
import com.complete.newsreporter.ui.activities.NewsActivity
import com.complete.newsreporter.ui.viewmodels.NewsViewModel
import com.complete.newsreporter.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_news.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {
    private lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter : NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).newsViewModel
        setUpRecyclerView()


        var job : Job? = null
        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(500L)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        Log.d("tagetv",it.toString())
                        viewModel.getSearchedNews(editable.toString())
                    }
                }
            }
        }
        /*searchbutton.setOnClickListener {
            showProgressBar()
            viewModel.getSearchedNews(etSearch.text.toString())
        }*/

        viewModel.searchedQuery.observe(viewLifecycleOwner, Observer{response->
            when(response){
                is Resources.Success ->{
                    hideProgressBar()
                    response.data?.let {newsResponse ->
                        newsAdapter.setList(newsResponse.articles!!.toList())
                        Log.d("tag",newsResponse.articles!!.size.toString())
                       }
                }
                is Resources.Error ->{
                    hideProgressBar()
                    Log.d("tagetv","error")
                    response.data?.let{
                        Toast.makeText(requireContext(),"Error", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resources.Loading ->{
                    showProgressBar()
                }
            }
        })

    }
    private fun hideProgressBar(){
        paginationProgressBarSearched.visibility = View.INVISIBLE
    }
    private fun showProgressBar(){
        paginationProgressBarSearched.visibility = View.VISIBLE
    }
    private fun setUpRecyclerView(){
        newsAdapter = NewsAdapter(requireContext())
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        newsAdapter.notifyDataSetChanged()
        newsAdapter.setOnClickListener {article->
            val bundle = Bundle().apply {
                putSerializable("article",article)
            }
            Log.d("taget",article.url.toString())
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

    }
}