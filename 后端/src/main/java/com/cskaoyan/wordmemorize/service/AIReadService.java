package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.dto.admin.PageDTO;
import com.cskaoyan.wordmemorize.dto.admin.UserVocAISituationDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserVocAISituationCommand;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AIReadService {

    /*
            添加AI阅读场景以及选项信息(后台等级特权管理-AI阅读)
    */
    void addAISituation(UserVocAISituationCommand command);

    /*
        更新AI阅读场景以及选项信息(后台等级特权管理-AI阅读)
     */
    void updateAISituation(UserVocAISituationCommand command);

    /*
        删除AI阅读场景(后台等级特权管理-AI阅读)
     */
    void deleteAISituation(Long id);

    /*
         删除AI阅读场景中的某个选项(后台等级特权管理-AI阅读)
      */
    void deleteAISituationOption(Long id);

    /*
       批量删除AI阅读场景(后台等级特权管理-AI阅读)
    */
    void batchDeleteAISituationOption(List<Long> ids);

    /*
      分页查询AI阅读场景信息(后台等级特权管理-AI阅读)
    */
    PageDTO<UserVocAISituationDTO> getAISituationList(PageRequest pageRequest);


}
