package br.com.golforex.model;

public class ExchangeRate {

    private String currency;
    private Double rate;

    public ExchangeRate(String currency, Double rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getRate() {
        return rate;
    }
}
