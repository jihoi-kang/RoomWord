package com.jay.roomword.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.jay.roomword.R
import com.jay.roomword.data.Word

class WordActivity : AppCompatActivity() {

    private lateinit var viewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)

        val wordAdapter = WordAdapter(this)
        findViewById<RecyclerView>(R.id.rv_word).apply {
            adapter = wordAdapter
            layoutManager = LinearLayoutManager(this@WordActivity)
        }

        viewModel = ViewModelProvider(this).get(WordViewModel::class.java)
        viewModel.allWords.observe(this, { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { wordAdapter.setWords(it) }
        })

        findViewById<FloatingActionButton>(R.id.fab_button).setOnClickListener {
            startActivityForResult(Intent(this@WordActivity, AddWordActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }, REQ_CODE_ADD_WORD)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_ADD_WORD && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(AddWordActivity.EXTRA_REPLY)?.let {
                val word = Word(it)
                viewModel.insert(word)
            }
        } else {
            Toast.makeText(
                    applicationContext,
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    companion object {
        private const val REQ_CODE_ADD_WORD = 2000
    }

}