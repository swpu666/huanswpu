package com.itswpu.huanswpu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itswpu.huanswpu.entity.Category;

public interface CategoryService extends IService<Category> {

    public void remove(Long id);

}
