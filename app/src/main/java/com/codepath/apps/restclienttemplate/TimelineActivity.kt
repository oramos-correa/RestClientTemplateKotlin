package com.codepath.apps.restclienttemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

class TimelineActivity : AppCompatActivity() {

    //Declares an instance of the TwitterClient class
    lateinit var client: TwitterClient

    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter

    val tweets = ArrayList<Tweet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        //Next 4 lines initialize adapter view
        client = TwitterApplication.getRestClient(this)

        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)

        rvTweets.layoutManager = LinearLayoutManager(this)
        rvTweets.adapter = adapter

        populateHomeTimeline()
    }

    fun populateHomeTimeline(){
        client.getHomeTimeLine(object : JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "onSuccess")

                val jsonArray = json.jsonArray
                try {
                    val listTweets = Tweet.fromJsonArray(jsonArray)
                    tweets.addAll(listTweets)
                    adapter.notifyDataSetChanged()
                } catch (e: JSONException){
                    Log.e(TAG, "JSON Exception $e")
                }
            }

        })

    }
    companion object{
        val TAG = "TimeLineActivity"
    }



}