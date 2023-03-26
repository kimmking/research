--[[
脚本说明：基于逻辑删除数据
脚本入参：eval/evalsha 脚本内容/sha1 1 keyName
脚本返回值：返回OK
 --]]
local dk = '{'..KEYS[1]..'}d'
redis.call("set",dk,1)
return "OK"