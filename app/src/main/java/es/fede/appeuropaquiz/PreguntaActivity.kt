package es.fede.appeuropaquiz

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Space
import kotlinx.android.synthetic.main.activity_pregunta.*
import java.util.*

class PreguntaActivity : AppCompatActivity() {

    var handler: Handler? = null
    var runnable: Runnable? = null
    var second = 0

    var numPregunta = 0
    var respuestas = 0

    var paises: ArrayList<ArrayList<String>> = arrayListOf<ArrayList<String>>()

    enum class Datos(val pos: Int, val dato: String) {
        PAIS(0,"el país"),
        GENTILICIO(1,"el gentilicio"),
        CAPITAL(2, "la capital"),
        RIO(3, "el río")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pregunta)

        cargarDatos()
        handler = Handler()
        //Hacemos una cuenta atras de 5 segundos - para responder
        countDown()

        respuestas = intent.extras.get("numRespuestas") as Int
        numPregunta = intent.extras.get("numPregunta") as Int

        numero_pregunta.progress = numPregunta * 10
        numero_pregunta_text.text = "${numPregunta} / 10"

        Log.i("TAG", "---------------------------")
        var listaRespuestas = arrayListOf<String>()

        var paisAleatorioBueno = (0..paises.size).random()
        var datoAleatorio = (1..4).random()

        Log.i("TAG", "Cual es ${Datos.values().get(datoAleatorio).dato} de ${paises.get(paisAleatorioBueno).get(0)}")
        pregunta.text = "¿Cual es ${Datos.values().get(datoAleatorio).dato} de ${paises.get(paisAleatorioBueno).get(0)}?"

        listaRespuestas.add(paises.get(paisAleatorioBueno).get(datoAleatorio))

        while (listaRespuestas.size < respuestas) {
            var paisAleatorioFalso = (0..paises.size).random()
            val paisFalso = paises.get(paisAleatorioFalso)

            if (paisFalso.get(datoAleatorio).length > 0 && !listaRespuestas.contains(paisFalso.get(datoAleatorio))) {
                Log.i("TAG", "${paisFalso.get(datoAleatorio)} - ${paisFalso.get(datoAleatorio).length}")
                listaRespuestas.add(paisFalso.get(datoAleatorio))
            }
        }

        //Recolocamos la primera (que es la buena por otra al azar)
        val posicionCorrecta = (0..listaRespuestas.size).random()
        val datoChange = listaRespuestas.get(posicionCorrecta)
        listaRespuestas.set(posicionCorrecta, listaRespuestas.get(0))
        listaRespuestas.set(0, datoChange)

        var pos = 1
        //var tableRow: TableRow? = TableRow(this)
        var linearLayout = LinearLayout(this)
        linearLayout.gravity = Gravity.CENTER_HORIZONTAL
        linearLayout.setPadding(15,15,15,15)

        for (dato in listaRespuestas) {
            val button = Button(this)
            button.setText(dato)
            button.background = resources.getDrawable(R.drawable.boton_respuesta)

            val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(15, 10, 15, 10)
            button.setLayoutParams(params)
            button.setTag(listaRespuestas.indexOf(dato))

            button.setOnClickListener {
                if (button.getTag() == posicionCorrecta) {
                    resultado.text = "Correcto !!"
                } else {
                    resultado.text = "Error !!"
                }
                if (numPregunta < 10) {
                    handler!!.removeCallbacks(runnable)
                    numPregunta++
                    val intent = Intent(this, PreguntaActivity::class.java)
                    intent.putExtra("numRespuestas", respuestas)
                    intent.putExtra("numPregunta", numPregunta)
                    startActivity(intent)
                }
            }

            linearLayout.addView(button)

            //val space = Space(this)
            //linearLayout.addView(space)

            if (pos % 2 == 0) {
                lista_respuestas.addView(linearLayout)
                linearLayout = LinearLayout(this)
                linearLayout.gravity = Gravity.CENTER_HORIZONTAL
                linearLayout.setPadding(15,15,15,15)
            }

            pos++
        }

    }

    fun countDown() {

        if (second < 21) {
            count_down.progress = second
            runnable = object : Runnable {
                override fun run() {
                    countDown()
                }
            }
            handler!!.postDelayed(runnable, 500)
            second ++
        } else {
            resultado.text = "Paso tu tiempo"
            SystemClock.sleep(1500)
            //Mostramos Error y pasamos a la siguiente pregunta
            if (numPregunta < 10) {
                numPregunta++
                val intent = Intent(this, PreguntaActivity::class.java)
                intent.putExtra("numRespuestas", respuestas)
                intent.putExtra("numPregunta", numPregunta)
                startActivity(intent)
            }
        }


    }

    fun ClosedRange<Int>.random() = Random().nextInt(endInclusive - start) +  start

    fun cargarDatos() {
        paises.add(arrayListOf<String>("ALEMANIA", "ALEMAN", "BERLIN", "SPREE"))
        paises.add(arrayListOf<String>("AUSTRIA", "AUSTRIACO", "VIENA", "DANUBIO"))
        paises.add(arrayListOf<String>("BELGICA", "BELGA", "BRUSELAS", ""))
        paises.add(arrayListOf<String>("BULGARIA", "BULGARO", "SOFIA", ""))
        paises.add(arrayListOf<String>("CHIPRE", "CHIPRIOTA", "NICOSIA", ""))

        paises.add(arrayListOf<String>("CROACIA", "CROATA", "ZAGRE", ""))
        paises.add(arrayListOf<String>("DINAMARCA", "DANÉS", "COPENHAGUE", ""))
        paises.add(arrayListOf<String>("ESLOVAQUIA", "ESLOVACO", "BRATISLAVA", "DANUBIO"))
        paises.add(arrayListOf<String>("ESLOVENIA", "ESLOVENO", "LIUBLIANA", ""))
        paises.add(arrayListOf<String>("ESPAÑA", "ESPAÑOL", "MADRID", "MANZANARES"))
        paises.add(arrayListOf<String>("ESTONIA", "ESTONIO", "TALLÍN", ""))
        paises.add(arrayListOf<String>("FINLANDIA", "FINLANDÉS", "HELSINKI", ""))
        paises.add(arrayListOf<String>("FRANCIA", "FRANCÉS", "PARÍS", "SENA"))
        paises.add(arrayListOf<String>("GRECIA", "GRIEGO", "ATENAS", ""))
        paises.add(arrayListOf<String>("HUNGRÍA", "HUNGARO", "BUDAPEST", "DANUBIO"))
        paises.add(arrayListOf<String>("IRLANDA", "IRLANDES", "DUBLÍN", "LIFFEY"))
        paises.add(arrayListOf<String>("ITALIA", "ITALIANO", "ROMA", "TIBER"))
        paises.add(arrayListOf<String>("LETONIA", "LETÓN", "RIGA", ""))
        paises.add(arrayListOf<String>("LITUANIA", "LITUANO", "VILNA", ""))
        paises.add(arrayListOf<String>("LUXEMBURGO", "LUXEMBURGUÉS", "LUXEMBURGO", ""))
        paises.add(arrayListOf<String>("MALTA", "MALTÉS", "LA VALLETA", ""))
        paises.add(arrayListOf<String>("PAISES BAJOS", "HOLANDÉS", "AMSTERDAM", "AMSTEL"))
        paises.add(arrayListOf<String>("POLONIA", "POLACO", "VARSOVIA", ""))
        paises.add(arrayListOf<String>("PORTUGAL", "PORTUGUÉS", "LISBOA", "TAJO"))
        paises.add(arrayListOf<String>("REINO UNIDO", "BRITÁNICO", "LONDRES", "TAMESIS"))
        paises.add(arrayListOf<String>("REPUBLICA CHECA", "CHECO", "PRAGA", "MOLDAVIA"))
        paises.add(arrayListOf<String>("RUMANIA", "RUMANO", "BUCAREST", ""))
        paises.add(arrayListOf<String>("SUECIA", "SUECO", "ESTOCOLMO", ""))
        paises.add(arrayListOf<String>("ALBANIA", "ALBANÉS", "TIRANA", ""))
        paises.add(arrayListOf<String>("ANDORRA", "ANDORRANO", "ANDORRA", ""))
        paises.add(arrayListOf<String>("ARMENIA", "ARMENIO", "EREVÁN", ""))
        paises.add(arrayListOf<String>("AZERBAIYAN", "AZERBAIYANO", "BAKÚ", ""))
        paises.add(arrayListOf<String>("BIELORRUSIA", "BIELORRUSO", "MINSK", ""))
        paises.add(arrayListOf<String>("BOSNIA Y HERZEGOVINA", "BOSNIO", "SARAJEVO", ""))
        paises.add(arrayListOf<String>("CIUDAD DEL VATICANO", "VATICANO", "CIUDAD DEL VATICANO", ""))
        paises.add(arrayListOf<String>("GEORGIA", "GEORGIANO", "TBILISI", ""))
        paises.add(arrayListOf<String>("ISLANDIA", "ISLANDES", "REYKJAVIK", ""))
        paises.add(arrayListOf<String>("KAZAJISTÁN", "KAZAJO", "ASTANÁ", ""))
        paises.add(arrayListOf<String>("KOSOVO", "KOSOVAR", "PRISTINA", ""))

    }
}
