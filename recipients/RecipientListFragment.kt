package com.example.pln8992.kotlindemo.app.recipients

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pln8992.kotlindemo.app.R
import com.example.pln8992.kotlindemo.app.databinding.RecipientListFragmentBinding
import com.example.pln8992.kotlindemo.app.recipient.RecipientFragment
import com.example.pln8992.kotlindemo.app.utils.UsaaViewModelFactory
import com.example.pln8992.kotlindemo.app.utils.inject
import com.example.pln8992.kotlindemo.app.utils.transaction
import kotlinx.android.synthetic.main.recipient_list_fragment.recipientList

class RecipientListFragment : Fragment() {

    private lateinit var viewModel: RecipientListViewModel

    val ctx by inject(Context::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout
        val binding = DataBindingUtil.inflate<RecipientListFragmentBinding>(inflater, R.layout.recipient_list_fragment, container, false)
        binding.setLifecycleOwner(this)

        // get the ViewModel
        viewModel = ViewModelProviders.of(this, UsaaViewModelFactory()).get(RecipientListViewModel::class.java)

        // pass viewModel to databinding
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setup RecyclerView
        recipientList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = RecipientListRecyclerViewAdapter(this@RecipientListFragment, viewModel)
        }
    }

    override fun onResume() {
        super.onResume()
        setupLiveDataObservers()
    }

    private fun setupLiveDataObservers() {
        // Initiate service call to populate data with no filter criteria
        viewModel.filterCriteria.value = ""

        // reset the selected recipient
        viewModel.selectedRecipient.value = null

        // Observe the selection from the list
        viewModel.selectedRecipient.observe(this, Observer { recipient ->
            if (recipient != null) {
                activity?.supportFragmentManager?.transaction {
                    replace(R.id.fragmentContainer, RecipientFragment.newInstance(recipient), "CURRENT")
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecipientListFragment()
    }
}