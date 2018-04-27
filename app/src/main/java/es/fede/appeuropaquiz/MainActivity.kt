package es.fede.appeuropaquiz

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




    }



    fun nivelQuiz(view: View) {

        var respuestas = 0
        when(view) {
            facil -> respuestas = 4
            medio -> respuestas = 6
            dificil -> respuestas = 8
        }

        val intent = Intent(this, PreguntaActivity::class.java)
        intent.putExtra("numRespuestas", respuestas)
        intent.putExtra("numPregunta", 1)
        startActivity(intent)

    }
}
