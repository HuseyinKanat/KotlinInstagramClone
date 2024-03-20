package com.example.instagramclone.view

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources.Theme
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth= Firebase.auth
        val currentUser = auth.currentUser
        if (isDarkThemeOn()) {
            binding.imageView4.setImageResource(R.drawable.ic_instagramdarknew)
        } else {
            binding.imageView4.setImageResource(R.drawable.ic_instagram)
        }
        if (currentUser!=null){
            val intent = Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(intent)
            finish()

        }



    }
    fun signIn(view: View){
        val email = binding.tvEmail.text.toString()
        val password = binding.tvPassword.text.toString()
        if(email.isNotEmpty()&&password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {
                //success
                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this, "Enter e-mail and password",Toast.LENGTH_LONG).show()
        }
    }
    fun singUp(view: View){
        val email = binding.tvEmail.text.toString()
        val password = binding.tvPassword.text.toString()
        if(email.isNotEmpty()&&password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                //success
                val intent = Intent(this@MainActivity, FeedActivity::class.java)
                startActivity(intent)
                finish()
            }.addOnFailureListener {
                //failure
                Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this, "Enter e-mail and password",Toast.LENGTH_LONG).show()
        }

    }
    private fun isDarkThemeOn(): Boolean {
        val nightModeFlags: Int = resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

}