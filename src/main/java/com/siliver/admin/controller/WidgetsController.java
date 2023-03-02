package com.siliver.admin.controller;

import com.siliver.admin.common.Result;
import com.siliver.admin.feign.UomgFeignApi;
import com.siliver.admin.model.NeteaseCloudCommentModel;
import com.siliver.admin.model.NeteaseCloudMusicModel;
import com.siliver.admin.response.uomg.NeteaseCloudResponse;
import com.siliver.admin.response.uomg.QinghuaResponse;
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

import static com.siliver.admin.neum.CommonValue.UOMG_SUCCESS_CODE;

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

    private final UomgFeignApi uomgFeignApi;

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

    /**
     * 随机土味情话
     *
     * @param format 返回格式
     * @return 土味情话
     */
    @Operation(description = "获取土味情话", summary = "获取土味情话")
    @GetMapping("/tuwei")
    public Result<String> getRandQingHua(
            @Parameter(name = "format", in = ParameterIn.QUERY, required = true, description = "格式") @RequestParam(value = "format", required = false, defaultValue = "json") String format
    ) {
        QinghuaResponse qinghuaResponse = uomgFeignApi.getRandQingHuaApi(format);
        if (UOMG_SUCCESS_CODE.equals(qinghuaResponse.code)) {
            return Result.successBuild(qinghuaResponse.getContent());
        }
        return Result.failBuild(qinghuaResponse.getMsg());
    }

    /**
     * 随机网易云评论
     *
     * @param format 返回格式
     * @param mid    网易云歌单ID
     * @return 土味情话
     */
    @Operation(description = "获取网易云评论", summary = "获取网易云评论")
    @GetMapping(value = "/netease/comment", consumes = "application/json", produces = "application/json")
    public Result<NeteaseCloudCommentModel> getNeteaseCloudComment(
            @Parameter(name = "format", in = ParameterIn.QUERY, required = true, description = "格式") @RequestParam(value = "format", required = false, defaultValue = "json") String format,
            @Parameter(name = "mid", in = ParameterIn.QUERY, required = true, description = "网易云歌单ID") @RequestParam(value = "mid", required = false) String mid
    ) {
        NeteaseCloudResponse<NeteaseCloudCommentModel> neteaseCloudResponse = uomgFeignApi.getNeteaseCloudCommentApi(mid, format);
        if (UOMG_SUCCESS_CODE.equals(neteaseCloudResponse.code)) {
            return Result.successBuild(neteaseCloudResponse.getData());
        }
        return Result.failBuild(neteaseCloudResponse.getMsg());
    }

    /**
     * 获取网易云歌曲
     *
     * @param format 返回格式
     * @param mid    网易云歌单ID
     * @return 土味情话
     */
    @Operation(description = "获取网易云歌曲", summary = "获取网易云歌曲")
    @GetMapping("/netease/music")
    public Result<NeteaseCloudMusicModel> getNeteaseCloudMusic(
            @Parameter(name = "format", in = ParameterIn.QUERY, required = true, description = "格式") @RequestParam(value = "format", required = false, defaultValue = "json") String format,
            @Parameter(name = "sort", in = ParameterIn.QUERY, required = true, description = "音乐榜单") @RequestParam(value = "sort", required = false) String sort,
            @Parameter(name = "mid", in = ParameterIn.QUERY, required = true, description = "网易云歌单ID") @RequestParam(value = "mid", required = false) String mid
    ) {
        NeteaseCloudResponse<NeteaseCloudMusicModel> neteaseCloudResponse = uomgFeignApi.getNeteaseCloudMusicApi(sort, mid, format);
        if (UOMG_SUCCESS_CODE.equals(neteaseCloudResponse.code)) {
            return Result.successBuild(neteaseCloudResponse.getData());
        }
        return Result.failBuild(neteaseCloudResponse.getMsg());
    }
}
