package com.siliver.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.siliver.admin.common.Result;
import com.siliver.admin.request.DictionaryChangeRequest;
import com.siliver.admin.response.DictionaryListResponse;
import com.siliver.admin.response.DictionaryResponse;
import com.siliver.admin.service.IDictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典相关接口
 *
 * @author siliver
 */
@Tag(name = "DictionaryController", description = "字典相关接口")
@RestController
@RequestMapping("/dictionary")
@RequiredArgsConstructor
public class DictionaryController {

    private final IDictionaryService dictionaryService;

    /**
     * 根据字典组获取字典列表
     *
     * @param userChannel 请求渠道
     * @param group       字典组
     * @return 对应类型的实体列表
     */
    @Operation(description = "分组字典项列表查询接口", summary = "字典相关接口")
    @GetMapping("/group/{group}")
    public Result<List<DictionaryResponse>> getDictionaryByGroup(
            @Parameter(name = "userChannel", in = ParameterIn.HEADER, required = true, description = "请求渠道") @RequestHeader("userChannel") String userChannel,
            @Parameter(name = "group", in = ParameterIn.PATH, required = true, description = "字典分组") @PathVariable("group") String group

    ) {
        return dictionaryService.getDictionaryByGroupService(group, userChannel);
    }

    /**
     * 字典维护接口
     *
     * @param change                  操作类型
     * @param dictionaryChangeRequest 维护实体
     * @return 维护结果
     */
    @Operation(description = "字典项维护接口", summary = "字典相关接口")
    @PostMapping("/change/{change}")
    public Result<Void> changeDictionary(
            @Parameter(name = "change", in = ParameterIn.PATH, required = true, description = "add: 新增；update: 更新；delete: 删除") @PathVariable("change") String change,
            @Parameter(name = "userCode", in = ParameterIn.HEADER, required = true, description = "操作人") @RequestHeader("userCode") String userCode,
            @RequestBody DictionaryChangeRequest dictionaryChangeRequest) {
        dictionaryChangeRequest.setCreateUser(userCode);
        dictionaryChangeRequest.setUpdateUser(userCode);
        return dictionaryService.changeDictionaryService(change, dictionaryChangeRequest);
    }

    /**
     * 字典项列表查询接口
     *
     * @param group   分组
     * @param name    名称
     * @param current 页码
     * @param size    页大小
     * @return 列表
     */
    @Operation(description = "字典项列表查询接口", summary = "字典相关接口")
    @GetMapping("/web/list")
    public Result<IPage<DictionaryListResponse>> getDictionaryList(
            @Parameter(name = "group", in = ParameterIn.QUERY, description = "分组类型") @RequestParam(value = "group", required = false) String group,
            @Parameter(name = "name", in = ParameterIn.QUERY, description = "字典名称") @RequestParam(value = "name", required = false) String name,
            @Parameter(name = "current", in = ParameterIn.QUERY, required = true, description = "页码") @RequestParam("current") int current,
            @Parameter(name = "size", in = ParameterIn.QUERY, required = true, description = "页大小") @RequestParam("size") int size) {
        return dictionaryService.getDictionaryListService(group, name, current, size);
    }

}
