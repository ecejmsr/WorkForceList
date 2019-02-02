package com.example.pln8992.kotlindemo.app.recipients

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pln8992.kotlindemo.app.R
import com.example.pln8992.kotlindemo.lib.recipients.model.Recipient

class RecipientListRecyclerViewAdapter(
    lifecycleOwner: LifecycleOwner,
    private val viewModel: RecipientListViewModel
) : RecyclerView.Adapter<RecipientListRecyclerViewAdapter.RecipientViewHolder>() {

    private val sortedListCallback = object : SortedList.Callback<Recipient>() {

        override fun areItemsTheSame(item1: Recipient?, item2: Recipient?): Boolean {
            if (item1 == null && item2 == null) return false
            if (item1 == null && item2 != null) return false
            if (item1 != null && item2 == null) return false
            return item1 === item2
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int) {
            notifyItemRangeChanged(position, count)
        }

        override fun onInserted(position: Int, count: Int) {
            notifyItemRangeInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            notifyItemRangeRemoved(position, count)
        }

        override fun compare(item1: Recipient?, item2: Recipient?): Int {
            return RecipientSortComparator().compare(item1, item2)
        }

        override fun areContentsTheSame(oldItem: Recipient?, newItem: Recipient?): Boolean {
            return oldItem?.equals(newItem) ?: false
        }
    }

    private val sortedList = SortedList<Recipient>(Recipient::class.java, sortedListCallback)

    init {

        viewModel.recipients.observe(lifecycleOwner, Observer { recipients ->

            // Begin Updates
            sortedList.beginBatchedUpdates()

            when {
                recipients == null || recipients.isEmpty() -> {
                    sortedList.clear()

                    // Show NO ITEMs view

                }
                else -> {

                    // Begin Updates
                    sortedList.beginBatchedUpdates()

                    // remove items from sortedList that are not in the passed in via list
                    for (i in sortedList.size() - 1 downTo 0) {
                        val recipient = sortedList.get(i)
                        if (!recipients.contains(recipient)) {
                            sortedList.remove(recipient)
                        }
                    }

                    // add items to sortedList that are in list but not in sortedList
                    val toBeAdded: MutableList<Recipient> = ArrayList()
                    for (recipient in recipients) {
                        if (sortedList.indexOf(recipient) == -1) {
                            toBeAdded.add(recipient)
                        }
                    }
                    sortedList.addAll(toBeAdded)
                }
            }

            // End Updates
            sortedList.endBatchedUpdates()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recipient_list_item, parent, false)
        return RecipientViewHolder(view, viewModel)
    }

    override fun getItemCount(): Int {
        return sortedList.size()
    }

    override fun onBindViewHolder(viewHolder: RecipientViewHolder, position: Int) {
        val recipient = sortedList[position]
        viewHolder.bind(recipient)
    }

    class RecipientViewHolder(
        private val root: View,
        private val viewModel: RecipientListViewModel
    ) : RecyclerView.ViewHolder(root) {

        private val lastName: TextView = root.findViewById(R.id.lastName)
        private val firstName: TextView = root.findViewById(R.id.firstName)
        private val emailAddress: TextView = root.findViewById(R.id.emailAddress)
        private val phoneNumber: TextView = root.findViewById(R.id.phoneNumber)

        fun bind(recipient: Recipient) {
            lastName.text = recipient.lastName
            firstName.text = recipient.firstName
            emailAddress.text = recipient.emailAddress
            phoneNumber.text = recipient.phoneNumber
            root.setOnClickListener {
                // Update the ViewModel with the selected recipient
                viewModel.selectedRecipient.postValue(recipient)
            }
        }
    }
}