package org.example.functions;

import java.time.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import javax.xml.crypto.Data;

/**
 * Azure Functions with Timer trigger.
 */
public class TimerTriggerJava {
    private DataManager dataManager = new DataManager();
    @FunctionName("WebHook")
    public void run(
            @TimerTrigger(name = "timerInfo", schedule = "0 */10 * * * *") String timerInfo,
        final ExecutionContext context
    ) {
        counter.tick();
        context.getLogger().info("Java Timer trigger function executed at: " + LocalDateTime.now() +
                "\n Execution count: " + counter.count);
        dataManager.getDataJson();

    }
}
