package com.ddcoding.framework.service;


import com.ddcoding.framework.api.data.StandbyJobData;
import com.ddcoding.framework.persistent.entity.StandbyJobSummary;

import java.util.List;

/**
 */
public interface StandbyJobSummaryService {

    List<StandbyJobSummary> getAllJobSummaries();

    void saveJobSummary(StandbyJobSummary standbyJobSummary);

    StandbyJobSummary getJobSummary(String id);

    void updateJobSummary(String id);

    void updateJobSummary(StandbyJobData.Data data);

}
