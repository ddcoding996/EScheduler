/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.ddcoding.framework.service.impl;

import com.ddcoding.framework.api.data.StandbyNodeData;
import com.ddcoding.framework.common.helper.LoggerHelper;
import com.ddcoding.framework.common.helper.ReflectHelper;
import com.ddcoding.framework.service.StandbyNodeService;
import com.ddcoding.framework.service.view.StandbyNodeView;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 */
@Service
public class StandbyNodeServiceImpl extends AbstractService implements StandbyNodeService {

    @Override
    public List<StandbyNodeView> getAllNodes() {
        List<StandbyNodeData> standbyNodeDataList;
        List<StandbyNodeView> standbyNodeViewList = new ArrayList<>();
        try {
            standbyNodeDataList = standbyApiFactory.nodeApi().getAllNodes();
        } catch (Exception e) {
            LoggerHelper.warn("select all standby nodes failed, has been ignored [" + e.getClass().getName() + ", " + e.getMessage() + "]");
            return standbyNodeViewList;
        }
        for (StandbyNodeData standbyNodeData : standbyNodeDataList) {
            StandbyNodeView standbyNodeView = new StandbyNodeView();
            standbyNodeView.setId(standbyNodeData.getId());
            if (standbyNodeData.getData() != null) {
                ReflectHelper.copyFieldValues(standbyNodeData.getData(), standbyNodeView);
            }
            standbyNodeViewList.add(standbyNodeView);
        }
        return standbyNodeViewList;
    }

}
