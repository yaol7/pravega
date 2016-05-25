package com.emc.logservice.server.logs;

import com.emc.logservice.server.core.TruncateableList;
import com.emc.logservice.server.logs.operations.Operation;

/**
 * In-Memory Operation Log.
 */
public class MemoryOperationLog extends TruncateableList<Operation> {
}
