package com.dhairytripathi.markettoday.data.news

import com.dhairytripathi.markettoday.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Describes endpoints to fetch the news from NewsAPI.
 *
 * Read the documentation [here](https://newsapi.org/docs/v2)
 */
interface NewsService {

    /**
     * Get top headlines.
     *
     * See [article documentation](https://newsapi.org/docs/endpoints/top-headlines).
     */
    @GET("top-headlines?apiKey=6a5ab2b58c6b4f6c9dcc0396156d2d41&category=business&country=in")
    suspend fun getTopHeadlines(): Response<NewsResponse>

}