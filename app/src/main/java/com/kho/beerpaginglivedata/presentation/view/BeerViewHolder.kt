package com.kho.beerpaginglivedata.presentation.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.kho.beerpaginglivedata.R
import com.kho.beerpaginglivedata.data.model.BeerResult
import kotlinx.android.synthetic.main.item_beer_layout.view.*
import java.text.DecimalFormat

class BeerViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun bindTo(item: BeerResult?) {
        itemView.tvNameBeer.text = item?.name
        itemView.tvDetail.text = item?.tags
        itemView.tvPrice.text = "$ "+convertCentToDollar(item?.priceInCents?:0)
        Glide.with(itemView.context).load(item?.imageThumbUrl).into(itemView.imgBeer)
    }

    companion object {
        fun create(parent: ViewGroup): BeerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_beer_layout, parent, false)
            return BeerViewHolder(view)
        }
    }

    private fun convertCentToDollar( cent:Int):String{
        val formatter = DecimalFormat("#0.00")
        val dDollar = cent*0.01
        return formatter.format(dDollar)

    }
}