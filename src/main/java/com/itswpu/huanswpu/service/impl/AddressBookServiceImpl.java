package com.itswpu.huanswpu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itswpu.huanswpu.entity.AddressBook;
import com.itswpu.huanswpu.mapper.AddressBookMapper;
import com.itswpu.huanswpu.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service//使Spring管理
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
