package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.dto.DishDto;
import com.itswpu.huanswpu.entity.Dish;
import com.itswpu.huanswpu.entity.DishFlavor;
import com.itswpu.huanswpu.mapper.DishMapper;
import com.itswpu.huanswpu.service.DishFlavorService;
import com.itswpu.huanswpu.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Transactional//多表操作 需要事务控制注释同时启动类加入@EnableTran..
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id
        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);//对象属性拷贝

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);//查到的口味封装到集合里
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);//传递子类对象直接更新父类的属性

        //清理当前菜品对应口味数据---dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());//构造条件

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味数据---dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();//没封装dishDto

        flavors = flavors.stream().map((item) -> {//set dishId
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }

    @Override
    @Transactional
    public void updateWithStatus(Integer status, List<String> ids){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        // 等价于 字段 IN (v0, v1, ...),例: in("age",{1,2,3}) ---> age in (1,2,3)
        queryWrapper.in(ids!=null,Dish::getId,ids);//构造条件


        List<Dish> dishs = dishService.list(queryWrapper);
        //修改当前菜品对应 status
        for (Dish dish:dishs) {
            if(dish!=null) {
                dish.setStatus(status);

                //2.精确 清理缓存数据(只清理 被更新类别 的缓存数据)
                String key = "dish_" + dish.getCategoryId() + "_" + dish.getStatus();
                redisTemplate.delete(key);

                dishService.updateById(dish);
            }
        }
    }
}
