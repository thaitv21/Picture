package com.nullexcom.picture.ui.discover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.paging.DatabasePagingOptions
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.nullexcom.editor.ext.logD
import com.nullexcom.picture.R
import com.nullexcom.picture.data.Picture
import com.nullexcom.picture.ui.viewpicture.ViewPictureFragment
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverFragment : Fragment() {

    private var disposable: Disposable? = null
    private val viewModel: PictureViewModel by lazy { ViewModelProvider(this).get(PictureViewModel::class.java) }
    private lateinit var adapter: PicturePagingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = view.context

        val options = DatabasePagingOptions.Builder<Picture>().apply {
            setLifecycleOwner(this@DiscoverFragment)
            setQuery(viewModel.query, viewModel.config, Picture::class.java)
        }.build()
        adapter = PicturePagingAdapter(context, options)
        rvPictures.layoutManager = LinearLayoutManager(context)
        rvPictures.adapter = adapter
        swipeLayout.setOnRefreshListener {
            swipeLayout.isRefreshing = false
            adapter.refresh()
        }
        adapter.setOnItemClickListener { openPicture(it) }
    }

    private fun openPicture(picture: Picture) {
        val fragment = ViewPictureFragment()
        fragment.arguments = Bundle().apply {
            putSerializable("picture", picture)
        }
        fragment.show(parentFragmentManager, null)
    }
}