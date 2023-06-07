package com.itswpu.huanswpu.dto;

import com.itswpu.huanswpu.entity.Setmeal;
import com.itswpu.huanswpu.entity.SetmealDish;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.util.List;

@Data
@ApiModel("套餐Dto")
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
