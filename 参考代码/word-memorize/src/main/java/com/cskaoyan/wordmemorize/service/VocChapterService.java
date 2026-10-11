package com.cskaoyan.wordmemorize.service;


import com.cskaoyan.wordmemorize.dto.admin.DeleteChapterOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocChapterDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocChapterCommand;
import com.cskaoyan.wordmemorize.request.VocChapterPageRequest;

import java.util.List;

public interface VocChapterService {

    // 分页查询章节列表(后端-物料管理-章节管理)
    PageDTO<VocChapterDTO> getChapterPage(VocChapterPageRequest pageQuery);
    // 添加章节(后端-物料管理-章节管理)
    void addChapter(VocChapterCommand command);
    // 修改章节(后端-物料管理-章节管理)
    void updateChapter(VocChapterCommand command);
    // 删除章节(后端-物料管理-章节管理)
    DeleteChapterOperateDTO deleteChapter(Long chapterId);
    // 批量删除选中章节(后端-物料管理-章节管理)
    List<DeleteChapterOperateDTO> deleteChapters(BatchDeleteCommand command);
}
