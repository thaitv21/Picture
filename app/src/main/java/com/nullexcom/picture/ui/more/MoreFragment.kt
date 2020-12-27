package com.nullexcom.picture.ui.more

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nullexcom.picture.ext.dp
import com.nullexcom.picture.BaseEditorFragment
import com.nullexcom.picture.MenuAdapter
import com.nullexcom.picture.R
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.fragment_more.imgClose
import kotlinx.android.synthetic.main.fragment_more.photoView

class MoreFragment : BaseEditorFragment() {

    companion object {
        const val QUOTE = 1
        const val SIGN = 2
    }

    private lateinit var items : List<MenuAdapter.Item>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        items = listOf(
                MenuAdapter.Item(QUOTE, getString(R.string.label_quote), R.drawable.ic_quote),
                MenuAdapter.Item(SIGN, getString(R.string.label_sign), R.drawable.ic_logo)
        )
        rvMenu.adapter = MenuAdapter(view.context, items).apply { setOnItemClickListener { selectPage(it) } }
        rvMenu.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        photoView.setImageBitmap(editorViewModel().getFilteredBitmap())
        imgClose.setOnClickListener { showMainPage() }
    }

    private fun selectPage(index: Int) {
        if (index == 0) {
            showTextPage()
        }
    }

    private fun showTextPage() {
        rvMenu.visibility = View.GONE
        pageText.visibility = View.VISIBLE
        imgClose.animate().translationY(0f).start()
    }

    private fun showMainPage() {
        imgClose.animate().translationY(dp(30f).toFloat()).start()
        rvMenu.visibility = View.VISIBLE
        pageText.visibility = View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onNewBitmap(bitmap: Bitmap) {

    }
}