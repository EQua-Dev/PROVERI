package com.androidstrike.proveri

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidstrike.proveri.adapter.ChecksAdapter
import com.androidstrike.proveri.data.ChecksViewModel
import kotlinx.android.synthetic.main.fragment_history.view.*

class History : Fragment() {

    private lateinit var mChecksViewModel: ChecksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        val adapter = ChecksAdapter()
        val recyclerView = view.rv_history
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                layoutManager.orientation
            )
        )

        mChecksViewModel = ViewModelProvider(this).get(ChecksViewModel::class.java)
        mChecksViewModel.getAllChecks.observe(viewLifecycleOwner, Observer {check ->
            adapter.setData(check)
        })

        return view
    }

}