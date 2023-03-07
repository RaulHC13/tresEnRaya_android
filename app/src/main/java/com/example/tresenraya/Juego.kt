package com.example.tresenraya

class Juego {
    val ganar = arrayOf(
        intArrayOf(0,1,2),
        intArrayOf(3,4,5),
        intArrayOf(6,7,8),
        intArrayOf(0,3,6),
        intArrayOf(1,4,7),
        intArrayOf(2,5,8),
        intArrayOf(0,4,8),
        intArrayOf(2,4,6)
    )

    var tableroLogico = IntArray(9, {i -> 0}) //Inicializa un array de 9, dandole a valor 0 a los campos.
    var jugador = 1

    fun cambioTurno() {
        jugador++
        if (jugador > 2) jugador = 1
    }
    fun comprobar(): Int {//1 si gana j1, 2 si gana j2, 0 empate.
        var ganado = true
        for (i in 0 until ganar.size) {
            ganado = true
            for (j in 0 until ganar[i].size) {
                if (tableroLogico[ganar[i][j]]!= jugador) {
                    ganado = false
                    break
                }
            }
            if (ganado) return jugador
        }
        if (isEmpate()) return 0
        return 3
    }

    private fun isEmpate(): Boolean {
        for (i in 0 until tableroLogico.size) {
            if (tableroLogico[i] == 0) return false
        }
        return true
    }

    fun piensaJugadaDificil(): Int {
        var op = atacar()
        if (op != -1) return op

        op = defender()
        if (op != -1) return op

        if (tableroLogico[4] == 0) return 4

        op = jugarEsquinas()
        if (op != -1) return op

        return jugadaRandom()
    }

    fun piensaJugadaFacil(): Int {
        var op = atacar()
        if (op != -1) return op
        if (tableroLogico[4] == 0) return 4
        return jugadaRandom()
    }

    fun piensaJugadaMedio(): Int {
        var op = atacar()
        if (op != -1) return op

        op = defender()
        if (op != -1) return op

        if (tableroLogico[4] == 0) return 4
        return jugadaRandom()
    }

    private fun jugarEsquinas(): Int {
        if (tableroLogico[0] == 0 || tableroLogico[2] == 0 || tableroLogico[6] == 0 || tableroLogico[8] == 0) {
            var a = arrayOf(0,2,6,8)
            do {
                var num = a.random()
                if (tableroLogico[num] == 0) return num
            }while (true)
        }
        return -1
    }

    private fun jugadaRandom(): Int {
        do {
            var casilla = (0 .. 8).random()
            if (tableroLogico[casilla] == 0) return casilla
        }while (true)
    }

    private fun defender(): Int {
        for (i in 0 until ganar.size) {
            if (cantidadDeValores(i, 1)) {
                var res = casillaFalta(i)
                if (res != -1) return res
            }
        }
        return -1
    }

    private fun atacar(): Int { //Devuelve 1 si puede ser la casilla ganadora o -1 si no hay
        for (i in 0 until ganar.size) {
            if (cantidadDeValores(i, 2)) {
                var res = casillaFalta(i)
                if (res != -1) return res
            }
        }
        return -1
    }

    private fun casillaFalta(fila: Int): Int {
        for (i in 0 until ganar[fila].size) {
            if (tableroLogico[ganar[fila][i]] == 0) return ganar[fila][i]
        }
        return -1
    }

    private fun cantidadDeValores(fila: Int, valor: Int): Boolean {
        var cont = 0
        for (i in 0 until ganar[fila].size) {
            if (tableroLogico[ganar[fila][i]] == valor) cont++
        }
        return (cont == 2)
    }
}