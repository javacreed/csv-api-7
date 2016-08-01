package com.javacreed.api.csv.writer;

public abstract class AbstractColumnFormatterProvider implements OrderedDataColumnFormatterProvider {

  private final int order;

  protected AbstractColumnFormatterProvider(final int order) {
    this.order = order;
  }

  @Override
  public int compareTo(final OrderedDataColumnFormatterProvider o) {
    return Integer.compare(order, o.getOrder());
  }

  @Override
  public int getOrder() {
    return order;
  }
}