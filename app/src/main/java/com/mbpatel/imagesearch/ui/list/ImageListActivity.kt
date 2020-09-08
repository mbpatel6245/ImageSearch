package com.mbpatel.imagesearch.ui.list

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.mbpatel.imagesearch.Injection
import com.mbpatel.imagesearch.R
import com.mbpatel.imagesearch.databinding.ActivityMainBinding
import com.mbpatel.imagesearch.utils.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Activity manages the search list
 */
class ImageListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SearchImagesViewModel
    private val adapter = ImageListAdapter()

    private var searchJob: Job? = null

    /**
     * perform the search query
     * @param query string query
     */
    private fun search(query: String) {
        if (!isInternetAvailable(this)) showToast(
            this,
            getString(R.string.error_internet_connectivity)
        )
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchImages(query).collect {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.btnRetrySearchList.setOnClickListener { adapter.retry() }
        // get the view model
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory())
            .get(SearchImagesViewModel::class.java)
        Picasso.setSingletonInstance(getCustomPicasso(this))
        initRecyclerList()

        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
        initSearch(query)
    }

    /**
     * set recyclerview properties
     */
    private fun initRecyclerList() {
        val spacing = resources.getDimensionPixelSize(R.dimen.recycler_spacing) / 2
        binding.rvSearchList.setPadding(spacing, spacing, spacing, spacing)
        binding.rvSearchList.layoutManager = GridLayoutManager(this, 4)
        binding.rvSearchList.setHasFixedSize(true)
        binding.rvSearchList.setItemViewCacheSize(20)

        binding.rvSearchList.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(spacing, spacing, spacing, spacing)
            }
        })
        binding.rvSearchList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
                    hideKeyboard(binding.edtSearchImage)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.edtSearchImage.text.trim().toString())
    }

    /**
     * Initialize the adapter for list
     */
    private fun initAdapter() {
        binding.rvSearchList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter { adapter.retry() },
            footer = PagingLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.rvSearchList.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.pbSearchList.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.btnRetrySearchList.isVisible = loadState.source.refresh is LoadState.Error
            binding.tvMessage.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
                ?: loadState.refresh as?  LoadState.Error
            errorState?.let {
                Log.e("Error",it.error.toString())
                binding.tvMessage.text = it.error.message.toString()
                setError(this@ImageListActivity,it)
            }
        }
    }

    /**
     * Initialize search field
     * @param query string value
     */
    private fun initSearch(query: String) {
        binding.edtSearchImage.setText(query)

        binding.edtSearchImage.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateImageListFromInput()
                true
            } else {
                false
            }
        }
        binding.edtSearchImage.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateImageListFromInput()
                true
            } else {
                false
            }
        }
        lifecycleScope.launch {
            @OptIn(ExperimentalPagingApi::class)
            adapter.dataRefreshFlow.collect {
                binding.rvSearchList.scrollToPosition(0)
            }
        }
    }

    /**
     * Used for update query to update list
     */
    private fun updateImageListFromInput() {
        binding.edtSearchImage.text.trim().let {
            if (it.isNotEmpty()) {
                binding.rvSearchList.scrollToPosition(0)
                search(it.toString())
            }
        }
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Cat"
    }
}