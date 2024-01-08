## I have a plan !
### 打算用到的技术栈
#### *后端：*
SpringBoot3.0、SpringCloud Alibaba 2022.0.0.0-RC2</br>
Feign、nacos、gateway</br>
Mysql8.0.33、Druid、Mybatis-Plus、Lombok、~~JWT~~、~~Spring Security~~......</br>
#### *前端：*
Vue3、Element-plus、Nuxt3、ES6

### *重新开发一个属于自己博客*
* > * 文章 √
* > * 友链
* > * BC~GPT
* > * 留言板
* > * 树洞(拉屎专用！)
* > * 目前就这些.......
### *开发一个在线影视项目*
* > * 影评
* > * 免费影视剧
* > * 求片
* > * 好片分享~
* > * 接入腾讯云点播，使用m3u8加密，防止被别的站白嫖。也可以用作未来设置模拟付费模块的拓展！</br>

目前的一些大体思路，数据库已经设计完前台的了，后台想做简单点.....

### 从2023-12-29开始，我要写开发日志！！！
> *2023-12-30 15:30* 
> allowMultiQueries=true mybatis批量插入允许 ';' 又又又tmd踩坑了，我排查了半天，我甚至都打开了菜鸟教程看我的SQL是不是T^T 
> *2023-12-31 08:01*
> 今晚收获不错，更加深入地理解Redis 中 Hash、List、以及 redis 序列化和反序列化的意义和细节。
> 本来浏览量 打算用 ZSet容器 单独装，然后每次浏览量同步数据库的时候获取Map<id,ViewCount> 用这个插入同步
> 但是Hash除了取的麻烦不能一下子获取指定hashkay的value集合，其他都好。目前用的 executePipelined.
> * 1.cron定时任务没踩坑 每h的第5n分钟 同步一次所有的浏览量和总浏览量
> * 2.有点小bug
> *2023-1-2 17:23*
> * 整合组件header category和menu接口，减少I/O次数
> *2023-1-6*
> * 今天啊，不得了啊！写了一点大堆浓shit！ 我称为屎上雕花！ 有两个 TODO优化的时候记得写
> *2023-1-8*
> * Expression #5 of SELECT list is not in GROUP BY clause and contains nonaggregated column 'db_bcblog.bc.update_time' which is not functionally dependent on columns in GROUP BY clause; this is incompatible with sql_mode=only_full_group_by
> * 这个报错是在 categoryMapper 的 categoryOfUrlNameIcon 中 报错原因是使用子查询时没有将 update_time 使用 GROUP BY 进行聚合 因为mysql8 默认限制了 sql_mode=only_full_group_by 也就也就是说你必须要用 GROUP BY 聚合。
> * 解决: 
> * 把 update_time 用 MAX(update_time) 区分一下