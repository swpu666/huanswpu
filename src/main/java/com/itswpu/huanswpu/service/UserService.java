package com.itswpu.huanswpu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.entity.ApUserSearch;
import com.itswpu.huanswpu.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    /**
     * 保存用户搜索历史记录
     * @param keyword
     * @param userId
     */
//    public void insert(String keyword,Long userId);

    /**
     查询搜索历史
     @return
     */
//    R<List<ApUserSearch>> findUserSearch();

}
