package com.kube.musicplayer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.kube.musicplayer.R
import com.kube.musicplayer.adapter.FavouriteAdapter
import com.kube.musicplayer.databinding.ActivityFavoriteBinding
import com.kube.musicplayer.model.Song

class FavouriteActivity : AppCompatActivity() {
    private lateinit var binding:ActivityFavoriteBinding
    private lateinit var favouriteAdapter: FavouriteAdapter

    companion object{
        var favoriteSongs:ArrayList<Song>  = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding=ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.favoriteRv.setHasFixedSize(true)
        binding.favoriteRv.setItemViewCacheSize(13)
        binding.favoriteRv.layoutManager = GridLayoutManager(this@FavouriteActivity,4 )
        favouriteAdapter = FavouriteAdapter(this@FavouriteActivity, favoriteSongs)
        binding.favoriteRv.adapter = favouriteAdapter
        if(favoriteSongs.size<1) binding.shuffleBtn.visibility=View.INVISIBLE
         binding.shuffleBtn.setOnClickListener {
             val intent = Intent(this@FavouriteActivity, PlayerActivity::class.java)
             intent.putExtra("index", 0)
             intent.putExtra("class", "FavouriteShuffle")
             startActivity(intent)
         }
    }

}