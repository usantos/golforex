package br.com.golforex.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import br.com.golforex.model.ExchangeRate;
import br.com.golforex.model.ForexRate;
import br.com.golforex.util.interfaces.StatusInterface;
import br.com.golforex.util.services.ForexApi;
import br.com.golforex.view.adapters.RateAdapter;

public class MainViewModel extends AndroidViewModel {

    public ObservableField<String> baseCurrency;
    public ObservableField<String> currencyConverted;
    public ObservableField<String> currencySelectedValue;
    public ObservableInt mainProgress;
    private RateAdapter adapter;
    private Context context;
    private ForexRate forexRate;
    public ObservableField<String> exchangeText;
    public ObservableField<String> valueToConvert;
    private ArrayList<ExchangeRate> exRateList;
    public final ObservableBoolean isValid = new ObservableBoolean();

    public MainViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void init() {
        adapter = new RateAdapter();
        mainProgress = new ObservableInt(View.GONE);
        baseCurrency = new ObservableField<>();
        currencyConverted = new ObservableField<>();
        currencySelectedValue = new ObservableField<>();
        exchangeText = new ObservableField<>("");
        valueToConvert = new ObservableField<>("");
        isValid.set(false);
    }

    public TextWatcher valueToConvertWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                valueToConvert.set(charSequence.toString().toUpperCase());
                valueToConvert.notifyChange();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                validation();
            }
        };
    }

    private void validation() {
        boolean isValidExchange = !TextUtils.isEmpty(exchangeText.get());
        isValid.set(isValidExchange);
    }

    public void onSelectItem(AdapterView<?> parent, View view, int pos, long id) {
        currencySelectedValue.set(parent.getSelectedItem().toString());
    }

    public void exchangeConvert() {

        mainProgress.set(View.VISIBLE);

        final double value;
        final String toCurr = currencySelectedValue.get();
        assert valueToConvert != null;
        if (valueToConvert.get().isEmpty()) {
            Toast.makeText(context, "Digite o valor", Toast.LENGTH_SHORT).show();
            return;
        } else {
            value = Double.valueOf(Objects.requireNonNull(valueToConvert.get()));
        }

        ForexApi forexApi = ForexApi.getInstance();
        forexApi.getLatest(new StatusInterface() {
            @Override
            public void success(JsonObject jsonObject) {
                forexRate = new Gson().fromJson(jsonObject, ForexRate.class);
                try {
                    JSONObject obj = new JSONObject(String.valueOf(jsonObject));
                    JSONObject b = obj.getJSONObject("rates");
                    String val = b.getString(toCurr);
                    double output = value * Double.valueOf(val);
                    currencyConverted.set(String.valueOf(output));

                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                    stopProgressView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void fail(String message) {
                stopProgressView();
                Log.e("MAIN-CallApi", message);

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }

    private void stopProgressView() {
        mainProgress.set(View.GONE);
    }
}
