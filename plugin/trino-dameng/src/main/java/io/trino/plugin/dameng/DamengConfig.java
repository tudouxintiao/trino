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

import io.airlift.configuration.Config;
import io.airlift.configuration.ConfigDescription;

public class DamengConfig {
    private int autoReconnect;
    private long connectTimeout = 5000;

    public int getAutoReconnect() {
        return autoReconnect;
    }

    @Config("dameng.auto-reconnect")
    @ConfigDescription("连接发生异常或一些特殊场景下连接处理策略。取值范围 0~7；0：关闭连接；1：当连接发生异常时自动切换到其他库，无论切换成功还是失败都会抛一个 SQLException，用于通知上层应用进行事务执行失败时的相关处理；2：配合 epSelector=1 使用，如果服务名列表前面的节点恢复了，将当前连接切换到前面的节点上；4：保持各节点会话动态均衡，通过后台线程检测节点及会话数变化，并切换连接使之保持均衡。\n" +
            "也可以将 autoReconnect 置为上述几个值的组合值，表示同时进行多项配置，如置为 3 表示同时配置 1 和 2；缺省为 0")
    public DamengConfig setAutoReconnect(int autoReconnect) {
        this.autoReconnect = autoReconnect;
        return this;
    }


    public long getConnectTimeout() {
        return connectTimeout;
    }

    @Config("dameng.connect-timeout")
    @ConfigDescription("连连接数据库超时时间；单位 ms，取值范围 0~2147483647，0 表示无限制；缺省为 5000")
    public DamengConfig setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }


}
