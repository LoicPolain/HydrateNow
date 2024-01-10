package be.shylo.hydratenow.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import be.shylo.hydratenow.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

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
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId){
            R.id.homePath -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Firebase.auth.signOut()
                finish()
            }
            else -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return false
    }

    public fun addActivityTitle(title: String){
        supportActionBar?.setTitle(title)
    }
}