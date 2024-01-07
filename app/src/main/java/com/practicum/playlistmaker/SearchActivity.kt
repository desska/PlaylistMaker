package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.isVisible

class SearchActivity : AppCompatActivity() {

    private var searchText:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = null

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val searchButton = findViewById<ImageView>(R.id.search_edit_search_button)
        val clearButton = findViewById<ImageView>(R.id.search_edit_clear_button)

        clearButton.setOnClickListener{
            searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentFocus?.windowToken , 0)
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

    private fun clearButtonVisibility(s: CharSequence?): Boolean {

        return !s.isNullOrEmpty()

    }

    private companion object{
        const val searchTextKey = "SEARCH_TEXT"
    }

}


