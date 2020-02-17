package br.com.golforex.view.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import br.com.golforex.R;
import br.com.golforex.databinding.ActivityHistoryBinding;
import br.com.golforex.viewmodel.HistoryViewModel;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataBinding(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setSubtitle("Histórico");
    }

    private void initDataBinding(Bundle savedInstanceState) {
        ActivityHistoryBinding historyBinding = DataBindingUtil.setContentView(this, R.layout.activity_history);

        HistoryViewModel historyViewModel = ViewModelProviders.of(this).get(HistoryViewModel.class);
        if (savedInstanceState == null) {
            historyViewModel.init();
        }
        historyBinding.setHistoryVm(historyViewModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}
