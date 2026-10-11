package com.cskaoyan.wordmemorize.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cskaoyan.wordmemorize.converter.VocBookConverter;
import com.cskaoyan.wordmemorize.dao.entity.VocBookDO;
import com.cskaoyan.wordmemorize.dao.mapper.VocBookMapper;
import com.cskaoyan.wordmemorize.dto.admin.DeleteBookOperateDTO;
import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.VocBookDTO;
import com.cskaoyan.wordmemorize.request.BatchDeleteCommand;
import com.cskaoyan.wordmemorize.request.VocBookCommand;
import com.cskaoyan.wordmemorize.request.VocBookPageRequest;
import com.cskaoyan.wordmemorize.service.VocBookService;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class VocBookServiceImpl implements VocBookService {


    @Autowired
    VocBookMapper vocBookMapper;


    @Autowired
    VocBookConverter vocBookConverter;



    @Override
    public PageDTO<VocBookDTO> getBookPage(VocBookPageRequest pageQuery) {
        //查询所有的书籍列表
        if (pageQuery.getPageNum() == null){
            List<VocBookDO> vocBookDOS = vocBookMapper.selectList(null);
            return vocBookConverter.vocBookList2Page(vocBookDOS,(long)vocBookDOS.size());
        }
        //需要分页
        Page<VocBookDO> pageParam = new Page<>(pageQuery.getPageNum(), pageQuery.getCount());
        LambdaQueryWrapper<VocBookDO> vocBookWrapper = new LambdaQueryWrapper<>();
        if (pageQuery.getKeyword() !=null){
            vocBookWrapper.like(VocBookDO::getBcName,pageQuery.getKeyword());
        }
        Page<VocBookDO> vocBookDOPage = vocBookMapper.selectPage(pageParam, vocBookWrapper);

        return vocBookConverter.vocBookList2Page(vocBookDOPage.getRecords(),vocBookDOPage.getTotal());
    }

    @Override
    public Long addVocBook(VocBookCommand command) {
        if (command==null){
            throw new IllegalArgumentException("书籍信息不能为空");
        }
        VocBookDO vocBookDO = vocBookConverter.vocBookCommand2VocBookDO(command);

        if (vocBookDO==null){
            throw new IllegalStateException("书籍转换失败");
        }
        vocBookMapper.insert(vocBookDO);
        return vocBookDO.getId();
    }

    @Override
    public void updateVocBook(VocBookCommand command) {
        if (command==null){
            throw new IllegalArgumentException("书籍信息不能为空");
        }
        VocBookDO vocBookDO = vocBookConverter.vocBookCommand2VocBookDO(command);
        vocBookDO.setId(Long.parseLong(command.getId()));

        UpdateWrapper<VocBookDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",vocBookDO.getId());

        vocBookMapper.update(vocBookDO,updateWrapper);
//            vocBookMapper.updateById(vocBookDO);

    }

    @Override
    public DeleteBookOperateDTO deleteVocBook(Long bookId) {

//        初始化返回的日志
        DeleteBookOperateDTO logDTO = new DeleteBookOperateDTO();
//        记录传入的ID
        logDTO.setId(bookId);
//查询书籍信息
        VocBookDO vocBookDO = vocBookMapper.selectById(bookId);
//        处理书籍不存在的情况
        if (vocBookDO==null){
            return logDTO;
        }
//        书籍存在时,填充DTO的书籍信息
        logDTO.setName(vocBookDO.getName());
        logDTO.setBcId(vocBookDO.getBcId());
        logDTO.setBcName(vocBookDO.getBcName());
//        执行删除操作
        int affectedRows = vocBookMapper.deleteById(bookId);
        return logDTO;
    }


    @Override
    public List<DeleteBookOperateDTO> deleteVocBooks(BatchDeleteCommand command) {

        List<String> ids = command.getIds();
        if (ids==null||ids.isEmpty()){
            return Collections.emptyList();
        }
//        查询要删除的书籍信息
        List<VocBookDO> booksToDelete = vocBookMapper.selectByIds(ids);

//      执行删除操作
        int affectedRows = vocBookMapper.deleteByIds(ids);
//        转化为日志DTO并返回
       return booksToDelete.stream()
               .map(book->{
                   DeleteBookOperateDTO dto = new DeleteBookOperateDTO();
                   dto.setId(book.getId());
                   dto.setName(book.getName());
                   dto.setBcId(book.getBcId());
                   dto.setBcName(book.getBcName());
                   return dto;
               })
               .collect(Collectors.toList());
    }
}
