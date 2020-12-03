package com.ddcoding.framework.service.view;

import com.ddcoding.framework.api.helper.PathHelper;
import com.ddcoding.framework.common.helper.ListHelper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 */
@Setter
@Getter
public class MasterNodeView extends AbstractNodeView {

    private List<String> jobPaths;

    public String getJobPathsHtmlString() {
        if (ListHelper.isEmpty(jobPaths)) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (String jobPath : jobPaths) {
            stringBuffer.append(PathHelper.getEndPath(jobPath)).append("<br/>");
        }
        return stringBuffer.substring(0, stringBuffer.lastIndexOf("<br/>"));
    }

    public String getStateLabelClass() {
        if ("Master".equals(getNodeState())) {
            return "label-important";
        }
        if ("Slave".equals(getNodeState())) {
            return "label-info";
        }
        return "";
    }

}
