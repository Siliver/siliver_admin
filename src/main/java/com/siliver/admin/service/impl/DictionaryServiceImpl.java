package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.siliver.admin.common.Result;
import com.siliver.admin.dao.DictionaryMapper;
import com.siliver.admin.dto.DictionaryDto;
import com.siliver.admin.request.DictionaryChangeRequest;
import com.siliver.admin.response.DictionaryListResponse;
import com.siliver.admin.response.DictionaryResponse;
import com.siliver.admin.service.IDictionaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字符服务实现
 *
 * @author siliver
 */
@RequiredArgsConstructor
@Service
public class DictionaryServiceImpl extends ServiceImpl<DictionaryMapper, DictionaryDto> implements IDictionaryService {

    /**
     * 通用的单表增删改方法
     */
    private final CommonServiceImpl<DictionaryChangeRequest, DictionaryDto> commonService;

    @Override
    public Result<List<DictionaryResponse>> getDictionaryByGroupService(String group, String userChannel) {
        List<DictionaryDto> tempList = list(Wrappers.<DictionaryDto>lambdaQuery().eq(DictionaryDto::getDictionaryGroup, group).eq(DictionaryDto::getShowFlag, Integer.valueOf(userChannel)));
        if (CollectionUtils.isEmpty(tempList)) {
            return Result.successBuild(new ArrayList<>(1));
        }
        return Result.successBuild(tempList.stream().map(t -> {
            DictionaryResponse dictionaryResponse = new DictionaryResponse();
            dictionaryResponse.setName(t.getDictionaryName());
            dictionaryResponse.setValue(t.getDictionaryCode());
            return dictionaryResponse;
        }).collect(Collectors.toList()));
    }

    @Override
    public Result<Void> changeDictionaryService(String change, DictionaryChangeRequest dictionaryChangeRequest) {
        return commonService.changeDictionaryService(change, dictionaryChangeRequest, new DictionaryDto());
    }

    @Override
    public Result<IPage<DictionaryListResponse>> getDictionaryListService(String group, String name, int current, int size) {
        // 创建查询对象
        IPage<DictionaryDto> page = new Page<>(current, size);
        LambdaQueryWrapper<DictionaryDto> lambdaQueryWrapper = Wrappers.lambdaQuery();
        if (StringUtils.hasText(group)) {
            lambdaQueryWrapper.like(DictionaryDto::getDictionaryCode, group);
        }
        if (StringUtils.hasText(name)) {
            lambdaQueryWrapper.like(DictionaryDto::getDictionaryName, name);
        }
        // 添加排序规则
        lambdaQueryWrapper.orderByDesc(DictionaryDto::getUpdateTime);
        IPage<DictionaryDto> tempResult = page(page, lambdaQueryWrapper);
        // 无数据判断
        if (Objects.isNull(tempResult) || CollectionUtils.isEmpty(tempResult.getRecords())) {
            return Result.successBuild(new Page<>(current, size));
        }
        List<DictionaryListResponse> tempList = new ArrayList<>(tempResult.getRecords().size());
        for (DictionaryDto item : tempResult.getRecords()) {
            DictionaryListResponse oneNode = new DictionaryListResponse();
            BeanUtils.copyProperties(item, oneNode);
            tempList.add(oneNode);
        }
        IPage<DictionaryListResponse> result = new Page<>(current, size);
        result.setTotal(tempResult.getTotal());
        result.setRecords(tempList);
        return Result.successBuild(result);
    }

}
