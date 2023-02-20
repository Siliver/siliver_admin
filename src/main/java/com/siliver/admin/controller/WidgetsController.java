package com.siliver.admin.controller;

import com.siliver.admin.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * IP地址获取接口
 *
 * @author siliver
 */
@Slf4j
@Tag(name = "WidgetsController", description = "小工具接口")
@RestController
@RequestMapping("/widget")
@RequiredArgsConstructor
public class WidgetsController {

    private final Ip2regionSearcher regionSearcher;

    /**
     * 根据IP获取地址
     *
     * @param ip ip地址
     * @return 实际地址
     */
    @Operation(description = "根据IP获取地址", summary = "小工具接口")
    @GetMapping("/ipRegion")
    public Result<String> search(
            @Parameter(name = "ip", in = ParameterIn.QUERY, required = true, description = "IP地址") @RequestParam("ip") String ip) {
        return Result.successBuild(regionSearcher.getAddressAndIsp(ip));
    }
}
