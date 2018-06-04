# Protocol

服务器和客户端都用json传递信息

所有的内容都用UTF-8编码

## 确认连接

+ Server -> Client:
```javascript
{
    "method": "checking",
    "time": time of server, represented as milliseconds,
    "id": the id of current player,
    "cid": random generated id for checking,
}
```

+ Client -> Server:
```javascript
{
    "method": "checking-response",
    "time": synchronized time of client, represented as milliseconds,
    "id": the id get from the server,
    "cid": the cid recieved from the server
}
```

## 数据发送

+ 传送动作 Client -> Server
```javascript
{
    "method": "action",
    "time": synchronized time of client, represented as milliseconds,
    "id": the id of the player,
    "action": The discription of action
}
```
+ 请求中断连接 Client -> Server
```javascript
{
    "method": "disconnect"
}
```
