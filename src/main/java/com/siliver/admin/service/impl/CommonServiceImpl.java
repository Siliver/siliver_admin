package com.siliver.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.siliver.admin.common.Result;
import com.siliver.admin.neum.CommonValue;
import com.siliver.admin.request.CommonChangeRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 通用的单表增删改方法
 *
 * @param <T> 请求对象
 * @param <V> 要变更的表的实体对象
 * @author siliver
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class CommonServiceImpl<T extends CommonChangeRequest, V extends Model<V>> {

    /**
     * 单表增删改查统一入口
     *
     * @param change  操作类型
     * @param request 请求参数
     * @return 操作结果
     */
    public Result<Void> changeDictionaryService(String change, T request, V dto) {
        if (!StringUtils.hasText(change)) {
            return Result.failBuild("操作类型不能为空！");
        }
        return switch (change) {
            case CommonValue.ADD_STRING -> addDictionary(request, dto);
            case CommonValue.UPDATE_STRING -> updateDictionary(request, dto);
            case CommonValue.DELETE_FLAG -> deleteDictionary(request, dto);
            default -> Result.failBuild("未知的操作！");
        };
    }

    /**
     * 自定新增方法
     *
     * @param request 维护请求实体
     * @return 新增结果
     */
    private Result<Void> addDictionary(T request, V dto) {
        // 兼容前端传错的情况
        request.setId(null);
        BeanUtils.copyProperties(request, dto);
        return dto.insert() ? Result.successBuild() : Result.failBuild("新增失败！");
    }

    /**
     * 自定义更新方法
     *
     * @param request 维护请求实体
     * @return 更新结果
     */
    private Result<Void> updateDictionary(T request, V dto) {
        if (Objects.isNull(request.getId())) {
            return Result.failBuild("更新的编号不能为空！");
        }
        // 查询原配置
        V dictionaryDto = dto.selectById(request.getId());
        if (Objects.isNull(dictionaryDto)) {
            return Result.failBuild("未查询到要更新的字典！");
        }
        BeanUtils.copyProperties(request, dictionaryDto);
        return dictionaryDto.updateById() ? Result.successBuild() : Result.failBuild("更新失败！");
    }

    /**
     * 私有的删除方法
     *
     * @param request 维护请求实体
     * @return 删除结果
     */
    private Result<Void> deleteDictionary(T request, V dto) {
        if (CollectionUtils.isEmpty(request.getIds())) {
            if (Objects.isNull(request.getId())) {
                return Result.failBuild("更新的编号不能为空！");
            }
            // 查询原配置
            V dictionaryDto = dto.selectById(request.getId());
            if (Objects.isNull(dictionaryDto)) {
                return Result.failBuild("未查询到要更新的字典！");
            }
            return dto.deleteById(request.getId()) ? Result.successBuild() : Result.failBuild("删除失败！");
        } else {
            // 查询原配置
            QueryWrapper<V> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", request.getIds());
            long count = dto.selectCount(queryWrapper);
            if (count != request.getIds().size()) {
                return Result.failBuild("改删除选择中有无效配置，请刷新页面！");
            }
            boolean flag = dto.delete(queryWrapper);
            return flag ? Result.successBuild() : Result.failBuild("删除失败！");
        }
    }

}
