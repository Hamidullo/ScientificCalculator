package com.criminal_code.calculator_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.*

class MainActivity : AppCompatActivity() {

    private val buttonMemoryClear: Button by bind(R.id.btn_memory_clear)
    private val buttonMemoryRecall: Button by bind(R.id.btn_memory_recall)
    private val buttonMemoryAdd: Button by bind(R.id.btn_memory_add)
    private val buttonMemorySubtract: Button by bind(R.id.btn_memory_subtract)

    private val textViewHistoryText: TextView by bind(R.id.number_history)
    private val textViewCurrentNumber: AppCompatTextView by bind(R.id.number_current)

    private var currentNumber: Double = 0.0 // Value can be changed.
    private var memory: Double = 0.0

    private var result = 0.0
    private var expression = ""
    private var text = ""
    private var isBackspaceClicked: Boolean = false
    private var isRootNClicked = false
    private var isMemory = true
    private var isEqual = false

    private var countP = 0

    private var dbHelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DBHelper(this)

        buttonMemoryClear.isEnabled = false
        buttonMemoryClear.setOnClickListener {
            buttonMemoryClear.isEnabled = false
            buttonMemoryRecall.isEnabled = false

            memory = 0.0

            Toast.makeText( applicationContext,getString(R.string.memory_cleared_toast),Toast.LENGTH_SHORT).show()
        }

        buttonMemoryRecall.isEnabled = false
        buttonMemoryRecall.setOnClickListener {
            clearEntry(memory)

            Toast.makeText( applicationContext,getString(R.string.memory_recalled_toast),Toast.LENGTH_SHORT).show()
        }

        buttonMemoryAdd.setOnClickListener {
            buttonMemoryClear.isEnabled = true
            buttonMemoryRecall.isEnabled = true

            if (textViewCurrentNumber.length() != 0 && isRootNClicked) {
                text = textViewCurrentNumber.text.toString()
                expression = textViewHistoryText.text.toString() + text + ")"
                isRootNClicked = false
            } else if (textViewCurrentNumber.length() != 0 || textViewHistoryText.length() != 0) {
                text = textViewCurrentNumber.text.toString()
                expression = textViewHistoryText.text.toString() + text
            }

            if (expression.isEmpty()) expression = "0.0"
            try {
                result = ExtendedDoubleEvaluator().evaluate(expression)
                when {
                    result.toString() == "6.123233995736766E-17" -> {
                        result = 0.0
                    }
                    result.toString() == "1.633123935319537E16" -> textViewCurrentNumber.text = "infinity"
                }

                val thisOperationNumber: Double = result
                val newMemory = memory + thisOperationNumber

                Toast.makeText(applicationContext,getString(R.string.memory_added_toast) + "${
                            formatDoubleToString( memory )
                        } + ${formatDoubleToString(thisOperationNumber)} = ${formatDoubleToString(newMemory)}",
                        Toast.LENGTH_SHORT
                ).show()
                memory = newMemory
            } catch (e: Exception) {
                textViewCurrentNumber.text = "Invalid Expression"
                textViewHistoryText.text = ""
                expression = ""
                e.printStackTrace()
            }
            isBackspaceClicked = false
            isMemory = true
        }

        buttonMemorySubtract.setOnClickListener {

            if (textViewCurrentNumber.length() != 0 && isRootNClicked) {
                text = textViewCurrentNumber.text.toString()
                expression = textViewHistoryText.text.toString() + text + ")"
                isRootNClicked = false
            } else if (textViewCurrentNumber.length() != 0 || textViewHistoryText.length() != 0) {
                text = textViewCurrentNumber.text.toString()
                expression = textViewHistoryText.text.toString() + text
            }
            if (expression.isEmpty()) expression = "0.0"
            try {
                result = ExtendedDoubleEvaluator().evaluate(expression)
                when {
                    result.toString() == "6.123233995736766E-17" -> {
                        result = 0.0
                    }
                    result.toString() == "1.633123935319537E16" -> textViewCurrentNumber.text ="infinity"
                }
                val thisOperationNumber: Double = result
                val newMemory = memory - thisOperationNumber
                Toast.makeText( applicationContext,getString(R.string.memory_subtracted_toast) + "${
                            formatDoubleToString(memory)
                        } - ${formatDoubleToString(thisOperationNumber)} = ${formatDoubleToString(newMemory)}",
                        Toast.LENGTH_SHORT
                ).show()

                memory = newMemory

            } catch (e: Exception) {
                textViewCurrentNumber.text = "Invalid Expression"
                textViewHistoryText.text = ""
                expression = ""
                e.printStackTrace()
            }
            isBackspaceClicked = false
            isMemory = true
        }
    }

    fun onClick(v: View) {

        if (isMemory){
            textViewCurrentNumber.text = "0"
            textViewHistoryText.text = ""
        }

        if (textViewCurrentNumber.text.isEmpty() && textViewHistoryText.text.isEmpty()) isBackspaceClicked = false

        when (v.id) {
            R.id.btn_0 -> {
                numberClicked("0")
            }
            R.id.btn_1 ->{
                numberClicked("1")
            }
            R.id.btn_2 ->{
                numberClicked("2")
            }
            R.id.btn_3 ->{
                numberClicked("3")
            }
            R.id.btn_4 ->{
                numberClicked("4")
            }
            R.id.btn_5 ->{
                numberClicked("5")
            }
            R.id.btn_6 ->{
                numberClicked("6")
            }
            R.id.btn_7 ->{
                numberClicked("7")
            }
            R.id.btn_8 ->{
                numberClicked("8")
            }
            R.id.btn_9 ->{
                numberClicked("9")
            }
            R.id.btn_exponent ->{
                numberClicked("2.71")
            }
            R.id.btn_comma ->{
                if (isEqual){
                    textViewCurrentNumber.text = "0"
                    textViewHistoryText.text = ""
                    isEqual = false
                }
                if (countP == 0 && textViewCurrentNumber.text.isNotEmpty()) {
                    textViewCurrentNumber.text = textViewCurrentNumber.text.toString() + "."
                    countP++
                } else if (textViewCurrentNumber.text.isEmpty() && isBackspaceClicked){
                    textViewHistoryText.text = textViewHistoryText.text.toString() + "."
                    countP++
                } else if (textViewCurrentNumber.text.isEmpty()){
                    textViewCurrentNumber.text = "0."
                    countP++
                }
            }
            R.id.btn_c -> {
                textViewHistoryText.text = ""
                textViewCurrentNumber.text = "0"
                countP = 0
                expression = ""
                isBackspaceClicked = false
                isEqual = false
            }
            R.id.btn_backspace -> {
                text = textViewCurrentNumber.text.toString()
                if (text.isNotEmpty()) {
                    if (text.endsWith(".")) {
                        countP = 0
                    }
                    var newText: String = text.substring(0, text.length - 1)
                    //to delete the data contained in the brackets at once
                    if (text.endsWith(")")) {
                        val a: CharArray = text.toCharArray()
                        var pos = a.size - 2
                        var counter = 1
                        //to find the opening bracket position
                        var i = a.size - 2
                        while (i >= 0) {
                            when {
                                a[i] == ')' -> {
                                    counter++
                                }
                                a[i] == '(' -> {
                                    counter--
                                }
                                a[i] == '.' -> {
                                    countP = 0
                                }
                            }
                            //if opening bracket pair for the last bracket is found
                            if (counter == 0) {
                                pos = i
                                break
                            }
                            i--
                        }
                        newText = text.substring(0, pos)
                    }
                    if (newText == "-" || newText.endsWith("sqrt") || newText.endsWith("log")
                            || newText.endsWith("ln") || newText.endsWith("cbrt") || newText.endsWith("%") ) {
                        newText = ""
                    } else if (newText.endsWith("^") || newText.endsWith("/")) newText =
                        newText.substring(
                            0,
                            newText.length - 1
                        ) else if (newText.endsWith("e") || newText.endsWith("e^")) newText =
                        newText.substring(0, newText.length - 2)
                    textViewCurrentNumber.text = newText
                    if (newText == "") isBackspaceClicked = true

                } else if (textViewHistoryText.text.isNotEmpty() && textViewCurrentNumber.text == "" && isBackspaceClicked){
                    text = textViewHistoryText.text.toString()
                    if (text.endsWith(".")) {
                        countP = 0
                    }
                    var newText: String = text.substring(0, text.length - 1)
                    //to delete the data contained in the brackets at once
                    if (text.endsWith(")")) {
                        val a: CharArray = text.toCharArray()
                        var pos = a.size - 2
                        var counter = 1
                        var i = a.size - 2
                        while (i >= 0) {
                            when {
                                a[i] == ')' -> {
                                    counter++
                                }
                                a[i] == '(' -> {
                                    counter--
                                }
                                a[i] == '.' -> {
                                    countP = 0
                                }
                            }
                            //if opening bracket pair for the last bracket is found
                            if (counter == 0) {
                                pos = i
                                break
                            }
                            i--
                        }
                        newText = text.substring(0, pos)
                    }

                    if (newText == "-" || newText.endsWith("sqrt") || newText.endsWith("log") || newText.endsWith(
                                    "ln" ) || newText.endsWith("cbrt") || newText.endsWith("%") ) {
                        newText = ""
                    } else if (newText.endsWith("^") || newText.endsWith("/"))
                        newText = newText.substring( 0,  newText.length - 1 )
                    else if (newText.endsWith("e") || newText.endsWith("e^"))
                                newText = newText.substring(0, newText.length - 2)
                    textViewHistoryText.text = newText
                    if (newText == ""){
                        textViewCurrentNumber.text = "0"
                        isBackspaceClicked = false}
                }
                isEqual = false
            }
            R.id.btn_addition -> operationClicked("+")
            R.id.btn_subtraction -> operationClicked("-")
            R.id.btn_division -> operationClicked("/")
            R.id.btn_multiplication -> operationClicked("*")
            R.id.btn_root -> if (textViewCurrentNumber.text.isNotEmpty()) {
                text = "cbrt(${textViewCurrentNumber.text.toString()})"
                instanceOperationClicked(text)
            }
            R.id.btn_root_cube -> if (textViewCurrentNumber.text.isNotEmpty()) {
                text = "cbrt(${textViewCurrentNumber.text.toString()})"
                instanceOperationClicked(text)
            }
            R.id.btn_square -> if (textViewCurrentNumber.text.isNotEmpty()) {
                text = "(${textViewCurrentNumber.text.toString()})^2"
                instanceOperationClicked(text)
            }
            R.id.btn_cube -> if (textViewCurrentNumber.text.isNotEmpty()) {
                text = "(${textViewCurrentNumber.text.toString()})^3"
                instanceOperationClicked(text)
            }
            R.id.btn_exponent_degree -> if (textViewCurrentNumber.text.isNotEmpty()) {
                text = "e^(${textViewCurrentNumber.text.toString()})"
                instanceOperationClicked(text)
            }
            R.id.btn_degree -> if (textViewCurrentNumber.length() != 0) {
                text = textViewCurrentNumber.text.toString()
                textViewCurrentNumber.text = "($text)^"
                isBackspaceClicked = false
                isEqual = false
            }
            R.id.btn_root_n -> if (textViewCurrentNumber.length() != 0) {
                text = textViewCurrentNumber.text.toString()
                textViewCurrentNumber.text = "($text)^(1/"
                isBackspaceClicked = false
                isRootNClicked = true
                isEqual = false
            }
            R.id.btn_ln -> if (textViewCurrentNumber.length() != 0) {
                text = "ln(${textViewCurrentNumber.text.toString()})"
                instanceOperationClicked(text)
            }
            R.id.btn_log_ten -> if (textViewCurrentNumber.length() != 0) {
                text = "log(${textViewCurrentNumber.text.toString()})"
                instanceOperationClicked(text)
            }
            R.id.btn_fraction -> if (textViewCurrentNumber.length() != 0) {
                text = "1/(${textViewCurrentNumber.text.toString()})"
                instanceOperationClicked(text)
            }
            R.id.btn_factorial -> if (textViewCurrentNumber.length() != 0) {
                text = textViewCurrentNumber.text.toString()
                var res = ""
                try {
                    val cf = CalculateFactorial()
                    val fac = cf.getfactorial(formatStringToDouble(text).toInt())
                    textViewCurrentNumber.text = fac.toString()
                } catch (e: Exception) {
                    if (e.toString().contains("ArrayIndexOutOfBoundsException")) {
                        textViewCurrentNumber.text = "Result too big!"
                    } else textViewCurrentNumber.text = "Invalid!!"
                    e.printStackTrace()
                }
                isBackspaceClicked = false
                isEqual = false
            }
            R.id.btn_percentage -> if (textViewCurrentNumber.length() != 0) {
                if (isEqual){
                    textViewCurrentNumber.text = "0"
                    textViewHistoryText.text = ""
                    isEqual = false
                }
                text = textViewCurrentNumber.text.toString()
                val cf = CalculateFactorial()
                val arr = cf.getPercentage( formatStringToDouble(text),1.0  ) //java.lang.String.valueOf(ExtendedDoubleEvaluator().evaluate(text)).toDouble().toInt()
                textViewCurrentNumber.text = "$arr"
                isBackspaceClicked = false
                isEqual = false
            }
            R.id.btn_plus_minus -> if (textViewCurrentNumber.length() != 0) {
                val s: String = textViewCurrentNumber.text.toString()
                val arr = s.toCharArray()
                if (arr[0] == '-') textViewCurrentNumber.text = s.substring(1, s.length) else textViewCurrentNumber.text = "-$s"
                isEqual = false
            }
            R.id.btn_equal -> {
                var exp: String = ""
                if (textViewCurrentNumber.length() != 0 && isRootNClicked) {
                    text = textViewCurrentNumber.text.toString()
                    expression = textViewHistoryText.text.toString() + text + ")"
                    isRootNClicked = false
                } else if (textViewCurrentNumber.length() != 0 && textViewCurrentNumber.text.toString() == "0") {
                    expression = textViewHistoryText.text.toString()
                }else if (textViewCurrentNumber.length() != 0 || textViewHistoryText.length() != 0) {
                    text = textViewCurrentNumber.text.toString()
                    expression = textViewHistoryText.text.toString() + text
                }
                if (expression.startsWith("(")) {
                    exp = expression.padStart(expression.length + 1, '*')
                    exp = exp.padStart(exp.length + 1, '1')
                    println(exp)
                }
                textViewHistoryText.text = ""
                if (expression.isEmpty()) expression = "0.0"
                try {

                    result = if (exp == ""){
                        ExtendedDoubleEvaluator().evaluate(expression)
                    } else {
                        ExtendedDoubleEvaluator().evaluate(exp)
                    }

                    //insert expression and result in sqlite database if expression is valid and not 0.0
                    when {
                        result.toString() == "6.123233995736766E-17" -> {
                            result = 0.0
                            textViewCurrentNumber.text = result.toString() + ""
                        }
                        result.toString() == "1.633123935319537E16" -> textViewCurrentNumber.text = "infinity"
                        else -> textViewCurrentNumber.text = result.toString() + ""
                    }
                    if (expression != "0.0") dbHelper!!.insert(
                            "SCIENTIFIC",
                            "$expression = $result"
                    )
                    isEqual = true
                } catch (e: Exception) {
                    textViewCurrentNumber.text = "Invalid Expression"
                    textViewHistoryText.text = ""
                    expression = ""
                    e.printStackTrace()
                }
                isBackspaceClicked = false
            }
            R.id.btn_bracket1 ->{
                if (textViewCurrentNumber.length() != 0  && textViewCurrentNumber.text == "0"
                        && !textViewHistoryText.text.toString().endsWith(")")){
                    textViewHistoryText.text = textViewHistoryText.text.toString() + "("
                    textViewCurrentNumber.text = "0"
                    isBackspaceClicked = false
                }else if (textViewCurrentNumber.length() != 0 && textViewCurrentNumber.text != "0"
                        && !textViewHistoryText.text.toString().endsWith("+")
                        && !textViewHistoryText.text.toString().endsWith("-")
                        && !textViewHistoryText.text.toString().endsWith("*")
                        && !textViewHistoryText.text.toString().endsWith("/")
                        && !textViewHistoryText.text.toString().endsWith("(")
                        && textViewHistoryText.text.toString().endsWith(")")
                        && textViewHistoryText.length() != 0) {
                    textViewHistoryText.text = textViewHistoryText.text.toString() + "*" + "(" +  textViewCurrentNumber.text.toString()
                    textViewCurrentNumber.text = "0"
                    isBackspaceClicked = false
                } else if (textViewHistoryText.length() != 0
                        && !textViewHistoryText.text.toString().endsWith("+")
                        && !textViewHistoryText.text.toString().endsWith("-")
                        && !textViewHistoryText.text.toString().endsWith("*")
                        && !textViewHistoryText.text.toString().endsWith("/")
                        && !textViewHistoryText.text.toString().endsWith("(")
                        && textViewHistoryText.text.toString().endsWith(")")) {
                    textViewHistoryText.text = textViewHistoryText.text.toString() + "*" + "("
                    textViewCurrentNumber.text = "0"
                    isBackspaceClicked = false
                }  else if (textViewHistoryText.length() != 0 ) {
                    textViewHistoryText.text = textViewHistoryText.text.toString() + "("
                    isBackspaceClicked = false
                } else{
                    textViewHistoryText.text = "("
                    isBackspaceClicked = false
                }
                isEqual = false
            }
            R.id.btn_bracket2 -> {
                if (textViewCurrentNumber.length() != 0  && textViewCurrentNumber.text == "0"){
                    textViewHistoryText.text = textViewHistoryText.text.toString() + ")"
                    textViewCurrentNumber.text = "0"
                    isBackspaceClicked = false
                } else if (textViewCurrentNumber.length() != 0 && textViewCurrentNumber.text != "0" ) {
                    textViewHistoryText.text = textViewHistoryText.text.toString() + textViewCurrentNumber.text.toString() + ")"
                    textViewCurrentNumber.text = "0"
                    isBackspaceClicked = false
                }else if ( textViewHistoryText.length() != 0){
                    textViewHistoryText.text = textViewHistoryText.text.toString() + ")"
                    isBackspaceClicked = false
                }
                isEqual = false
            }

        }
        isMemory = false
    }

    private fun numberClicked(number: String){
        if (isEqual){
            textViewCurrentNumber.text = "0"
            textViewHistoryText.text = ""
            isEqual = false
        }
        when {
            textViewCurrentNumber.text == "0" -> {
                textViewCurrentNumber.text =  number
            }
            isBackspaceClicked -> textViewHistoryText.text = textViewHistoryText.text.toString() + number
            else -> textViewCurrentNumber.text = textViewCurrentNumber.text.toString() + number
        }
    }

    private fun instanceOperationClicked(op: String){
        try {
            result = ExtendedDoubleEvaluator().evaluate(op)
            when {
                result.toString() == "6.123233995736766E-17" -> {
                    result = 0.0
                    textViewCurrentNumber.text = result.toString() + ""
                }
                result.toString() == "1.633123935319537E16" -> textViewCurrentNumber.text =
                        "infinity"
                else -> textViewCurrentNumber.text = result.toString() + ""
            }
        } catch (e: Exception) {
            if (e.toString().contains("ArrayIndexOutOfBoundsException")) {
                textViewCurrentNumber.text = "Result too big!"
            } else textViewCurrentNumber.text = "Invalid!!"
            e.printStackTrace()
        }
        isBackspaceClicked = false
        isEqual = false
    }

    private fun clearEntry(newNumber: Double = 0.0) {
        currentNumber = newNumber
        textViewCurrentNumber.text = formatDoubleToString(newNumber)
    }

    private fun operationClicked(op: String) {

        if (textViewCurrentNumber.length() != 0 && isRootNClicked) {
            val text: String = textViewCurrentNumber.text.toString()
            textViewHistoryText.text = textViewHistoryText.text.toString() + text + ")" + op
            textViewCurrentNumber.text = ""
            countP = 0
            isRootNClicked = false
        } else if (textViewCurrentNumber.length() != 0 && textViewCurrentNumber.text != "0" ){
            val text: String = textViewCurrentNumber.text.toString()
            textViewHistoryText.text = textViewHistoryText.text.toString() + text + op
            textViewCurrentNumber.text = ""
            countP = 0
        }else if (textViewHistoryText.text.toString().endsWith(")") || isBackspaceClicked){
            val text: String = textViewHistoryText.text.toString()
            if (text.isNotEmpty()) {
                val newText = text + op
                textViewHistoryText.text = newText
            }
        }else {
            val text: String = textViewHistoryText.text.toString()
            if (text.isNotEmpty()) {
                val newText = text.substring(0, text.length - 1) + op
                textViewHistoryText.text = newText
            }
        }
        isBackspaceClicked = false
        isMemory = false
        isEqual = false
    }

    private fun useNumberFormat(): DecimalFormat {

        val symbols = DecimalFormatSymbols()
        symbols.decimalSeparator = ','

        val format = DecimalFormat("#.##############")
        format.decimalFormatSymbols = symbols

        return format
    }

    private fun formatDoubleToString(number: Double): String {
        return useNumberFormat().format(number)
    }

    private fun formatStringToDouble(number: String): Double {
        return useNumberFormat().parse(number).toDouble()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Safe call operator ? added to the variable before invoking the property instructs the compiler to invoke the property only if the value isn't null.
        return when (item.itemId) {
            R.id.menu_item_history -> {
                val i = Intent(this, History::class.java)
                i.putExtra("calcName", "SCIENTIFIC")
                startActivity(i)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("currentText", textViewCurrentNumber.text.toString())
        outState.putString("historyText", textViewHistoryText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        textViewCurrentNumber.text = savedInstanceState.getString("currentText", "0")
        textViewHistoryText.text = savedInstanceState.getString("historyText", "")
    }

    private fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
        // Function will be called only by the main thread to improve performance.
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(idRes) }
    }
}