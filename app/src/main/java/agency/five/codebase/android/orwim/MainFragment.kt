package agency.five.codebase.android.orwim

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainFragment : Fragment(), PlayerRecycleAdapter.ContentListener {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerAdapter: PlayerRecycleAdapter

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var items = ArrayList<Player>()
        var cachedItems = items
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.main_fragment_recycler)
        db.collection("players")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (data in querySnapshot.documents) {
                    val player = Player(
                        id = data.id,
                        imageUrl = data.get("imageUrl").toString(),
                        description = data.get("description").toString(),
                        name = data.get("name").toString(),
                        nation = data.get("nation").toString()
                    )
                    items.add(player)
                }
                recyclerAdapter = PlayerRecycleAdapter(items, this@MainFragment)
                recyclerView?.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = recyclerAdapter
                }
                recyclerAdapter.setOnItemClickListener(listener = object :
                    PlayerRecycleAdapter.ItemListener {
                    override fun onItemClick(index: Int) {
                        val detailsFragment = DetailsFragment()
                        val bundle = Bundle()
                        bundle.putString("IMAGE", items[index].imageUrl)
                        bundle.putString("NAME", items[index].name)
                        bundle.putString("DESCRIPTION", items[index].description)
                        bundle.putString("NATION", items[index].nation)
                        detailsFragment.arguments = bundle

                        val fragmentTransaction: FragmentTransaction =
                            requireActivity().supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.mainFragment, detailsFragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }
                })
            }
        val addBtn = view.findViewById<Button>(R.id.add_button_main)
        addBtn.setOnClickListener {
            val fragmentTransaction: FragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.mainFragment, AddFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val switch = view.findViewById<Switch>(R.id.switch1)
        val searchText = view.findViewById<EditText>(R.id.search_input)
        val searchBtn = view.findViewById<ImageButton>(R.id.search_button)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switch.text = getString(R.string.byNation)
            } else {
                switch.text = getString(R.string.byName)
            }
        }
        searchBtn.setOnClickListener {
            items = cachedItems
            items = if (searchText.text.isEmpty()) {
                cachedItems
            } else {
                var tempStartWithPlayers: List<Player>
                var tempContainsPlayers: List<Player>
                if (switch.isChecked) {
                    tempStartWithPlayers =
                        items.filter { it.nation.startsWith(searchText.text, true) }
                            .sortedBy { it.nation }
                    tempContainsPlayers = items.filter {
                        it.nation.contains(
                            searchText.text,
                            true
                        ) && !tempStartWithPlayers.contains(it)
                    }
                        .sortedBy { it.nation }
                    (tempStartWithPlayers + tempContainsPlayers) as ArrayList<Player>

                } else {
                    tempStartWithPlayers =
                        items.filter { it.name.startsWith(searchText.text, true) }
                            .sortedBy { it.name }
                    tempContainsPlayers = items.filter {
                        it.name.contains(
                            searchText.text,
                            true
                        ) && !tempStartWithPlayers.contains(it)
                    }
                        .sortedBy { it.name }
                    (tempStartWithPlayers + tempContainsPlayers) as ArrayList<Player>
                }
            }
            recyclerAdapter.updateItems(items)
        }
        return view
    }

    override fun onItemButtonClick(index: Int, item: Player) {
        recyclerAdapter.removeItem(index)
        db.collection("players")
            .document(item.id)
            .delete()
    }
}
