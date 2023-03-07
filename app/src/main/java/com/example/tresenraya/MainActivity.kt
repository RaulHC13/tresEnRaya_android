package com.example.tresenraya


import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
import com.example.tresenraya.databinding.ActivityMainBinding

class MainActivity: Base(), View.OnClickListener {
    lateinit var binding: ActivityMainBinding
    val casillas = arrayOf(R.drawable.blanco, R.drawable.circulo, R.drawable.equis)
    val tablero: Array<ImageView?> = Array(9, {null})
    var dificultad = 0
    var contador1 = 0
    var contador2 = 0

    var dosJugadores = true
    lateinit var juego: Juego

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pantallaCompleta()
        iniciaTablero()
        setListeners()
    }

    private fun setListeners() {
        binding.btnSalir.setOnClickListener {
            finish()
        }
        binding.btnJugar.setOnClickListener {
            enableCasillas()
            comprobarModo()
        }
        binding.switch1.setOnCheckedChangeListener { _, isChecked ->
            dosJugadores = !(isChecked)
            if (isChecked) {
                binding.tvDificultad.visibility = View.VISIBLE
                binding.tvSeekbar.visibility = View.VISIBLE
                binding.sbDificultad.visibility = View.VISIBLE
            } else if (!isChecked) {
                binding.tvDificultad.visibility = View.INVISIBLE
                binding.tvSeekbar.visibility = View.INVISIBLE
                binding.sbDificultad.visibility = View.INVISIBLE
            }
            contador1 = 0
            contador2 = 0
            binding.tvJugador1.text = contador1.toString()
            binding.tvJugador1.text = contador2.toString()
        }
        binding.sbDificultad.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dificultad = progress
                when (dificultad) {
                    0 -> binding.tvDificultad.text = "Facil"
                    1 -> binding.tvDificultad.text = "Normal"
                    2 -> binding.tvDificultad.text = "Dificil"
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun comprobarModo() {
        if(dosJugadores) {
            Toast.makeText(this, "Modo dos jugadores", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Modo un jugador", Toast.LENGTH_SHORT).show()
        }
        jugar()
    }

    private fun jugar() {
        juego = Juego()
        ponerListeners()

    }

    private fun ponerListeners() {
        for (i in tablero.indices) {
            tablero[i]?.setOnClickListener(this)
            tablero[i]?.setImageResource(casillas[0])
        }
    }

    private fun iniciaTablero() {
        tablero[0] = binding.iv0
        tablero[1] = binding.iv1
        tablero[2] = binding.iv2
        tablero[3] = binding.iv3
        tablero[4] = binding.iv4
        tablero[5] = binding.iv5
        tablero[6] = binding.iv6
        tablero[7] = binding.iv7
        tablero[8] = binding.iv8
    }

    override fun onClick(v: View?) {
        for (i in 0 until tablero.size) {
            if (v == tablero[i]) {
                jugarCasilla(i)
            }
        }
    }

    private fun jugarCasilla(i: Int) {
        if (juego.tableroLogico[i] != 0) return
        tablero[i]?.setImageResource(casillas[juego.jugador])
        juego.tableroLogico[i] = juego.jugador
        var op = juego.comprobar()
        evalua(op)
        if (op != 3) return
        juego.cambioTurno()
        if (!dosJugadores) {
            var jugada = juego.piensaJugadaFacil()

            when (dificultad) {
                0 -> jugada = juego.piensaJugadaFacil()
                1 -> jugada = juego.piensaJugadaMedio()
                2 -> jugada = juego.piensaJugadaDificil()
        }
            tablero[jugada]?.setImageResource(casillas[juego.jugador])
            juego.tableroLogico[jugada] = juego.jugador
            var op = juego.comprobar()
            evalua(op)
            if (op != 3) return
            juego.cambioTurno()
        }
    }

    private fun evalua(op: Int) {
        val mensaje = AlertDialog.Builder(this)
        mensaje.setTitle("TRES EN RAYA")
        when(op) {
            0 -> mostrar("EMPATE", mensaje)
            1 -> {
                mostrar("Gana jugador 1", mensaje)
                contador1++
                binding.tvJugador1.text = contador1.toString()
                disableCasillas()
            }
            2 -> {
                mostrar("Gana jugador 2", mensaje)
                contador2++
                binding.tvJugador2.text = contador2.toString()
                disableCasillas()
            }
            3 -> return
        }
    }

    private fun mostrar(s: String, mensaje: AlertDialog.Builder) {
        mensaje.setMessage(s)
        mensaje.show()
    }

    private fun disableCasillas() {
        for (i in 0 until binding.tr1.childCount) {
            var child = binding.tr1.getChildAt(i)
            child.isEnabled = false
        }
        for (i in 0 until binding.tr2.childCount) {
            var child = binding.tr2.getChildAt(i)
            child.isEnabled = false
        }
        for (i in 0 until binding.tr2.childCount) {
            var child = binding.tr3.getChildAt(i)
            child.isEnabled = false
        }
    }

    private fun enableCasillas() {
        for (i in 0 until binding.tr1.childCount) {
            var child = binding.tr1.getChildAt(i)
            child.isEnabled = true
        }
        for (i in 0 until binding.tr2.childCount) {
            var child = binding.tr2.getChildAt(i)
            child.isEnabled = true
        }
        for (i in 0 until binding.tr3.childCount) {
            var child = binding.tr3.getChildAt(i)
            child.isEnabled = true
        }
    }
}