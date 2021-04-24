package com.example.cleanarchitechture.presentation.ui

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
import com.example.cleanarchitechture.R
import com.example.cleanarchitechture.domain.entity.Person
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
    private lateinit var stateText: TextView
    private lateinit var addPersonBtn: Button
    private lateinit var personsList: RecyclerView
    private var allPersonsAdapter = PersonAdapter()
    private lateinit var topPersonsList: RecyclerView
    private var topPersonsAdapter = PersonAdapter()

    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
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
        })

        viewModel.getTopPersons().observe(viewLifecycleOwner, Observer {
            topPersonsAdapter.setData(it)
        })

        viewModel.getError().observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        })

        viewModel.calculationState.observe(viewLifecycleOwner, {
            when (it) {
                CalculationState.Free -> addPersonBtn.isEnabled = true
                else -> addPersonBtn.isEnabled = false
            }
            stateText.text = getString(
                when (it) {
                    CalculationState.Free -> R.string.state_free
                    CalculationState.Loading -> R.string.state_calculating
                    CalculationState.Result -> R.string.state_result
                }
            )
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nameInput = view.findViewById(R.id.name_input)
        ratingInput = view.findViewById(R.id.rating_input)
        addPersonBtn = view.findViewById(R.id.add_person_btn)
        personsList = view.findViewById(R.id.persons_list)
        topPersonsList = view.findViewById(R.id.top_rating_persons_list)
        stateText = view.findViewById(R.id.state_text)

        personsList.layoutManager = LinearLayoutManager(requireContext())
        personsList.adapter = allPersonsAdapter
        allPersonsAdapter.setListener(this)

        topPersonsList.layoutManager = LinearLayoutManager(requireContext())
        topPersonsList.adapter = topPersonsAdapter
       // topPersonsAdapter.setListener(this)
    }

    override fun onItemClick(person: Person) {
        viewModel.onPersonSelected(person)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        allPersonsAdapter.setListener(null)
    }
}
