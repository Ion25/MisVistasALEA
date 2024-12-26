package com.example.alea.ui.voice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseExpandableListAdapter

// Clase que representa los ítems de comida
// data class FoodItem(val nombre: String, val cantidad: Int, val calorias: Int)

// Adaptador personalizado para ExpandableListView
class FoodExpandableListAdapter(
    private val context: Context,
    private val grupos: List<String>,
    private val datosHijos: List<List<FoodItem>>
) : BaseExpandableListAdapter() {

    override fun getGroupCount(): Int {
        return grupos.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return datosHijos[groupPosition].size
    }

    override fun getGroup(groupPosition: Int): Any {
        return grupos[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return datosHijos[groupPosition][childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?
    ): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_expandable_list_item_1, parent, false)
        val groupName = getGroup(groupPosition) as String
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = groupName
        return view
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?,
        parent: ViewGroup?
    ): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val foodItem = getChild(groupPosition, childPosition) as FoodItem
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = "${foodItem.nombre} - ${foodItem.cantidad}g - ${foodItem.calorias} kcal"
        return view
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}
