--[[
脚本说明：基于获取数据，如果数据被逻辑删除则返回nil
脚本入参：eval/evalsha 脚本内容/sha1 1 keyName
脚本返回值：返回数据项
 --]]
local dk = '{'..KEYS[1]..'}d'
if(redis.call('exists', dk)==1) then
    return nil
else
    return redis.call("get",KEYS[1])
end
