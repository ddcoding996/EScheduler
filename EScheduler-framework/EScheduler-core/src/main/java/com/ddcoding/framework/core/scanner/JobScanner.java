package com.ddcoding.framework.core.scanner;

import com.ddcoding.framework.core.scanner.job.JobDescriptor;
import java.util.List;

/**
 * 任务的扫描器.
 *
 */
public interface JobScanner {

    String APPLICATION_CONTEXT_XML_PATH = "applicationContext.xml";

    List<JobDescriptor> getJobDescriptorList();

    boolean hasSpringEnvironment();

}
