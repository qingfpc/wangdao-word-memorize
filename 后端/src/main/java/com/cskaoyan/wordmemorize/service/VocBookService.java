package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.DeleteBookOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocBookDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocBookCommand;
import com.cskaoyan.wordmemorize.request.VocBookPageRequest;

import java.util.List;

public interface VocBookService {

    // 分页展示单词书籍(后端-物料管理-书籍管理)
    PageDTO<VocBookDTO> getBookPage(VocBookPageRequest pageQuery);
    // 添加单词书籍(后端-物料管理-书籍管理)
    Long addVocBook(VocBookCommand command);
    // 更新单词书籍(后端-物料管理-书籍管理)
    void updateVocBook(VocBookCommand command);
    // 删除单词书籍(后端-物料管理-书籍管理)
    DeleteBookOperateDTO deleteVocBook(Long bookId);
    // 批量删除选中的单词书籍(后端-物料管理-书籍管理)
    List<DeleteBookOperateDTO> deleteVocBooks(BatchDeleteCommand command);

}
