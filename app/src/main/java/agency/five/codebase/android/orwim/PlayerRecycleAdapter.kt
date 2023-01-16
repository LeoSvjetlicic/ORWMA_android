package agency.five.codebase.android.orwim

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.nio.file.Files.delete

class PlayerRecycleAdapter(
    val items: ArrayList<Player>,
    val listener: ContentListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is PlayerViewHolder -> {
                holder.bind(position, items[position], listener)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
        notifyItemRangeChanged(index, itemCount)
    }

    class PlayerViewHolder( val view: View): RecyclerView.ViewHolder(view) {
        private val personImage = view.findViewById<ImageView>(R.id.personImage)
        private val personName = view.findViewById<EditText>(R.id.personName)
        private val deleteBtn = view.findViewById<ImageButton>(R.id.deleteButton)

        fun bind(index: Int, person: Player, listener: ContentListener) {
            Glide.with(view.context).load(person.imageUrl).into(personImage)
            personName.setText(person.name)
            deleteBtn.setOnClickListener {
                listener.onItemButtonClick(index, person)
            }
        }
    }
    interface ContentListener {
        fun onItemButtonClick(index: Int, item: Player)
    }
}

