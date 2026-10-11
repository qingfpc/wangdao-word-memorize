package com.cskaoyan.wordmemorize.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.dao.entity.*;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface VocMapper extends BaseMapper<VocDO> {
    /**
     * 分页查询单词信息
     * @param bookId 书籍 ID
     * @param chapterId 章节 ID
     * @param keyword 关键词
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 单词列表
     */
    List<VocDO> getVocabulariesPage(@Param("bookId") String bookId, @Param("chapterId") String chapterId, @Param("keyword") String keyword, @Param("offset") int offset, @Param("pageSize") int pageSize);

    /**
     * 获取符合条件的单词总数
     * @param bookId 书籍 ID
     * @param chapterId 章节 ID
     * @param keyword 关键词
     * @return 单词总数
     */
    int getVocabulariesTotal(@Param("bookId") String bookId, @Param("chapterId") String chapterId, @Param("keyword") String keyword);
    // 新增：处理voc_chap_book表的插入或更新
    @Insert("INSERT INTO voc_chap_book (id, voc_id, voc_name, chapter_id, chapter_name, book_id, book_name) " +
            "VALUES (null, #{vocId}, #{vocName}, #{chapterId}, #{chapterName}, #{bookId}, #{bookName}) " +
            "ON DUPLICATE KEY UPDATE " +
            "voc_name = VALUES(voc_name), " +
            "chapter_name = VALUES(chapter_name), " +
            "book_name = VALUES(book_name)")
    int insertOrUpdateVocChapBook(VocChapBookDO entity);


    /**
     * 获取用户所选书籍的单词列表（无章节版）
     * @param userId 用户 ID
     * @param bookId 书籍 ID

     * @return 单词列表
     */
    Page<VocDO> getVocsOfUserBook(@Param("userId") Long userId, @Param("bookId") Long bookId,
                                  Page<VocDO> page);
    /**
     * 获取用户所选书籍的章节词汇列表
     * @param userId 用户 ID
     * @param bookId 书籍 ID

     * @return 章节词汇列表
     */
    Page<VocChapterDO> getChapterVocsOfUserBook(@Param("userId") Long userId, @Param("bookId") Long bookId,
                                                Page<VocChapterDO> page);

    /**
     * 获取用户所选书籍的章节词汇总数量
     * @param userId 用户 ID
     * @param bookId 书籍 ID
     * @return 总数量
     */

    /**
     * 查询某用户某书的所有单词（不分页）
     */
    List<VocDO> getAllVocsOfUserBook(@org.apache.ibatis.annotations.Param("userId") Long userId, @org.apache.ibatis.annotations.Param("bookId") Long bookId);

    /**
     * 查询某用户某书的所有章节单词（不分页）
     */
    List<VocChapterDO> getAllChapterVocsOfUserBook(@org.apache.ibatis.annotations.Param("userId") Long userId, @org.apache.ibatis.annotations.Param("bookId") Long bookId);
}
