package com.example.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var searchIcon : ImageView
    private lateinit var searchEditText : EditText
    private lateinit var searchResultRecyclerView: RecyclerView
    private lateinit var searchResultAdapter : SearchResultRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchResultRecyclerView = binding.recyclerViewResult
        searchIcon = binding.imgSearch
        searchEditText = binding.edtSearch

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        searchIcon.setOnClickListener {
            val searchFor = searchEditText.text.toString()

            if(searchFor.isNotEmpty()) {
                val retrofitData = retrofitBuilder.getData(searchFor)

                retrofitData.enqueue(object : Callback<MusicSearch>{
                    override fun onResponse(
                        call: Call<MusicSearch>,
                        response: Response<MusicSearch>
                    ) {
                        if(response.isSuccessful) {
                            val searchResponse = response.body()
                            val musicSearchList = searchResponse?.data!!

                            searchResultAdapter = SearchResultRecyclerAdapter(this@MainActivity, musicSearchList)
                            searchResultRecyclerView.adapter = searchResultAdapter
                            searchResultRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                        } else {
                            Toast.makeText(this@MainActivity, "No Result!", Toast.LENGTH_SHORT).show()
                            println("Unsuccessful response : ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<MusicSearch>, t: Throwable) {
                    }

                })
            } else {
                Toast.makeText(this, "Search field can't be empty..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}