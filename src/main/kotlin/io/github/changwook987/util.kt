package io.github.changwook987

import com.github.hanshsieh.pixivj.api.PixivApiClient
import com.github.hanshsieh.pixivj.model.FilterType
import com.github.hanshsieh.pixivj.model.Illustration
import com.github.hanshsieh.pixivj.model.SearchTarget
import com.github.hanshsieh.pixivj.model.SearchedIllustsFilter
import kotlinx.coroutines.suspendCancellableCoroutine
import net.dv8tion.jda.api.utils.FileUpload
import kotlin.coroutines.resume

fun PixivApiClient.search(query: String): List<Illustration> {
    val filter = SearchedIllustsFilter()

    filter.apply {
        setFilter(FilterType.FOR_ANDROID)
        includeTranslatedTagResults = true
        mergePlainKeywordResults = true
        offset = 0
        searchTarget = SearchTarget.PARTIAL_MATCH_FOR_TAGS
        word = query
    }

    return searchIllusts(filter).illusts
}

suspend fun PixivApiClient.download(illustration: Illustration): FileUpload = suspendCancellableCoroutine {
    val url = illustration.imageUrls.large
    val filename = "${illustration.id}.${url.split('.').last()}"
    it.resume(FileUpload.fromData(download(url).body?.bytes() ?: ByteArray(0), filename))
}