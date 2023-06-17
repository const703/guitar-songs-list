package com.konst.guitarsongslist

import java.time.Instant

class Song(val name: String, val artist: String) {
    var lastPlayed: Instant? = null
}