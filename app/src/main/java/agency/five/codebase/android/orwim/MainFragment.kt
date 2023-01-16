package agency.five.codebase.android.orwim

import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainFragment : Fragment(), PlayerRecycleAdapter.ContentListener {
    private val  db = FirebaseFirestore.getInstance()
    private lateinit var recyclerAdapter: PlayerRecycleAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.main_fragment_recycler)
        db.collection("players")
            .get()
            .addOnSuccessListener {
                val items:ArrayList<Player> = ArrayList()
                for (data in it.documents) {
                    val player = data.toObject(Player::class.java)
                    if (player != null) {
                        player.id = data.id
                        items.add(player)
                    }
                }
                recyclerAdapter = PlayerRecycleAdapter(items, this@MainFragment)
                recyclerView?.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = recyclerAdapter
                }
            }
        val addBtn= view?.findViewById<Button>(R.id.add_button)
        val item = Player(
            id="",
            // tu sam stao
        )
    }

    override fun onItemButtonClick(index: Int, item: Player) {
        TODO("Not yet implemented")
    }

}
