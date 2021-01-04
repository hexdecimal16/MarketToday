package com.dhairytripathi.markettoday.data.model

import com.dhairytripathi.markettoday.adapters.NewsArticlesAdapter
/**
 * Describes all the events originated from
 * [NewsArticlesAdapter].
 */
sealed class NewsAdapterEvent {

    /* Describes item click event  */
    object ClickEvent : NewsAdapterEvent()
}