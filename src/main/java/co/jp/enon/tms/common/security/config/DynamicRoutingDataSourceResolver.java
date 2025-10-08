package co.jp.enon.tms.common.security.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDataSourceResolver extends AbstractRoutingDataSource {

  @Override
  protected Object determineCurrentLookupKey() {

    // Default is tms
    return "tms";
  }

}
