package com.example.testexomind.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testexomind.R
import com.example.testexomind.databinding.FragmentWeatherBinding
import com.example.testexomind.model.WeatherResponse
import com.example.testexomind.service.ApiClient
import com.example.testexomind.service.ApiService
import kotlinx.coroutines.*


class WeatherFragment : Fragment() {
    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    private val adapterWeather = AdapterWeather(::onClickWeatherItem)

    private fun onClickWeatherItem(weatherResponse: WeatherResponse) {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val weatherList: MutableList<WeatherResponse> = ArrayList()
    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Rendre le bouton recommencer invisible à l'affichage de l'écran
        binding.btnRestart.visibility=View.GONE

        //Fonction de recuperation de la météo
        getWeather(0)
        //Fonction d'affichage des messages de progression
        getProgressMessage(0)
        var counter = 0
        //Evolution de la barre de progression
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                counter += 1
                binding.progressView.labelText = ((counter / 60.0) * 100).toInt().toString() + "%"
                binding.progressView.progress = ((counter / 60.0) * 100).toFloat()
            }

            override fun onFinish() {
                binding.progressView.labelText = "100%"
                binding.btnRestart.visibility=View.VISIBLE
                binding.progressView.visibility=View.GONE
                binding.txtProgress.visibility=View.GONE
                adapterWeather.submitList(weatherList)
                scopeWeather.cancel()
                scopeMessage.cancel()
            }
        }.start()
        //Initialisation du RecyclerView pour l'affichage de la météo des différentes villes
        binding.weatherRecyclerView.adapter = adapterWeather
        binding.weatherRecyclerView.itemAnimator = null
        binding.weatherRecyclerView.adapter=adapterWeather
        //Redirection vers la page de démarrage
        binding.btnRestart.setOnClickListener {
            findNavController().popBackStack(R.id.start_fragment, false)
        }
    }
    private val scopeWeather = CoroutineScope(Dispatchers.IO)
    private val scopeMessage = CoroutineScope(Dispatchers.IO)
    @DelicateCoroutinesApi
    private fun getWeather(i: Int) {
        //Liste des villes
        val cityList: List<String> = listOf("Rennes", "Paris", "Nantes", "Bordeaux", "Lyon")
        val weatherApi = ApiClient.client!!.create(ApiService::class.java)
        if (i == 0) {
            GlobalScope.launch {
                val result = weatherApi.getWeather(cityList[i])
                weatherList.add(result)
            }
        }
        val next = i + 1
        //Un scope pour recuperer les données météo d'une ville tous les 10 secondes
        scopeWeather.launch {
            delay(10000)
            CoroutineScope(Dispatchers.Main).launch {
                val result = weatherApi.getWeather(cityList[next])
                weatherList.add(result)
                if (next < cityList.size-1)
                    getWeather(next)
            }
        }
    }
    @DelicateCoroutinesApi
    private fun getProgressMessage(i: Int) {
        val messageList: List<String> = listOf("Nous téléchargeons les données …", "C’est presque fini …", " Plus que quelques secondes avant d’avoir le résultat …")
        if (i == 0) {
            binding.txtProgress.text=messageList[i]
        }
        val next = i + 1
        //Un scope pour changer de messages tous les 6 secondes
        scopeMessage.launch {
            delay(6000)
            CoroutineScope(Dispatchers.Main).launch {
                binding.txtProgress.text=messageList[next]
                if (next < messageList.size-1){
                    getProgressMessage(next)
                }else{
                    getProgressMessage(-1)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}