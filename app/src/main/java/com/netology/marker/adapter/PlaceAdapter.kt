package com.netology.marker.adapter

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.netology.marker.databinding.CardPlacePointBinding
import com.netology.marker.dto.Place
import androidx.recyclerview.widget.ListAdapter
import com.netology.marker.R
import com.netology.marker.databinding.MapShowPointBinding

interface OnInteractionListener {
    fun onEdit(place: Place)
    fun onRemove(place: Place)
    fun onShow(place: Place)
}

class PlaceAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Place, PlaceAdapter.PlaceCardViewHolder>(PlaceCardViewHolder.PlaceDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceCardViewHolder {
        val cardBinding =
            CardPlacePointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val mapBinding = MapShowPointBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaceCardViewHolder(
            cardBinding,
            mapBinding,
            onInteractionListener
        )
    }

    override fun onBindViewHolder(holder: PlaceCardViewHolder, position: Int) {
        val place = getItem(position)
        holder.bind(place)
    }

    class PlaceCardViewHolder(
        private val cardPlaceBinding: CardPlacePointBinding,
        private val mapBinding: MapShowPointBinding,
        private val onInteractionListener: OnInteractionListener
    ) : RecyclerView.ViewHolder(cardPlaceBinding.root) {
        fun bind(place: Place) {
            cardPlaceBinding.apply {
                placeName.text = place.name
                placeDescription.text = place.description

                menu.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.place_card_option)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.edit -> {
                                    onInteractionListener.onEdit(place)
                                    true
                                }

                                R.id.remove -> {
                                    onInteractionListener.onRemove(place)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }

                placeCard.setOnClickListener {
                    onInteractionListener.onShow(place)
                }
            }

//            mapBinding.apply {
//                placeName.text = place.name
//                placeDescription.text = place.description
//                placeCoordinates.text = place.coordinates.toString()
//
//                menu.setOnClickListener {
//                    PopupMenu(it.context, it).apply {
//                        inflate(R.menu.place_card_option)
//                        setOnMenuItemClickListener { item ->
//                            when (item.itemId) {
//                                R.id.edit -> {
//                                    onInteractionListener.onEdit(place)
//                                    true
//                                }
//
//                                R.id.remove -> {
//                                    onInteractionListener.onRemove(place)
//                                    true
//                                }
//
//                                else -> false
//                            }
//                        }
//                    }.show()
//                }
//
//            }
        }

        class PlaceDiffCallback : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
                if (oldItem::class != newItem::class) {
                    return false
                }

                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
                return oldItem == newItem
            }


        }

    }
}
