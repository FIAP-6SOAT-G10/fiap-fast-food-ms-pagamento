package br.com.fiap.techchallenge.infra.config.database.dialect;

import org.hibernate.dialect.PostgreSQLDialect;

public class AWSRedShiftDialect extends PostgreSQLDialect {

    @Override
    public String getQuerySequencesString() {
        return """
               select\s
                1 as sequence_catalog,
                1 as sequence_schema,
                1 as sequence_name,
                1 as data_type,
                1 as numeric_precision,
                1 as numeric_precision_redix,
                1 as numeric_scale,
                1 as start_value,
                1 as minimum_value,
                1 as maximum_value,
                1 as increment,
                1 as cycle_option
              \s""";
    }
}
