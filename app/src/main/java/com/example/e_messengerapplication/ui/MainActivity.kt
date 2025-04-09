package com.example.e_messengerapplication.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.TokenManager
import com.example.e_messengerapplication.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars =
                insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom + imeHeight
            )
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val controller = navHostFragment.navController
        val bottomNavigationView = binding.bottomMenu
        bottomNavigationView.setupWithNavController(controller)

        controller.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id) {
                R.id.homeFragment,
                R.id.contactsFragment,
                R.id.groupsFragment,
                R.id.chatBotFragment
                    -> bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }

        if (checkCurrentUser()) {
            controller.navigate(R.id.homeFragment)
        }
        else {
            controller.navigate(R.id.signInFragment)
        }
    }

    private fun checkCurrentUser(): Boolean {
        return tokenManager.getAccessToken() != null
    }
}