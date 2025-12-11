/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.dameng;

import com.google.inject.*;
import com.google.inject.Module;
import dm.jdbc.driver.DmDriver;
import io.opentelemetry.api.OpenTelemetry;
import io.trino.plugin.jdbc.*;
import io.trino.plugin.jdbc.credential.CredentialProvider;

import java.sql.SQLException;
import java.util.Properties;

import static io.airlift.configuration.ConfigBinder.configBinder;

public class DamengClientModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(JdbcClient.class).annotatedWith(ForBaseJdbc.class).to(DamengClient.class).in(Scopes.SINGLETON);
        binder.install(new DecimalModule());
        configBinder(binder).bindConfig(DamengConfig.class);
    }

//    @Provides
//    @Singleton
//    @ForBaseJdbc
//    public static ConnectionFactory createConnectionFactory(BaseJdbcConfig config, CredentialProvider credentialProvider, DamengConfig damengConfig, OpenTelemetry openTelemetry)
//            throws SQLException {
//        return DriverConnectionFactory.builder(new DmDriver(), config.getConnectionUrl(), credentialProvider)
//                .setConnectionProperties(getConnectionProperties(damengConfig))
//                .setOpenTelemetry(openTelemetry)
//                .build();
//    }

    @Provides
    @Singleton
    @ForBaseJdbc
    public static ConnectionFactory createConnectionFactory(BaseJdbcConfig config, CredentialProvider credentialProvider, DamengConfig damengConfig, OpenTelemetry openTelemetry)
            throws SQLException {
        return new DriverConnectionFactory(
                new DmDriver(),
                config.getConnectionUrl(),
                getConnectionProperties(damengConfig),
                credentialProvider,
                openTelemetry);
    }

    public static Properties getConnectionProperties(DamengConfig damengConfig) {
        Properties connectionProperties = new Properties();
        int autoReconnect = damengConfig.getAutoReconnect();
        if (autoReconnect >= 0 && autoReconnect <= 4) {
            connectionProperties.setProperty("autoReconnect", String.valueOf(autoReconnect));
        }
        if (damengConfig.getConnectTimeout() >= 0) {
            connectionProperties.setProperty("connectTimeout", String.valueOf(damengConfig.getConnectTimeout()));
        }
        return connectionProperties;
    }

}
