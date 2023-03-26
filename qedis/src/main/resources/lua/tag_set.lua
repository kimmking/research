--[[
脚本说明：基于版本更新数据，如果版本小于等于当前版本则不处理，如果写入数据则同时清理逻辑删除标记
脚本入参：eval/evalsha 脚本内容/sha1 1 keyName newVersion newValue
脚本返回值：更新成功则返回OK，否则返回nil
 --]]
local dk = '{'..KEYS[1]..'}d'
local vk = '{'..KEYS[1]..'}v'
if(redis.call('exists', vk)==1) then
    local ov = redis.call("get",vk)
    if (tonumber(ov) < tonumber(ARGV[1])) then
        redis.call("mset",vk,ARGV[1],KEYS[1],ARGV[2])
        redis.call("del",dk)
        return "OK"
    end
else
    redis.call("mset",vk,ARGV[1],KEYS[1],ARGV[2])
    redis.call("del",dk)
    return "OK"
end
return nil