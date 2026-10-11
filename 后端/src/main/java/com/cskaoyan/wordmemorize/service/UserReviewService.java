package com.cskaoyan.wordmemorize.service;

import com.cskaoyan.wordmemorize.common.result.Result;
import com.cskaoyan.wordmemorize.dto.app.DownLoadUserReviewDTO;
import com.cskaoyan.wordmemorize.request.PageRequest;
import com.cskaoyan.wordmemorize.request.UserReviewDataCommand;

import java.util.List;

public interface UserReviewService {

    // 上传APP段复习数据
    void synchronizeUserReviewData(UserReviewDataCommand command, Long userId);

    // APP端下载复习数据(首次登录时)
    DownLoadUserReviewDTO downloadUserReviewData(PageRequest pageRequest, Long userId);
    // APP端添加复习单词
    Result addUserRovRev(List<Long> vocIds);

}
