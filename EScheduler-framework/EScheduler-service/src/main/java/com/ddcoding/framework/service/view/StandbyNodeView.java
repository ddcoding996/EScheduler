package com.ddcoding.framework.service.view;

/**
 */
public class StandbyNodeView extends AbstractNodeView {

    public String getStateLabelClass() {
        if ("Master".equals(getNodeState())) {
            return "label-important";
        }
        if ("Backup".equals(getNodeState())) {
            return "label-info";
        }
        return "";
    }

}
