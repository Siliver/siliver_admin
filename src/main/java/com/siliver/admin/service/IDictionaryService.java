package com.siliver.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.siliver.admin.common.Result;
import com.siliver.admin.dto.DictionaryDto;
import com.siliver.admin.request.DictionaryChangeRequest;
import com.siliver.admin.response.DictionaryListResponse;
import com.siliver.admin.response.DictionaryResponse;

import java.util.List;

/**
 * 字典类型服务
 *
 * @author siliver
 */
public interface IDictionaryService extends IService<DictionaryDto> {

    /**
     * 根据字典组获取字典列表
     *
     * @param userChannel 请求渠道
     * @param group       字典组
     * @return 对应类型的实体列表
     */
    Result<List<DictionaryResponse>> getDictionaryByGroupService(String group, String userChannel);

    /**
     * 字典维护接口
     *
     * @param change                  操作类型
     * @param dictionaryChangeRequest 维护实体
     * @return 维护结果
     */
    Result<Void> changeDictionaryService(String change, DictionaryChangeRequest dictionaryChangeRequest);

    /**
     * 字典项列表查询接口
     *
     * @param group   分组
     * @param name    名称
     * @param current 页码
     * @param size    页大小
     * @return 列表
     */
    Result<IPage<DictionaryListResponse>> getDictionaryListService(String group, String name, int current, int size);
}
