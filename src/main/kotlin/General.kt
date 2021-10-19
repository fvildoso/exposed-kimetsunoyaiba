fun main(){
    val list = mutableListOf(1, 2, 3,"hola")
    println(list) // [1, 2, 3]

    list.add("hola2")

    list.forEach { t->
        println(t)
    }

    println(list[0])

}