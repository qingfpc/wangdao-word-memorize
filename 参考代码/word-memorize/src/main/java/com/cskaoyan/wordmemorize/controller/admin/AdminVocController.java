package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.auth.SaAdminCheckPermission;
import com.cskaoyan.wordmemorize.auth.StpKit;
import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocChapBookDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocCommand;
import com.cskaoyan.wordmemorize.request.VocPageRequest;
import com.cskaoyan.wordmemorize.service.FileUploadService;
import com.cskaoyan.wordmemorize.service.VocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Tag(name = "后台管理-物料管理-单词管理相关接口")
@RestController
@RequestMapping("admin/voc")
public class AdminVocController {

    @Resource
    VocService vocService;


    @Resource
    FileUploadService fileUploadService;

    @Operation(summary = "****分页查询单词列表",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("list")
    @SaAdminCheckPermission("admin:voc:search")
    public Result<PageDTO<VocDTO>> getVocabularies(VocPageRequest pageQuery) {
        PageDTO<VocDTO> vocabularies = vocService.getVocabularies(pageQuery);
        return Result.ok(vocabularies);
    }

    @Operation(summary = "****添加单词",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PostMapping("")
    @SaAdminCheckPermission("admin:voc:add")
    public Result addVocabulary(@RequestBody VocCommand command) {
        vocService.addVoc(command);
        return Result.ok();
    }
    @Operation(summary = "****修改单词",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @PutMapping()
    @SaAdminCheckPermission("admin:voc:update")
    public Result updateVocabulary(@RequestBody VocCommand command) {
        vocService.updateVoc(command);
        return Result.ok();
    }
    @Operation(summary = "***删除指定单词",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("{vocId}")
    @SaAdminCheckPermission("admin:voc:delete")
    public Result deleteVocabulary(@PathVariable @Parameter(description = "单词id") Long vocId) {
        vocService.deleteVocabulary(vocId);
        return Result.ok();
    }
    @Operation(summary = "****批量删除单词",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @DeleteMapping("")
    @SaAdminCheckPermission("admin:voc:batchdelete")
    public Result deleteVocabularies(@RequestBody BatchDeleteCommand command) {
        vocService.deleteVocabularies(command);
        return Result.ok();
    }
    @Operation(summary = "***指定单词的单词-章节-书籍关系列表",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }

    )
    @GetMapping("relations/{vocId}")
    @SaAdminCheckPermission("admin:voc:add")
    public Result<List<VocChapBookDTO>> getVocRelations(@PathVariable Long vocId) {
        List<VocChapBookDTO> vocRelations = vocService.getVocRelations(vocId);
        return Result.ok(vocRelations);
    }
    @Operation(summary = "****校验上传单词的excel表格",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @PostMapping("excel/validate")
    @SaAdminCheckPermission("admin:voc:excel:upload")
    public Result vocExcelVerify(@RequestParam("vocExcel") MultipartFile file) throws IOException {
        vocService.vocExcelValidate(file);
        return Result.ok();
    }
    @Operation(summary = "****通过excel表格上传单词",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @PostMapping("excel")
    @SaAdminCheckPermission("admin:voc:excel:upload")
    public Result addVocExcel(@RequestParam("vocExcel") MultipartFile file) throws IOException {
        Long employeeId = StpKit.ADMIN.getLoginIdAsLong();
        vocService.addVocExcel(file, employeeId);
        return Result.ok();
    }
    @Operation(summary = "*下载单词的excel表格模版",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @GetMapping("template")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        // 使用ClassPathResource读取资源文件

        ClassPathResource resource;
        resource = new ClassPathResource("templates/单词上传模版.xlsx");

        // 检查文件是否存在
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 读取文件内容
        InputStream inputStream = resource.getInputStream();
        byte[] fileBytes = IOUtils.toByteArray(inputStream);

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment",
                URLEncoder.encode(resource.getFilename(), "UTF-8"));

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }


    @Operation(summary = "***上传单词对应的音频文件",
            description = "主要用于物料管理-单词管理",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @PostMapping("file")
    public Result<String> upload(MultipartFile file) throws IOException {
        String upload = fileUploadService.upload(file);
        return Result.ok(upload);
    }

    @Operation(summary = "***删除单词对应的音频文件",
            description = "主要用于物料管理-单词管理(在添加修改对话框中)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @DeleteMapping("file/{type}/{id}")
    public Result deleteFile(@PathVariable("id")@Parameter(description = "单词id") Long vocId,
                             @PathVariable("type") @Parameter(description = "音频文件类型uk或us") String type,
                             @RequestParam("name") @Parameter(description = "文件名称(对象名称)") String name){
        vocService.deleteFile(vocId, type, name);
        return Result.ok();
    }

    @Operation(summary = "**删除单词与书籍和章节的关系",
            description = "主要用于物料管理-单词管理(在添加修改对话框中)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @DeleteMapping("relation/{id}")
    public Result deleteRelation(@PathVariable("id")@Parameter(description = "关系id") Long vocChapBookId) throws IOException {
        vocService.deleteVocRelation(vocChapBookId);
        return Result.ok();
    }

    @Operation(summary = "*删除单词释义",
            description = "主要用于物料管理-单词管理(在添加修改对话框中)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @DeleteMapping("interpretation/{id}")
    public Result deleteInterpretation(@PathVariable("id") @Parameter(description = "释义id") Long id) throws IOException {
        vocService.deleteInterpretation(id);
        return Result.ok();
    }


    @Operation(summary = "*删除单词例句",
            description = "主要用于物料管理-单词管理(在添加修改对话框中)",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "登出成功"

                    ),
                    @ApiResponse(responseCode = "500",
                            description = "请求失败"
                    )
            }
    )
    @DeleteMapping("/example/{id}")
    public Result deleteExampleSentence(@PathVariable("id") @Parameter(description = "释义id") Long id) throws IOException {
        vocService.deleteExample(id);
        return Result.ok();
    }
}
