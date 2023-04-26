package org.natimercy.generator.platform;

import org.natimercy.generator.entity.TableMetaData;

import java.util.List;

/**
 * Executor
 *
 * @author qian.he
 * @since 2023-04-26
 * @version 1.0.0
 */
public interface Executor {

    String getPlatform();

    List<TableMetaData> execute();
}
