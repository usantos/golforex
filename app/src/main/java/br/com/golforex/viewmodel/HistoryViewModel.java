package br.com.golforex.viewmodel;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.golforex.model.ExchangeRate;
import br.com.golforex.model.ForexRate;
import br.com.golforex.util.interfaces.StatusInterface;
import br.com.golforex.util.services.ForexApi;
import br.com.golforex.view.adapters.RateAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HistoryViewModel extends AndroidViewModel {

    public ObservableField<String> baseCurrency;
    public ObservableInt historyProgress;
    private RateAdapter adapter;
    private ForexRate forexRate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private Calendar myCalendar;
    private Context context;
    public ObservableField<String> datetext;
    private ArrayList<ExchangeRate> exRateList;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void init() {
        setupDatePicker();
        adapter = new RateAdapter();
        historyProgress = new ObservableInt(GONE);
        baseCurrency = new ObservableField<>();
        datetext = new ObservableField<>("Escolha a data");
    }

    public void openDatePicker(View view) {
        DatePickerDialog datePicker = new DatePickerDialog(view.getContext(), dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
    }

    private void setupDatePicker() {
        myCalendar = Calendar.getInstance();

        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String selectedDate = DateFormat.format("dd/MM/yyyy", myCalendar).toString();

                datetext.set(selectedDate);
            }

        };
    }

    public void searchHistoryNow() {
        String dateText = datetext.get();
        assert dateText != null;
        if (!dateText.equals("Escolha a data")) {
            resetAdapter();
            callApi(DateFormat.format("yyyy-MM-dd", myCalendar).toString());
        } else {
            Toast.makeText(context, "Escolha a data", Toast.LENGTH_SHORT).show();
        }
    }

    private void callApi(String searchDate) {

        historyProgress.set(VISIBLE);

        ForexApi forexApi = ForexApi.getInstance();

        forexApi.getHistorical(searchDate, new StatusInterface() {
            @Override
            public void success(JsonObject jsonObject) {

                forexRate = new Gson().fromJson(jsonObject, ForexRate.class);

                baseCurrency.set(forexRate.getBase());

                ForexRate.RatesBean ratesBean = forexRate.getRates();
                setupExchangeList(ratesBean);
            }

            @Override
            public void fail(String message) {
                stopProgressView();
                System.out.println("latestForexJobj e " + message);
            }
        });
    }

    private void resetAdapter() {
        if (exRateList != null) {
            exRateList.clear();
            adapter.notifyDataSetChanged();
        }
    }

    private void setupExchangeList(ForexRate.RatesBean ratesBean) {

        exRateList = new ArrayList<>();

        exRateList.add(new ExchangeRate("AUD", ratesBean.getAUD()));
        exRateList.add(new ExchangeRate("BGN", ratesBean.getBGN()));
        exRateList.add(new ExchangeRate("CAD", ratesBean.getCAD()));
        exRateList.add(new ExchangeRate("DKK", ratesBean.getDKK()));
        exRateList.add(new ExchangeRate("GBP", ratesBean.getGBP()));
        exRateList.add(new ExchangeRate("HKD", ratesBean.getHKD()));
        exRateList.add(new ExchangeRate("MYR", ratesBean.getMYR()));
        exRateList.add(new ExchangeRate("INR", ratesBean.getINR()));
        exRateList.add(new ExchangeRate("JPY", ratesBean.getJPY()));
        exRateList.add(new ExchangeRate("KRW", ratesBean.getKRW()));
        exRateList.add(new ExchangeRate("SGD", ratesBean.getSGD()));
        exRateList.add(new ExchangeRate("TRY", ratesBean.getTRY()));
        exRateList.add(new ExchangeRate("USD", ratesBean.getUSD()));
        exRateList.add(new ExchangeRate("ZAR", ratesBean.getZAR()));
        exRateList.add(new ExchangeRate("BRL", ratesBean.getBRL()));

        stopProgressView();

        if (exRateList.size() > 0) {
            adapter.setItemList(exRateList);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Sem resulados...", Toast.LENGTH_SHORT).show();
        }

    }

    public RateAdapter getAdapter() {
        return adapter;
    }

    private void stopProgressView() {
        historyProgress.set(GONE);
    }
}
