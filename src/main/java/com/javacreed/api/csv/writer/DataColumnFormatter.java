package com.javacreed.api.csv.writer;

public interface DataColumnFormatter {

  String format(int columnIndex, Object value);
}
