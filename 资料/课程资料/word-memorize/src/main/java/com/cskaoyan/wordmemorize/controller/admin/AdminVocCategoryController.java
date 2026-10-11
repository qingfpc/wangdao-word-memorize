package com.cskaoyan.wordmemorize.controller.admin;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocCategoryDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.service.VocCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/voc")
public class AdminVocCategoryController {


    @Autowired
    VocCategoryService vocCategoryService;

    @GetMapping("category/list")
    public Result<PageDTO<VocCategoryDTO>> getCategoryList(PageRequest pageRequest) {

        PageDTO<VocCategoryDTO> bookCategories
                = vocCategoryService.getBookCategories(pageRequest);


        return Result.ok(bookCategories);
    }
}
