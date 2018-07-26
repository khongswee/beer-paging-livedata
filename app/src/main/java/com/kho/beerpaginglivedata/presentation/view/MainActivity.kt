package com.kho.beerpaginglivedata.presentation.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.kho.beerpaginglivedata.R
import com.kho.beerpaginglivedata.data.local.AppDatabase
import com.kho.beerpaginglivedata.data.local.ServiceLocal
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(container.id, BeerPagedFragment()).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate = menuInflater
        inflate.inflate(R.menu.page_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        val fragment = when (item?.itemId) {
            R.id.action_page -> BeerPagedFragment()
            R.id.action_item -> BeerItemKeyFragment()
            else -> BeerPositionalFragment()
        }
        supportFragmentManager.beginTransaction().replace(container.id, fragment).commit()

        return true

    }
}
