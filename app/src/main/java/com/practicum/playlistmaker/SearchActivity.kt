package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val trackList = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(trackList)

    private var searchText: String = ""
    private val itunesUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesService::class.java)

    private lateinit var msgImgView: ImageView
    private lateinit var msgTxtView: TextView
    private lateinit var updateButtonView: Button

    private lateinit var trackListView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        msgImgView = findViewById(R.id.search_msg_img)
        msgTxtView = findViewById(R.id.search_msg_text)
        updateButtonView = findViewById(R.id.search_update_button)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        trackListView = findViewById(R.id.track_list)


        trackListView.adapter = trackAdapter

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val searchButton = findViewById<ImageView>(R.id.search_edit_search_button)
        val clearButton = findViewById<ImageView>(R.id.search_edit_clear_button)

        clearButton.setOnClickListener {

            clearMessage()
            updateButtonView.isVisible = false
            searchEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)

            if (trackList.isNotEmpty()) {
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
            }

        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                searchButton.isVisible = true
                clearButton.isVisible = clearButtonVisibility(s = p0)

            }

            override fun afterTextChanged(p0: Editable?) {

                searchText = searchEditText.text.toString()

            }

        }

        searchEditText.addTextChangedListener(watcher)

        searchEditText.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                update()

                true
            }

            false
        }

        updateButtonView.setOnClickListener {

            update()

        }

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putString(searchTextKey, searchText)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {

            searchText = savedInstanceState.getString(searchTextKey).toString()
            val editText = findViewById<EditText>(R.id.search_edit_text)
            editText.setText(searchText)

        }

    }

    private fun update() {

        if (searchText.isEmpty()) {

            return
        }

        clearMessage()
        updateButtonView.isVisible = false

        itunesService.search(searchText)
            .enqueue(object : Callback<TrackSearchResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackSearchResponse>,
                    response: Response<TrackSearchResponse>
                ) {


                    if (response.code() == 200) {

                        trackList.clear()

                        if (response.body()?.results?.isNotEmpty() == true) {

                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()

                        }

                        if (trackList.isEmpty()) {

                            showMessage(
                                R.drawable.not_found_error,
                                getString(R.string.not_found_error)
                            )

                        }

                    } else {

                        showMessage(
                            R.drawable.connection_error,
                            getString(R.string.connection_error)

                        )
                        updateButtonView.isVisible = true

                    }

                }

                override fun onFailure(call: Call<TrackSearchResponse>, t: Throwable) {

                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    showMessage(R.drawable.connection_error, getString(R.string.connection_error))
                    updateButtonView.isVisible = true
                }


            })
    }


    private fun clearButtonVisibility(s: CharSequence?): Boolean {

        return !s.isNullOrEmpty()

    }

    private companion object {
        const val searchTextKey = "SEARCH_TEXT"
    }

    private fun showMessage(messageRes: Int, messageTxt: String) {

        msgImgView.isVisible = true
        msgImgView.setBackgroundResource(messageRes)

        msgTxtView.isVisible = true
        msgTxtView.text = messageTxt

    }

    private fun clearMessage() {

        msgImgView.isVisible = false
        msgTxtView.isVisible = false

    }

}

