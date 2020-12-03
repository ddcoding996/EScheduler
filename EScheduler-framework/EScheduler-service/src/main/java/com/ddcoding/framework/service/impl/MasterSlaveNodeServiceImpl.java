package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.api.data.MasterSlaveNodeData;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.common.helper.ReflectHelper;
import com.ddcoding.framework.service.MasterSlaveNodeService;
import com.ddcoding.framework.service.view.MasterNodeView;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Service
public class MasterSlaveNodeServiceImpl extends AbstractService implements MasterSlaveNodeService {

    @Override
    public List<MasterNodeView> getAllNodes() {
        List<MasterSlaveNodeData> masterSlaveNodeDataList;
        List<MasterNodeView> masterNodeViewList = new ArrayList<>();
        try {
            masterSlaveNodeDataList = masterSlaveApiFactory.nodeApi().getAllNodes();
        } catch (Exception e) {
            LoggerHelper.warn("select all standby nodes failed, has been ignored [" + e.getClass().getName() + ", " + e.getMessage() + "]");
            return masterNodeViewList;
        }
        if (masterSlaveNodeDataList == null) {
            return new ArrayList<>();
        }
        for (MasterSlaveNodeData masterSlaveNodeData : masterSlaveNodeDataList) {
            MasterNodeView masterNodeView = new MasterNodeView();
            masterNodeView.setId(masterSlaveNodeData.getId());
            if (masterSlaveNodeData.getData() != null) {
                ReflectHelper.copyFieldValues(masterSlaveNodeData.getData(), masterNodeView);
            }
            masterNodeViewList.add(masterNodeView);
        }
        return masterNodeViewList;
    }

}
