# ElasticSearch学习总结

## 基本概念
|RDB|Elasticsearch|
| :--- | :--- |
|データベース|インデックス|
|テーブル|マッピングタイプ|
|カラム（列）|フィールド|
|レコード（行）|文書（ドキュメント）|

## 基本语法
1. update 修改文档  
API URL\: Post Method \<doc_api\>\/\_update
    ```
    {
      "doc":{
        修改的内容
      }
    }
    ```

2. 批量查询
  * POST URL:\<doc_api\>\/_mget  
    Body:
    ```
     {
      "docs" : [
        {
            "_index": <index>,
            "_type" : <type>,
            "_id" : "1"
        },
        {
            "_index": <index>,
            "_type" : <type>,
            "_id" : "2"
        }
    ]}
    ```
  * POST URL:\<doc_api\>\/\<index\>/_mget  
    Body:
     ```
     {"docs" : [
        {
            "_type" : <type>,
            "_id" : "1"
        },
        {
            "_type" : <type>,
            "_id" : "2"
        }
    ]}
    ```
  * POST URL:\<doc_api\>\/\<index\>/\<type\>/_mget  
    Body:
     ```
     {"ids" : ["1", "2"]}
    ```

3. Bulk批量请求
* 请求格式
  ```
  {action:{metadata}}\n
  {request body}\n
  {action:{metadata}}\n
  {request body}\n
  ```
  |action|解释|
  | :---: | :--- |
  |create|当文档不存在时创建|
  |index|创建新文档或者替换已有文档|
  |update|局部更新文档|
  |delete|删除一个文档|

  Eg:
  ``` 
  { "index" : { "_index" : "test", "_type" : "_doc", "_id" : "1" } }
  { "field1" : "value1" }
  { "delete" : { "_index" : "test", "_type" : "_doc", "_id" : "2" } }
  { "create" : { "_index" : "test", "_type" : "_doc", "_id" : "3" } }
  { "field1" : "value3" }
  { "update" : {"_id" : "1", "_type" : "_doc", "_index" : "test"} }
  { "doc" : {"field2" : "value2"} }
  ```
4. Mapping射影  
  * 数据类型 
    |string|字符串|
    |:---|:---|
    |integer|数字|
    |long|A long value(64bit)|
    |float|A floating-point number(32bit):such as 1,2,3,4|
    |double|A floating-poing number(64bit)|
    |bool|A bool value,such as True,False|
    |date|such as:2017-02-20|
    |binary|二进制|
  详细用法参照：https://blog.csdn.net/napoay/article/details/73100110
  * 修改索引
    + 使用PUT方法 /现有索引/_alias/别名A
    + 新建一个索引，定义好新的映射
    + 将别名指向新的索引，并且取消之前索引的指向
      - POST方法 /_aliases
      ```
       {
         "actions":{
           [{
             "remove":{ "index": "现有索引名", "alias":"别名A"}
           }，
           {"add":{
               "index":"新索引名","alias":"别名A"
             }
           ]
         }
       }
       注意：通过这几个实现了索引的平滑过渡，并且是零停机的。 
      ``` 
