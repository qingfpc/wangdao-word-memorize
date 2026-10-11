package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.DeleteBookCategoryOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocCategoryDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.VocCategoryCommand;

import java.util.List;

public interface VocCategoryService {

   // 分页获取书籍类目(后端-物料管理-书记类目管理)
    PageDTO<VocCategoryDTO> getBookCategories(PageRequest pageQuery);
   // 获取书籍类目(后端-物料管理-书记类目管理)
   VocCategoryDTO getBookCategory(Long categoryId);

   // 添加书籍类目(后端-物料管理-书记类目管理)
   Long addBookCategory(VocCategoryCommand command);
   // 更新书籍类目(后端-物料管理-书记类目管理)
   void updateBookCategory(VocCategoryCommand command);
   // 删除书籍类目(后端-物料管理-书记类目管理)
   DeleteBookCategoryOperateDTO deleteBookCategory(Long categoryId);
   // 批量删除选中的书籍类目(后端-物料管理-书记类目管理)
   List<DeleteBookCategoryOperateDTO> deleteBookCategories(BatchDeleteCommand command);

}
