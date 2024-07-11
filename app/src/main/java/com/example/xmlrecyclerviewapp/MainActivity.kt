package com.example.xmlrecyclerviewapp

import PersonAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var totalAmountTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var personAdapter: PersonAdapter
    private var totalAmount = 100.0
    private val personList = mutableListOf(
        Person("Alice", 0.0),
        Person("Bob", 0.0),
        Person("Charlie", 0.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        totalAmountTextView = findViewById(R.id.total_amount)
        recyclerView = findViewById(R.id.recycler_view)
        var splitAmount = (totalAmount/personList.size)
        val mainEdittext = findViewById<EditText>(R.id.main_field)
        mainEdittext.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                totalAmount = editable.toString().toDouble()
                splitAmount = (totalAmount/personList.size)
                personList.onEach {
                    it.amount = splitAmount
                }

                personAdapter.removeTextWatcher(true)

                if (recyclerView.isComputingLayout.not()){
                    for (i in personList.indices){
                        personAdapter.notifyItemChanged(i)
                    }
                }
                personAdapter.removeTextWatcher(false)
            }

        })

        personList.onEach {
           it.amount = splitAmount
        }
        personAdapter = PersonAdapter(personList, totalAmount) {
            println("triggered")
            dowork(it)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = personAdapter

        //splitEqually()
        totalAmountTextView.text = "Total Amount: $%.2f".format(totalAmount)
    }

    private fun dowork(person: Person) {
        val remaining = (totalAmount - person.amount)/2
        val positions = mutableListOf<Int>()

        personList.onEach {
            if (it.name != person.name) {
                it.amount = remaining
                positions.add(personList.indexOf(it))
            }
        }

        personAdapter.removeTextWatcher(true)
        personList.forEach {
            println(it)
        }
        println("position is ${positions.size}")
        if (recyclerView.isComputingLayout.not()){
            for (i in positions){
                personAdapter.notifyItemChanged(i)
            }
        }
        personAdapter.removeTextWatcher(false)
    }

    private fun splitEqually() {
        val splitAmount = totalAmount / personList.size
        personList.forEach { it.amount = splitAmount }
    }

}
