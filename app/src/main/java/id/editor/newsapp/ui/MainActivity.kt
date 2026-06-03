package id.editor.newsapp.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import id.editor.newsapp.R
import id.editor.newsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var prefs: SharedPreferences

    // Fragments that show in bottom nav (top-level destinations)
    private val topLevelDestinations = setOf(
        R.id.homeFragment,
        R.id.categoryListFragment,
        R.id.tagListFragment,
        R.id.searchFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("editor_news_prefs", MODE_PRIVATE)
        applyDarkModePreference()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinations,
            binding.drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.navigationView.setupWithNavController(navController)

        // Keep bottom nav in sync and hide on detail screens
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = topLevelDestinations.contains(destination.id)
            binding.bottomNavigation.visibility =
                if (isTopLevel) android.view.View.VISIBLE else android.view.View.GONE
        }

        // Drawer nav extra destinations
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_pages -> navController.navigate(R.id.pageListFragment)
                R.id.nav_contact -> navController.navigate(R.id.contactFragment)
                else -> navController.navigate(item.itemId)
            }
            binding.drawerLayout.closeDrawers()
            true
        }

        // Toolbar menu
        binding.toolbar.inflateMenu(R.menu.toolbar_menu)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_dark_mode -> {
                    toggleDarkMode()
                    true
                }
                R.id.action_search -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun applyDarkModePreference() {
        val isDark = prefs.getBoolean("dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun toggleDarkMode() {
        val isDark = prefs.getBoolean("dark_mode", false)
        val newMode = !isDark
        prefs.edit().putBoolean("dark_mode", newMode).apply()
        AppCompatDelegate.setDefaultNightMode(
            if (newMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (binding.drawerLayout.isOpen) {
                    binding.drawerLayout.closeDrawers()
                } else {
                    navController.navigateUp(appBarConfiguration)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
