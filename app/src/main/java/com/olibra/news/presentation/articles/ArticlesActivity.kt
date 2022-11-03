package com.olibra.news.presentation.articles

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.olibra.news.R
import com.olibra.news.databinding.ActivityArticlesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticlesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArticlesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityArticlesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupDrawer()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.appBarLayout.toolbar)
        binding.appBarLayout.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    private fun setupDrawer() {
        setupDrawerContent()
        setupDrawerToggle()
        selectDrawerItem(binding.navigationView.menu.getItem(0))
    }

    private fun setupDrawerToggle() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarLayout.toolbar,
            R.string.content_desc_open_drawer,
            R.string.content_desc_close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
    }

    private fun setupDrawerContent() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        menuItem.isChecked = true
        title = menuItem.title

        val articlesFragment = ArticlesFragment.getInstance(menuItem.itemId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.articlesFragmentContainerView, articlesFragment)
            .commit()

        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }
}