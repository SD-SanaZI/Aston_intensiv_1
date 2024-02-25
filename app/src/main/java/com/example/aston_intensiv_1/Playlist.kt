package com.example.aston_intensiv_1


class Playlist(private val list: List<Int>) {
    private val size:Int = list.size
    private var current:Int = 0

    fun getCurrent():Int{
        return list[current]
    }

    fun getNext(): Int{
        if(current == size-1){
            current = 0
        }else{
            current++
        }
        return list[current]
    }

    fun getPrev(): Int{
        if(current == 0){
            current = size-1
        }else{
            current--
        }
        return list[current]
    }
}