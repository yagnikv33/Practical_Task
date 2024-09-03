package com.example.hevintechnowebpractical.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hevintechnowebpractical.R
import com.example.hevintechnowebpractical.data.local.AppDatabase
import com.example.hevintechnowebpractical.data.model.User
import com.example.hevintechnowebpractical.data.remote.UserService
import com.example.hevintechnowebpractical.data.repository.UserRepository
import com.example.hevintechnowebpractical.databinding.ActivityMainBinding
import com.example.hevintechnowebpractical.ui.adapter.UserAdapter
import com.example.hevintechnowebpractical.ui.viewmodel.UserViewModel
import com.example.hevintechnowebpractical.ui.viewmodel.UserViewModelFactory
import com.example.hevintechnowebpractical.utils.PaginationScrollListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            UserRepository(
                UserService.create(),
                AppDatabase.getDatabase(applicationContext).userDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        userAdapter = UserAdapter(mutableListOf())

        binding.apply {

            //default view states
            search.isEnabled = false
            search.isActivated = false

            //Rcv Config
            userRv.apply {
                adapter = userAdapter
                layoutManager = LinearLayoutManager(this@MainActivity)

                addOnScrollListener(object : RecyclerView.OnScrollListener(),
                    PaginationScrollListener {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        if (!recyclerView.canScrollVertically(1) && dy > 0) {
                            onScrolledToEnd()
                        }
                    }

                    override fun onScrolledToEnd() {
                        viewModel.fetchUsers()
                    }
                })
            }

            //Other Views Config
            search.setOnClickListener {
                userAdapter.clearAdapter()
                val query = searchEditText.text.toString()
                viewModel.searchUsers(query)
                this.root.hideKeyboard()
            }

            sortByFirstName.setOnClickListener {
                userAdapter.clearAdapter()
                viewModel.sortUsersByFirstName()
                this.root.hideKeyboard()
            }

            sortByEmail.setOnClickListener {
                userAdapter.clearAdapter()
                viewModel.sortUsersByEmail()
                this.root.hideKeyboard()
            }

            restoreDefault.setOnClickListener {
                userAdapter.clearAdapter()
                viewModel.fetchUsers()
            }

            searchEditText.doOnTextChanged { text, start, before, count ->
                search.isEnabled = text?.isEmpty() != true
                search.isActivated = text?.isEmpty() != true
            }
        }

        viewModel.users.observe(this) { users ->
            userAdapter.addUsers(users)
        }
    }

    private fun View.hideKeyboard() =
        ViewCompat.getWindowInsetsController(this)?.hide(WindowInsetsCompat.Type.ime())
}
