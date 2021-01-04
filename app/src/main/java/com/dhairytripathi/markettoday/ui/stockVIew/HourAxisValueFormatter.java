package com.dhairytripathi.markettoday.ui.stockVIew;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HourAxisValueFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        Date date = new Date((long)value);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
        return sdf.format(date);
    }
}
