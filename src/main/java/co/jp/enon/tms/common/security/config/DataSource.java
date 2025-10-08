package co.jp.enon.tms.common.security.config;

public @interface DataSource {

  /**
   * defaultはSTG.
   *
   * @return datasource
   */
  DataSourceType value() default DataSourceType.TMS;
  enum DataSourceType {
    TMS
  }
}

