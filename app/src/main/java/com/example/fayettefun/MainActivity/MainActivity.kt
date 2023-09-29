package com.example.fayettefun.MainActivity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.fayettefun.NewEditWordActivity.EXTRA_ID
import com.example.fayettefun.NewEditWordActivity.NewWordActivity
import com.example.fayettefun.R
import com.example.fayettefun.FayettefunApplication

class MainActivity : AppCompatActivity() {

    //This is our viewModel instance for the MainActivity class
    private val wordListViewModel: WordListViewModel by viewModels {
        WordListViewModelFactory((application as FayettefunApplication).repository)
    }
    //This is our ActivityResultContracts value that defines
    //the behavior of our application when the activity has finished.
    val startNewWordActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
        if(result.resultCode== Activity.RESULT_OK){
            //Note that all we are doing is logging that we completed
            //This means that the other activity is handling updates to the data
            Log.d("MainActivity","Completed")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter {
            //This is the callback function to be executed
            //when a view in the WordListAdapter is clicked

            //First we log the word
            Log.d("MainActivity",it.word)
            //Then create a new intent with the ID of the word
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            intent.putExtra(EXTRA_ID,it.id)
            //And start the activity through the results contract
            startNewWordActivity.launch(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        wordListViewModel.allWords.observe( this) { words ->
            // Update the cached copy of the words in the adapter.
            words.let { adapter.submitList(it) }
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewWordActivity::class.java)
            startNewWordActivity.launch(intent)
        }
    }
}
