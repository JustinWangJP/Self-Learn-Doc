# MongoDB学习总结

## 基本语句
1. update({arg1},{arg2},{arg3},{arg4})  
* arg1:条件  
* arg2:需要操作的更新内容
* arg3:如果文档不存在是否启用插入true/false
* arg4:是否更新全部匹配的文档（默认更新第一条）

2. find({arg1},{arg2})
  * arg1:条件
  * arg2:射影
  * 搜索操作符  
    + (>) 大于 - $gt
    + (<) 小于 - $lt
    + (>=) 大于等于 - $gte
    + (<= ) 小于等于 - $lt
  * 正则表达式
    + \^ : 匹配开头
    + \$ : 匹配结尾


## 修改器总结
1. \"\$set\":用来制定一个字段值，若不存在，则创建
   * 一般用于点加一个字段  
   ```db.users.update({name:"joe"},{"$set":{"favorite book":"War and Peace"}})```

   * 还可以修改一个字段  
   ```db.users.update({name:"joe"},{"$set":{"favorite book":"Your"}})```
 
   * 还可以删除  
   ```db.users.update({name:"joe"},{"$unset":{"favorite book":"Your"}})```

2. \"\$inc\"：增加或者减少
   * 用来增加已有的键的值，不存在则创建一个，比如某网站的访问量  
   ```db.users.update({name:"joe"},{"$inc":{"score":1}})```  
   每次访问都会增加1

3. \$push：会向已有的数组末尾添加一个元素，要是没有，创建一个新的数组。

  比如要添加一个评论：
 ```
 db.users.update({name:"joe"},{"$push":{"comments":{"name":"joe","email":"joe@yahoo.com","conment":"nice"}}})
 ```

 还想添加，继续修改即可

```
db.users.update({name:"joe"},{"$push":{"comments":{"name":"bob","email":"bob@yahoo.com","conment":"good"}}})
```

4. \$each与\$push结合，可以操作复杂的数组。可以一次添加多个值
```
var users={"username":"joe",
          "email":[
                       "joe@example.com",
                       "joe@exam.com",
                     "joe@yahoo.com"]}
 db.users.insert(users) 
 db.users.find() 
 db.users.update({"username":"joe"},
                          {"$addToSet":{"email":"joe@mail.com"}}) 
 db.users.update({"username":"joe"},
                           {"$addToSet":{
                                     "email":{
                                     "$each":["joe@qq.com","joe@Hadoop.com","joe@python.com"]}}})
```

结果展示：
```
{
"_id" : ObjectId("57600ffd73b4b5108cfff9b0"),
    "username" : "joe",
   "email" : [ 
   "joe@example.com", 
   "joe@exam.com", 
   "joe@yahoo.com", 
   "joe@mail.com", 
   "joe@qq.com", 
    "joe@Hadoop.com", 
  "joe@python.com"
   ]
}
```

5. \$pop，\$pull 删除元素的方法。类似于堆栈中的出栈操作
  * 从数据末尾删除一元素<br/>```{$pop:{key:1}}```
  * 从数据开头删除一元素<br/>```{$pop:{key:-11}}```
  * 删除一个数组内等于val的值<br/>```{$pull:{key:val}}```
  * 删除多个数组内等于val的值<br/>```{$pullAll:{key:[val1,val2,val2]}}```

db.lists.insert({"todo":["dishes","laundry","dry cleaning"]}) 
db.lists.find()
db.lists.update({},{"$pull":{"todo":"laundry"}})

6. 基于位置的修改可以使用定位操作符\$  
  如: <br/>```db.blog.plogs.update({"post","post_id"},{"$inc":"comments.0.votes":1}) ```  
   实际在数据库中，数组的下标很难确定，我们采用<br/>```db.blog.update({"comments.author":"Jim"},{"$set":{"comments.$.author":"Wade"}}) ```


## 注意事项
1. Null值的查找  
   通过\$in和\$exists进行过滤处理  
   ```
   db.users.find({"username":{"$in":[null],"$exists":true}})
   ```