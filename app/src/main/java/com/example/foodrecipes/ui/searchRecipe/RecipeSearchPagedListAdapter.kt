package com.example.foodrecipes.ui.searchRecipe

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.Repository.NetworkState
import com.example.foodrecipes.ui.recipeDetails.RecipeActivity
import com.example.foodrecipes.vo.Recipe
import kotlinx.android.synthetic.main.activity_recipe_list.view.*
import kotlinx.android.synthetic.main.layout_recipe_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.*
import kotlinx.android.synthetic.main.network_state_item.view.*


class RecipeSearchPagedListAdapter(private val context: Context) :
    PagedListAdapter<Recipe, RecyclerView.ViewHolder>(RecipeDiffCallBack()) {

    val RECIPE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null
    private lateinit var view: View
    private lateinit var layoutInflater : LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        layoutInflater = LayoutInflater.from(parent.context)

        if (viewType == RECIPE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.layout_recipe_list_item, parent, false)
            return RecipeItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (getItemViewType(position) == RECIPE_VIEW_TYPE) {
            (holder as RecipeItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }


    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            RECIPE_VIEW_TYPE
        }
    }


    class RecipeDiffCallBack : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem == newItem
        }

    }

    class RecipeItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var row: Recipe? = null

        fun bind(recipe: Recipe?, context: Context) {
            this.row = recipe
            itemView.recipe_title.text = recipe?.title
            itemView.recipe_publisher.text = recipe?.publisher
            itemView.recipe_social_score.text = recipe?.social_rank?.toInt().toString()

            Glide.with(itemView.context)
                .load(recipe?.image_url)
                .into(itemView.recipe_image)


            itemView.recipe_image.setOnClickListener({

                val context = itemView.context
                val showIntent = Intent(context, RecipeActivity::class.java)

                showIntent.putExtra(STATS_KEY, row?.recipe_id)
                context.startActivity(showIntent)
                Log.d("recipeItemViewHolder", "CLICK!")
            })

        }

        companion object {
            private val STATS_KEY = "RECIPE_ID"
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            } else {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            } else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }


    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                             //hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())    //remove the progressbar at the end
            } else {                                       //hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())   //add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) { //hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.ENDOFLIST)
            notifyItemChanged(itemCount - 1)       //add the network message at the end
        }

    }

    fun clear() {
        val size = super.getItemCount()
        notifyDataSetChanged()
        notifyItemRangeRemoved(0, size)
    }

//    var parent = findViewById(android.R.id.content) as ViewGroup
    fun setErrorNetworkState(networkState: NetworkState?, parent: ViewGroup){
        if(networkState == NetworkState.ERROR){

            layoutInflater = LayoutInflater.from(parent.context)
            view = layoutInflater.inflate(R.layout.network_state_item, null)
            view.dotted_progress.visibility = View.VISIBLE
            var obj  = NetworkStateItemViewHolder(view)
            setNetworkState(networkState)
            obj.bind(networkState)
        }
    }

}