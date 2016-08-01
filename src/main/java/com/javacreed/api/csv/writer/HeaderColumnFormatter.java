package com.javacreed.api.csv.writer;

public interface HeaderColumnFormatter {

  String format(int columnIndex, String value);
}
