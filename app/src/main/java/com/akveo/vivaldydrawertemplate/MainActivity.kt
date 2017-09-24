package com.akveo.vivaldydrawertemplate

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initDrawer()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStackImmediate(
                    supportFragmentManager.getBackStackEntryAt(0).id,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        } else {
            super.onBackPressed()
        }
        System.out.println(supportFragmentManager.backStackEntryCount)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        displayScreen(item.itemId)

        item.isChecked = true
        title = item.title

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun initDrawer() {
        val toggle = ActionBarDrawerToggle(
                this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.getHeaderView(0).setOnClickListener {
            val instance = ProfileFragment::class.java.newInstance() as Fragment
            openFragment(instance)
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }

    private fun displayScreen(id: Int) {
        val fragment = chooseFragment(id)
        var instance: Fragment

        try {
            instance = fragment.newInstance() as Fragment
            openFragment(instance)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "We haven't got this view yet)", Toast.LENGTH_LONG).show()
        }
    }

    private fun chooseFragment(id: Int): Class<*> = when (id) {
        R.id.nav_vehicles -> VehiclesFragment::class.java
        else -> DriverScoreFragment::class.java
    }

    private fun openFragment(instance: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, instance)
                .addToBackStack(instance.tag)
                .commit()
    }
}
