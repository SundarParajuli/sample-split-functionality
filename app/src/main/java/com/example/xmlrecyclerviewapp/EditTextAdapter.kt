import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xmlrecyclerviewapp.Person
import com.example.xmlrecyclerviewapp.R

class PersonAdapter(
    private val personList: MutableList<Person>,
    private val totalAmount: Double,
    private val onAmountChanged: (Person) -> Unit
) : RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {


    private var disableTextWatcher: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return PersonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val person = personList[position]
        holder.personName.text = person.name
        val formatted = String.format("%.2f", person.amount)
        holder.amount.setText(formatted)



        holder.amount.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                holder.amount.addTextChangedListener(holder.textWatcher)
            } else {
                holder.amount.removeTextChangedListener(holder.textWatcher)
            }
        }
        if (disableTextWatcher.not()) {
            holder.textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val amount = s.toString().toDouble()
                    onAmountChanged(person.copy(amount = amount))
                }
            }
        }
    }

    override fun getItemCount() = personList.size
    fun removeTextWatcher(b: Boolean) {
        this.disableTextWatcher = b
    }

    class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val personName: TextView = view.findViewById(R.id.person_name)
        val amount: EditText = view.findViewById(R.id.amount)
        lateinit var textWatcher: TextWatcher
    }
}
