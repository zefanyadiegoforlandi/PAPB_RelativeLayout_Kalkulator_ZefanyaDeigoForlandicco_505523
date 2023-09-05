package com.example.kalkulator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.kalkulator.databinding.ActivityMainBinding
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var lastOperation: String
    private lateinit var savedSubCalc: String
    private var optDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lastOperation = ""
        savedSubCalc = ""
    }

    fun inputNumber(view: View) {
        val inputtedValue = (view as Button).text.toString()

        with(binding) {
            if (!((hasilKal.text.isEmpty() || (hasilkal2.text.isNotEmpty() && optDone)) && (inputtedValue == "00" || inputtedValue == "0"))) {
                if (!optDone) {
                    if (lastOperation.isEmpty()) {
                        hasilKal.text = "${hasilKal.text}$inputtedValue"
                    } else {
                        if (hasilkal2.text.isEmpty()) {
                            hasilkal2.text = hasilKal.text
                            hasilKal.text = inputtedValue
                        } else {
                            hasilKal.text = "${hasilKal.text}$inputtedValue"
                        }
                    }
                } else {
                    savedSubCalc = ""
                    hasilkal2.text = ""
                    hasilKal.text = inputtedValue
                    lastOperation = ""
                    optDone = false
                }
            }
        }
    }

    fun inputOperation(view: View) {
        val currentOperation = (view as Button).text.toString()

        with(binding) {
            if (!optDone) {
                if (lastOperation.isEmpty() && currentOperation != "C" && currentOperation != "c") {
                    if (hasilKal.text.isNotEmpty()) {
                        savedSubCalc = hasilKal.text.toString()
                        hasilkal2.text = "${hasilKal.text} $currentOperation"
                        hasilKal.text = ""
                        lastOperation = currentOperation
                    }
                } else if (currentOperation == "c") {
                    if (hasilKal.text.isNotEmpty()) {
                        clearLastDigit()
                    } else if (hasilKal.text.isEmpty() && hasilkal2.text.isNotEmpty()) {
                        clearAll()
                    }
                } else {
                    if (currentOperation == "=") {
                        if (hasilKal.text.isNotEmpty() && hasilkal2.text.isNotEmpty()) {
                            calculateResult()
                        }
                    } else if (hasilKal.text.isEmpty() && currentOperation != "C") {
                        lastOperation = currentOperation
                        hasilkal2.text = "$savedSubCalc $currentOperation"
                    } else if (currentOperation == "C") {
                        clearAll()
                    }
                }
            } else {
                if (currentOperation != "C" && currentOperation != "c" && currentOperation != "=") {
                    if (hasilKal.text.isNotEmpty()) {
                        savedSubCalc = hasilKal.text.toString()
                        hasilkal2.text = "${hasilKal.text} $currentOperation"
                        hasilKal.text = ""
                    } else {
                        savedSubCalc = ""
                        hasilkal2.text = ""
                    }

                    if (currentOperation != "=" && optDone) {
                        lastOperation = currentOperation
                    } else {
                        lastOperation = ""
                    }

                    optDone = false
                } else {
                    clearAll()
                }
            }
        }
    }

    fun inputDelete(view: View) {
        with(binding) {
            if (hasilKal.text.isNotEmpty()) {
                val currentText = hasilKal.text.toString()
                hasilKal.text = currentText.substring(0, currentText.length - 1)
            }
        }
    }

    private fun clearAll() {
        with(binding) {
            savedSubCalc = ""
            hasilkal2.text = ""
            hasilKal.text = ""
            lastOperation = ""
        }
    }

    private fun clearLastDigit() {
        with(binding) {
            if (hasilKal.text.length > 1) {
                val temp = StringBuilder(hasilKal.text.toString())
                temp.deleteCharAt(temp.length - 1)
                hasilKal.text = temp.toString()
            } else if (hasilkal2.text.isNotEmpty()) {
                hasilKal.text = ""
            } else {
                clearAll()
            }
        }
    }

    private fun calculateResult() {
        with(binding) {
            try {
                when (lastOperation) {
                    "+" -> hasilKal.text = (hasilKal.text.toString().toBigDecimal() + savedSubCalc.toBigDecimal()).toString()
                    "-" -> hasilKal.text = (savedSubCalc.toBigDecimal() - hasilKal.text.toString().toBigDecimal()).toString()
                    "*" -> hasilKal.text = (hasilKal.text.toString().toBigDecimal() * savedSubCalc.toBigDecimal()).toString()
                    "/" -> {
                        if (!hasilKal.text.toString().contains('.')) hasilKal.text = "${hasilKal.text}.0"
                        if (!savedSubCalc.contains('.')) savedSubCalc = "${savedSubCalc}.0"

                        hasilKal.text = (savedSubCalc.toDouble() / hasilKal.text.toString().toDouble()).toString()
                    }
                    "%" -> hasilKal.text = (savedSubCalc.toBigDecimal() % hasilKal.text.toString().toBigDecimal()).toString()
                }
            } catch (e: NumberFormatException) {
                hasilKal.text = "Angka terlalu besar"
            }

            hasilkal2.text = ""
            lastOperation = ""
            optDone = true
        }
    }

    fun makeDecimal(view: View) {
        with(binding) {
            hasilKal.text = "${hasilKal.text}."
        }
    }
}
