package com.example.aplicacionquiz_nicolino

import android.os.Bundle
import android.text.style.BackgroundColorSpan
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private var puntiacion by mutableStateOf(0)
    private val totalPreguntas = RespuestasPreguntas.preguntas.size
    private var preuntaActualIndex by mutableStateOf(0)
    private var showDialog by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // No inicialices showDialog aquí

        setContent {
            MyApp()

            // Inicializa showDialog después de que el contenido se haya establecido
            showDialog = false
        }
    }
    private fun onClick(answer: String) {
        if (answer == RespuestasPreguntas.respuestasCorrectas[preuntaActualIndex]) {
            puntiacion++
        }

        preuntaActualIndex++
        nuevaPregunta()
    }

    private fun nuevaPregunta() {
        if (preuntaActualIndex == totalPreguntas) {
            showDialog = true
        }
    }

    private fun reiniciar() {
        puntiacion = 0
        preuntaActualIndex = 0
        showDialog = false
    }

    @Composable
    fun FinalPreguntas(puntuacion: Int, totalPreguntas: Int, onDismiss: () -> Unit) {
        val passStatus = if (puntuacion >= totalPreguntas / 2) "¡Felicidades!" else "Mejor suerte la próxima vez"

        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            title = { Text(text = passStatus) },
            text = { Text(text = "Tu puntuación es $puntuacion de $totalPreguntas") },
            confirmButton = {
                Button(
                    onClick = {
                        reiniciar()
                        onDismiss()
                    }
                ) {
                    Text(text = "Reintentar")
                }
            }
        )
    }

    @Composable
    fun MyApp() {
        Column {
            QuizScreen()

            if (showDialog) {
                FinalPreguntas(puntiacion, totalPreguntas) {
                    reiniciar()
                }
            }
        }
    }

    @Composable
    fun QuizScreen() {
        var selectedAnswer by remember { mutableStateOf("") }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val preguntaActual = RespuestasPreguntas.preguntas.getOrNull(preuntaActualIndex)
            val imagenActual = RespuestasPreguntas.imagenesPreguntas.getOrNull(preuntaActualIndex)

            // Verificamos si hay más preguntas
            if (preguntaActual != null && imagenActual != null) {
                // Muestra la imagen correspondiente
                Image(
                    painter = painterResource(imagenActual),
                    contentDescription = "Pregunta ${preuntaActualIndex + 1}",
                    modifier = Modifier
                        .size(350.dp) // Tamaño de la imagen según lo necesario
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text("Pregunta ${preuntaActualIndex + 1}")
                Spacer(modifier = Modifier.height(16.dp))
                Text(preguntaActual)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RespuestasPreguntas.elecciones.getOrNull(preuntaActualIndex)?.forEach { opcion ->
                        Button(
                            onClick = {
                                selectedAnswer = opcion
                                onClick(opcion)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .background(
                                    if (opcion == selectedAnswer) MaterialTheme.colorScheme.primary else Color.White
                                )
                        ) {
                            Text(opcion)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Verificar si es la primera pregunta antes de mostrar el cuadro de diálogo
                if (preuntaActualIndex > 0) {
                    Button(
                        onClick = { retrocederPregunta() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Retroceder")
                    }
                } else {
                    Text("BUENA SUERTE")
                }
            } else {
                // Mostrar el cuadro de diálogo en lugar de texto
                showDialog = true
            }
        }
    }

    private fun retrocederPregunta() {
        preuntaActualIndex--
    }

    object RespuestasPreguntas {
        val preguntas = listOf(
            "¿Cuantos juegos componen la saga soulsborne?",
            "¿Como se llama la muñeca del sueño del cazador?",
            "¿Cuantos finals posibles hay en Dark Souls 3?",
            "¿Cual es el arma mas iconica de la saga?",
            "¿Como se llama este jefe?",
            "¿Como se llama el consumible que se usa para curar tu vida?",
            "¿Que es esto?",
            "¿Que arma mitica es utilizada para derrotar a Yhorm el ultimo gigante?",
            "¿Como diferenciamos un mimico de un cofre comun?",
            "¿A que compañia pertenece la saga?",
            "¿Como se le llama a la lobby del DS3?",
            "¿Cual es el nivel maximo al que podemos llegar en Elden Ring?",
            "¿Como se llama el jefe oculto del segundo DLC de DS3?",
            "¿En Bloodborne que necesitamos tener para ver el mundo real?",
            "¿Te gusto el Quiz?"
        )

        val elecciones = listOf(
            listOf("3", "5", "4", "6"),
            listOf("Vicaria Amelia", "Lady Maria", "Ebrietas", "Malenia"),
            listOf("1", "2", "3", "4"),
            listOf("Martillo del gigante", "Espada recta rota", "Garrote", "Espada de luz de luna"),
            listOf("Rey sin nombre", "Manus", "Amigdala", "Artorias"),
            listOf("Juguito de naranja", "Sunny Delight", "Frasco de estus", "Pocion de vida"),
            listOf("Una tortuga", "Un perro", "Un enemigo", "Un NPC"),
            listOf("Soberano de las Tormentas", "Espada matagigantes", "Cualquier arma sirve", "Sin armas (soy masoca)"),
            listOf("Con el color del cofre", "Con la posicion de la cadena", "No se Puede", "No existen los mimicos"),
            listOf("FromSoftware", "Nintendo(no me demandes pls)", "Valve", "Atary"),
            listOf("Santuario del enlace de fuego", "Mi casa", "No tiene nombre", "Zona segura"),
            listOf("99", "100", "255", "713"),
            listOf("Martir Logarius", "Campeon Gundyr", "Midir, El devorador de oscuridad", "Antiguo rey demonio"),
            listOf("Mucha lucidez", "Un item", "Matar a un jefe en concreto", "lo ves desde el principio"),
            listOf("Si", "es Si", "La primera y la segunda son ciertas", "La tercera dice la verdad")
        )

        val respuestasCorrectas = listOf(
            "6",
            "Lady Maria",
            "3",
            "Espada de luz de luna",
            "Artorias",
            "Frasco de estus",
            "Un perro",
            "Soberano de las Tormentas",
            "Con la posicion de la cadena",
            "FromSoftware",
            "Santuario del enlace de fuego",
            "713",
            "Midir, El devorador de oscuridad",
            "Mucha lucidez",
            "Si"
        )

        val imagenesPreguntas = listOf(
            R.drawable.juegos,
            R.drawable.ladymaria,
            R.drawable.pegruntafinales,
            R.drawable.espada,
            R.drawable.artorias,
            R.drawable.frasco,
            R.drawable.tortuga,
            R.drawable.yhorm,
            R.drawable.mimico,
            R.drawable.empresa,
            R.drawable.santuario,
            R.drawable.nivelelden,
            R.drawable.midir,
            R.drawable.loco,
            R.drawable.preguntafina
        )
    }
}
