package top.woaibocai.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @program: woaibocai-parent
 * @description: mybatisPlus自动填充功能
 * @author: woaibocai
 * @create: 2023-11-09 13:42
 **/
@Slf4j
@Component
public class MyBatisPlusMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
      this.strictInsertFill(metaObject,"createTime", LocalDateTime::now,LocalDateTime.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
