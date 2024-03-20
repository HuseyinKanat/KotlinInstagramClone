package com.example.instagramclone.view

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageSwitcher
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.instagramclone.databinding.ActivityFeedBinding
import com.example.instagramclone.R
import com.example.instagramclone.adapter.FeedRecyclerAdapter
import com.example.instagramclone.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FeedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var  db : FirebaseFirestore
    private lateinit var postArrayList: ArrayList<Post>
    private  lateinit var feedAdapter : FeedRecyclerAdapter
    private  var i=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth= Firebase.auth
        db = Firebase.firestore
        getData()
        postArrayList = ArrayList()
        binding.recyclerView.layoutManager = LinearLayoutManager(this@FeedActivity)
        feedAdapter= FeedRecyclerAdapter(postArrayList)
        binding.recyclerView.adapter = feedAdapter



    }
    private fun getData(){
        db.collection("Posts").orderBy("time",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error!=null){
                Toast.makeText(this@FeedActivity,error.localizedMessage,Toast.LENGTH_LONG).show()
            }
            if (value!= null){
                if (!value.isEmpty){
                    val documents = value.documents
                    postArrayList.clear()
                    for (document in documents){
                        val comment = document.get("comment") as String
                        val userEmail = document.get("userEmail") as String
                        val downloadUrl = document.get("downloadUrl") as String
                        val post = Post(userEmail,comment,downloadUrl)
                        postArrayList.add(post)
                    }
                    feedAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater= menuInflater
        menuInflater.inflate(R.menu.instagram_menu,menu)
        supportActionBar?.title = "Instagram"
        supportActionBar?.setIcon(R.drawable.ic_instagramlogo)

        if ( menu != null) {
            if (menu.javaClass.simpleName == "MenuBuilder") {
                try {
                    val m = menu.javaClass.getDeclaredMethod(
                        "setOptionalIconsVisible", Boolean::class.javaPrimitiveType)
                    m.isAccessible = true
                    m.invoke(menu, true)
                } catch (e: Exception) {
                    Log.e(javaClass.simpleName, "Cannot display menu icons", e)
                }
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId== R.id.addPost){
            val intent = Intent(this@FeedActivity, UploadActivity::class.java)
            startActivity(intent)
        }else if(item.itemId== R.id.logOut){
            auth.signOut()
            val intent = Intent(this@FeedActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
    fun like(view: View){
        val imageSwitcher = view as ImageSwitcher
        val likeDrawable = ContextCompat.getDrawable(this, R.drawable.baseline_favorite_24)
        val unLike = ContextCompat.getDrawable(this, R.drawable.baseline_favorite_border_24)
        if( i%2 ==0) {
            imageSwitcher.foreground = unLike
        }
        else{
            imageSwitcher.foreground= likeDrawable
        }
        i++



    }

}