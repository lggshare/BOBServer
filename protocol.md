# Protocol

服务器和客户端都用json传递信息

## 确认连接

+ Server -> Client:
```javascript
{
    "method": "checking",
    "time": time of server, represents as milliseconds,
    "id": the id of current player,
    "cid": random generated id for checking,
}
```

+ Client -> Server:
```javascript
{
    "method": "checking-response",
    "time": time of client, represents as milliseconds,
    "id": the id get from the server,
    "cid": the cid recieved from the server
}
```
