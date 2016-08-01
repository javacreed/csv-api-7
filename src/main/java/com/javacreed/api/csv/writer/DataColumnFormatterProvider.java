package com.javacreed.api.csv.writer;

public interface DataColumnFormatterProvider {

  DataColumnFormatter find(int columnIndex, Object value);

}