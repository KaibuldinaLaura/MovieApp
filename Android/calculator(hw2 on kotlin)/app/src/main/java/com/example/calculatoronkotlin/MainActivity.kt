package com.example.calculatoronkotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {

    var mValue1: Float = 0.0f
    var mValue2: Float = 0.0f

    var mAdd: Boolean = false
    var mSub: Boolean = false
    var mMul: Boolean = false
    var mDiv: Boolean = false
    var mRoot: Boolean = false
    var mSquare: Boolean = false
    var isError: Boolean = false

    var operation: String = ""
    val STATE_MValueOne: String = "mValueOne"
    val STATE_MValueTwo: String = "mValueTwo"
    val STATE_MOperation: String = "operation"

    lateinit var crunchTextView: TextView

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat("mValue1", mValue1)
        outState.putFloat("mValue2", mValue2)
        outState.putString("operation", operation)
        outState.putString("textView", crunchTextView.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button0 = findViewById<Button>(R.id.button0)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)
        val button10 = findViewById<Button>(R.id.button10)
        val buttonAdd = findViewById<Button>(R.id.buttonadd)
        val buttonSub = findViewById<Button>(R.id.buttonsub)
        val buttonMul = findViewById<Button>(R.id.buttonmul)
        val buttonDiv = findViewById<Button>(R.id.buttondiv)
        val buttonC = findViewById<Button>(R.id.buttonC)
        val buttonEql = findViewById<Button>(R.id.buttoneql)
        val buttonAC = findViewById<Button>(R.id.buttonAC)
        val buttonRoot = findViewById<Button>(R.id.buttonRoot)
        val buttonSquare = findViewById<Button>(R.id.buttonSquare)
        this.crunchTextView = findViewById(R.id.textView)

        if (savedInstanceState != null) {
            mValue1 = savedInstanceState.getFloat("mValue1")
            mValue2 = savedInstanceState.getFloat("mValue2")
            operation = savedInstanceState.getString("operation").toString()
            var displayText: String? = savedInstanceState.getString("textView")
            this.crunchTextView.text = displayText
        }

        button0.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "0"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "0"
        })

        button1.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "1"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "1"
        })

        button2.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "2"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "2"
        })

        button3.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "3"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "3"
        })

        button4.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "4"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "4"
        })

        button5.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "5"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "5"
        })

        button6.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "6"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "6"
        })

        button7.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "7"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "7"
        })

        button8.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "8"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "8"
        })

        button9.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.equals("0") || isError || s.equals("error") || s.equals("Infinity")){
                crunchTextView.text = "9"
                isError = false
            }
            else crunchTextView.text = crunchTextView.text.toString() + "9"
        })

        button10.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(!s.contains(".") && !s.equals("error") && !s.equals("Infinity")){
                if(s.isEmpty()){
                    crunchTextView.text = "0."
                }
                else crunchTextView.text = crunchTextView.text.toString() + "."
            }
        })

        buttonC.setOnClickListener(View.OnClickListener {
            var s : String = crunchTextView.text.toString()
            if(s.isNotEmpty() && !s.equals("error") && !s.equals("Infinity")){
                crunchTextView.text = s.substring(0, s.length - 1)
            }
            else crunchTextView.text = null
        })

        buttonAC.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.isNotEmpty()){
                crunchTextView.text = null
            }
        })

        buttonAdd.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.isNotEmpty() && !mAdd && !s.equals("Infinity") && !s.equals("error")){
                operation += "+"
                mValue1 = s.toFloat()
                mAdd = true
                crunchTextView.text = null
            }
            else operation += "+"
        })

        buttonMul.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.isNotEmpty() && !mMul && !s.equals("Infinity") && !s.equals("error")){
                operation += "*"
                mValue1 = s.toFloat()
                mMul = true
                crunchTextView.text = null
            }
            else operation += "*"
        })

        buttonDiv.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.isNotEmpty() && !mDiv && !s.equals("Infinity") && !s.equals("error")){
                operation += "/"
                mValue1 = s.toFloat()
                mDiv = true
                crunchTextView.text = null
            }
            else operation += "/"
        })

        buttonRoot.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.isNotEmpty() && !mRoot && !s.equals("Infinity") && !s.equals("error")){
                operation += "v"
                mValue1 = s.toFloat()
                mRoot = true
            }
            else operation += "v"
        })

        buttonSquare.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.isNotEmpty() && !mSquare && !s.equals("Infinity") && !s.equals("error")){
                operation += "s"
                mValue1 = s.toFloat()
                mSquare = true
            }
            else operation += "s"
        })

        buttonSub.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.isNotEmpty() && !mSub && !s.equals("Infinity") && !s.equals("error")){
                operation += "-"
                mValue1 = s.toFloat()
                mSub = true
                crunchTextView.text = null
            }
            else operation += "-"
        })

        buttonEql.setOnClickListener(View.OnClickListener {
            val s : String = crunchTextView.text.toString()
            if(s.isNotEmpty() && !s.equals("error") && !s.equals("Infinity")){
                mValue2 = s.toFloat()

                if(operation.get(operation.length - 1) == '+'){
                    crunchTextView.text = (mValue1 + mValue2).toString()
                    mAdd = false
                }

                if(operation.get(operation.length - 1) == '-'){
                    crunchTextView.text = (mValue1 - mValue2).toString()
                    mSub = false
                }

                if(operation.get(operation.length - 1) == '*'){
                    crunchTextView.text = (mValue1 * mValue2).toString()
                    mMul = false
                }

                if(operation.get(operation.length - 1) == '/'){
                    if(mValue2 == 0.00f){
                        crunchTextView.text = "error"
                    }
                    else crunchTextView.text = (mValue1 / mValue2).toString()
                    mDiv = false
                }

                if(operation.get(operation.length - 1) == 'v'){
                    crunchTextView.text = (sqrt(mValue1)).toString()
                    mRoot = false
                }

                if(operation.get(operation.length - 1) == 's'){
                    crunchTextView.text = (mValue1 * mValue1).toString()
                    mSquare = false
                }
            }
        })
    }
}
