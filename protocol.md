# Protocol

服务器和客户端都用json传递信息

所有的内容都用UTF-8编码

## 建立连接
客户端向服务器主动发送信息
+ Client -> Server
```javascript
{
    "method": "register",
    "name": 玩家设定的用户名
}
```
服务器给客户端的回复。告诉客户端用户名是否可用。如果可用，这次的消息中会含有`id`和`sync-msg`字段。
+ Server -> Client
```javascript
{
    "method": "register-response",
    "valid": 如果成功，返回1，失败返回0
    "id": 由服务器分配的id，Model内部可能会用id标记不同的玩家。id以第一次成功时为准。
    "sync-msg": 同步信息，是一个json对象，能够直接解码成SyncronizationMessage（就是用来喂给model的同步包，这样可以少写一个Model初始化的函数）
}
```
如果用户名重了，服务器不会断开和客户端的连接，客户端可以多次请求加入游戏，直到请求通过。

## 游戏进行
+ Client -> Server
    一旦有动作（比如按键按下，按键抬起），客户端就告诉服务器
    + 传送动作 Client -> Server
    ```javascript
    {
        "method": "action",
        "id": 玩家id,
        "action": 动作，是一个json对象，可以直接解码成Model更新状态需要的信息（暂时假设这个叫Action，名字虽然可能会有所不同，但是Model里肯定会有类似的东西，所以应该不需要担心）
    }
    ```
    客户端可以中断连接，客户端每次关闭之前都要给服务器发送这个消息，告诉它自己退出游戏了
    + 请求中断连接 Client -> Server
    ```javascript
    {
        "method": "disconnect"
    }
    ```
+ Server -> Client
    + 服务器定时发送同步包 Server -> Client
    ```javascript
    {
        "method": "sync",
        "sync-msg": 同步信息，是一个json对象，能够直接解码成SyncronizationMessage（就是用来喂给model的同步包）
    }
    ```

## 接口
上文中提到的两个类都必须实现如下接口
```java
/**
 * The class that can parse and encode JSON
 */
public interface JSONSerializable {
    /**
     * Parse the json as object
     *
     * @param json
     */
    void parseJSON(JSONObject json);

    /**
     * Encode the object as json
     *
     * @return encoded Json
     */
    JSONObject encodeJSON();
}
```
应有
```java
public class Action implements JSONSerializable {
    // Implementation goes here
}
```

```java
public class SyncronizationMessage implements JSONSerializable {
    // Implementation goes here
}
```
具体的实现Model怎么方便怎么来
