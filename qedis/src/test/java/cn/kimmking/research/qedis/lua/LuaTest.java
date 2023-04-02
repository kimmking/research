package cn.kimmking.research.qedis.lua;

import org.junit.Test;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2023/3/25 03:00
 */
public class LuaTest {

    @Test
    public void test_lua() {
        Globals globals = JsePlatform.standardGlobals();
        LuaValue script = globals.load("return 1");
        LuaValue value = script.call();
        assertEquals(1, value.toint());

        LuaValue script2 = globals.load("local f,v = string.match('123:abcd', '(%d+):(%a+)') return {f,v}");
        LuaValue value2 = script2.call();
        assertTrue(value2.istable());
        LuaTable table = (LuaTable) value2;
        assertEquals(2, table.length());
        LuaValue f = table.get(1);
        assertEquals("123",f.toString());
        LuaValue v = table.get(2);
        assertEquals("abcd",v.toString());
    }

    @Test
    public void test_context_lua() {
        Globals globals = JsePlatform.standardGlobals();
        LuaValue[] array = new LuaValue[2];
        for (int i = 0; i < 2; i++) {
            array[i] = LuaValue.valueOf("k" + i);
        }
        globals.set("KEYS", LuaValue.listOf(array));
        LuaValue script = globals.load("return KEYS[1]");
        LuaValue value = script.call();
        assertEquals("k0", value.toString());
    }

    @Test
    public void test_configJava_lua() {
        Globals globals = JsePlatform.standardGlobals();
        RedisLib.configJava(globals);
        RedisLib.config(globals, "KEYS", "tag1");
        RedisLib.config(globals, "ARGV", "101", "value101");

        LuaValue script = globals.load("printJava('#KEYS='..#KEYS,'ARGV[1]='..ARGV[1],'ARGV[2]='..ARGV[2]) return #ARGV");
        LuaValue value = script.call();
        assertEquals("2", value.toString());
    }

    @Test
    public void test_set_get_lua() {
        
        // 1. prepare for Lua engine
        Globals globals = JsePlatform.standardGlobals();
        globals.load(new RedisLib());

        // 2. prepare for Lua parameters
        RedisLib.config(globals, "KEYS", "tagA");
        RedisLib.config(globals, "ARGV", "1", "value01");

        // 3. load tag_set lua script 
        LuaValue setScript = globals.load(RedisLib.loadLua("/lua/tag_set.lua"));

        // 4. call tag_set lua script and check for set command return OK
        LuaValue setValue = setScript.call();
        assertEquals("OK", setValue.toString());

        // 5. load tag_get lua script 
        LuaValue getScript = globals.load(RedisLib.loadLua("/lua/tag_get.lua"));

        // 6. call tag_set lua script and check for set command return value in ARGV context
        LuaValue getValue = getScript.call();
        assertEquals("value01", getValue.toString());

        // 7. change the new value, but keep the version, do call set lua script, then check return nil
        RedisLib.config(globals, "ARGV", "1", "value02");
        setValue = setScript.call();
        assertTrue(setValue.isnil());

        // 8. change the new value and the new version, do call set lua script, then check return OK
        RedisLib.config(globals, "ARGV", "2", "value02");
        setValue = setScript.call();
        assertEquals("OK", setValue.toString());

        // 9. do call get lua script, then check return the new value
        getValue = getScript.call();
        assertEquals("value02", getValue.toString());

    }

    

}
