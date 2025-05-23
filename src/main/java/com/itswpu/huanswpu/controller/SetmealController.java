package com.itswpu.huanswpu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.dto.SetmealDto;
import com.itswpu.huanswpu.entity.Category;
import com.itswpu.huanswpu.entity.Setmeal;
import com.itswpu.huanswpu.entity.SetmealEmployee;
import com.itswpu.huanswpu.mapper.SetmealEmployeeMapper;
import com.itswpu.huanswpu.service.CategoryService;
import com.itswpu.huanswpu.service.SetmealDishService;
import com.itswpu.huanswpu.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealEmployeeMapper setmealEmployeeMapper;

    /**
     * 新增套餐
     * @CacheEvict：清理指定缓存（缓存名称）
     * @param setmealDto
     * @return
     */
    @ApiOperation(value = "新增套餐接口")
    @PostMapping
    @CacheEvict(value = "setmealCache",allEntries = true )
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto);

        //保存 菜品套餐关联 信息
        setmealService.saveWithDish(setmealDto);

        //复制类属性 到关联表
        {
            SetmealEmployee se = new SetmealEmployee();
            Long currentId = BaseContext.getCurrentId();
            se.setEmployeeId(currentId);
            se.setSetmealId(setmealDto.getSetmealDishes().get(0).getSetmealId());
            se.setName(setmealDto.getName());
            System.out.println(setmealDto.toString()+" **"+se.toString());
            setmealEmployeeMapper.insert(se);
        }


        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @ApiOperation(value = "套餐分页查询接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true),
            @ApiImplicitParam(name = "pageSize",value = "每页记录数",required = true),
            @ApiImplicitParam(name = "name",value = "套餐名称",required = false)
    })
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        //从 关联表 查 套餐id
            Long currentId = BaseContext.getCurrentId();
            LambdaQueryWrapper<SetmealEmployee> queryWrapperNew = new LambdaQueryWrapper<>();
            //getCategoryId() != null 满足条件才查
            queryWrapperNew.eq(SetmealEmployee::getEmployeeId,currentId);
            List<SetmealEmployee> seList = setmealEmployeeMapper.selectList(queryWrapperNew);
            List<Long> seListIds=new ArrayList<Long>();
            for (SetmealEmployee se:seList) {
                seListIds.add( se.getSetmealId() );
            }
            if(!CollectionUtils.isNotEmpty(seListIds)){
                log.info("数据库中未查到相关数据");
                return R.success(dtoPage.setTotal(0) );
            }
            log.info("关联表数据"+seListIds);


        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据name进行like模糊查询
            queryWrapper.in(CollectionUtils.isNotEmpty(seListIds),Setmeal::getId,seListIds);

        queryWrapper.like(name != null,Setmeal::getName,name);
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        //对象拷贝 records 泛型不一样要ignore
        BeanUtils.copyProperties(pageInfo,dtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @ApiOperation(value = "套餐删除接口")
    @DeleteMapping
    @CacheEvict(value = "setmealCache",allEntries = true )
    //allEntries = true 删除这个value分类下的所有缓存
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);

        setmealService.removeWithDish(ids);

        return R.success("套餐数据删除成功");
    }

    /**
     * 根据条件查询套餐数据
     * @Cacheable 方法执行前spring先看缓存中是否有数据，有数据则直接返回
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "套餐条件查询接口")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId+'_'+ #setmeal.status")
    public R<List<Setmeal>> list(Setmeal setmeal){
        //从 关联表 查 套餐id
            Long currentId = BaseContext.getCurrentId();
            LambdaQueryWrapper<SetmealEmployee> queryWrapperNew = new LambdaQueryWrapper<>();
            //getCategoryId() != null 满足条件才查
            queryWrapperNew.eq(SetmealEmployee::getEmployeeId,currentId);

            List<SetmealEmployee> seList = setmealEmployeeMapper.selectList(queryWrapperNew);
            List<Long> seListIds=new ArrayList<Long>();
            for (SetmealEmployee se:seList) {
                seListIds.add( se.getId() );
            }
            log.info("********关联表数据"+seListIds);
            if(!CollectionUtils.isNotEmpty(seListIds)){
                return R.success(null);
            }

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //getCategoryId() != null 满足条件才查
            queryWrapper.in(CollectionUtils.isNotEmpty(seListIds),Setmeal::getId,seListIds);
        queryWrapper.eq(setmeal.getCategoryId() != null,
                Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,
                Setmeal::getStatus,setmeal.getStatus());
        //根据更新时间 降序排
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 停/起 售菜品
     * @param "8080/setmeal/status/0?ids=1664889535902797825"
     * @return
     */

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,@RequestParam List<String> ids){
//        log.info(ids);

        setmealService.updateWithStatus(status,ids);

        return R.success("修改菜品状态成功");
    }


}
