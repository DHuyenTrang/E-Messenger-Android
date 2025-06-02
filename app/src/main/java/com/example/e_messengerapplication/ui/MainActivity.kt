package com.example.e_messengerapplication.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.data.websocket.WebSocketService
import com.example.e_messengerapplication.databinding.ActivityMainBinding
import com.example.e_messengerapplication.ui.profile.SharedUserViewModel
import com.example.e_messengerapplication.utils.Constant.TAG_MESSAGE
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var appStore: AppStore
    val userViewModel: SharedUserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val controller = navHostFragment.navController
        val bottomNavigationView = binding.bottomMenu
        setSelectedBottomMenuItem(R.id.homeFragment)

        bottomNavigationView.setOnItemSelectedListener { id ->
            when (id) {
                R.id.homeFragment -> {
                    controller.navigate(R.id.homeFragment)
                }
                R.id.profileFragment -> {
                    controller.navigate(R.id.profileFragment)
                }
                R.id.groupsFragment ->{
                    controller.navigate(R.id.groupsFragment)
                }
                R.id.chatBotFragment ->{
                    controller.navigate(R.id.chatBotFragment)
                }
            }
        }

        controller.addOnDestinationChangedListener{_, destination, _ ->
            when(destination.id) {
                R.id.homeFragment,
                R.id.profileFragment,
                R.id.groupsFragment,
                R.id.chatBotFragment
                    -> bottomNavigationView.visibility = View.VISIBLE
                else -> bottomNavigationView.visibility = View.GONE
            }
        }

        if (checkCurrentUser()) {
            userViewModel.fetchProfile()
            controller.navigate(R.id.homeFragment)
        }
        else {
            controller.navigate(R.id.signInFragment)
        }
        checkPermission()
    }

    fun setSelectedBottomMenuItem(menuItemId: Int) {
        binding.bottomMenu.setItemSelected(menuItemId, true)
    }

    private fun checkCurrentUser(): Boolean {
        return appStore.getAccessToken() != null
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), 100)
            } else {
                Log.d(TAG_MESSAGE, "Permission granted")
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            } else {
                Log.d(TAG_MESSAGE, "Permission granted")
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 101)
        }
    }
}