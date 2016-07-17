package io.github.dinolupo.doit.business.monitoring.boundary;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.LongSummaryStatistics;

/**
 * Created by dinolupo.github.io on 17/07/16.
 */
@Path("boundary-statistics")
public class BoundaryStatisticsResource {

    @Inject
    MonitoringSink monitoringSink;

    @GET
    public JsonObject get() {
        LongSummaryStatistics statistics = monitoringSink.getStatistics();
        JsonObject jsonStats = Json.createObjectBuilder()
                .add("average-duration", statistics.getAverage())
                .add("max-duration", statistics.getMax())
                .add("min-duration", statistics.getMin())
                .add("invocations-count", statistics.getCount())
                .build();
        return jsonStats;
    }

}
