package com.javacreed.api.csv.writer;

public interface OrderedDataColumnFormatterProvider
    extends DataColumnFormatterProvider, Comparable<OrderedDataColumnFormatterProvider> {

  int getOrder();

}