package com.example.aishhwiki

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL



class LandingViewModel: ViewModel() {
    private val _featuredImages = MutableLiveData<List<String>>()
    val featuredImages: LiveData<List<String>> = _featuredImages

    private val _randomArticles = MutableLiveData<List<RandomArticle>>()
    val randomArticles: LiveData<List<RandomArticle>> = _randomArticles

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories



    fun fetchFeaturedImages(page:Int=1) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url= "https://commons.wikimedia.org/w/api.php?action=query&prop=imageinfo&iiprop=timestamp|user|url&generator=categorymembers&gcmtype=file&gcmtitle=Category:Featured_pictures_on_Wikimedia_Commons&format=json&utf8"

                val response = fetchApiData(url,page)
                val featuredImagesData = parseFeaturedImages(response)
                withContext(Dispatchers.Main) {
                    _featuredImages.value = featuredImagesData
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
    private fun parseFeaturedImages(response: JSONObject): List<String> {
        val featuredImages = mutableListOf<String>()


        val queryObject = response.getJSONObject("query")
        val pagesObject = queryObject.getJSONObject("pages")
        val pageIds = pagesObject.keys()
        for (pageId in pageIds) {
            val pageObject = pagesObject.getJSONObject(pageId)
            val imageInfoArray = pageObject.getJSONArray("imageinfo")
            val imageInfoObject = imageInfoArray.getJSONObject(0)
            if (imageInfoArray != null && imageInfoArray.length() > 0) {
               // val title = imageInfoObject.getString("title")
                val imageUrl = imageInfoObject.getString("url")

         //       val featuredImage = FeaturedImage(imageUrl)
                featuredImages.add(imageUrl)
            }
        }
        // Parse the response data accordingly

        return featuredImages
    }
    fun fetchCategories(page:Int=1) {
        GlobalScope.launch {
            try {
                val url="https://en.wikipedia.org/w/api.php?action=query&list=allcategories&acprefix=List%20of&formatversion=2"
                val response =fetchApiData(url,page)
                val categoriesData = parseCategories(response)
                withContext(Dispatchers.Main){
                _categories.value = categoriesData
            }
            } catch (e: IOException) {
                // Handle network or other errors
                // ...
            }
        }
    }
    private fun parseCategories(response: JSONObject): List<Category> {
        val categories = mutableListOf<Category>()
try{
        val queryObject = response.getJSONObject("query")
        val categoriesArray = queryObject.getJSONArray("allcategories")
        for (i in 0 until categoriesArray.length()) {
            val categoryObject = categoriesArray.getJSONObject(i)
            val categoryName = categoryObject.getString("name")

            val category = Category(categoryName)
            categories.add(category)
        }
} catch (e: JSONException) {
    // Handle JSON parsing error
    e.printStackTrace()
}


        return categories
    }
    fun fetchRandomArticles(page: Int = 1) {
        GlobalScope.launch {
            try {
                val url="https://en.wikipedia.org/w/api.php?format=json&action=query&generator=random&grnnamespace=0&prop=revisions|images&rvprop=content&grnlimit=10"
                val response = fetchApiData(url, page)
                val randomArticlesData = parseRandomArticles(response)
                withContext(Dispatchers.Main) {
                    _randomArticles.value = randomArticlesData
                }
            } catch (e: IOException) {
                // Handle network or other errors
                // ...
            }
        }
    }
    private fun parseRandomArticles(response: JSONObject): List<RandomArticle> {
        val randomArticles = mutableListOf<RandomArticle>()

        val queryObject = response.getJSONObject("query")
        val pagesObject = queryObject.getJSONObject("pages")
        val keys = pagesObject.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val pageObject = pagesObject.getJSONObject(key)


            val title = pageObject.getString("title")
            val revisionsArray = pageObject.optJSONArray("revisions")
            val content = revisionsArray?.optJSONObject(0)?.optString("content", "") ?: ""
            val thumbnailObject = pageObject.optJSONObject("thumbnail")
            val imageUrl = thumbnailObject?.optString("source") ?: ""

            val randomArticle = RandomArticle(title, content, imageUrl)
            randomArticles.add(randomArticle)
            // Parse the response data accordingly
        }
        return randomArticles
    }

    @Throws(IOException::class)
    private fun makeApiRequest(url: String): String {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""
    }


    private suspend fun fetchApiData(url: String, page: Int): JSONObject {
        val modifiedUrl = if (page > 1) {
            "$url&continue=${getPageContinueValue(page)}"
        } else {
            url
        }
        return withContext(Dispatchers.IO) {
            val jsonString = URL(modifiedUrl).readText()
            JSONObject(jsonString)
        }
    }



    private fun getPageContinueValue(page: Int): String {
        val continueValue = (page - 1) * 10 // Assuming 10 items per page
        return "grncontinue||gcmcontinue||accontinue|$continueValue"
    }

}