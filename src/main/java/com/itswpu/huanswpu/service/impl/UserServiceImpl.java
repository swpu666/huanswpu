package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.common.BaseContext;
import com.itswpu.huanswpu.common.R;
import com.itswpu.huanswpu.entity.ApUserSearch;
import com.itswpu.huanswpu.entity.User;
import com.itswpu.huanswpu.mapper.UserMapper;
import com.itswpu.huanswpu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

//    @Autowired
//    private MongoTemplate mongoTemplate;
//    /**
//     * 保存用户搜索历史记录
//     * @param keyword
//     * @param userId
//     */
//    @Override
////    @Async
//    public void insert(String keyword, Long userId) {
//        //1.查询当前用户的搜索关键词
//        Query query = Query.query(Criteria.where("userId").
//                is(userId).and("keyword").is(keyword));
//        ApUserSearch apUserSearch = mongoTemplate.findOne(query, ApUserSearch.class);
//
//        //2.存在相同关键词 更新创建时间
//        if(apUserSearch != null){
//            apUserSearch.setCreatedTime(new Date());
//            mongoTemplate.save(apUserSearch);
//            return;
//        }
//
//        //3.不存在，判断当前 历史记录总数量是否超过10
//        apUserSearch = new ApUserSearch();
//        apUserSearch.setUserId(userId);
//        apUserSearch.setKeyword(keyword);
//        apUserSearch.setCreatedTime(new Date());
//
//        Query query1 = Query.query(Criteria.where("userId").is(userId));
//        query1.with(Sort.by(Sort.Direction.DESC,"createdTime"));
//        List<ApUserSearch> apUserSearchList = mongoTemplate.find(query1, ApUserSearch.class);
////        log.warn("mongoDB.find到的"+apUserSearchList.toString() );
//
//        if(apUserSearchList == null || apUserSearchList.size() < 10){
//            mongoTemplate.save(apUserSearch);
////            log.warn("mongoDB.save后的"+apUserSearch.toString() );
//        }else {
//            ApUserSearch lastUserSearch = apUserSearchList.get(apUserSearchList.size() - 1);
//            mongoTemplate.findAndReplace(Query.query(Criteria.where("id").
//                    is( lastUserSearch.getId() ) ),apUserSearch);
////            log.warn("mongoDB.findAndReplace替换最后一个 后的"+apUserSearch.toString() );
//        }
//    }
//
//    /**
//     * 查询搜索历史
//     *
//     * @return
//     */
//    @Override
//    public R<List<ApUserSearch>> findUserSearch() {
//        //获取当前用户
////        Long user = 2126557185L;
//        Long user = BaseContext.getCurrentId();
//        if(user == null){
//            return R.error("未登录");
//        }
//
//        //根据用户查询数据，按照时间倒序
//        List<ApUserSearch> apUserSearches = mongoTemplate.find
//                (Query.query(Criteria.where("userId").is(user)).with
//                        (Sort.by(Sort.Direction.DESC, "createdTime")), ApUserSearch.class);
//
//        log.warn("*/*/*/*/* 调用findUserSearch方法");
//        log.warn("*/*/*/****"+ apUserSearches+"----");
//        return R.success(apUserSearches);
//    }
}
