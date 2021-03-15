import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.model.Movimentation
import com.example.bluepocket.view.adapter.AllMovimentationsViewHolder
import kotlinx.android.synthetic.main.item_recycleview_demonstrativo.view.*
import java.util.*

class ReceitasMovimentationAdapter(
    val context: Context,
    val listMovimentation: ArrayList<Movimentation>) : RecyclerView.Adapter<DespesasMovimentationsViewHolder>() {

    private val layoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DespesasMovimentationsViewHolder {

        val view = layoutInflater.inflate(R.layout.item_recycleview_demonstrativo, parent, false)

        var holder = DespesasMovimentationsViewHolder(view)


        return holder
    }

    override fun getItemCount(): Int {
        if (listMovimentation.size < 5) {
            return listMovimentation.size
        } else {
            return 5
        }
    }

    override fun onBindViewHolder(holder: DespesasMovimentationsViewHolder, position: Int) {
        holder.movimentatioName.text = listMovimentation[position].name
        holder.movimentatioType.text = listMovimentation[position].type
        holder.movimentatioDate.text = listMovimentation[position].date
        holder.movimentatioValue.text = listMovimentation[position].value.toString()

    }


}

class ReceitasMovimentationsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val movimentatioName: TextView = view.findViewById(R.id.item_recycledemo_name)
    val movimentatioType: TextView = view.findViewById(R.id.item_recycledemo_type)
    val movimentatioDate: TextView = view.findViewById(R.id.item_recycledemo_date)
    val movimentatioValue: TextView = view.findViewById(R.id.item_recycledemo_value)
}