package br.com.golforex.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.golforex.R;
import br.com.golforex.databinding.ExchangeRateItemBinding;
import br.com.golforex.model.ExchangeRate;


public class RateAdapter extends RecyclerView.Adapter<RateAdapter.GenericViewHolder> {

    private List<ExchangeRate> exchangeRateList;

    public RateAdapter() {

    }

    @NonNull
    public GenericViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ExchangeRateItemBinding itemBinding = DataBindingUtil.inflate(LayoutInflater.
                from(parent.getContext()), R.layout.exchange_rate_item, parent, false);
        return new GenericViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull GenericViewHolder holder, int position) {
        final ExchangeRate exchangeRate = exchangeRateList.get(position);
        holder.bind(exchangeRate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked " + exchangeRate.getCurrency());
            }
        });
    }


    @Override
    public int getItemCount() {
        return exchangeRateList == null ? 0 : exchangeRateList.size();
    }

    public void setItemList(List<ExchangeRate> itemList) {
        this.exchangeRateList = itemList;
    }

    class GenericViewHolder extends RecyclerView.ViewHolder {
        final ViewDataBinding binding;

        GenericViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ExchangeRate exchangeRate) {
            binding.setVariable(br.com.golforex.BR.exRate, exchangeRate);
            binding.executePendingBindings();
        }
    }
}