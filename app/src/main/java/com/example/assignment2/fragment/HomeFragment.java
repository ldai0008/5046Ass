package com.example.assignment2.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.example.assignment2.databinding.HomeFragmentBinding;
import com.example.assignment2.roomdatabase.viewmodel.PainRecordViewModel;
import com.example.assignment2.viewmodel.SharedViewModel;
import com.example.assignment2.weather.Main;
import com.example.assignment2.weather.RetrofitClient;
import com.example.assignment2.weather.Root;
import com.example.assignment2.weather.TempInterface;
import com.example.assignment2.weather.Weather;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    // the model of sharedViewModel
    // the addBinding of the fragment
    // log out button
    // the authen of the firebase
    // the type of the temperature UNIT
    // current location of the weather
    // weather APP ID
    // interface of the weather
    // the list of weather
    // the weather information in Main class
    // view model
    private SharedViewModel model;
    private HomeFragmentBinding addBinding;
    private Button logout;
    private FirebaseAuth authen;
    private static final String UNIT = "metric";
    private String keyword = "Jiaxing,CN";
    private static final String APP_ID = "f66bff2ae862a90743a9712f0dd423bb";
    private TempInterface tempInterface;
    private static List<Weather> weatherList;
    public static Main weatherMain;
    public static PainRecordViewModel painRecordViewModel;

    /**
     * Constructor for objects of class HomeFragment
     * have a non-parameterised (“default”) constructor
     */
    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the View for this fragment
        addBinding = HomeFragmentBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        // set the weather of current city
        tempInterface = RetrofitClient.getRetrofitService();
        Call<Root> rootCall = tempInterface.weatherSearch(UNIT, APP_ID, keyword);
        rootCall.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                // get the value of temperature, humidity and pressure
                if (response.isSuccessful()) {
                    weatherList = response.body().weather;
                    String weather = weatherList.get(0).getMain();
                    weatherMain = response.body().main;
                    double temp = weatherMain.getTemp();
                    int humidity = weatherMain.getHumidity();
                    int pressure = weatherMain.getPressure();
                    addBinding.weatherTemp.setText("Today's Weather: " + weather);
                    addBinding.weatherHumidity.setText("Temperature: " + temp + " °C " + "\nHumidity: " + humidity + " %" +
                            "\nPressure: " + pressure + " hPa");
                } else {
                    Log.i("ERROR", "No response!");
                }
            }
            // the function failed, there would be message
            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

        // log out the system
        logout = addBinding.bLogout;
        authen = FirebaseAuth.getInstance();
        logout.setOnClickListener(v -> {
            authen.signOut();
        });
        return view;
    }

    // initial the database view model
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        addBinding = null;
    }
}
