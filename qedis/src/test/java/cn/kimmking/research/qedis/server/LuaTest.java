package cn.kimmking.research.qedis.server;

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
        assertEquals("123",f.tojstring());
        LuaValue v = table.get(2);
        assertEquals("abcd",v.tojstring());
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
        assertEquals("k0", value.tojstring());
    }

    @Test
    public void test_set_get_lua() {

    }

}
