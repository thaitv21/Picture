package com.nullexcom.picture.component

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.view.menu.MenuBuilder
import com.nullexcom.editor.ext.OnItemClickListener
import com.nullexcom.picture.R
import java.util.*

class TabBar : LinearLayout {
    private val tabBarItemList: MutableList<TabBarItem> = ArrayList()
    private var currentIndex = 0
    private var onPageSelected: ((Int) -> Unit)? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        val menuInflater = MenuInflater(context)
        @SuppressLint("RestrictedApi") val menu: Menu = MenuBuilder(context)
        menuInflater.inflate(R.menu.tabbar, menu)
        val total = menu.size()
        for (i in 0 until total) {
            val menuItem = menu.getItem(i)
            val tabBarItem = addMenu(context, menuItem, i == currentIndex)
            tabBarItemList.add(tabBarItem)
            tabBarItem.setOnClickListener { selectMenu(i) }
            addView(tabBarItem)
        }
    }

    private fun selectMenu(i: Int) {
        tabBarItemList.forEachIndexed { index, tabBarItem ->
            tabBarItem.setActive(index == i)
        }
        onPageSelected?.invoke(i)
    }

    private fun addMenu(context: Context, menuItem: MenuItem, isActive: Boolean): TabBarItem {
        val tabBarItem = TabBarItem(context)
        tabBarItem.id = menuItem.itemId
        tabBarItem.setIcon(menuItem.icon)
        tabBarItem.setLabel(menuItem.title.toString())
        tabBarItem.setActive(isActive)
        val layoutParams = LayoutParams(0, LayoutParams.MATCH_PARENT, 1f)
        tabBarItem.layoutParams = layoutParams
        return tabBarItem
    }

    fun setPageSelected(onPageSelected: (Int) -> Unit) {
        this.onPageSelected = onPageSelected
    }
}