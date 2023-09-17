package com.itswpu.huanswpu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.CustomException;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.dto.DishDto;
import com.itswpu.huanswpu.entity.*;
import com.itswpu.huanswpu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 菜品管理
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DishEmployeeService dishEmployeeService;
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        //新增关联
        DishEmployee dishEmployee = DishEmployee.builder()
                .dishId(dishDto.getId())
                .employeeId(BaseContext.getCurrentId())
                .name(dishDto.getName())
                .build();

        dishEmployeeService.save(dishEmployee);
        //1.更新完 就清理所有缓存数据
        //Set keys = redisTemplate.keys("dish_*");//获得所有以‘dish_’开头的key
        //redisTemplate.delete(keys);

        //2.精确 清理缓存数据(只清理 被更新类别 的缓存数据)
        String key="dish_" + dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("新增菜品成功");
    }

    /**
     * 菜品信息分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

            //获取商家关联菜品id列表
            LambdaQueryWrapper<DishEmployee> qw = new LambdaQueryWrapper<>();
            qw.eq(DishEmployee::getEmployeeId, BaseContext.getCurrentId());
            List<DishEmployee> lists = dishEmployeeService.list(qw);


            ArrayList<Long> ids = new ArrayList<>();
            for (DishEmployee dishEmployee : lists) {
                ids.add(dishEmployee.getDishId());
            }
            if(!CollectionUtils.isNotEmpty(ids)){
                log.info("数据库中未查到相关数据");
                return R.success(dishDtoPage.setTotal(0) );
            }

        //条件构造器
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.in(CollectionUtils.isNotEmpty(ids),Dish::getId,ids);
        queryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,queryWrapper);

        //对象属性(想拷贝的东西,目的对象,ignore属性)拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();


        List<DishDto> list = records.stream().map((item) -> {
            //item代表stream遍历的每个Dish菜品
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);//其他属性的拷贝赋值
            Long categoryId = item.getCategoryId();//每个菜品分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());//收集遍历返回的dishDto对象 转成集合

        dishDtoPage.setRecords(list);



        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){//传参注解
        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }

    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        //1.更新完 就清理所有缓存数据
        //Set keys = redisTemplate.keys("dish_*");//获得所有以‘dish_’开头的key
        //redisTemplate.delete(keys);

        //2.精确 清理缓存数据(只清理 被更新类别 的缓存数据)
        String key="dish_" + dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);

        LambdaQueryWrapper<DishEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishEmployee::getDishId,dishDto.getId());
        DishEmployee dishEmployee = dishEmployeeService.getOne(queryWrapper);
        dishEmployee.setName(dishDto.getName());
        dishEmployeeService.updateById(dishEmployee);

        return R.success("修改菜品成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    /*@GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }*/

    //根据条件查询对应的菜品数据
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<DishEmployee> qw = new LambdaQueryWrapper<>();
        qw.eq(DishEmployee::getEmployeeId, BaseContext.getCurrentId());
        List<DishEmployee> lists = dishEmployeeService.list(qw);
        ArrayList<Long> ids = new ArrayList<>();
        for (DishEmployee dishEmployee : lists) {
            ids.add(dishEmployee.getDishId());
        }

        if(!CollectionUtils.isNotEmpty(ids)){
            return R.success(null);
        }

        List<DishDto> dishDtoList =null;
        //动态构造key
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();//

        //先从redis中获取缓存数据,按照菜单分类缓存
        dishDtoList= (List<DishDto>) redisTemplate.opsForValue().get(key);

        if(dishDtoList != null) {
            //如果存在!=null，直接返回，不用查询数据库
            return R.success(dishDtoList);
        }
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids);
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件 排序顺序，创建时间倒序
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);

        dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        //不存在，需要查询数据库，将查询到的数据缓存到redis
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.HOURS);//60分钟

        return R.success(dishDtoList);
    }

    /**
     * 停/起 售菜品
     * @param "8080/dish/status/0?ids=1413385247889891330,1397862198033297410,1397861683434139649"
     * @return
     */

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable Integer status,@RequestParam List<String> ids){
//        log.info(ids);

        dishService.updateWithStatus(status,ids);

        return R.success("修改菜品状态成功");
    }

    @DeleteMapping()
    public R<String> deleteById(@RequestParam List<Long> ids){
        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            if(dish.getStatus() == 1){
                //当前菜品处于起售中
                return R.error("当前菜品处于起售中不能被删除");
            }
        }
        //判断当前菜品是否和套餐关联
        LambdaQueryWrapper<SetmealDish> qw = new LambdaQueryWrapper<>();
        qw.in(SetmealDish::getDishId,ids);
        List<SetmealDish> list = setmealDishService.list(qw);

        if (list != null && list.size() > 0){
            return R.success("和套餐关联不能被删除");
        }
        //删除当前菜品表中的数据
        dishService.removeByIds(ids);
        //删除口味表对应的数据
        LambdaQueryWrapper<DishFlavor> qw1 = new LambdaQueryWrapper<>();
        qw1.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(qw1);

        //删除关联表
        LambdaQueryWrapper<DishEmployee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishEmployee::getDishId,ids);
        dishEmployeeService.remove(queryWrapper);

        return R.success("删除成功");
    }


}
