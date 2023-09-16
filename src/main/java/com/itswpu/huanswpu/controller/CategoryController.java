package com.itswpu.huanswpu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.entity.Category;
import com.itswpu.huanswpu.entity.CategoryEmployee;
import com.itswpu.huanswpu.service.CategoryEmployeeService;
import com.itswpu.huanswpu.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryEmployeeService categoryEmployeeService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);

        CategoryEmployee categoryEmployee = CategoryEmployee.builder()
                        .categoryId(category.getId())
                        .employeeId(BaseContext.getCurrentId())
                        .name(category.getName())
                        .build();

        boolean save = categoryEmployeeService.save(categoryEmployee);
        System.out.println(save);

        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //分页构造器
        Page<Category> pageInfo = new Page<>(page,pageSize);
        //获取id列表
        LambdaQueryWrapper<CategoryEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEmployee::getEmployeeId, BaseContext.getCurrentId());
        List<CategoryEmployee> list = categoryEmployeeService.list(queryWrapper);
        ArrayList<Long> ids = new ArrayList<>();
        for (CategoryEmployee categoryEmployee : list) {
            ids.add(categoryEmployee.getCategoryId());
        }
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapperCategory = new LambdaQueryWrapper<>();
        //添加排序条件，根据sort进行排序
        queryWrapperCategory.in(Category::getId,ids);
        queryWrapperCategory.orderByAsc(Category::getSort);

        //分页查询
        categoryService.page(pageInfo,queryWrapperCategory);
        return R.success(pageInfo);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id){
        log.info("删除分类，id为：{}",id);

        categoryService.remove(id);
        //删除关联表
        LambdaQueryWrapper<CategoryEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEmployee::getCategoryId,id);
        categoryEmployeeService.remove(queryWrapper);

        return R.success("分类信息删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);

        categoryService.updateById(category);
        LambdaQueryWrapper<CategoryEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEmployee::getCategoryId,category.getId());
        CategoryEmployee categoryEmployee = categoryEmployeeService.getOne(queryWrapper);
        categoryEmployee.setName(category.getName());
        categoryEmployeeService.updateById(categoryEmployee);
        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //获取id列表
        LambdaQueryWrapper<CategoryEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEmployee::getEmployeeId, BaseContext.getCurrentId());
        List<CategoryEmployee> list = categoryEmployeeService.list(queryWrapper);
        ArrayList<Long> ids = new ArrayList<>();
        for (CategoryEmployee categoryEmployee : list) {
            ids.add(categoryEmployee.getCategoryId());
        }

        //条件构造器
        LambdaQueryWrapper<Category> qw = new LambdaQueryWrapper<>();
        //添加条件
        qw.eq(category.getType() != null,Category::getType,category.getType());
        qw.in(Category::getId,ids);
        //添加排序条件
        qw.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> categoryList = categoryService.list(qw);
        return R.success(categoryList);
    }
}
