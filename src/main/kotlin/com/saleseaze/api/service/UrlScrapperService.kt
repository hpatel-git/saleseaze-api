package com.saleseaze.api.service

import com.github.siyoon210.ogparser4j.OgParser
import org.springframework.stereotype.Service

@Service
class UrlScrapperService {

    fun scrapeUrl(url: String): MutableMap<String, String> {
        val scrappedDetails = mutableMapOf<String, String>()
        val ogParser = OgParser()
        try {
            val openGraph = ogParser.getOpenGraphOf(url)
            if (openGraph.allProperties.isNotEmpty()) {
                openGraph.allProperties.forEach { property ->
                    scrappedDetails[property] = openGraph.getContentOf(property)
                        .value
                }
            }
        } catch (e: Exception) {

        }
        return scrappedDetails
    }
}