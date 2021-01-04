package com.dhairytripathi.markettoday.ui.news

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dhairytripathi.markettoday.data.repository.NewsRepository
import com.dhairytripathi.markettoday.data.storage.entity.NewsArticleDb
import com.dhairytripathi.markettoday.ui.ViewState

private const val TAG = "NewsViewModel"
class NewsViewModel @ViewModelInject constructor(
    repository: NewsRepository,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    fun logNews() {
        Log.d(TAG, "logNews: $newsArticleDb")
    }
    private val newsArticleDb: LiveData<ViewState<List<NewsArticleDb>>> = repository.getNewsArticles().asLiveData()

    /**
     * Return news articles to observeNotNull on the UI.
     */
    fun getNewsArticles(): LiveData<ViewState<List<NewsArticleDb>>> = newsArticleDb

    //TODO: Implement in app news view
    fun openArticle() {

    }
}