package com.test.pokedex.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.test.pokedex.R
import kotlinx.android.synthetic.main.activity_list.*

import kotlinx.android.synthetic.main.activity_pokemon_detail.*

class PokemonDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var intent: Intent = getIntent()
        var url:String = intent.getStringExtra("url_api")
        setContentView(R.layout.activity_pokemon_detail)
        var txt_name:TextView = findViewById(R.id.pokemon_detail_name)
        var txt_number:TextView = findViewById(R.id.pokemon_detail_number)
        var txt_types:TextView = findViewById(R.id.pokemon_detail_types)
        var txt_stats:TextView = findViewById(R.id.pokemon_detail_stats)
        var txt_moves:TextView = findViewById(R.id.pokemon_detail_moves)
        var image_pokemon:ImageView = findViewById(R.id.pokemon_detail_sprite)

        Ion.with(this)
            .load(url)
            .asJsonObject()
            .done { e, result ->
                if(e == null){
                    txt_name.setText(result.get("name").toString().replace("\"","").toUpperCase())
                    txt_number.setText("Pokemon number: "+result.get("id").toString())
                    txt_types.setText(this.generateTypeList(result.get("types").asJsonArray))
                    txt_stats.setText(this.generateStatsList(result.get("stats").asJsonArray))
                    txt_moves.setText(this.generateMovesList(result.get("moves").asJsonArray))
                }
                if(!result.get("sprites").isJsonNull){
                    if(result.get("sprites").asJsonObject.get("front_default") != null){
                        Log.i("Salida", result.get("sprites").asJsonObject.get("front_default").asString)

                        Glide
                            .with(this)
                            .load(result.get("sprites").asJsonObject.get("front_default").asString)
                            .placeholder(R.drawable.pokemon_logo_min)
                            .error(R.drawable.pokemon_logo_min)
                            .into(image_pokemon);


                    }else{
                        image_pokemon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pokemon_logo_min))
                    }

                }else{
                    image_pokemon.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pokemon_logo_min))
                }
            }
    }

    fun generateTypeList(types: JsonArray): String {
        var ans:String = ""
        for(type in types){
            var type_object:JsonObject = type.asJsonObject
            var type_object_object:JsonObject = type_object.getAsJsonObject("type")
            ans += (type_object_object.get("name").asString+"\n")
        }
        return ans
    }

    fun generateStatsList(stats: JsonArray): String {
        var ans:String = ""
        for(stat in stats){
            var stat_object:JsonObject = stat.asJsonObject
            var stat_object_object:JsonObject = stat_object.getAsJsonObject("stat")
            ans += (stat_object_object.get("name").asString+": ")
            ans += (stat_object.get("base_stat").asString+"\n")
        }
        return ans
    }

    fun generateMovesList(moves:JsonArray): String {
        var ans:String = ""
        for(move in moves){
            var move_object:JsonObject = move.asJsonObject
            var move_object_object:JsonObject = move_object.getAsJsonObject("move")
            ans += (move_object_object.get("name").asString+"\n")
        }
        return ans
    }

}
