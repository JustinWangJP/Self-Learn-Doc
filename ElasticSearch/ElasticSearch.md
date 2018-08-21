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

5. 查询表达式  
  +  基本查询  
    Query DSL是一种非常灵活又富有表现力的 查询语言。 Elasticsearch 使用它可以以简单的 JSON 接口来展现 Lucene 功能的绝大部分。在你的应用中，你应该用它来编写你的查询语句。它可以使你的查询语句更灵活、更精确、易读和易调试。
   
     - 要使用这种查询表达式，只需将查询语句传递给 query 参数：
        ```
        GET /_search
        {
          "query": YOUR_QUERY_HERE
        }
       ```

      - 空查询（empty search） —{}— 在功能上等价于使用 match_all 查询， 正如其名字一样，匹配所有文档：  
        ```
          GET /_search
          {
            "query": {
              "match_all": {}
            } 
          }
        ```
        
  +  合并查询语句
    (Query clauses) 就像一些简单的组合块 ，这些组合块可以彼此之间合并组成更复杂的查询。这些语句可以是如下形式：

      -  叶子语句（Leaf clauses） (就像 match 语句) 被用于将查询字符串和一个字段（或者多个字段）对比。
   
      -  复合(Compound) 语句 主要用于 合并其它查询语句。 比如，一个 bool 语句 允许在你需要的时候组合其它语句，无论是 must 匹配、 must_not 匹配还是 should 匹配，同时它可以包含不评分的过滤器（filters）：
    
          ```
          {
            "bool": {
                "must":     { "match": { "tweet": "elasticsearch" }},
                "must_not": { "match": { "name":  "mary" }},
                "should":   { "match": { "tweet": "full text" }},
                "filter":   { "range": { "age" : { "gt" : 30 }} }
            }
          }
          ```


    
    一条复合语句可以合并 任何 其它查询语句，包括复合语句，了解这一点是很重要的。这就意味着，复合语句之间可以互相嵌套，可以表达非常复杂的逻辑。

例如，以下查询是为了找出信件正文包含 business opportunity 的星标邮件，或者在收件箱正文包含 business opportunity 的非垃圾邮件：

{
    "bool": {
        "must": { "match":   { "email": "business opportunity" }},
        "should": [
            { "match":       { "starred": true }},
            { "bool": {
                "must":      { "match": { "folder": "inbox" }},
                "must_not":  { "match": { "spam": true }}
            }}
        ],
        "minimum_should_match": 1
    }
}
到目前为止，你不必太在意这个例子的细节，我们会在后面详细解释。最重要的是你要理解到，一条复合语句可以将多条语句 — 叶子语句和其它复合语句 — 合并成一个单一的查询语句。
  
