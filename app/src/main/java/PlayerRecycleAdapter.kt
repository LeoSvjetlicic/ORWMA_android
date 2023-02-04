package agency.five.codebase.android.orwim

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class PlayerRecycleAdapter(
    private var items: ArrayList<Player>,
    private var listener: ContentListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var mListener: ItemListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PlayerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycle_item, parent, false),
            mListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
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

    class PlayerViewHolder(private val view: View, clickListener: ItemListener) :
        RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
                Log.d("kansfliadcpad", "$adapterPosition")
            }
        }

        private val personImage = view.findViewById<ImageView>(R.id.personImage)
        private val personName = view.findViewById<TextView>(R.id.personName)
        private val deleteBtn = view.findViewById<ImageButton>(R.id.deleteButton)

        fun bind(index: Int, player: Player, listener: ContentListener) {
            Glide.with(view.context).load(player.imageUrl).apply(
                RequestOptions().transform(RoundedCorners(50))
            ).into(personImage)
            personName.text = player.name
            deleteBtn.setOnClickListener {
                listener.onItemButtonClick(index, player)
            }
        }
    }

    fun setOnItemClickListener(listener: ItemListener) {
        mListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateItems(newItems: ArrayList<Player>) {
        items = newItems
        notifyDataSetChanged()
    }

    interface ItemListener {
        fun onItemClick(index: Int)
    }

    interface ContentListener {
        fun onItemButtonClick(index: Int, item: Player)
    }
}

