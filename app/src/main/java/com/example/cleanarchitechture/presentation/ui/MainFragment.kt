package com.example.cleanarchitechture.presentation.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cleanarchitechture.R
import com.example.cleanarchitechture.domain.entity.Person
import com.example.cleanarchitechture.presentation.AddPersonService
import com.example.cleanarchitechture.presentation.Constants
import com.example.cleanarchitechture.presentation.adapter.ItemClickListener
import com.example.cleanarchitechture.presentation.adapter.PersonAdapter
import com.example.cleanarchitechture.presentation.viewmodel.CalculationState
import com.example.cleanarchitechture.presentation.viewmodel.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainFragment : Fragment(), ItemClickListener {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var nameInput: EditText
    private lateinit var ratingInput: EditText
    private lateinit var addPersonBtn: Button
    private lateinit var personsList: RecyclerView
    private var allPersonsAdapter = PersonAdapter()

    private lateinit var refresher: SwipeRefreshLayout

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val batteryLeverBroadcastReceiver: BatteryLeverBroadcastReceiver by lazy {
        BatteryLeverBroadcastReceiver()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()

        requireContext().registerReceiver(
            batteryLeverBroadcastReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    override fun onPause() {
        super.onPause()
        requireActivity().unregisterReceiver(
            batteryLeverBroadcastReceiver
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        nameInput.doAfterTextChanged {
            viewModel.name = it.toString()
        }
        ratingInput.doAfterTextChanged {
            viewModel.rating = it.toString()
        }
        refresher.setOnRefreshListener {
            viewModel.updatePersons()
        }

        val observable = Observable.create<Unit> { emitter ->
            addPersonBtn.setOnClickListener {
                emitter.onNext(Unit)
            }
        }
        val subscribe = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { viewModel.addPerson() }
        disposable.add(subscribe)

        viewModel.getPersons().observe(viewLifecycleOwner, {
            allPersonsAdapter.setData(it)
            refresher.isRefreshing = false
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        viewModel.getPersonDataReady().observe(viewLifecycleOwner, Observer {
            val addPersonServiceIntent =
                Intent(requireContext(), AddPersonService::class.java).apply {
                    this.putExtra(Constants.NAME, it.first)
                    this.putExtra(Constants.RATING, it.second)
                }
            requireActivity().startService(addPersonServiceIntent)
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInput = view.findViewById(R.id.name_input)
        ratingInput = view.findViewById(R.id.rating_input)
        addPersonBtn = view.findViewById(R.id.add_person_btn)
        personsList = view.findViewById(R.id.persons_list)

        personsList.layoutManager = LinearLayoutManager(requireContext())
        personsList.adapter = allPersonsAdapter
        allPersonsAdapter.setListener(this)

        refresher = view.findViewById(R.id.refresher)
    }

    override fun onItemClick(person: Person) {
        viewModel.onPersonSelected(person)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        allPersonsAdapter.setListener(null)
    }

    inner class BatteryLeverBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val level: Int = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            ratingInput.setText(level.toString())
        }
    }
}
