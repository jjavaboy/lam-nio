package lam.kotlin.study

// kotlin分可变集合 和 不可变集合

fun main(args: Array<String>) {
    // 标准库: listOf()
    var readOnlyList: List<Int> = listOf(74, 33);
    println("readOnlyList:" + readOnlyList);

    // 标准库: setOf()
    var readOnlySet: Set<String> = setOf("a", "a", "b", "aaa");
    println("readOnlySet:" + readOnlySet);

    // 标准库: mutableListOf()
    var list: MutableList<Int> = mutableListOf(3, 3, 5, 2, 2);
    list.add(10)
    print("list:" + list + "\n");

    // 标准库: mutableSetOf()
    var set: MutableSet<Int> = mutableSetOf(3, 3, 5, 2, 2);
    set.add(10)
    print("set:" + set + "\n");

    var readOnlyMap: Map<Int, String> = mapOf(1 to "2", 3 to "4");
    println("readOnlyMap:" + readOnlyMap);
    var readOnlyMap0: Map<Int, String> = mapOf(Pair(1, "2"), Pair(3, "4"));
    println("readOnlyMap0:" + readOnlyMap0);

    var map: MutableMap<Int, String> = hashMapOf(1 to "11", 2 to "22", 3 to "33");
    map.put(4, "44");
    println("map:" + map);

    var map0: MutableMap<Int, String> = mutableMapOf(1 to "11", 2 to "22", 3 to "33");
    map0.put(4, "44");
    println("map0:" + map0);
}