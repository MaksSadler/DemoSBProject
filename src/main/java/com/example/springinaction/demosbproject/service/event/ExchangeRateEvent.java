package com.example.springinaction.demosbproject.service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExchangeRateEvent {

    private int id;
    private String currencyId;
    private String numCode;
    private String charCode;
    private int nominal;
    private String name;
    private double value;
    private LocalDate date;

}
