package be.shylo.hydratenow.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import be.shylo.hydratenow.R
import com.google.android.material.navigation.NavigationView

open class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout

    override fun setContentView(view: View?) {
        drawerLayout = layoutInflater.inflate(R.layout.activity_drawer, null) as DrawerLayout
        val ctnr: FrameLayout = drawerLayout.findViewById(R.id.activityCtnr)
        ctnr.addView(view)
        super.setContentView(drawerLayout)

        val toolBar: Toolbar = drawerLayout.findViewById(R.id.toolBar)
        setSupportActionBar(toolBar)

        val navigationView: NavigationView = drawerLayout.findViewById(R.id.navView)
        navigationView.setNavigationItemSelectedListener(this)

        val actionBarDrawerToggle: ActionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.menu_open, R.string.menu_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return false
    }

    private fun addActivityTitle(title: String){
        supportActionBar?.setTitle(title)
    }
}